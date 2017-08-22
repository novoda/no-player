package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.HashMap;

class MediaSourceInfoForwarder implements AdaptiveMediaSourceEventListener {

    private final NoPlayer.InfoListener infoListener;

    MediaSourceInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                              Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("dataSpec", String.valueOf(dataSpec));
        callingMethodParameters.put("dataType", String.valueOf(dataType));
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("trackFormat", String.valueOf(trackFormat));
        callingMethodParameters.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        callingMethodParameters.put("trackSelectionData", String.valueOf(trackSelectionData));
        callingMethodParameters.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));

        infoListener.onNewInfo("onLoadStarted", callingMethodParameters);
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                                Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs,
                                long loadDurationMs, long bytesLoaded) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("dataSpec", String.valueOf(dataSpec));
        callingMethodParameters.put("dataType", String.valueOf(dataType));
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("trackFormat", String.valueOf(trackFormat));
        callingMethodParameters.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        callingMethodParameters.put("trackSelectionData", String.valueOf(trackSelectionData));
        callingMethodParameters.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));
        callingMethodParameters.put("loadDurationMs", String.valueOf(loadDurationMs));
        callingMethodParameters.put("bytesLoaded", String.valueOf(bytesLoaded));

        infoListener.onNewInfo("onLoadCompleted", callingMethodParameters);
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                               Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs,
                               long loadDurationMs, long bytesLoaded) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("dataSpec", String.valueOf(dataSpec));
        callingMethodParameters.put("dataType", String.valueOf(dataType));
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("trackFormat", String.valueOf(trackFormat));
        callingMethodParameters.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        callingMethodParameters.put("trackSelectionData", String.valueOf(trackSelectionData));
        callingMethodParameters.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));
        callingMethodParameters.put("loadDurationMs", String.valueOf(loadDurationMs));
        callingMethodParameters.put("bytesLoaded", String.valueOf(bytesLoaded));

        infoListener.onNewInfo("onLoadCanceled", callingMethodParameters);
    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason,
                            Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs,
                            long bytesLoaded, IOException error, boolean wasCanceled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("dataSpec", String.valueOf(dataSpec));
        callingMethodParameters.put("dataType", String.valueOf(dataType));
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("trackFormat", String.valueOf(trackFormat));
        callingMethodParameters.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        callingMethodParameters.put("trackSelectionData", String.valueOf(trackSelectionData));
        callingMethodParameters.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put("elapsedRealtimeMs", String.valueOf(elapsedRealtimeMs));
        callingMethodParameters.put("loadDurationMs", String.valueOf(loadDurationMs));
        callingMethodParameters.put("bytesLoaded", String.valueOf(bytesLoaded));
        callingMethodParameters.put("IOException", String.valueOf(error));
        callingMethodParameters.put("wasCanceled", String.valueOf(wasCanceled));

        infoListener.onNewInfo("onLoadError", callingMethodParameters);
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("mediaStartTimeMs", String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put("mediaEndTimeMs", String.valueOf(mediaEndTimeMs));

        infoListener.onNewInfo("onUpstreamDiscarded", callingMethodParameters);
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData,
                                          long mediaTimeMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("trackFormat", String.valueOf(trackFormat));
        callingMethodParameters.put("trackSelectionReason", String.valueOf(trackSelectionReason));
        callingMethodParameters.put("trackSelectionData", String.valueOf(trackSelectionData));
        callingMethodParameters.put("mediaTimeMs", String.valueOf(mediaTimeMs));

        infoListener.onNewInfo("onDownstreamFormatChanged", callingMethodParameters);
    }
}
