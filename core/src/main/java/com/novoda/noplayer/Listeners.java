package com.novoda.noplayer;

public interface Listeners {

    /**
     * Add an {@link NoPlayer.ErrorListener} to be notified of Player errors.
     *
     * @param errorListener to notify.
     */
    void addErrorListener(NoPlayer.ErrorListener errorListener);

    /**
     * Remove a given {@link NoPlayer.ErrorListener}.
     *
     * @param errorListener to remove.
     */
    void removeErrorListener(NoPlayer.ErrorListener errorListener);

    /**
     * Add a {@link NoPlayer.PreparedListener} to be notified when the {@link NoPlayer}
     * is prepared and playback can begin.
     *
     * @param preparedListener to notify.
     */
    void addPreparedListener(NoPlayer.PreparedListener preparedListener);

    /**
     * Remove a given {@link NoPlayer.PreparedListener}.
     *
     * @param preparedListener to remove.
     */
    void removePreparedListener(NoPlayer.PreparedListener preparedListener);

    /**
     * Add a {@link NoPlayer.BufferStateListener} to be notified of buffer state events.
     *
     * @param bufferStateListener to notify.
     */
    void addBufferStateListener(NoPlayer.BufferStateListener bufferStateListener);

    /**
     * Remove a given {@link NoPlayer.BufferStateListener}.
     *
     * @param bufferStateListener to remove.
     */
    void removeBufferStateListener(NoPlayer.BufferStateListener bufferStateListener);

    /**
     * Add a {@link NoPlayer.CompletionListener} to be notified when a media asset has completed playback.
     *
     * @param completionListener to notify.
     */
    void addCompletionListener(NoPlayer.CompletionListener completionListener);

    /**
     * Remove a given {@link NoPlayer.CompletionListener}.
     *
     * @param completionListener to remove.
     */
    void removeCompletionListener(NoPlayer.CompletionListener completionListener);

    /**
     * Add a {@link NoPlayer.StateChangedListener} to be notified of Player state changes.
     * e.g. Play/Pause/Stop
     *
     * @param stateChangedListener to notify.
     */
    void addStateChangedListener(NoPlayer.StateChangedListener stateChangedListener);

    /**
     * Remove a given {@link NoPlayer.StateChangedListener}.
     *
     * @param stateChangedListener to remove.
     */
    void removeStateChangedListener(NoPlayer.StateChangedListener stateChangedListener);

    /**
     * Add an {@link NoPlayer.InfoListener} to be notified of internal player callbacks
     * with additional information.
     *
     * @param infoListener to notify.
     */
    void addInfoListener(NoPlayer.InfoListener infoListener);

    /**
     * Remove a given {@link NoPlayer.InfoListener}.
     *
     * @param infoListener to remove.
     */
    void removeInfoListener(NoPlayer.InfoListener infoListener);

    /**
     * Add a {@link NoPlayer.BitrateChangedListener} to be notified of video and audio bitrate changes.
     *
     * @param bitrateChangedListener to notify.
     */
    void addBitrateChangedListener(NoPlayer.BitrateChangedListener bitrateChangedListener);

    /**
     * Remove a given {@link NoPlayer.BitrateChangedListener}.
     *
     * @param bitrateChangedListener to remove.
     */
    void removeBitrateChangedListener(NoPlayer.BitrateChangedListener bitrateChangedListener);

    /**
     * Add a {@link NoPlayer.HeartbeatCallback} to be notified on every tick of playback with a {@link NoPlayer}.
     *
     * @param heartbeatCallback to notify.
     */
    void addHeartbeatCallback(NoPlayer.HeartbeatCallback heartbeatCallback);

    /**
     * Remove a given {@link NoPlayer.HeartbeatCallback}.
     *
     * @param heartbeatCallback to remove.
     */
    void removeHeartbeatCallback(NoPlayer.HeartbeatCallback heartbeatCallback);

    /**
     * Add a {@link NoPlayer.VideoSizeChangedListener} to be notified whenever the video size changes.
     *
     * @param videoSizeChangedListener to notify.
     */
    void addVideoSizeChangedListener(NoPlayer.VideoSizeChangedListener videoSizeChangedListener);

    /**
     * Remove a given {@link NoPlayer.VideoSizeChangedListener}.
     *
     * @param videoSizeChangedListener to remove.
     */
    void removeVideoSizeChangedListener(NoPlayer.VideoSizeChangedListener videoSizeChangedListener);

    /**
     * Add a given {@link NoPlayer.DroppedVideoFramesListener} to be notified when video playback drops frames
     *
     * @param droppedVideoFramesListener to notify
     */
    void addDroppedVideoFrames(NoPlayer.DroppedVideoFramesListener droppedVideoFramesListener);

    /**
     * Remove a given {@link NoPlayer.DroppedVideoFramesListener}.
     *
     * @param droppedVideoFramesListener to remove.
     */
    void removeDroppedVideoFrames(NoPlayer.DroppedVideoFramesListener droppedVideoFramesListener);
}
