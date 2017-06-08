package com.novoda.noplayer;

import com.novoda.noplayer.listeners.BitrateChangedListeners;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

public interface Listeners {

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

    void addInfoListener(Player.InfoListener infoListener);

    void removeInfoListener(Player.InfoListener infoListener);

    void addBitrateChangedListener(Player.BitrateChangedListener bitrateChangedListener);

    void removeBitrateChangedListener(Player.BitrateChangedListener bitrateChangedListener);

    void addHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback);

    void removeHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback);

    void addVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener);

    void removeVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener);

    ErrorListeners getErrorListeners();

    PreparedListeners getPreparedListeners();

    BufferStateListeners getBufferStateListeners();

    CompletionListeners getCompletionListeners();

    StateChangedListeners getStateChangedListeners();

    InfoListeners getInfoListeners();

    HeartbeatCallbacks<Player> getHeartbeatCallbacks();

    VideoSizeChangedListeners getVideoSizeChangedListeners();

    BitrateChangedListeners getBitrateChangedListeners();
}
