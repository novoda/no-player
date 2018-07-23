package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayer.BitrateChangedListener;

public class PlayerListenersHolder implements Listeners {

    private final ErrorListeners errorListeners;
    private final PreparedListeners preparedListeners;
    private final BufferStateListeners bufferStateListeners;
    private final CompletionListeners completionListeners;
    private final StateChangedListeners stateChangedListeners;
    private final InfoListeners infoListeners;
    private final VideoSizeChangedListeners videoSizeChangedListeners;
    private final BitrateChangedListeners bitrateChangedListeners;
    private final DroppedFramesListeners droppedFramesListeners;

    private final HeartbeatCallbacks heartbeatCallbacks;

    public PlayerListenersHolder() {
        errorListeners = new ErrorListeners();
        preparedListeners = new PreparedListeners();
        bufferStateListeners = new BufferStateListeners();
        completionListeners = new CompletionListeners();
        stateChangedListeners = new StateChangedListeners();
        infoListeners = new InfoListeners();
        videoSizeChangedListeners = new VideoSizeChangedListeners();
        bitrateChangedListeners = new BitrateChangedListeners();
        heartbeatCallbacks = new HeartbeatCallbacks();
        droppedFramesListeners = new DroppedFramesListeners();
    }

    @Override
    public void addErrorListener(NoPlayer.ErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    @Override
    public void removeErrorListener(NoPlayer.ErrorListener errorListener) {
        errorListeners.remove(errorListener);
    }

    @Override
    public void addPreparedListener(NoPlayer.PreparedListener preparedListener) {
        preparedListeners.add(preparedListener);
    }

    @Override
    public void removePreparedListener(NoPlayer.PreparedListener preparedListener) {
        preparedListeners.remove(preparedListener);
    }

    @Override
    public void addBufferStateListener(NoPlayer.BufferStateListener bufferStateListener) {
        bufferStateListeners.add(bufferStateListener);
    }

    @Override
    public void removeBufferStateListener(NoPlayer.BufferStateListener bufferStateListener) {
        bufferStateListeners.remove(bufferStateListener);
    }

    @Override
    public void addCompletionListener(NoPlayer.CompletionListener completionListener) {
        completionListeners.add(completionListener);
    }

    @Override
    public void removeCompletionListener(NoPlayer.CompletionListener completionListener) {
        completionListeners.remove(completionListener);
    }

    @Override
    public void addStateChangedListener(NoPlayer.StateChangedListener stateChangedListener) {
        stateChangedListeners.add(stateChangedListener);
    }

    @Override
    public void removeStateChangedListener(NoPlayer.StateChangedListener stateChangedListener) {
        stateChangedListeners.remove(stateChangedListener);
    }

    @Override
    public void addInfoListener(NoPlayer.InfoListener infoListener) {
        infoListeners.add(infoListener);
    }

    @Override
    public void removeInfoListener(NoPlayer.InfoListener infoListener) {
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
    public void addHeartbeatCallback(NoPlayer.HeartbeatCallback heartbeatCallback) {
        heartbeatCallbacks.registerCallback(heartbeatCallback);
    }

    @Override
    public void removeHeartbeatCallback(NoPlayer.HeartbeatCallback heartbeatCallback) {
        heartbeatCallbacks.unregisterCallback(heartbeatCallback);
    }

    @Override
    public void addVideoSizeChangedListener(NoPlayer.VideoSizeChangedListener videoSizeChangedListener) {
        videoSizeChangedListeners.add(videoSizeChangedListener);
    }

    @Override
    public void removeVideoSizeChangedListener(NoPlayer.VideoSizeChangedListener videoSizeChangedListener) {
        videoSizeChangedListeners.remove(videoSizeChangedListener);
    }

    @Override
    public void addDroppedVideoFrames(NoPlayer.DroppedVideoFramesListener droppedVideoFramesListener) {
        droppedFramesListeners.add(droppedVideoFramesListener);
    }

    @Override
    public void removeDroppedVideoFrames(NoPlayer.DroppedVideoFramesListener droppedVideoFramesListener) {
        droppedFramesListeners.remove(droppedVideoFramesListener);
    }

    public NoPlayer.ErrorListener getErrorListeners() {
        return errorListeners;
    }

    public NoPlayer.PreparedListener getPreparedListeners() {
        return preparedListeners;
    }

    public NoPlayer.BufferStateListener getBufferStateListeners() {
        return bufferStateListeners;
    }

    public NoPlayer.CompletionListener getCompletionListeners() {
        return completionListeners;
    }

    public NoPlayer.StateChangedListener getStateChangedListeners() {
        return stateChangedListeners;
    }

    public NoPlayer.InfoListener getInfoListeners() {
        return infoListeners;
    }

    public NoPlayer.HeartbeatCallback getHeartbeatCallbacks() {
        return heartbeatCallbacks;
    }

    public NoPlayer.VideoSizeChangedListener getVideoSizeChangedListeners() {
        return videoSizeChangedListeners;
    }

    public NoPlayer.BitrateChangedListener getBitrateChangedListeners() {
        return bitrateChangedListeners;
    }

    public NoPlayer.DroppedVideoFramesListener getDroppedVideoFramesListeners() {
        return droppedFramesListeners;
    }

    public void resetState() {
        preparedListeners.resetPreparedState();
        completionListeners.resetCompletedState();
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
        droppedFramesListeners.clear();
    }
}
