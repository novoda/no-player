package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.video.VideoListener;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerState;

public class ExoPlayerForwarder {

    private final ExoPlayerListener exoPlayerListener;

    public ExoPlayerForwarder() {
        exoPlayerListener = new ExoPlayerListener();
    }

    public Player.EventListener exoPlayerEventListener() {
        return exoPlayerListener;
    }

    public MediaSourceEventListener mediaSourceEventListener() {
        return exoPlayerListener;
    }

    public VideoListener videoListener() {
        return exoPlayerListener;
    }

    public DefaultDrmSessionEventListener drmSessionEventListener() {
        return exoPlayerListener;
    }

    public AnalyticsListener analyticsListener() {
        return exoPlayerListener;
    }

    public void bind(NoPlayer.PreparedListener preparedListener, PlayerState playerState) {
        exoPlayerListener.add(new OnPrepareForwarder(preparedListener, playerState));
    }

    public void bind(NoPlayer.CompletionListener completionListener, NoPlayer.StateChangedListener stateChangedListener) {
        exoPlayerListener.add(new OnCompletionForwarder(completionListener));
        exoPlayerListener.add(new OnCompletionStateChangedForwarder(stateChangedListener));
    }

    public void bind(NoPlayer.ErrorListener errorListener) {
        exoPlayerListener.add(new PlayerOnErrorForwarder(errorListener));
    }

    public void bind(NoPlayer.BufferStateListener bufferStateListener) {
        exoPlayerListener.add(new BufferStateForwarder(bufferStateListener));
    }

    public void bind(NoPlayer.VideoSizeChangedListener videoSizeChangedListener) {
        exoPlayerListener.add(new VideoSizeChangedForwarder(videoSizeChangedListener));
    }

    public void bind(NoPlayer.BitrateChangedListener bitrateChangedListener) {
        exoPlayerListener.add(new BitrateForwarder(bitrateChangedListener));
    }

    public void bind(NoPlayer.InfoListener infoListeners) {
        exoPlayerListener.add(new EventInfoForwarder(infoListeners));
        exoPlayerListener.add(new MediaSourceEventForwarder(infoListeners));
        exoPlayerListener.add(new DrmSessionInfoForwarder(infoListeners));
        exoPlayerListener.add(new AnalyticsListenerForwarder(infoListeners));
    }

    public void bind(NoPlayer.DroppedVideoFramesListener droppedVideoFramesListeners) {
        exoPlayerListener.add(droppedVideoFramesListeners);
    }
}
