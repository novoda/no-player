package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Heart;
import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.Player.BitrateChangedListener;

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
        videoSizeChangedListeners = new VideoSizeChangedListeners();
        bitrateChangedListeners = new BitrateChangedListeners();
        heartbeatCallbacks = new HeartbeatCallbacks<>();
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

    public Player.ErrorListener getErrorListeners() {
        return errorListeners;
    }

    public Player.PreparedListener getPreparedListeners() {
        return preparedListeners;
    }

    public Player.BufferStateListener getBufferStateListeners() {
        return bufferStateListeners;
    }

    public Player.CompletionListener getCompletionListeners() {
        return completionListeners;
    }

    public Player.StateChangedListener getStateChangedListeners() {
        return stateChangedListeners;
    }

    public Player.InfoListener getInfoListeners() {
        return infoListeners;
    }

    public Heart.Heartbeat.Callback<Player> getHeartbeatCallbacks() {
        return heartbeatCallbacks;
    }

    public Player.VideoSizeChangedListener getVideoSizeChangedListeners() {
        return videoSizeChangedListeners;
    }

    public Player.BitrateChangedListener getBitrateChangedListeners() {
        return bitrateChangedListeners;
    }

    public void clear() {
        errorListeners.clear();
        preparedListeners.clear();
        bufferStateListeners.clear();
        completionListeners.clear();
        stateChangedListeners.clear();
        infoListeners.clear();
        videoSizeChangedListeners.clear();
        bitrateChangedListeners.clear();
        heartbeatCallbacks.clear();
    }
}
