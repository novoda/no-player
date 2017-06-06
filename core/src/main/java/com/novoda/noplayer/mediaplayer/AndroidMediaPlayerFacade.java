package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.view.SurfaceHolder;

import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.IDLE;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.PAUSED;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.PLAYING;

class AndroidMediaPlayerFacade {

    private static final Map<String, String> NO_HEADERS = null;

    private final Context context;
    private final AudioManager audioManager;
    private final AndroidMediaPlayerAudioTrackSelector trackSelector;
    private final PlaybackStateChecker playbackStateChecker;

    private PlaybackState currentState = IDLE;

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener;
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private int currentBufferPercentage;
    private MediaPlayer.OnErrorListener onErrorListener;
    private MediaPlayer.OnVideoSizeChangedListener onSizeChangedListener;

    private SurfaceHolderRequester surfaceHolderRequester;

    static AndroidMediaPlayerFacade newInstance(Context context) {
        AndroidMediaPlayerAudioTrackSelector trackSelector = new AndroidMediaPlayerAudioTrackSelector();
        PlaybackStateChecker playbackStateChecker = new PlaybackStateChecker();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return new AndroidMediaPlayerFacade(context, audioManager, trackSelector, playbackStateChecker);
    }

    AndroidMediaPlayerFacade(Context context, AudioManager audioManager, AndroidMediaPlayerAudioTrackSelector trackSelector, PlaybackStateChecker playbackStateChecker) {
        this.context = context;
        this.audioManager = audioManager;
        this.trackSelector = trackSelector;
        this.playbackStateChecker = playbackStateChecker;
    }

    void setSurfaceHolderRequester(SurfaceHolderRequester surfaceHolderRequester) {
        this.surfaceHolderRequester = surfaceHolderRequester;
    }

    void prepareVideo(final Uri videoUri) {
        if (surfaceHolderRequester == null) {
            logPlayerNotAttachedWarning("prepareVideo()");
            return;
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
        MediaPlayer mediaPlayer = createMediaPlayer();
        mediaPlayer.setOnPreparedListener(internalPeparedListener);
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

    // TODO: Tracked in https://github.com/novoda/no-player/issues/35
    @VisibleForTesting
    protected MediaPlayer createMediaPlayer() {
        return new MediaPlayer();
    }

    private void reportCreationError(Exception ex, Uri videoUri) {
        Log.w(ex, "Unable to open content: " + videoUri);
        currentState = PlaybackState.ERROR;
        internalErrorListener.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
    }

    private final MediaPlayer.OnVideoSizeChangedListener internalSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (onSizeChangedListener == null) {
                return;
            }
            onSizeChangedListener.onVideoSizeChanged(mp, width, height);
        }
    };

    private final MediaPlayer.OnPreparedListener internalPeparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            currentState = PlaybackState.PREPARED;

            if (onPreparedListener != null) {
                onPreparedListener.onPrepared(mediaPlayer);
            }
        }
    };

    private final MediaPlayer.OnCompletionListener internalCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            currentState = PlaybackState.COMPLETED;
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(mediaPlayer);
            }
        }
    };

    private final MediaPlayer.OnErrorListener internalErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d("Error: " + what + "," + extra);
            currentState = PlaybackState.ERROR;
            if (onErrorListener != null) {
                return onErrorListener.onError(mediaPlayer, what, extra);
            }
            return true;
        }
    };

    private final MediaPlayer.OnBufferingUpdateListener internalBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            currentBufferPercentage = percent;
        }
    };

    void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        onPreparedListener = listener;
    }

    void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        onCompletionListener = listener;
    }

    void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        onErrorListener = listener;
    }

    void setOnSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener) {
        onSizeChangedListener = listener;
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
                logPlayerNotAttachedWarning("start()");
                return;
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

    private void logPlayerNotAttachedWarning(String action) {
        Log.w(String.format("Attempt to %s the video has been ignored because the Player has not been attached to a PlayerView", action));
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
