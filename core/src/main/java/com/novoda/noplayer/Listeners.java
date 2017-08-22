package com.novoda.noplayer;

public interface Listeners {

    void addErrorListener(NoPlayer.ErrorListener errorListener);

    void removeErrorListener(NoPlayer.ErrorListener errorListener);

    void addPreparedListener(NoPlayer.PreparedListener preparedListener);

    void removePreparedListener(NoPlayer.PreparedListener preparedListener);

    void addBufferStateListener(NoPlayer.BufferStateListener bufferStateListener);

    void removeBufferStateListener(NoPlayer.BufferStateListener bufferStateListener);

    void addCompletionListener(NoPlayer.CompletionListener completionListener);

    void removeCompletionListener(NoPlayer.CompletionListener completionListener);

    void addStateChangedListener(NoPlayer.StateChangedListener stateChangedListener);

    void removeStateChangedListener(NoPlayer.StateChangedListener stateChangedListener);

    void addInfoListener(NoPlayer.InfoListener infoListener);

    void removeInfoListener(NoPlayer.InfoListener infoListener);

    void addBitrateChangedListener(NoPlayer.BitrateChangedListener bitrateChangedListener);

    void removeBitrateChangedListener(NoPlayer.BitrateChangedListener bitrateChangedListener);

    void addHeartbeatCallback(NoPlayer.HeartbeatCallback heartbeatCallback);

    void removeHeartbeatCallback(NoPlayer.HeartbeatCallback heartbeatCallback);

    void addVideoSizeChangedListener(NoPlayer.VideoSizeChangedListener videoSizeChangedListener);

    void removeVideoSizeChangedListener(NoPlayer.VideoSizeChangedListener videoSizeChangedListener);
}
