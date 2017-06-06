package com.novoda.noplayer.exoplayer.forwarder;

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

    public ExoPlayerForwarder() {
        exoPlayerEventListener = new EventListener();
        mediaSourceEventListener = new MediaSourceEventListener();
        videoRendererEventListener = new ExoPlayerVideoRendererEventListener();
        extractorMediaSourceListener = new ExoPlayerExtractorMediaSourceListener();
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

    public ExoPlayerExtractorMediaSourceListener extractorMediaSourceListener() {
        return extractorMediaSourceListener;
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
