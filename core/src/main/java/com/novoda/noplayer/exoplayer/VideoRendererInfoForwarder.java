package com.novoda.noplayer.exoplayer;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class VideoRendererInfoForwarder implements VideoRendererEventListener {

    private final InfoListeners infoListeners;

    VideoRendererInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("count", String.valueOf(count));
        keyValuePairs.put("elapsedMs", String.valueOf(elapsedMs));

        infoListeners.onNewInfo("onDroppedFrames", keyValuePairs);
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
}
