package com.novoda.noplayer;

import android.net.Uri;

import com.novoda.noplayer.exoplayer.Bitrate;
import com.novoda.noplayer.player.PlayerInformation;

public interface Player extends PlayerState, PlayerListeners {

    void play();

    void play(Time position);

    void pause();

    void seekTo(Time position);

    void reset();

    void release();

    void loadVideo(Uri uri, ContentType contentType);

    void loadVideoWithTimeout(Uri uri, ContentType contentType, Time timeout, LoadTimeoutCallback loadTimeoutCallback);

    PlayerInformation getPlayerInformation();

    void attach(PlayerView playerView);

    interface PreReleaseListener {

        PreReleaseListener NULL_IMPL = new PreReleaseListener() {
            @Override
            public void onPlayerPreRelease(Player player) {
                // No-op
            }
        };

        void onPlayerPreRelease(Player player);
    }

    interface PlayerError {

        String getType();

        Throwable getCause();

    }

    interface ErrorListener {

        void onError(Player player, PlayerError error);
    }

    interface PreparedListener {

        void onPrepared(PlayerState playerMonitor);
    }

    interface BufferStateListener {

        void onBufferStarted();

        void onBufferCompleted();
    }

    interface CompletionListener {

        void onCompletion();
    }

    interface StateChangedListener {

        void onVideoPlaying();

        void onVideoPaused();

        void onVideoReleased();
    }

    interface BitrateChangedListener {

        void onBitrateChanged(Bitrate audioBitrate, Bitrate videoBitrate);
    }

    interface VideoSizeChangedListener {

        void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio);
    }

    interface LoadTimeoutCallback {

        LoadTimeoutCallback NULL_IMPL = new LoadTimeoutCallback() {
            @Override
            public void onLoadTimeout() {
                // do nothing
            }
        };

        void onLoadTimeout();
    }
}
