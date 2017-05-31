package com.novoda.noplayer.exoplayer.forwarder;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.listeners.InfoListeners;

import java.io.IOException;
import java.util.HashMap;

class MediaSourceInfoForwarder implements AdaptiveMediaSourceEventListener {

    private final InfoListeners infoListeners;

    MediaSourceInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                              Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("dataSpec", String.valueOf(dataSpec));
        keyValuePairs.put("dataType", String.valueOf(dataType));
        keyValuePairs.put("trackType", String.valueOf(trackType));
        keyValuePairs.put("trackFormat", String.valueOf(trackFormat));
        keyValuePairs.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        keyValuePairs.put("trackSelectionData", String.valueOf(trackSelectionData));
        keyValuePairs.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        keyValuePairs.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        keyValuePairs.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));

        infoListeners.onNewInfo("onLoadStarted", keyValuePairs);
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                                Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs,
                                long loadDurationMs, long bytesLoaded) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("dataSpec", String.valueOf(dataSpec));
        keyValuePairs.put("dataType", String.valueOf(dataType));
        keyValuePairs.put("trackType", String.valueOf(trackType));
        keyValuePairs.put("trackFormat", String.valueOf(trackFormat));
        keyValuePairs.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        keyValuePairs.put("trackSelectionData", String.valueOf(trackSelectionData));
        keyValuePairs.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        keyValuePairs.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        keyValuePairs.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));
        keyValuePairs.put("loadDurationMs", String.valueOf(loadDurationMs));
        keyValuePairs.put("bytesLoaded", String.valueOf(bytesLoaded));

        infoListeners.onNewInfo("onLoadCompleted", keyValuePairs);
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                               Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs,
                               long loadDurationMs, long bytesLoaded) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("dataSpec", String.valueOf(dataSpec));
        keyValuePairs.put("dataType", String.valueOf(dataType));
        keyValuePairs.put("trackType", String.valueOf(trackType));
        keyValuePairs.put("trackFormat", String.valueOf(trackFormat));
        keyValuePairs.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        keyValuePairs.put("trackSelectionData", String.valueOf(trackSelectionData));
        keyValuePairs.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        keyValuePairs.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        keyValuePairs.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));
        keyValuePairs.put("loadDurationMs", String.valueOf(loadDurationMs));
        keyValuePairs.put("bytesLoaded", String.valueOf(bytesLoaded));

        infoListeners.onNewInfo("onLoadCanceled", keyValuePairs);
    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                            Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs,
                            long bytesLoaded, IOException error, boolean wasCanceled) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("dataSpec", String.valueOf(dataSpec));
        keyValuePairs.put("dataType", String.valueOf(dataType));
        keyValuePairs.put("trackType", String.valueOf(trackType));
        keyValuePairs.put("trackFormat", String.valueOf(trackFormat));
        keyValuePairs.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        keyValuePairs.put("trackSelectionData", String.valueOf(trackSelectionData));
        keyValuePairs.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        keyValuePairs.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        keyValuePairs.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));
        keyValuePairs.put("loadDurationMs", String.valueOf(loadDurationMs));
        keyValuePairs.put("bytesLoaded", String.valueOf(bytesLoaded));
        keyValuePairs.put("IOException", String.valueOf(error));
        keyValuePairs.put("wasCanceled", String.valueOf(wasCanceled));

        infoListeners.onNewInfo("onLoadError", keyValuePairs);
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("trackType", String.valueOf(trackType));
        keyValuePairs.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        keyValuePairs.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));

        infoListeners.onNewInfo("onUpstreamDiscarded", keyValuePairs);
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData,
                                          long mediaTimeMs) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("trackType", String.valueOf(trackType));
        keyValuePairs.put("trackFormat", String.valueOf(trackFormat));
        keyValuePairs.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        keyValuePairs.put("trackSelectionData", String.valueOf(trackSelectionData));
        keyValuePairs.put("mediaTimeMs", String.valueOf(mediaTimeMs));

        infoListeners.onNewInfo("onDownstreamFormatChanged", keyValuePairs);
    }
}
