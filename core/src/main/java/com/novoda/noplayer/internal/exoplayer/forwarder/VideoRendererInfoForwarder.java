package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.NoPlayer;

import java.util.HashMap;

class VideoRendererInfoForwarder implements VideoRendererEventListener {

    private final NoPlayer.InfoListener infoListener;

    VideoRendererInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("count", String.valueOf(count));
        callingMethodParameters.put("elapsedMs", String.valueOf(elapsedMs));

        infoListener.onNewInfo("onDroppedFrames", callingMethodParameters);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("counters", String.valueOf(counters));

        infoListener.onNewInfo("onVideoEnabled", callingMethodParameters);
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("decoderName", String.valueOf(decoderName));
        callingMethodParameters.put("initializedTimestampMs", String.valueOf(initializedTimestampMs));
        callingMethodParameters.put("initializationDurationMs", String.valueOf(initializationDurationMs));

        infoListener.onNewInfo("onVideoDecoderInitialized", callingMethodParameters);
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("format", String.valueOf(format));

        infoListener.onNewInfo("onVideoInputFormatChanged", callingMethodParameters);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("width", String.valueOf(width));
        callingMethodParameters.put("height", String.valueOf(height));
        callingMethodParameters.put("unappliedRotationDegrees", String.valueOf(unappliedRotationDegrees));
        callingMethodParameters.put("pixelWidthHeightRatio", String.valueOf(pixelWidthHeightRatio));

        infoListener.onNewInfo("onVideoSizeChanged", callingMethodParameters);
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("surface", String.valueOf(surface));

        infoListener.onNewInfo("onRenderedFirstFrame", callingMethodParameters);
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("counters", String.valueOf(counters));

        infoListener.onNewInfo("onVideoDisabled", callingMethodParameters);
    }
}
