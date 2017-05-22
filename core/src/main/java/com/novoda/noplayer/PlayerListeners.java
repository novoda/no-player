package com.novoda.noplayer;

import com.novoda.noplayer.exoplayer.InfoListener;

public interface PlayerListeners {

    void addErrorListener(Player.ErrorListener errorListener);

    void removeErrorListener(Player.ErrorListener errorListener);

    void addPreparedListener(Player.PreparedListener preparedListener);

    void removePreparedListener(Player.PreparedListener preparedListener);

    void addBufferStateListener(Player.BufferStateListener bufferStateListener);

    void removeBufferStateListener(Player.BufferStateListener bufferStateListener);

    void addCompletionListener(Player.CompletionListener completionListener);

    void removeCompletionListener(Player.CompletionListener completionListener);

    void addStateChangedListener(Player.StateChangedListener stateChangedListener);

    void removeStateChangedListener(Player.StateChangedListener stateChangedListener);

    void addInfoListener(InfoListener infoListener);

    void removeInfoListener(InfoListener infoListener);

    void setPreReleaseListener(Player.PreReleaseListener playerReleaseListener);

    void addHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback);

    void removeHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback);

    void addVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener);

    void removeVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener);
}
