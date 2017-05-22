package com.novoda.noplayer;

import com.novoda.noplayer.exoplayer.InfoListener;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

public class PlayerListenersHolder implements PlayerListeners {

    private final ErrorListeners errorListeners;
    private final PreparedListeners preparedListeners;
    private final BufferStateListeners bufferStateListeners;
    private final CompletionListeners completionListeners;
    private final StateChangedListeners stateChangedListeners;
    private final InfoListeners infoListeners;
    private final VideoSizeChangedListeners videoSizeChangedListeners;

    private final HeartbeatCallbacks<Player> heartbeatCallbacks;

    private Player.PreReleaseListener playerReleaseListener = Player.PreReleaseListener.NULL_IMPL;

    public PlayerListenersHolder() {
        errorListeners = new ErrorListeners();
        preparedListeners = new PreparedListeners();
        bufferStateListeners = new BufferStateListeners();
        completionListeners = new CompletionListeners();
        stateChangedListeners = new StateChangedListeners();
        infoListeners = new InfoListeners();
        heartbeatCallbacks = new HeartbeatCallbacks<>();
        videoSizeChangedListeners = new VideoSizeChangedListeners();
    }

    @Override
    public void addErrorListener(Player.ErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    @Override
    public void removeErrorListener(Player.ErrorListener errorListener) {
        errorListeners.remove(errorListener);
    }

    @Override
    public void addPreparedListener(Player.PreparedListener preparedListener) {
        preparedListeners.add(preparedListener);
    }

    @Override
    public void removePreparedListener(Player.PreparedListener preparedListener) {
        preparedListeners.remove(preparedListener);
    }

    @Override
    public void addBufferStateListener(Player.BufferStateListener bufferStateListener) {
        bufferStateListeners.add(bufferStateListener);
    }

    @Override
    public void removeBufferStateListener(Player.BufferStateListener bufferStateListener) {
        bufferStateListeners.remove(bufferStateListener);
    }

    @Override
    public void addCompletionListener(Player.CompletionListener completionListener) {
        completionListeners.add(completionListener);
    }

    @Override
    public void removeCompletionListener(Player.CompletionListener completionListener) {
        completionListeners.remove(completionListener);
    }

    @Override
    public void addStateChangedListener(Player.StateChangedListener stateChangedListener) {
        stateChangedListeners.add(stateChangedListener);
    }

    @Override
    public void removeStateChangedListener(Player.StateChangedListener stateChangedListener) {
        stateChangedListeners.remove(stateChangedListener);
    }

    @Override
    public void addInfoListener(InfoListener infoListener) {
        infoListeners.add(infoListener);
    }

    @Override
    public void removeInfoListener(InfoListener infoListener) {
        infoListeners.remove(infoListener);
    }

    @Override
    public void setPreReleaseListener(Player.PreReleaseListener playerReleaseListener) {
        this.playerReleaseListener = playerReleaseListener;
    }

    @Override
    public void addHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback) {
        heartbeatCallbacks.registerCallback(callback);
    }

    @Override
    public void removeHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback) {
        heartbeatCallbacks.unregisterCallback(callback);
    }

    @Override
    public void addVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener) {
        videoSizeChangedListeners.add(videoSizeChangedListener);
    }

    @Override
    public void removeVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener) {
        videoSizeChangedListeners.remove(videoSizeChangedListener);
    }

    protected final ErrorListeners getErrorListeners() {
        return errorListeners;
    }

    protected final PreparedListeners getPreparedListeners() {
        return preparedListeners;
    }

    protected final BufferStateListeners getBufferStateListeners() {
        return bufferStateListeners;
    }

    protected final CompletionListeners getCompletionListeners() {
        return completionListeners;
    }

    protected final StateChangedListeners getStateChangedListeners() {
        return stateChangedListeners;
    }

    protected final InfoListeners getInfoListeners() {
        return infoListeners;
    }

    protected final Player.PreReleaseListener getPlayerReleaseListener() {
        return playerReleaseListener;
    }

    protected final HeartbeatCallbacks<Player> getHeartbeatCallbacks() {
        return heartbeatCallbacks;
    }

    protected final VideoSizeChangedListeners getVideoSizeChangedListeners() {
        return videoSizeChangedListeners;
    }
}
