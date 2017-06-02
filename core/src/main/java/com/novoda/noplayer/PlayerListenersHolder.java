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

public class PlayerListenersHolder {

    private final ErrorListeners errorListeners;
    private final PreparedListeners preparedListeners;
    private final BufferStateListeners bufferStateListeners;
    private final CompletionListeners completionListeners;
    private final StateChangedListeners stateChangedListeners;
    private final InfoListeners infoListeners;
    private final VideoSizeChangedListeners videoSizeChangedListeners;
    private final BitrateChangedListeners bitrateChangedListeners;

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
        bitrateChangedListeners = new BitrateChangedListeners();
    }

    public void addErrorListener(Player.ErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    public void removeErrorListener(Player.ErrorListener errorListener) {
        errorListeners.remove(errorListener);
    }

    public void addPreparedListener(Player.PreparedListener preparedListener) {
        preparedListeners.add(preparedListener);
    }

    public void removePreparedListener(Player.PreparedListener preparedListener) {
        preparedListeners.remove(preparedListener);
    }

    public void addBufferStateListener(Player.BufferStateListener bufferStateListener) {
        bufferStateListeners.add(bufferStateListener);
    }

    public void removeBufferStateListener(Player.BufferStateListener bufferStateListener) {
        bufferStateListeners.remove(bufferStateListener);
    }

    public void addCompletionListener(Player.CompletionListener completionListener) {
        completionListeners.add(completionListener);
    }

    public void removeCompletionListener(Player.CompletionListener completionListener) {
        completionListeners.remove(completionListener);
    }

    public void addStateChangedListener(Player.StateChangedListener stateChangedListener) {
        stateChangedListeners.add(stateChangedListener);
    }

    public void removeStateChangedListener(Player.StateChangedListener stateChangedListener) {
        stateChangedListeners.remove(stateChangedListener);
    }

    public void addInfoListener(Player.InfoListener infoListener) {
        infoListeners.add(infoListener);
    }

    public void removeInfoListener(Player.InfoListener infoListener) {
        infoListeners.remove(infoListener);
    }

    public void addBitrateChangedListener(BitrateChangedListener bitrateChangedListener) {
        bitrateChangedListeners.add(bitrateChangedListener);
    }

    public void removeBitrateChangedListener(BitrateChangedListener bitrateChangedListener) {
        bitrateChangedListeners.remove(bitrateChangedListener);
    }

    public void setPreReleaseListener(Player.PreReleaseListener playerReleaseListener) {
        this.playerReleaseListener = playerReleaseListener;
    }

    public void addHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback) {
        heartbeatCallbacks.registerCallback(callback);
    }

    public void removeHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback) {
        heartbeatCallbacks.unregisterCallback(callback);
    }

    public void addVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener) {
        videoSizeChangedListeners.add(videoSizeChangedListener);
    }

    public void removeVideoSizeChangedListener(Player.VideoSizeChangedListener videoSizeChangedListener) {
        videoSizeChangedListeners.remove(videoSizeChangedListener);
    }

    public ErrorListeners getErrorListeners() {
        return errorListeners;
    }

    public PreparedListeners getPreparedListeners() {
        return preparedListeners;
    }

    public BufferStateListeners getBufferStateListeners() {
        return bufferStateListeners;
    }

    public CompletionListeners getCompletionListeners() {
        return completionListeners;
    }

    public StateChangedListeners getStateChangedListeners() {
        return stateChangedListeners;
    }

    public InfoListeners getInfoListeners() {
        return infoListeners;
    }

    public Player.PreReleaseListener getPlayerReleaseListener() {
        return playerReleaseListener;
    }

    public HeartbeatCallbacks<Player> getHeartbeatCallbacks() {
        return heartbeatCallbacks;
    }

    public VideoSizeChangedListeners getVideoSizeChangedListeners() {
        return videoSizeChangedListeners;
    }

    public BitrateChangedListeners getBitrateChangedListeners() {
        return bitrateChangedListeners;
    }
}
