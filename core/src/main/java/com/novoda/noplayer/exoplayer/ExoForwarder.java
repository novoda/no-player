package com.novoda.noplayer.exoplayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.BitrateChangedListeners;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExoForwarder {

    private final List<ExoPlayerForwarder> forwarders;
    private final EventListener exoPlayerEventListener;
    private final MediaSourceEventListener mediaSourceEventListener;
    private final ExoPlayerVideoRendererEventListener videoRendererEventListener;
    private final ExoPlayerExtractorMediaSourceListener exoPlayerExtractorMediaSourceListener;

    public ExoForwarder() {
        forwarders = new CopyOnWriteArrayList<>();
        exoPlayerEventListener = new EventListener(forwarders);
        mediaSourceEventListener = new MediaSourceEventListener(forwarders);
        videoRendererEventListener = new ExoPlayerVideoRendererEventListener(forwarders);
        exoPlayerExtractorMediaSourceListener = new ExoPlayerExtractorMediaSourceListener(forwarders);
    }

    public EventListener exoPlayerEventListener() {
        return exoPlayerEventListener;
    }

    public MediaSourceEventListener mediaSourceEventListener() {
        return mediaSourceEventListener;
    }

    public ExoPlayerVideoRendererEventListener videoRendererEventListener() {
        return videoRendererEventListener;
    }

    public ExoPlayerExtractorMediaSourceListener mediaSourceListener() {
        return exoPlayerExtractorMediaSourceListener;
    }

    public void bind(PreparedListeners preparedListeners, PlayerState playerState) {
        forwarders.add(new OnPrepareForwarder(preparedListeners, playerState));
    }

    public void bind(CompletionListeners completionListeners) {
        forwarders.add(new OnCompletionForwarder(completionListeners));
    }

    public void bind(ErrorListeners errorListeners, Player player) {
        forwarders.add(new OnErrorForwarder(player, errorListeners));
    }

    public void bind(BufferStateListeners bufferStateListeners) {
        forwarders.add(new BufferStateForwarder(bufferStateListeners));
    }

    public void bind(VideoSizeChangedListeners videoSizeChangedListeners) {
        forwarders.add(new VideoSizeChangedForwarder(videoSizeChangedListeners));
    }

    public void bind(BitrateChangedListeners bitrateChangedListeners) {
        forwarders.add(new BitrateForwarder(bitrateChangedListeners));
    }

    public void bind(InfoListeners infoListeners) {
        forwarders.add(new InfoForwarder(infoListeners));
    }

    public void bind(ExoPlayerTwoFacade facade) {
        facade.setForwarder(this);
    }
}
