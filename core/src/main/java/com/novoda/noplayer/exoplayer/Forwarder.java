package com.novoda.noplayer.exoplayer;

import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.IOException;

public interface Forwarder {

    void forwardPlayerStateChanged(boolean playWhenReady, int playbackState);

    void forwardPlayerError(ExoPlaybackException error);

    void forwardVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio);

    void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs);

    void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded);

    void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded);

    void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled);

    void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs);

    void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs);

    void onVideoEnabled(DecoderCounters counters);

    void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs);

    void onVideoInputFormatChanged(Format format);

    void onDroppedFrames(int count, long elapsedMs);

    void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio);

    void onRenderedFirstFrame(Surface surface);

    void onVideoDisabled(DecoderCounters counters);
}
