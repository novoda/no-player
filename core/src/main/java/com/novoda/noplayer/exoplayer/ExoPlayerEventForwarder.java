package com.novoda.noplayer.exoplayer;

import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.IOException;

abstract class ExoPlayerEventForwarder implements ExoPlayerForwarder {

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // no-op
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        // no-op
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // no-op
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // no-op
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // no-op
    }

    @Override
    public void onPositionDiscontinuity() {
        // no-op
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // no-op
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
        // no-op
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        // no-op
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        // no-op
    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
        // no-op
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        // no-op
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {
        // no-op
    }

    @Override
    public void onLoadError(IOException error) {
        // no-op
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        // no-op
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        // no-op
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        // no-op
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        // no-op
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        // no-op
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        // no-op
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        // no-op
    }
}
