package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState;
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.utils.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.IDLE;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.PAUSED;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.PLAYING;

class AndroidMediaPlayerFacade {

    private static final Map<String, String> NO_HEADERS = null;

    private final Context context;
    private final MediaPlayerForwarder forwarder;
    private final AudioManager audioManager;
    private final AndroidMediaPlayerAudioTrackSelector trackSelector;
    private final PlaybackStateChecker playbackStateChecker;

    private PlaybackState currentState = IDLE;

    private MediaPlayer mediaPlayer;
    private int currentBufferPercentage;

    private MediaPlayerCreator mediaPlayerCreator;

    static AndroidMediaPlayerFacade newInstance(Context context, MediaPlayerForwarder forwarder) {
        TrackInfosFactory trackInfosFactory = new TrackInfosFactory();
        AndroidMediaPlayerAudioTrackSelector trackSelector = new AndroidMediaPlayerAudioTrackSelector(trackInfosFactory);
        PlaybackStateChecker playbackStateChecker = new PlaybackStateChecker();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayerCreator mediaPlayerCreator = new MediaPlayerCreator();
        return new AndroidMediaPlayerFacade(context, forwarder, audioManager, trackSelector, playbackStateChecker, mediaPlayerCreator);
    }

    AndroidMediaPlayerFacade(Context context,
                             MediaPlayerForwarder forwarder,
                             AudioManager audioManager,
                             AndroidMediaPlayerAudioTrackSelector trackSelector,
                             PlaybackStateChecker playbackStateChecker,
                             MediaPlayerCreator mediaPlayerCreator) {
        this.context = context;
        this.forwarder = forwarder;
        this.audioManager = audioManager;
        this.trackSelector = trackSelector;
        this.playbackStateChecker = playbackStateChecker;
        this.mediaPlayerCreator = mediaPlayerCreator;
    }

    void prepareVideo(Uri videoUri, SurfaceHolder surfaceHolder) {
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
            MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedForwarder = forwarder.onSizeChangedListener();
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

            MediaPlayer.OnPreparedListener onPreparedForwarder = forwarder.onPreparedListener();
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
            MediaPlayer.OnCompletionListener onCompletionForwarder = forwarder.onCompletionListener();
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
            MediaPlayer.OnErrorListener onErrorForwarder = forwarder.onErrorListener();
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

    void release() {
        if (hasPlayer()) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            currentState = PlaybackState.IDLE;
        }
    }

    void start(SurfaceHolder surfaceHolder) {
        assertIsInPlaybackState();
        mediaPlayer.setDisplay(surfaceHolder);
        currentState = PLAYING;
        mediaPlayer.start();
    }

    void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
            currentState = PAUSED;
        }
    }

    int getDuration() {
        assertIsInPlaybackState();
        return mediaPlayer.getDuration();
    }

    int getCurrentPosition() {
        assertIsInPlaybackState();
        return mediaPlayer.getCurrentPosition();
    }

    void seekTo(int msec) {
        assertIsInPlaybackState();
        mediaPlayer.seekTo(msec);
    }

    boolean isPlaying() {
        return playbackStateChecker.isPlaying(mediaPlayer, currentState);
    }

    int getBufferPercentage() {
        assertIsInPlaybackState();
        return currentBufferPercentage;
    }

    void stop() {
        assertIsInPlaybackState();
        mediaPlayer.stop();
    }

    List<PlayerAudioTrack> getAudioTracks() {
        assertIsInPlaybackState();
        return trackSelector.getAudioTracks(mediaPlayer);
    }

    void selectAudioTrack(PlayerAudioTrack playerAudioTrack) {
        assertIsInPlaybackState();
        trackSelector.selectAudioTrack(mediaPlayer, playerAudioTrack);
    }

    void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener seekToResettingSeekListener) {
        assertIsInPlaybackState();
        mediaPlayer.setOnSeekCompleteListener(seekToResettingSeekListener);
    }

    private void assertIsInPlaybackState() {
        if (!playbackStateChecker.isInPlaybackState(mediaPlayer, currentState)) {
            throw new IllegalStateException("Video must be loaded and not in an error state before trying to interact with the player");
        }
    }

    public boolean hasPlayedContent() {
        return hasPlayer();
    }

    private boolean hasPlayer() {
        return mediaPlayer != null;
    }
}
