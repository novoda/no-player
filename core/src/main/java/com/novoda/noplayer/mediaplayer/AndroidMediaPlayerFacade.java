package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState;
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.*;

class AndroidMediaPlayerFacade {

    private static final Map<String, String> NO_HEADERS = null;

    private final Context context;
    private final AudioManager audioManager;
    private final AndroidMediaPlayerAudioTrackSelector trackSelector;
    private final PlaybackStateChecker playbackStateChecker;

    private PlaybackState currentState = IDLE;

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionForwarder;
    private MediaPlayer.OnPreparedListener onPreparedForwarder;
    private int currentBufferPercentage;
    private MediaPlayer.OnErrorListener onErrorForwarder;
    private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedForwarder;

    private SurfaceHolderRequester surfaceHolderRequester;
    private MediaPlayerCreator mediaPlayerCreator;

    static AndroidMediaPlayerFacade newInstance(Context context) {
        TrackInfosFactory trackInfosFactory = new TrackInfosFactory();
        AndroidMediaPlayerAudioTrackSelector trackSelector = new AndroidMediaPlayerAudioTrackSelector(trackInfosFactory);
        PlaybackStateChecker playbackStateChecker = new PlaybackStateChecker();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayerCreator mediaPlayerCreator = new MediaPlayerCreator();
        return new AndroidMediaPlayerFacade(context, audioManager, trackSelector, playbackStateChecker, mediaPlayerCreator);
    }

    AndroidMediaPlayerFacade(Context context,
                             AudioManager audioManager,
                             AndroidMediaPlayerAudioTrackSelector trackSelector,
                             PlaybackStateChecker playbackStateChecker,
                             MediaPlayerCreator mediaPlayerCreator) {
        this.context = context;
        this.audioManager = audioManager;
        this.trackSelector = trackSelector;
        this.playbackStateChecker = playbackStateChecker;
        this.mediaPlayerCreator = mediaPlayerCreator;
    }

    void setSurfaceHolderRequester(SurfaceHolderRequester surfaceHolderRequester) {
        this.surfaceHolderRequester = surfaceHolderRequester;
    }

    void prepareVideo(final Uri videoUri) {
        if (surfaceHolderRequester == null) {
            throw new IllegalStateException("Must set a SurfaceHolderRequester before preparing video");
        }
        surfaceHolderRequester.requestSurfaceHolder(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                requestAudioFocus();
                release();
                try {
                    currentState = PlaybackState.PREPARING;
                    mediaPlayer = createAndBindMediaPlayer(surfaceHolder, videoUri);
                    mediaPlayer.prepareAsync();
                } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
                    reportCreationError(ex, videoUri);
                }
            }
        });
    }

    private void requestAudioFocus() {
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private MediaPlayer createAndBindMediaPlayer(SurfaceHolder surfaceHolder, Uri videoUri) throws IOException {
        MediaPlayer mediaPlayer = mediaPlayerCreator.createMediaPlayer();
        mediaPlayer.setOnPreparedListener(internalPreparedListener);
        mediaPlayer.setOnVideoSizeChangedListener(internalSizeChangedListener);
        mediaPlayer.setOnCompletionListener(internalCompletionListener);
        mediaPlayer.setOnErrorListener(internalErrorListener);
        mediaPlayer.setOnBufferingUpdateListener(internalBufferingUpdateListener);
        mediaPlayer.setDataSource(context, videoUri, NO_HEADERS);
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);

        currentBufferPercentage = 0;

        return mediaPlayer;
    }

    private void reportCreationError(Exception ex, Uri videoUri) {
        Log.w(ex, "Unable to open content: " + videoUri);
        currentState = PlaybackState.ERROR;
        internalErrorListener.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
    }

    private final MediaPlayer.OnVideoSizeChangedListener internalSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (onVideoSizeChangedForwarder == null) {
                throw new IllegalStateException("Should bind a OnVideoSizeChangedListener. Cannot forward events.");
            }
            onVideoSizeChangedForwarder.onVideoSizeChanged(mp, width, height);
        }
    };

    private final MediaPlayer.OnPreparedListener internalPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            currentState = PlaybackState.PREPARED;

            if (onPreparedForwarder == null) {
                throw new IllegalStateException("Should bind a OnPreparedListener. Cannot forward events.");
            }
            onPreparedForwarder.onPrepared(mediaPlayer);
        }
    };

    private final MediaPlayer.OnCompletionListener internalCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            currentState = PlaybackState.COMPLETED;
            if (onCompletionForwarder == null) {
                throw new IllegalStateException("Should bind a OnCompletionListener. Cannot forward events.");
            }
            onCompletionForwarder.onCompletion(mediaPlayer);
        }
    };

    private final MediaPlayer.OnErrorListener internalErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d("Error: " + what + "," + extra);
            currentState = PlaybackState.ERROR;
            if (onErrorForwarder == null) {
                throw new IllegalStateException("Should bind a OnErrorListener. Cannot forward events.");
            }
            return onErrorForwarder.onError(mediaPlayer, what, extra);
        }
    };

    private final MediaPlayer.OnBufferingUpdateListener internalBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            currentBufferPercentage = percent;
        }
    };

    void setForwarder(MediaPlayerForwarder forwarder) {
        this.onPreparedForwarder = forwarder.onPreparedListener();
        this.onCompletionForwarder = forwarder.onCompletionListener();
        this.onErrorForwarder = forwarder.onErrorListener();
        this.onVideoSizeChangedForwarder = forwarder.onSizeChangedListener();
    }

    void release() {
        if (hasPlayer()) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            currentState = PlaybackState.IDLE;
        }
    }

    private boolean hasPlayer() {
        return mediaPlayer != null;
    }

    void start() {
        if (playbackStateChecker.isInPlaybackState(mediaPlayer, currentState)) {
            if (surfaceHolderRequester == null) {
                throw new IllegalStateException("Must set a SurfaceHolderRequester before starting video");
            }
            surfaceHolderRequester.requestSurfaceHolder(new SurfaceHolderRequester.Callback() {
                @Override
                public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                    mediaPlayer.setDisplay(surfaceHolder);
                    currentState = PLAYING;
                    mediaPlayer.start();
                }
            });
        }
    }

    void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
            currentState = PAUSED;
        }
    }

    int getDuration() {
        if (playbackStateChecker.isInPlaybackState(mediaPlayer, currentState)) {
            return mediaPlayer.getDuration();
        }

        return -1;
    }

    int getCurrentPosition() {
        if (playbackStateChecker.isInPlaybackState(mediaPlayer, currentState)) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    void seekTo(int msec) {
        if (playbackStateChecker.isInPlaybackState(mediaPlayer, currentState)) {
            mediaPlayer.seekTo(msec);
        }
    }

    boolean isPlaying() {
        return playbackStateChecker.isPlaying(mediaPlayer, currentState);
    }

    int getBufferPercentage() {
        if (hasPlayer()) {
            return currentBufferPercentage;
        }
        return 0;
    }

    void stop() {
        if (hasPlayer()) {
            mediaPlayer.stop();
        }
    }

    List<PlayerAudioTrack> getAudioTracks() {
        return trackSelector.getAudioTracks(mediaPlayer);
    }

    void selectAudioTrack(PlayerAudioTrack playerAudioTrack) {
        trackSelector.selectAudioTrack(mediaPlayer, playerAudioTrack);
    }

    void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener seekToResettingSeekListener) {
        mediaPlayer.setOnSeekCompleteListener(seekToResettingSeekListener);
    }
}
