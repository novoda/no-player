package com.novoda.noplayer.internal.exoplayer.forwarder;

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

import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Methods;
import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Parameters;

class AnalyticsListenerForwarder implements AnalyticsListener {

    private final NoPlayer.InfoListener infoListeners;

    AnalyticsListenerForwarder(NoPlayer.InfoListener infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int playbackState) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.PLAY_WHEN_READY, String.valueOf(playWhenReady));
        callingMethodParameters.put(Parameters.PLAYBACK_STATE, String.valueOf(playbackState));

        infoListeners.onNewInfo(Methods.ON_PLAYER_STATE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onTimelineChanged(EventTime eventTime, int reason) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.REASON, String.valueOf(reason));

        infoListeners.onNewInfo(Methods.ON_TIMELINE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onPositionDiscontinuity(EventTime eventTime, int reason) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.REASON, String.valueOf(reason));

        infoListeners.onNewInfo(Methods.ON_POSITION_DISCONTINUITY, callingMethodParameters);
    }

    @Override
    public void onSeekStarted(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_SEEK_STARTED, callingMethodParameters);
    }

    @Override
    public void onSeekProcessed(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_SEEK_PROCESSED, callingMethodParameters);
    }

    @Override
    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.PLAYBACK_PARAMETERS, playbackParameters.toString());

        infoListeners.onNewInfo(Methods.ON_PLAYBACK_PARAMETERS_CHANGED, callingMethodParameters);
    }

    @Override
    public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.REPEAT_MODE, String.valueOf(repeatMode));

        infoListeners.onNewInfo(Methods.ON_REPEAT_MODE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.SHUFFLE_MODE_ENABLED, String.valueOf(shuffleModeEnabled));

        infoListeners.onNewInfo(Methods.ON_SHUFFLE_MODE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onLoadingChanged(EventTime eventTime, boolean isLoading) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.IS_LOADING, String.valueOf(isLoading));

        infoListeners.onNewInfo(Methods.ON_LOADING_CHANGED, callingMethodParameters);
    }

    @Override
    public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.ERROR, error.toString());

        infoListeners.onNewInfo(Methods.ON_PLAYER_ERROR, callingMethodParameters);
    }

    @Override
    public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.TRACK_GROUPS, trackGroups.toString());
        callingMethodParameters.put(Parameters.TRACK_SELECTIONS, trackSelections.toString());

        infoListeners.onNewInfo(Methods.ON_TRACKS_CHANGED, callingMethodParameters);
    }

    @Override
    public void onLoadStarted(EventTime eventTime,
                              MediaSourceEventListener.LoadEventInfo loadEventInfo,
                              MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListeners.onNewInfo(Methods.ON_LOAD_STARTED, callingMethodParameters);
    }

    @Override
    public void onLoadCompleted(EventTime eventTime,
                                MediaSourceEventListener.LoadEventInfo loadEventInfo,
                                MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListeners.onNewInfo(Methods.ON_LOAD_COMPLETED, callingMethodParameters);
    }

    @Override
    public void onLoadCanceled(EventTime eventTime,
                               MediaSourceEventListener.LoadEventInfo loadEventInfo,
                               MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListeners.onNewInfo(Methods.ON_LOAD_CANCELED, callingMethodParameters);
    }

    @Override
    public void onLoadError(EventTime eventTime,
                            MediaSourceEventListener.LoadEventInfo loadEventInfo,
                            MediaSourceEventListener.MediaLoadData mediaLoadData,
                            IOException error,
                            boolean wasCanceled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());
        callingMethodParameters.put(Parameters.ERROR, error.toString());
        callingMethodParameters.put(Parameters.WAS_CANCELED, String.valueOf(wasCanceled));

        infoListeners.onNewInfo(Methods.ON_LOAD_ERROR, callingMethodParameters);
    }

    @Override
    public void onDownstreamFormatChanged(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListeners.onNewInfo(Methods.ON_DOWNSTREAM_FORMAT_CHANGED, callingMethodParameters);
    }

    @Override
    public void onUpstreamDiscarded(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListeners.onNewInfo(Methods.ON_UPSTREAM_DISCARDED, callingMethodParameters);
    }

    @Override
    public void onMediaPeriodCreated(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_MEDIA_PERIOD_CREATED, callingMethodParameters);
    }

    @Override
    public void onMediaPeriodReleased(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_MEDIA_PERIOD_RELEASED, callingMethodParameters);
    }

    @Override
    public void onReadingStarted(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_READING_STARTED, callingMethodParameters);
    }

    @Override
    public void onBandwidthEstimate(EventTime eventTime,
                                    int totalLoadTimeMs,
                                    long totalBytesLoaded,
                                    long bitrateEstimate) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.TOTAL_LOAD_TIME_MS, String.valueOf(totalLoadTimeMs));
        callingMethodParameters.put(Parameters.TOTAL_BYTES_LOADED, String.valueOf(totalBytesLoaded));
        callingMethodParameters.put(Parameters.BITRATE_ESTIMATE, String.valueOf(bitrateEstimate));

        infoListeners.onNewInfo(Methods.ON_BANDWIDTH_ESTIMATE, callingMethodParameters);
    }

    @Override
    public void onSurfaceSizeChanged(EventTime eventTime, int width, int height) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.WIDTH, String.valueOf(width));
        callingMethodParameters.put(Parameters.HEIGHT, String.valueOf(height));

        infoListeners.onNewInfo(Methods.ON_SURFACE_SIZE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onMetadata(EventTime eventTime, Metadata metadata) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.METADATA, metadata.toString());

        infoListeners.onNewInfo(Methods.ON_METADATA, callingMethodParameters);
    }

    @Override
    public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.DECODER_COUNTERS, decoderCounters.toString());

        infoListeners.onNewInfo(Methods.ON_DECODER_ENABLED, callingMethodParameters);
    }

    @Override
    public void onDecoderInitialized(EventTime eventTime,
                                     int trackType,
                                     String decoderName,
                                     long initializationDurationMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.DECODER_NAME, decoderName);
        callingMethodParameters.put(Parameters.INITIALIZATION_DURATION_MS, String.valueOf(initializationDurationMs));

        infoListeners.onNewInfo(Methods.ON_DECODER_INITIALIZED, callingMethodParameters);
    }

    @Override
    public void onDecoderInputFormatChanged(EventTime eventTime, int trackType, Format format) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.FORMAT, format.toString());

        infoListeners.onNewInfo(Methods.ON_DECODER_INPUT_FORMAT_CHANGED, callingMethodParameters);
    }

    @Override
    public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.DECODER_COUNTERS, decoderCounters.toString());

        infoListeners.onNewInfo(Methods.ON_DECODER_DISABLED, callingMethodParameters);
    }

    @Override
    public void onAudioSessionId(EventTime eventTime, int audioSessionId) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.AUDIO_SESSION_ID, String.valueOf(audioSessionId));

        infoListeners.onNewInfo(Methods.ON_AUDIO_SESSION_ID, callingMethodParameters);
    }

    @Override
    public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.BUFFER_SIZE, String.valueOf(bufferSize));
        callingMethodParameters.put(Parameters.BUFFER_SIZE_MS, String.valueOf(bufferSizeMs));
        callingMethodParameters.put(Parameters.ELAPSED_SINCE_LAST_FEED_MS, String.valueOf(elapsedSinceLastFeedMs));

        infoListeners.onNewInfo(Methods.ON_AUDIO_UNDERRUN, callingMethodParameters);
    }

    @Override
    public void onDroppedVideoFrames(EventTime eventTime, int droppedFrames, long elapsedMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.DROPPED_FRAMES, String.valueOf(droppedFrames));
        callingMethodParameters.put(Parameters.ELAPSED_MS, String.valueOf(elapsedMs));

        infoListeners.onNewInfo(Methods.ON_DROPPED_VIDEO_FRAMES, callingMethodParameters);
    }

    @Override
    public void onVideoSizeChanged(EventTime eventTime,
                                   int width,
                                   int height,
                                   int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.WIDTH, String.valueOf(width));
        callingMethodParameters.put(Parameters.HEIGHT, String.valueOf(height));
        callingMethodParameters.put(Parameters.UNAPPLIED_ROTATION_DEGREES, String.valueOf(unappliedRotationDegrees));
        callingMethodParameters.put(Parameters.PIXEL_WIDTH_HEIGHT_RATIO, String.valueOf(pixelWidthHeightRatio));

        infoListeners.onNewInfo(Methods.ON_VIDEO_SIZE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.SURFACE, surface.toString());

        infoListeners.onNewInfo(Methods.ON_RENDERED_FIRST_FRAME, callingMethodParameters);
    }

    @Override
    public void onDrmKeysLoaded(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_DRM_KEYS_LOADED, callingMethodParameters);
    }

    @Override
    public void onDrmSessionManagerError(EventTime eventTime, Exception error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());
        callingMethodParameters.put(Parameters.ERROR, error.toString());

        infoListeners.onNewInfo(Methods.ON_DRM_SESSION_MANAGER_ERROR, callingMethodParameters);
    }

    @Override
    public void onDrmKeysRestored(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_DRM_KEYS_RESTORED, callingMethodParameters);
    }

    @Override
    public void onDrmKeysRemoved(EventTime eventTime) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.EVENT_TIME, eventTime.toString());

        infoListeners.onNewInfo(Methods.ON_DRM_KEYS_REMOVED, callingMethodParameters);
    }
}
