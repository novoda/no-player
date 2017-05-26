package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.IOException;
import java.util.List;

class MediaSourceEventListener implements AdaptiveMediaSourceEventListener {

    private final List<ExoPlayerForwarder> forwarders;

    MediaSourceEventListener(List<ExoPlayerForwarder> forwarders) {
        this.forwarders = forwarders;
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec,
                              int dataType,
                              int trackType,
                              Format trackFormat,
                              int trackSelectionReason,
                              Object trackSelectionData,
                              long mediaStartTimeMs,
                              long mediaEndTimeMs,
                              long elapsedRealtimeMs) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onLoadStarted(
                    dataSpec,
                    dataType,
                    trackType,
                    trackFormat,
                    trackSelectionReason,
                    trackSelectionData,
                    mediaStartTimeMs,
                    mediaEndTimeMs,
                    elapsedRealtimeMs
            );
        }
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec,
                                int dataType,
                                int trackType,
                                Format trackFormat,
                                int trackSelectionReason,
                                Object trackSelectionData,
                                long mediaStartTimeMs,
                                long mediaEndTimeMs,
                                long elapsedRealtimeMs,
                                long loadDurationMs,
                                long bytesLoaded) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onLoadCompleted(
                    dataSpec,
                    dataType,
                    trackType,
                    trackFormat,
                    trackSelectionReason,
                    trackSelectionData,
                    mediaStartTimeMs,
                    mediaEndTimeMs,
                    elapsedRealtimeMs,
                    loadDurationMs,
                    bytesLoaded
            );
        }
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec,
                               int dataType,
                               int trackType,
                               Format trackFormat,
                               int trackSelectionReason,
                               Object trackSelectionData,
                               long mediaStartTimeMs,
                               long mediaEndTimeMs,
                               long elapsedRealtimeMs,
                               long loadDurationMs,
                               long bytesLoaded) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onLoadCanceled(
                    dataSpec,
                    dataType,
                    trackType,
                    trackFormat,
                    trackSelectionReason,
                    trackSelectionData,
                    mediaStartTimeMs,
                    mediaEndTimeMs,
                    elapsedRealtimeMs,
                    loadDurationMs,
                    bytesLoaded
            );
        }
    }

    @Override
    public void onLoadError(DataSpec dataSpec,
                            int dataType,
                            int trackType,
                            Format trackFormat,
                            int trackSelectionReason,
                            Object trackSelectionData,
                            long mediaStartTimeMs,
                            long mediaEndTimeMs,
                            long elapsedRealtimeMs,
                            long loadDurationMs,
                            long bytesLoaded,
                            IOException error,
                            boolean wasCanceled) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onLoadError(
                    dataSpec,
                    dataType,
                    trackType,
                    trackFormat,
                    trackSelectionReason,
                    trackSelectionData,
                    mediaStartTimeMs,
                    mediaEndTimeMs,
                    elapsedRealtimeMs,
                    loadDurationMs,
                    bytesLoaded,
                    error,
                    wasCanceled
            );
        }
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onUpstreamDiscarded(trackType, mediaStartTimeMs, mediaEndTimeMs);
        }
    }

    @Override
    public void onDownstreamFormatChanged(int trackType,
                                          Format trackFormat,
                                          int trackSelectionReason,
                                          Object trackSelectionData,
                                          long mediaTimeMs) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onDownstreamFormatChanged(trackType, trackFormat, trackSelectionReason, trackSelectionData, mediaTimeMs);
        }
    }
}
