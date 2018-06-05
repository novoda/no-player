package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.NoPlayer;

import java.util.HashMap;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Methods;
import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Parameters;

class VideoRendererInfoForwarder implements VideoRendererEventListener {

    private final NoPlayer.InfoListener infoListener;

    VideoRendererInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.COUNT, String.valueOf(count));
        callingMethodParameters.put(Parameters.ELAPSED_MS, String.valueOf(elapsedMs));

        infoListener.onNewInfo(Methods.ON_DROPPED_VIDEO_FRAMES, callingMethodParameters);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DECODER_COUNTERS, String.valueOf(counters));

        infoListener.onNewInfo(Methods.ON_VIDEO_ENABLED, callingMethodParameters);
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DECODER_NAME, String.valueOf(decoderName));
        callingMethodParameters.put(Parameters.INITIALIZED_TIMESTAMP_MS, String.valueOf(initializedTimestampMs));
        callingMethodParameters.put(Parameters.INITIALIZATION_DURATION_MS, String.valueOf(initializationDurationMs));

        infoListener.onNewInfo(Methods.ON_VIDEO_DECODER_INITIALIZED, callingMethodParameters);
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.FORMAT, String.valueOf(format));

        infoListener.onNewInfo(Methods.ON_VIDEO_INPUT_FORMAT_CHANGED, callingMethodParameters);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WIDTH, String.valueOf(width));
        callingMethodParameters.put(Parameters.HEIGHT, String.valueOf(height));
        callingMethodParameters.put(Parameters.UNAPPLIED_ROTATION_DEGREES, String.valueOf(unappliedRotationDegrees));
        callingMethodParameters.put(Parameters.PIXEL_WIDTH_HEIGHT_RATIO, String.valueOf(pixelWidthHeightRatio));

        infoListener.onNewInfo(Methods.ON_VIDEO_SIZE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.SURFACE, String.valueOf(surface));

        infoListener.onNewInfo(Methods.ON_RENDERED_FIRST_FRAME, callingMethodParameters);
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DECODER_COUNTERS, String.valueOf(counters));

        infoListener.onNewInfo(Methods.ON_VIDEO_DISABLED, callingMethodParameters);
    }
}
