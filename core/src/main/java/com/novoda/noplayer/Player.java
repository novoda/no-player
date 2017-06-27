package com.novoda.noplayer;

import android.net.Uri;

import com.novoda.noplayer.model.Bitrate;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.Timeout;
import com.novoda.noplayer.model.VideoPosition;
import com.novoda.noplayer.player.PlayerInformation;

import java.util.List;
import java.util.Map;

public interface Player extends PlayerState {

    /**
     * Plays content of a prepared Player.
     *
     * @see Player.PreparedListener
     */
    void play();

    /**
     * Plays content of a prepared Player at a position.
     *
     * @param position to start playing content from.
     * @see Player.PreparedListener
     */
    void play(VideoPosition position);

    /**
     * Pauses content of a Player.
     */
    void pause();

    /**
     * Seeks content of a prepared Player to a given position.
     * Will not cause content to play if not already playing.
     *
     * @param position to seek content to.
     * @see Player.PreparedListener
     */
    void seekTo(VideoPosition position);

    /**
     * Stops playback of content and then requires call to {@link Player#loadVideo(Uri, ContentType)} to continue playback.
     */
    void stop();

    /**
     * Stops playback of content and drops all internal resources. The instance of Player should not be
     * used after calling release.
     */
    void release();

    /**
     * Loads the video content and triggers the {@link Player.PreparedListener}.
     *
     * @param uri         link to the content.
     * @param contentType format of the content.
     */
    void loadVideo(Uri uri, ContentType contentType);

    /**
     * Loads the video content and triggers the {@link Player.PreparedListener}.
     *
     * @param uri                 link to the content.
     * @param contentType         format of the content.
     * @param timeout             amount of time to wait before triggering {@link LoadTimeoutCallback}.
     * @param loadTimeoutCallback callback when loading has hit the timeout.
     */
    void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback);

    /**
     * Supplies information about the underlying player.
     *
     * @return {@link PlayerInformation}.
     */
    PlayerInformation getPlayerInformation();

    /**
     * Attaches a given {@link PlayerView} to the Player.
     *
     * @param playerView for displaying video content.
     */
    void attach(PlayerView playerView);

    /**
     * Detaches a given {@link PlayerView} from the Player.
     *
     * @param playerView for displaying video content.
     */
    void detach(PlayerView playerView);

    /**
     * Retrieves all of the available {@link PlayerAudioTrack} of a prepared Player.
     *
     * @return A list of available {@link PlayerAudioTrack}.
     * @see Player.PreparedListener
     */
    List<PlayerAudioTrack> getAudioTracks();

    /**
     * Selects a given {@link PlayerAudioTrack}.
     *
     * @param audioTrack the audio track to select.
     * @return whether the selection was successful.
     */
    boolean selectAudioTrack(PlayerAudioTrack audioTrack);

    /**
     * Retrieves all of the available {@link PlayerSubtitleTrack} of a prepared Player.
     *
     * @return A list of available {@link PlayerSubtitleTrack}.
     * @see Player.PreparedListener
     */
    List<PlayerSubtitleTrack> getSubtitleTracks();

    /**
     * Selects a given {@link PlayerSubtitleTrack}.
     *
     * @param subtitleTrack the subtitle track to select.
     * @return whether the selection was successful.
     */
    boolean showSubtitleTrack(PlayerSubtitleTrack subtitleTrack);

    /**
     * Clear and hide the subtitles.
     */
    void hideSubtitleTrack();

    /**
     * Retrieves a holder, which allows you to add and remove listeners to on the Player.
     *
     * @return {@link Listeners} holder.
     */
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
