package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidMediaPlayerFacade {

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final Map<String, String> NO_HEADERS = null;

    private final Context context;

    private int currentState = STATE_IDLE;

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener;
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private int currentBufferPercentage;
    private MediaPlayer.OnErrorListener onErrorListener;
    private MediaPlayer.OnVideoSizeChangedListener onSizeChangedListener;

    private SurfaceHolderRequester surfaceHolderRequester;

    public AndroidMediaPlayerFacade(Context context) {
        this.context = context;
        currentState = STATE_IDLE;
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
                    currentState = STATE_PREPARING;
                    mediaPlayer = createMediaPlayer(surfaceHolder, videoUri);
                    mediaPlayer.prepareAsync();
                } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
                    reportCreationError(ex, videoUri);
                }
            }
        });
    }

    private void requestAudioFocus() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private MediaPlayer createMediaPlayer(SurfaceHolder surfaceHolder, Uri videoUri) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
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

    private void reportCreationError(Exception ex, Uri videoUri) {
        Log.w(ex, "Unable to open content: " + videoUri);
        currentState = STATE_ERROR;
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
            currentState = STATE_PREPARED;

            if (onPreparedListener != null) {
                onPreparedListener.onPrepared(mediaPlayer);
            }
        }
    };

    private final MediaPlayer.OnCompletionListener internalCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            currentState = STATE_PLAYBACK_COMPLETED;
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(mediaPlayer);
            }
        }
    };

    private final MediaPlayer.OnErrorListener internalErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d("Error: " + what + "," + extra);
            currentState = STATE_ERROR;
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

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        onPreparedListener = listener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        onCompletionListener = listener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        onErrorListener = listener;
    }

    public void setOnSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener) {
        onSizeChangedListener = listener;
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            currentState = STATE_IDLE;
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            if (surfaceHolderRequester == null) {
                logPlayerNotAttachedWarning("start()");
                return;
            }
            surfaceHolderRequester.requestSurfaceHolder(new SurfaceHolderRequester.Callback() {
                @Override
                public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                    mediaPlayer.setDisplay(surfaceHolder);
                    currentState = STATE_PLAYING;
                    mediaPlayer.start();
                }
            });
        }
    }

    private void logPlayerNotAttachedWarning(String action) {
        Log.w(String.format("Attempt to %s the video has been ignored because the Player has not been attached to a PlayerView", action));
    }

    public void pause() {
        if (isInPlaybackState() && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentState = STATE_PAUSED;
        }
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return mediaPlayer.getDuration();
        }

        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mediaPlayer.seekTo(msec);
        }
    }

    public boolean isPlaying() {
        return isInPlaybackState() && mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (mediaPlayer != null) {
            return currentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return mediaPlayer != null
                && currentState != STATE_ERROR
                && currentState != STATE_IDLE
                && currentState != STATE_PREPARING;
    }

    public List<PlayerAudioTrack> getAudioTracks() {
        ArrayList<PlayerAudioTrack> audioTracks = new ArrayList<>();
        for (MediaPlayer.TrackInfo trackInfo : mediaPlayer.getTrackInfo()) {
            if (trackInfo.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                audioTracks.add(new PlayerAudioTrack(String.valueOf(trackInfo.hashCode()), trackInfo.getLanguage(), "", -1, -1));
            }
        }
        return audioTracks;
    }

    public void setAudioTrack(int audioTrackIndex) {
        int index = 0;
        MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();

        for (int i = 0; i < trackInfos.length; i++) {
            MediaPlayer.TrackInfo trackInfo = trackInfos[i];
            if (trackInfo.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO && index == audioTrackIndex) {
                mediaPlayer.selectTrack(i);
            } else if (trackInfo.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                index++;
            }
        }
        Log.e(String.format(
                "Attempt to %s has been ignored because an invalid position was specified: %s, total: %s",
                "selectAudioTrack()",
                audioTrackIndex,
                index
              )
        );
    }
}
