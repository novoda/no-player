package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.NoPlayer;
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

    public void bind(NoPlayer.PreparedListener preparedListener, PlayerState playerState) {
        exoPlayerEventListener.add(new OnPrepareForwarder(preparedListener, playerState));
    }

    public void bind(NoPlayer.CompletionListener completionListener, NoPlayer.StateChangedListener stateChangedListener) {
        exoPlayerEventListener.add(new OnCompletionForwarder(completionListener));
        exoPlayerEventListener.add(new OnCompletionStateChangedForwarder(stateChangedListener));
    }

    public void bind(NoPlayer.ErrorListener errorListener) {
        exoPlayerEventListener.add(new PlayerOnErrorForwarder(errorListener));
        extractorMediaSourceListener.add(new MediaSourceOnErrorForwarder(errorListener));
        drmSessionEventListener.add(new DrmSessionErrorForwarder(errorListener));
    }

    public void bind(NoPlayer.BufferStateListener bufferStateListener) {
        exoPlayerEventListener.add(new BufferStateForwarder(bufferStateListener));
    }

    public void bind(NoPlayer.VideoSizeChangedListener videoSizeChangedListener) {
        videoRendererEventListener.add(new VideoSizeChangedForwarder(videoSizeChangedListener));
    }

    public void bind(NoPlayer.BitrateChangedListener bitrateChangedListener) {
        mediaSourceEventListener.add(new BitrateForwarder(bitrateChangedListener));
    }

    public void bind(NoPlayer.InfoListener infoListeners) {
        exoPlayerEventListener.add(new EventInfoForwarder(infoListeners));
        mediaSourceEventListener.add(new MediaSourceInfoForwarder(infoListeners));
        videoRendererEventListener.add(new VideoRendererInfoForwarder(infoListeners));
        extractorMediaSourceListener.add(new ExtractorInfoForwarder(infoListeners));
    }
}
