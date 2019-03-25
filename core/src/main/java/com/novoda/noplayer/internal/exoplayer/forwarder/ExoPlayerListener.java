package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.support.annotation.Nullable;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerListener implements Player.EventListener, MediaSourceEventListener, AnalyticsListener, VideoListener, DefaultDrmSessionEventListener {

    private final List<Player.EventListener> playerEventListeners = new CopyOnWriteArrayList<>();
    private final List<MediaSourceEventListener> mediaSourceEventListeners = new CopyOnWriteArrayList<>();
    private final List<AnalyticsListener> analyticsListeners = new CopyOnWriteArrayList<>();
    private final List<NoPlayer.DroppedVideoFramesListener> droppedVideoFramesListeners = new CopyOnWriteArrayList<>();
    private final List<VideoListener> videoListeners = new CopyOnWriteArrayList<>();
    private final List<DefaultDrmSessionEventListener> drmSessionEventListeners = new CopyOnWriteArrayList<>();

    public void add(Player.EventListener listener) {
        playerEventListeners.add(listener);
    }

    public void add(MediaSourceEventListener listener) {
        mediaSourceEventListeners.add(listener);
    }

    public void add(AnalyticsListener listener) {
        analyticsListeners.add(listener);
    }

    public void add(NoPlayer.DroppedVideoFramesListener listener) {
        droppedVideoFramesListeners.add(listener);
    }

    public void add(VideoListener listener) {
        videoListeners.add(listener);
    }

    void add(DefaultDrmSessionEventListener listener) {
        drmSessionEventListeners.add(listener);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, @Player.TimelineChangeReason int reason) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onTimelineChanged(timeline, manifest, reason);
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onTracksChanged(trackGroups, trackSelections);
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onLoadingChanged(isLoading);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    @Override
    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onRepeatModeChanged(repeatMode);
        }
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onShuffleModeEnabledChanged(shuffleModeEnabled);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onPlayerError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onPositionDiscontinuity(reason);
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onPlaybackParametersChanged(playbackParameters);
        }
    }

    @Override
    public void onSeekProcessed() {
        for (Player.EventListener listener : playerEventListeners) {
            listener.onSeekProcessed();
        }
    }

    @Override
    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onMediaPeriodCreated(windowIndex, mediaPeriodId);
        }
    }

    @Override
    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onMediaPeriodReleased(windowIndex, mediaPeriodId);
        }
    }

    @Override
    public void onLoadStarted(int windowIndex,
                              @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                              LoadEventInfo loadEventInfo,
                              MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onLoadStarted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCompleted(int windowIndex,
                                @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                LoadEventInfo loadEventInfo,
                                MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onLoadCompleted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCanceled(int windowIndex,
                               @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                               LoadEventInfo loadEventInfo,
                               MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onLoadCanceled(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadError(int windowIndex,
                            @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                            LoadEventInfo loadEventInfo,
                            MediaLoadData mediaLoadData,
                            IOException error, boolean wasCanceled) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onLoadError(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData, error, wasCanceled);
        }
    }

    @Override
    public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onReadingStarted(windowIndex, mediaPeriodId);
        }
    }

    @Override
    public void onUpstreamDiscarded(int windowIndex,
                                    MediaSource.MediaPeriodId mediaPeriodId,
                                    MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onUpstreamDiscarded(windowIndex, mediaPeriodId, mediaLoadData);
        }
    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex,
                                          @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                          MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : mediaSourceEventListeners) {
            listener.onDownstreamFormatChanged(windowIndex, mediaPeriodId, mediaLoadData);
        }
    }

    @Override
    public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int playbackState) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onPlayerStateChanged(eventTime, playWhenReady, playbackState);
        }
    }

    @Override
    public void onTimelineChanged(EventTime eventTime, int reason) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onTimelineChanged(eventTime, reason);
        }
    }

    @Override
    public void onPositionDiscontinuity(EventTime eventTime, int reason) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onPositionDiscontinuity(eventTime, reason);
        }
    }

    @Override
    public void onSeekStarted(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onSeekStarted(eventTime);
        }
    }

    @Override
    public void onSeekProcessed(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onSeekProcessed(eventTime);
        }
    }

    @Override
    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onPlaybackParametersChanged(eventTime, playbackParameters);
        }
    }

    @Override
    public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onRepeatModeChanged(eventTime, repeatMode);
        }
    }

    @Override
    public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onShuffleModeChanged(eventTime, shuffleModeEnabled);
        }
    }

    @Override
    public void onLoadingChanged(EventTime eventTime, boolean isLoading) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onLoadingChanged(eventTime, isLoading);
        }
    }

    @Override
    public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onPlayerError(eventTime, error);
        }
    }

    @Override
    public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onTracksChanged(eventTime, trackGroups, trackSelections);
        }
    }

    @Override
    public void onLoadStarted(EventTime eventTime,
                              MediaSourceEventListener.LoadEventInfo loadEventInfo,
                              MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onLoadStarted(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCompleted(EventTime eventTime,
                                MediaSourceEventListener.LoadEventInfo loadEventInfo,
                                MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCanceled(EventTime eventTime,
                               MediaSourceEventListener.LoadEventInfo loadEventInfo,
                               MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onLoadCanceled(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadError(EventTime eventTime,
                            MediaSourceEventListener.LoadEventInfo loadEventInfo,
                            MediaSourceEventListener.MediaLoadData mediaLoadData,
                            IOException error,
                            boolean wasCanceled) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onLoadError(eventTime, loadEventInfo, mediaLoadData, error, wasCanceled);
        }
    }

    @Override
    public void onDownstreamFormatChanged(EventTime eventTime,
                                          MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDownstreamFormatChanged(eventTime, mediaLoadData);
        }
    }

    @Override
    public void onUpstreamDiscarded(EventTime eventTime,
                                    MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onUpstreamDiscarded(eventTime, mediaLoadData);
        }
    }

    @Override
    public void onMediaPeriodCreated(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onMediaPeriodCreated(eventTime);
        }
    }

    @Override
    public void onMediaPeriodReleased(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onMediaPeriodReleased(eventTime);
        }
    }

    @Override
    public void onReadingStarted(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onReadingStarted(eventTime);
        }
    }

    @Override
    public void onBandwidthEstimate(EventTime eventTime,
                                    int totalLoadTimeMs,
                                    long totalBytesLoaded,
                                    long bitrateEstimate) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onBandwidthEstimate(eventTime, totalLoadTimeMs, totalBytesLoaded, bitrateEstimate);
        }
    }

    @Override
    public void onSurfaceSizeChanged(EventTime eventTime, int width, int height) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onSurfaceSizeChanged(eventTime, width, height);
        }
    }

    @Override
    public void onMetadata(EventTime eventTime, Metadata metadata) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onMetadata(eventTime, metadata);
        }
    }

    @Override
    public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDecoderEnabled(eventTime, trackType, decoderCounters);
        }
    }

    @Override
    public void onDecoderInitialized(EventTime eventTime,
                                     int trackType,
                                     String decoderName,
                                     long initializationDurationMs) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDecoderInitialized(eventTime, trackType, decoderName, initializationDurationMs);
        }
    }

    @Override
    public void onDecoderInputFormatChanged(EventTime eventTime,
                                            int trackType,
                                            Format format) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDecoderInputFormatChanged(eventTime, trackType, format);
        }
    }

    @Override
    public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDecoderDisabled(eventTime, trackType, decoderCounters);
        }
    }

    @Override
    public void onAudioSessionId(EventTime eventTime, int audioSessionId) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onAudioSessionId(eventTime, audioSessionId);
        }
    }

    @Override
    public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onAudioUnderrun(eventTime, bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }

    @Override
    public void onDroppedVideoFrames(EventTime eventTime, int droppedFrames, long elapsedMs) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDroppedVideoFrames(eventTime, droppedFrames, elapsedMs);
        }

        for (NoPlayer.DroppedVideoFramesListener listener : droppedVideoFramesListeners) {
            listener.onDroppedVideoFrames(droppedFrames, elapsedMs);
        }
    }

    @Override
    public void onVideoSizeChanged(EventTime eventTime,
                                   int width,
                                   int height,
                                   int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onVideoSizeChanged(eventTime, width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onRenderedFirstFrame(eventTime, surface);
        }
    }

    @Override
    public void onDrmKeysLoaded(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDrmKeysLoaded(eventTime);
        }
    }

    @Override
    public void onDrmSessionManagerError(EventTime eventTime, Exception error) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDrmSessionManagerError(eventTime, error);
        }
    }

    @Override
    public void onDrmKeysRestored(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDrmKeysRestored(eventTime);
        }
    }

    @Override
    public void onDrmKeysRemoved(EventTime eventTime) {
        for (AnalyticsListener listener : analyticsListeners) {
            listener.onDrmKeysRemoved(eventTime);
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (VideoListener listener : videoListeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame() {
        for (VideoListener listener : videoListeners) {
            listener.onRenderedFirstFrame();
        }
    }

    @Override
    public void onDrmKeysLoaded() {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmKeysLoaded();
        }
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmSessionManagerError(e);
        }
    }

    @Override
    public void onDrmKeysRestored() {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmKeysRestored();
        }
    }

    @Override
    public void onDrmKeysRemoved() {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmKeysRemoved();
        }
    }

}
