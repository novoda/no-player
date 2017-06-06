package com.novoda.noplayer;

import com.novoda.noplayer.Player.BitrateChangedListener;
import com.novoda.noplayer.listeners.BitrateChangedListeners;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

public class PlayerListenersHolder implements Listeners {

    private final ErrorListeners errorListeners;
    private final PreparedListeners preparedListeners;
    private final BufferStateListeners bufferStateListeners;
    private final CompletionListeners completionListeners;
    private final StateChangedListeners stateChangedListeners;
    private final InfoListeners infoListeners;
    private final VideoSizeChangedListeners videoSizeChangedListeners;
    private final BitrateChangedListeners bitrateChangedListeners;

    private final HeartbeatCallbacks<Player> heartbeatCallbacks;

    public PlayerListenersHolder() {
        errorListeners = new ErrorListeners();
        preparedListeners = new PreparedListeners();
        bufferStateListeners = new BufferStateListeners();
        completionListeners = new CompletionListeners();
        stateChangedListeners = new StateChangedListeners();
        infoListeners = new InfoListeners();
        heartbeatCallbacks = new HeartbeatCallbacks<>();
        videoSizeChangedListeners = new VideoSizeChangedListeners();
        bitrateChangedListeners = new BitrateChangedListeners();
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
    public void addInfoListener(Player.InfoListener infoListener) {
        infoListeners.add(infoListener);
    }

    @Override
    public void removeInfoListener(Player.InfoListener infoListener) {
        infoListeners.remove(infoListener);
    }

    @Override
    public void addBitrateChangedListener(BitrateChangedListener bitrateChangedListener) {
        bitrateChangedListeners.add(bitrateChangedListener);
    }

    @Override
    public void removeBitrateChangedListener(BitrateChangedListener bitrateChangedListener) {
        bitrateChangedListeners.remove(bitrateChangedListener);
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

    @Override
    public ErrorListeners getErrorListeners() {
        return errorListeners;
    }

    @Override
    public PreparedListeners getPreparedListeners() {
        return preparedListeners;
    }

    @Override
    public BufferStateListeners getBufferStateListeners() {
        return bufferStateListeners;
    }

    @Override
    public CompletionListeners getCompletionListeners() {
        return completionListeners;
    }

    @Override
    public StateChangedListeners getStateChangedListeners() {
        return stateChangedListeners;
    }

    @Override
    public InfoListeners getInfoListeners() {
        return infoListeners;
    }

    @Override
    public HeartbeatCallbacks<Player> getHeartbeatCallbacks() {
        return heartbeatCallbacks;
    }

    @Override
    public VideoSizeChangedListeners getVideoSizeChangedListeners() {
        return videoSizeChangedListeners;
    }

    @Override
    public BitrateChangedListeners getBitrateChangedListeners() {
        return bitrateChangedListeners;
    }
}
