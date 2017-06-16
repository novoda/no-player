package com.novoda.noplayer;

import android.net.Uri;

import com.novoda.noplayer.player.PlayerInformation;

import java.util.List;
import java.util.Map;

public interface Player extends PlayerState {

    void play();

    void play(VideoPosition position);

    void pause();

    void seekTo(VideoPosition position);

    void stop();

    void release();

    void loadVideo(Uri uri, ContentType contentType);

    void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback);

    PlayerInformation getPlayerInformation();

    void attach(PlayerView playerView);

    void detach(PlayerView playerView);

    void selectAudioTrack(PlayerAudioTrack audioTrack);

    void showSubtitleTrack(PlayerSubtitleTrack subtitleTrack);

    void showFirstAvailableSubtitleTrack();

    void hideSubtitleTrack();

    List<PlayerAudioTrack> getAudioTracks();

    List<PlayerSubtitleTrack> getSubtitleTracks();

    Listeners getListeners();

    interface PlayerError {

        String getType();

        Throwable getCause();
    }

    interface ErrorListener {

        void onError(Player player, PlayerError error);
    }

    interface PreparedListener {

        void onPrepared(PlayerState playerState);
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

        void onVideoStopped();
    }

    interface BitrateChangedListener {

        void onBitrateChanged(Bitrate audioBitrate, Bitrate videoBitrate);
    }

    interface VideoSizeChangedListener {

        void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio);
    }

    /**
     * A listener for debugging information.
     */
    interface InfoListener {

        /**
         * All event listeners attached to implementations of Player will
         * forward information through this to provide debugging
         * information to client applications.
         *
         * @param callingMethod       The method name from where this call originated.
         * @param callingMethodParams Parameter name and value pairs from where this call originated.
         *                            Pass only string representations not whole objects.
         */
        void onNewInfo(String callingMethod, Map<String, String> callingMethodParams);
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
