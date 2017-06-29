package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class MediaSourceEventListener implements AdaptiveMediaSourceEventListener {

    private final List<AdaptiveMediaSourceEventListener> listeners = new CopyOnWriteArrayList<>();

    public void add(AdaptiveMediaSourceEventListener listener) {
        listeners.add(listener);
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
        for (AdaptiveMediaSourceEventListener listener : listeners) {
            listener.onLoadStarted(
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
        for (AdaptiveMediaSourceEventListener listener : listeners) {
            listener.onLoadCompleted(
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
        for (AdaptiveMediaSourceEventListener listener : listeners) {
            listener.onLoadCanceled(
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
        for (AdaptiveMediaSourceEventListener listener : listeners) {
            listener.onLoadError(
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
        for (AdaptiveMediaSourceEventListener listener : listeners) {
            listener.onUpstreamDiscarded(trackType, mediaStartTimeMs, mediaEndTimeMs);
        }
    }

    @Override
    public void onDownstreamFormatChanged(int trackType,
                                          Format trackFormat,
                                          int trackSelectionReason,
                                          Object trackSelectionData,
                                          long mediaTimeMs) {
        for (AdaptiveMediaSourceEventListener listener : listeners) {
            listener.onDownstreamFormatChanged(trackType, trackFormat, trackSelectionReason, trackSelectionData, mediaTimeMs);
        }
    }
}
