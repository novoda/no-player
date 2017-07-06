package com.novoda.noplayer;

import android.net.Uri;

import com.novoda.noplayer.model.Bitrate;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.Timeout;
import com.novoda.noplayer.model.VideoPosition;

import java.util.List;
import java.util.Map;

public interface Player extends PlayerState {

    /**
     * Plays content of a prepared Player.
     *
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     * @see Player.PreparedListener
     */
    void play() throws IllegalStateException;

    /**
     * Plays content of a prepared Player at a position.
     *
     * @param position to start playing content from.
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     * @see Player.PreparedListener
     */
    void play(VideoPosition position) throws IllegalStateException;

    /**
     * Pauses content of a prepared Player.
     *
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     * @see Player.PreparedListener
     */
    void pause() throws IllegalStateException;

    /**
     * Seeks content of a prepared Player to a given position.
     * Will not cause content to play if not already playing.
     *
     * @param position to seek content to.
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     * @see Player.PreparedListener
     */
    void seekTo(VideoPosition position) throws IllegalStateException;

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
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     * @see Player.PreparedListener
     */
    List<PlayerAudioTrack> getAudioTracks() throws IllegalStateException;

    /**
     * Selects a given {@link PlayerAudioTrack}.
     *
     * @param audioTrack the audio track to select.
     * @return whether the selection was successful.
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     */
    boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException;

    /**
     * Retrieves all of the available {@link PlayerSubtitleTrack} of a prepared Player.
     *
     * @return A list of available {@link PlayerSubtitleTrack}.
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     * @see Player.PreparedListener
     */
    List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException;

    /**
     * Selects a given {@link PlayerSubtitleTrack} on an attached PlayerView.
     *
     * @param subtitleTrack the subtitle track to select.
     * @return whether the selection was successful.
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     */
    boolean showSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException;

    /**
     * Clear and hide the subtitles on an attached PlayerView.
     *
     * @throws IllegalStateException - if called before {@link Player#loadVideo(Uri, ContentType)}.
     */
    void hideSubtitleTrack() throws IllegalStateException;

    /**
     * Retrieves a holder, which allows you to add and remove listeners to on the Player.
     *
     * @return {@link Listeners} holder.
     */
    Listeners getListeners();

    interface PlayerError {

        PlayerErrorType type();

        String message();
    }

    interface ErrorListener {

        void onError(PlayerError error);
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

    interface HeartbeatCallback {

        void onBeat(Player player);
    }
}
