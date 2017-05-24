package com.novoda.noplayer.listeners;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.exoplayer.InfoListener;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class InfoListeners implements InfoListener {

    private final Set<InfoListener> listeners = new CopyOnWriteArraySet<>();

    public void add(InfoListener listener) {
        listeners.add(listener);
    }

    public void remove(InfoListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        for (InfoListener listener : listeners) {
            listener.onDroppedFrames(count, elapsed);
        }
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
        for (InfoListener listener : listeners) {
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
        for (InfoListener listener : listeners) {
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
        for (InfoListener listener : listeners) {
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
        for (InfoListener listener : listeners) {
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
        for (InfoListener listener : listeners) {
            listener.onUpstreamDiscarded(trackType, mediaStartTimeMs, mediaEndTimeMs);
        }
    }

}
