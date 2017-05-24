package com.novoda.noplayer.exoplayer;

import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.listeners.InfoListeners;

import java.io.IOException;
import java.util.HashMap;

class InfoForwarder extends ExoPlayerEventForwarder {

    private final InfoListeners infoListeners;

    InfoForwarder(InfoListeners infoListeners) {
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

        infoListeners.onNewInfo("onLoadStarted", keyValuePairs);
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

        infoListeners.onNewInfo("onLoadStarted", keyValuePairs);
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

        infoListeners.onNewInfo("onLoadStarted", keyValuePairs);
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

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("count", String.valueOf(count));
        keyValuePairs.put("elapsedMs", String.valueOf(elapsedMs));

        infoListeners.onNewInfo("onDroppedFrames", keyValuePairs);
    }

    @Override
    public void forwardPlayerStateChanged(boolean playWhenReady, int playbackState) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("playWhenReady", String.valueOf(playWhenReady));
        keyValuePairs.put("playbackState", String.valueOf(playbackState));

        infoListeners.onNewInfo("forwardPlayerStateChanged", keyValuePairs);
    }

    @Override
    public void forwardPlayerError(ExoPlaybackException error) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("error", String.valueOf(error));

        infoListeners.onNewInfo("forwardPlayerError", keyValuePairs);
    }

    @Override
    public void forwardVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("width", String.valueOf(width));
        keyValuePairs.put("height", String.valueOf(height));
        keyValuePairs.put("unappliedRotationDegrees", String.valueOf(unappliedRotationDegrees));
        keyValuePairs.put("pixelWidthHeightRatio", String.valueOf(pixelWidthHeightRatio));

        infoListeners.onNewInfo("forwardVideoSizeChanged", keyValuePairs);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("counters", String.valueOf(counters));

        infoListeners.onNewInfo("onVideoEnabled", keyValuePairs);
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("decoderName", String.valueOf(decoderName));
        keyValuePairs.put("initializedTimestampMs", String.valueOf(initializedTimestampMs));
        keyValuePairs.put("initializationDurationMs", String.valueOf(initializationDurationMs));

        infoListeners.onNewInfo("onVideoDecoderInitialized", keyValuePairs);
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("format", String.valueOf(format));

        infoListeners.onNewInfo("onVideoInputFormatChanged", keyValuePairs);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("width", String.valueOf(width));
        keyValuePairs.put("height", String.valueOf(height));
        keyValuePairs.put("unappliedRotationDegrees", String.valueOf(unappliedRotationDegrees));
        keyValuePairs.put("pixelWidthHeightRatio", String.valueOf(pixelWidthHeightRatio));

        infoListeners.onNewInfo("onVideoSizeChanged", keyValuePairs);
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("surface", String.valueOf(surface));

        infoListeners.onNewInfo("onRenderedFirstFrame", keyValuePairs);
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("counters", String.valueOf(counters));

        infoListeners.onNewInfo("onVideoDisabled", keyValuePairs);
    }

    @Override
    public void onLoadError(IOException error) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("error", String.valueOf(error));

        infoListeners.onNewInfo("onLoadError", keyValuePairs);
    }
}
