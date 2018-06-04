package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.HashMap;

class AnalyticsListenerForwarder implements AnalyticsListener {

    private final NoPlayer.InfoListener infoListeners;

    AnalyticsListenerForwarder(NoPlayer.InfoListener infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int playbackState) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("playWhenReady", String.valueOf(playWhenReady));
        callingMethodParameters.put("playbackState", String.valueOf(playbackState));

        infoListeners.onNewInfo("onPlayerStateChanged", callingMethodParameters);
    }

    @Override
    public void onTimelineChanged(EventTime eventTime, int reason) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("reason", String.valueOf(reason));

        infoListeners.onNewInfo("onTimelineChanged", callingMethodParameters);
    }

    @Override
    public void onPositionDiscontinuity(EventTime eventTime, int reason) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("reason", String.valueOf(reason));

        infoListeners.onNewInfo("onPositionDiscontinuity", callingMethodParameters);
    }

    @Override
    public void onSeekStarted(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onSeekStarted", callingMethodParameters);
    }

    @Override
    public void onSeekProcessed(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onSeekProcessed", callingMethodParameters);
    }

    @Override
    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("playbackParameters", playbackParameters.toString());

        infoListeners.onNewInfo("onPlaybackParametersChanged", callingMethodParameters);
    }

    @Override
    public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("repeatMode", String.valueOf(repeatMode));

        infoListeners.onNewInfo("onRepeatModeChanged", callingMethodParameters);
    }

    @Override
    public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("shuffleModeEnabled", String.valueOf(shuffleModeEnabled));

        infoListeners.onNewInfo("onShuffleModeChanged", callingMethodParameters);
    }

    @Override
    public void onLoadingChanged(EventTime eventTime, boolean isLoading) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("isLoading", String.valueOf(isLoading));

        infoListeners.onNewInfo("onLoadingChanged", callingMethodParameters);
    }

    @Override
    public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("error", error.toString());

        infoListeners.onNewInfo("onPlayerError", callingMethodParameters);
    }

    @Override
    public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("trackGroups", trackGroups.toString());
        callingMethodParameters.put("trackSelections", trackSelections.toString());

        infoListeners.onNewInfo("onTracksChanged", callingMethodParameters);
    }

    @Override
    public void onLoadStarted(EventTime eventTime,
                              MediaSourceEventListener.LoadEventInfo loadEventInfo,
                              MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("loadEventInfo", loadEventInfo.toString());
        callingMethodParameters.put("mediaLoadData", mediaLoadData.toString());

        infoListeners.onNewInfo("onLoadStarted", callingMethodParameters);
    }

    @Override
    public void onLoadCompleted(EventTime eventTime,
                                MediaSourceEventListener.LoadEventInfo loadEventInfo,
                                MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("loadEventInfo", loadEventInfo.toString());
        callingMethodParameters.put("mediaLoadData", mediaLoadData.toString());

        infoListeners.onNewInfo("onLoadCompleted", callingMethodParameters);
    }

    @Override
    public void onLoadCanceled(EventTime eventTime,
                               MediaSourceEventListener.LoadEventInfo loadEventInfo,
                               MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("loadEventInfo", loadEventInfo.toString());
        callingMethodParameters.put("mediaLoadData", mediaLoadData.toString());

        infoListeners.onNewInfo("onLoadCanceled", callingMethodParameters);
    }

    @Override
    public void onLoadError(EventTime eventTime,
                            MediaSourceEventListener.LoadEventInfo loadEventInfo,
                            MediaSourceEventListener.MediaLoadData mediaLoadData,
                            IOException error,
                            boolean wasCanceled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("loadEventInfo", loadEventInfo.toString());
        callingMethodParameters.put("mediaLoadData", mediaLoadData.toString());
        callingMethodParameters.put("error", error.toString());
        callingMethodParameters.put("wasCanceled", String.valueOf(wasCanceled));

        infoListeners.onNewInfo("onLoadError", callingMethodParameters);
    }

    @Override
    public void onDownstreamFormatChanged(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("mediaLoadData", mediaLoadData.toString());

        infoListeners.onNewInfo("onDownstreamFormatChanged", callingMethodParameters);
    }

    @Override
    public void onUpstreamDiscarded(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("mediaLoadData", mediaLoadData.toString());

        infoListeners.onNewInfo("onUpstreamDiscarded", callingMethodParameters);
    }

    @Override
    public void onMediaPeriodCreated(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onMediaPeriodCreated", callingMethodParameters);
    }

    @Override
    public void onMediaPeriodReleased(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onMediaPeriodReleased", callingMethodParameters);
    }

    @Override
    public void onReadingStarted(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onReadingStarted", callingMethodParameters);
    }

    @Override
    public void onBandwidthEstimate(EventTime eventTime,
                                    int totalLoadTimeMs,
                                    long totalBytesLoaded,
                                    long bitrateEstimate) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("totalLoadTimeMs", String.valueOf(totalLoadTimeMs));
        callingMethodParameters.put("totalBytesLoaded", String.valueOf(totalBytesLoaded));
        callingMethodParameters.put("bitrateEstimate", String.valueOf(bitrateEstimate));

        infoListeners.onNewInfo("onBandwidthEstimate", callingMethodParameters);
    }

    @Override
    public void onViewportSizeChange(EventTime eventTime, int width, int height) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("width", String.valueOf(width));
        callingMethodParameters.put("height", String.valueOf(height));

        infoListeners.onNewInfo("onViewportSizeChange", callingMethodParameters);
    }

    @Override
    public void onNetworkTypeChanged(EventTime eventTime, @Nullable NetworkInfo networkInfo) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("networkInfo", networkInfo == null ? "null" : networkInfo.toString());

        infoListeners.onNewInfo("onNetworkTypeChanged", callingMethodParameters);
    }

    @Override
    public void onMetadata(EventTime eventTime, Metadata metadata) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("metadata", metadata.toString());

        infoListeners.onNewInfo("onMetadata", callingMethodParameters);
    }

    @Override
    public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("decoderCounters", decoderCounters.toString());

        infoListeners.onNewInfo("onDecoderEnabled", callingMethodParameters);
    }

    @Override
    public void onDecoderInitialized(EventTime eventTime,
                                     int trackType,
                                     String decoderName,
                                     long initializationDurationMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("decoderName", decoderName);
        callingMethodParameters.put("initializationDurationMs", String.valueOf(initializationDurationMs));

        infoListeners.onNewInfo("onDecoderInitialized", callingMethodParameters);
    }

    @Override
    public void onDecoderInputFormatChanged(EventTime eventTime, int trackType, Format format) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("format", format.toString());

        infoListeners.onNewInfo("onDecoderInputFormatChanged", callingMethodParameters);
    }

    @Override
    public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("trackType", String.valueOf(trackType));
        callingMethodParameters.put("decoderCounters", decoderCounters.toString());

        infoListeners.onNewInfo("onDecoderDisabled", callingMethodParameters);
    }

    @Override
    public void onAudioSessionId(EventTime eventTime, int audioSessionId) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("audioSessionId", String.valueOf(audioSessionId));

        infoListeners.onNewInfo("onAudioSessionId", callingMethodParameters);
    }

    @Override
    public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("bufferSize", String.valueOf(bufferSize));
        callingMethodParameters.put("bufferSizeMs", String.valueOf(bufferSizeMs));
        callingMethodParameters.put("elapsedSinceLastFeedMs", String.valueOf(elapsedSinceLastFeedMs));

        infoListeners.onNewInfo("onAudioUnderrun", callingMethodParameters);
    }

    @Override
    public void onDroppedVideoFrames(EventTime eventTime, int droppedFrames, long elapsedMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("droppedFrames", String.valueOf(droppedFrames));
        callingMethodParameters.put("elapsedMs", String.valueOf(elapsedMs));

        infoListeners.onNewInfo("onDroppedVideoFrames", callingMethodParameters);
    }

    @Override
    public void onVideoSizeChanged(EventTime eventTime,
                                   int width,
                                   int height,
                                   int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("width", String.valueOf(width));
        callingMethodParameters.put("height", String.valueOf(height));
        callingMethodParameters.put("unappliedRotationDegrees", String.valueOf(unappliedRotationDegrees));
        callingMethodParameters.put("pixelWidthHeightRatio", String.valueOf(pixelWidthHeightRatio));

        infoListeners.onNewInfo("onVideoSizeChanged", callingMethodParameters);
    }

    @Override
    public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("surface", surface.toString());

        infoListeners.onNewInfo("onRenderedFirstFrame", callingMethodParameters);
    }

    @Override
    public void onDrmKeysLoaded(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onDrmKeysLoaded", callingMethodParameters);
    }

    @Override
    public void onDrmSessionManagerError(EventTime eventTime, Exception error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());
        callingMethodParameters.put("error", error.toString());

        infoListeners.onNewInfo("onDrmSessionManagerError", callingMethodParameters);
    }

    @Override
    public void onDrmKeysRestored(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onDrmKeysRestored", callingMethodParameters);
    }

    @Override
    public void onDrmKeysRemoved(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("eventTime", eventTime.toString());

        infoListeners.onNewInfo("onDrmKeysRemoved", callingMethodParameters);
    }
}
