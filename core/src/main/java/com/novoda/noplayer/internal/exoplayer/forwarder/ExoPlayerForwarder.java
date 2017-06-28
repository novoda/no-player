package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.BitrateChangedListeners;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

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

    public void bind(PreparedListeners preparedListeners, PlayerState playerState) {
        exoPlayerEventListener.add(new OnPrepareForwarder(preparedListeners, playerState));
    }

    public void bind(CompletionListeners completionListeners, StateChangedListeners stateChangedListeners) {
        exoPlayerEventListener.add(new OnCompletionForwarder(completionListeners));
        exoPlayerEventListener.add(new OnCompletionStateChangedForwarder(stateChangedListeners));
    }

    public void bind(ErrorListeners errorListeners, Player player) {
        exoPlayerEventListener.add(new PlayerOnErrorForwarder(player, errorListeners));
        extractorMediaSourceListener.add(new MediaSourceOnErrorForwarder(player, errorListeners));
        drmSessionEventListener.add(new DrmSessionErrorForwarder(player, errorListeners));
    }

    public void bind(BufferStateListeners bufferStateListeners) {
        exoPlayerEventListener.add(new BufferStateForwarder(bufferStateListeners));
    }

    public void bind(VideoSizeChangedListeners videoSizeChangedListeners) {
        videoRendererEventListener.add(new VideoSizeChangedForwarder(videoSizeChangedListeners));
    }

    public void bind(BitrateChangedListeners bitrateChangedListeners) {
        mediaSourceEventListener.add(new BitrateForwarder(bitrateChangedListeners));
    }

    public void bind(InfoListeners infoListeners) {
        exoPlayerEventListener.add(new EventInfoForwarder(infoListeners));
        mediaSourceEventListener.add(new MediaSourceInfoForwarder(infoListeners));
        videoRendererEventListener.add(new VideoRendererInfoForwarder(infoListeners));
        extractorMediaSourceListener.add(new ExtractorInfoForwarder(infoListeners));
    }
}
