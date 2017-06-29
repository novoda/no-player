package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;

public class ExoPlayerForwarder {

    private final EventListener exoPlayerEventListener;
    private final MediaSourceEventListener mediaSourceEventListener;
    private final ExoPlayerVideoRendererEventListener videoRendererEventListener;
    private final ExoPlayerExtractorMediaSourceListener extractorMediaSourceListener;
    private final ExoPlayerDrmSessionEventListener drmSessionEventListener;

    public ExoPlayerForwarder() {
        exoPlayerEventListener = new EventListener();
        mediaSourceEventListener = new MediaSourceEventListener();
        videoRendererEventListener = new ExoPlayerVideoRendererEventListener();
        extractorMediaSourceListener = new ExoPlayerExtractorMediaSourceListener();
        drmSessionEventListener = new ExoPlayerDrmSessionEventListener();
    }

    public EventListener exoPlayerEventListener() {
        return exoPlayerEventListener;
    }

    public AdaptiveMediaSourceEventListener mediaSourceEventListener() {
        return mediaSourceEventListener;
    }

    public VideoRendererEventListener videoRendererEventListener() {
        return videoRendererEventListener;
    }

    public ExtractorMediaSource.EventListener extractorMediaSourceListener() {
        return extractorMediaSourceListener;
    }

    public DefaultDrmSessionManager.EventListener drmSessionEventListener() {
        return drmSessionEventListener;
    }

    public void bind(Player.PreparedListener preparedListener, PlayerState playerState) {
        exoPlayerEventListener.add(new OnPrepareForwarder(preparedListener, playerState));
    }

    public void bind(Player.CompletionListener completionListener, Player.StateChangedListener stateChangedListener) {
        exoPlayerEventListener.add(new OnCompletionForwarder(completionListener));
        exoPlayerEventListener.add(new OnCompletionStateChangedForwarder(stateChangedListener));
    }

    public void bind(Player.ErrorListener errorListener, Player player) {
        exoPlayerEventListener.add(new PlayerOnErrorForwarder(player, errorListener));
        extractorMediaSourceListener.add(new MediaSourceOnErrorForwarder(player, errorListener));
        drmSessionEventListener.add(new DrmSessionErrorForwarder(player, errorListener));
    }

    public void bind(Player.BufferStateListener bufferStateListener) {
        exoPlayerEventListener.add(new BufferStateForwarder(bufferStateListener));
    }

    public void bind(Player.VideoSizeChangedListener videoSizeChangedListener) {
        videoRendererEventListener.add(new VideoSizeChangedForwarder(videoSizeChangedListener));
    }

    public void bind(Player.BitrateChangedListener bitrateChangedListener) {
        mediaSourceEventListener.add(new BitrateForwarder(bitrateChangedListener));
    }

    public void bind(Player.InfoListener infoListeners) {
        exoPlayerEventListener.add(new EventInfoForwarder(infoListeners));
        mediaSourceEventListener.add(new MediaSourceInfoForwarder(infoListeners));
        videoRendererEventListener.add(new VideoRendererInfoForwarder(infoListeners));
        extractorMediaSourceListener.add(new ExtractorInfoForwarder(infoListeners));
    }
}