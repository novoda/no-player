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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class NoPlayerAnalyticsListener implements AnalyticsListener {

    private final List<AnalyticsListener> listeners = new CopyOnWriteArrayList<>();
    private final List<NoPlayer.DroppedVideoFramesListener> droppedVideoFramesListeners = new CopyOnWriteArrayList<>();

    public void add(AnalyticsListener listener) {
        listeners.add(listener);
    }

    public void add(NoPlayer.DroppedVideoFramesListener listener) {
        droppedVideoFramesListeners.add(listener);
    }

    @Override
    public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int playbackState) {
        for (AnalyticsListener listener : listeners) {
            listener.onPlayerStateChanged(eventTime, playWhenReady, playbackState);
        }
    }

    @Override
    public void onTimelineChanged(EventTime eventTime, int reason) {
        for (AnalyticsListener listener : listeners) {
            listener.onTimelineChanged(eventTime, reason);
        }
    }

    @Override
    public void onPositionDiscontinuity(EventTime eventTime, int reason) {
        for (AnalyticsListener listener : listeners) {
            listener.onPositionDiscontinuity(eventTime, reason);
        }
    }

    @Override
    public void onSeekStarted(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onSeekStarted(eventTime);
        }
    }

    @Override
    public void onSeekProcessed(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onSeekProcessed(eventTime);
        }
    }

    @Override
    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        for (AnalyticsListener listener : listeners) {
            listener.onPlaybackParametersChanged(eventTime, playbackParameters);
        }
    }

    @Override
    public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {
        for (AnalyticsListener listener : listeners) {
            listener.onRepeatModeChanged(eventTime, repeatMode);
        }
    }

    @Override
    public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {
        for (AnalyticsListener listener : listeners) {
            listener.onShuffleModeChanged(eventTime, shuffleModeEnabled);
        }
    }

    @Override
    public void onLoadingChanged(EventTime eventTime, boolean isLoading) {
        for (AnalyticsListener listener : listeners) {
            listener.onLoadingChanged(eventTime, isLoading);
        }
    }

    @Override
    public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {
        for (AnalyticsListener listener : listeners) {
            listener.onPlayerError(eventTime, error);
        }
    }

    @Override
    public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        for (AnalyticsListener listener : listeners) {
            listener.onTracksChanged(eventTime, trackGroups, trackSelections);
        }
    }

    @Override
    public void onLoadStarted(EventTime eventTime,
                              MediaSourceEventListener.LoadEventInfo loadEventInfo,
                              MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : listeners) {
            listener.onLoadStarted(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCompleted(EventTime eventTime,
                                MediaSourceEventListener.LoadEventInfo loadEventInfo,
                                MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : listeners) {
            listener.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCanceled(EventTime eventTime,
                               MediaSourceEventListener.LoadEventInfo loadEventInfo,
                               MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : listeners) {
            listener.onLoadCanceled(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadError(EventTime eventTime,
                            MediaSourceEventListener.LoadEventInfo loadEventInfo,
                            MediaSourceEventListener.MediaLoadData mediaLoadData,
                            IOException error,
                            boolean wasCanceled) {
        for (AnalyticsListener listener : listeners) {
            listener.onLoadError(eventTime, loadEventInfo, mediaLoadData, error, wasCanceled);
        }
    }

    @Override
    public void onDownstreamFormatChanged(EventTime eventTime,
                                          MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : listeners) {
            listener.onDownstreamFormatChanged(eventTime, mediaLoadData);
        }
    }

    @Override
    public void onUpstreamDiscarded(EventTime eventTime,
                                    MediaSourceEventListener.MediaLoadData mediaLoadData) {
        for (AnalyticsListener listener : listeners) {
            listener.onUpstreamDiscarded(eventTime, mediaLoadData);
        }
    }

    @Override
    public void onMediaPeriodCreated(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onMediaPeriodCreated(eventTime);
        }
    }

    @Override
    public void onMediaPeriodReleased(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onMediaPeriodReleased(eventTime);
        }
    }

    @Override
    public void onReadingStarted(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onReadingStarted(eventTime);
        }
    }

    @Override
    public void onBandwidthEstimate(EventTime eventTime,
                                    int totalLoadTimeMs,
                                    long totalBytesLoaded,
                                    long bitrateEstimate) {
        for (AnalyticsListener listener : listeners) {
            listener.onBandwidthEstimate(eventTime, totalLoadTimeMs, totalBytesLoaded, bitrateEstimate);
        }
    }

    @Override
    public void onSurfaceSizeChanged(EventTime eventTime, int width, int height) {
        for (AnalyticsListener listener : listeners) {
            listener.onSurfaceSizeChanged(eventTime, width, height);
        }
    }

    @Override
    public void onMetadata(EventTime eventTime, Metadata metadata) {
        for (AnalyticsListener listener : listeners) {
            listener.onMetadata(eventTime, metadata);
        }
    }

    @Override
    public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        for (AnalyticsListener listener : listeners) {
            listener.onDecoderEnabled(eventTime, trackType, decoderCounters);
        }
    }

    @Override
    public void onDecoderInitialized(EventTime eventTime,
                                     int trackType,
                                     String decoderName,
                                     long initializationDurationMs) {
        for (AnalyticsListener listener : listeners) {
            listener.onDecoderInitialized(eventTime, trackType, decoderName, initializationDurationMs);
        }
    }

    @Override
    public void onDecoderInputFormatChanged(EventTime eventTime,
                                            int trackType,
                                            Format format) {
        for (AnalyticsListener listener : listeners) {
            listener.onDecoderInputFormatChanged(eventTime, trackType, format);
        }
    }

    @Override
    public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        for (AnalyticsListener listener : listeners) {
            listener.onDecoderDisabled(eventTime, trackType, decoderCounters);
        }
    }

    @Override
    public void onAudioSessionId(EventTime eventTime, int audioSessionId) {
        for (AnalyticsListener listener : listeners) {
            listener.onAudioSessionId(eventTime, audioSessionId);
        }
    }

    @Override
    public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        for (AnalyticsListener listener : listeners) {
            listener.onAudioUnderrun(eventTime, bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }

    @Override
    public void onDroppedVideoFrames(EventTime eventTime, int droppedFrames, long elapsedMs) {
        for (AnalyticsListener listener : listeners) {
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
        for (AnalyticsListener listener : listeners) {
            listener.onVideoSizeChanged(eventTime, width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {
        for (AnalyticsListener listener : listeners) {
            listener.onRenderedFirstFrame(eventTime, surface);
        }
    }

    @Override
    public void onDrmKeysLoaded(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onDrmKeysLoaded(eventTime);
        }
    }

    @Override
    public void onDrmSessionManagerError(EventTime eventTime, Exception error) {
        for (AnalyticsListener listener : listeners) {
            listener.onDrmSessionManagerError(eventTime, error);
        }
    }

    @Override
    public void onDrmKeysRestored(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onDrmKeysRestored(eventTime);
        }
    }

    @Override
    public void onDrmKeysRemoved(EventTime eventTime) {
        for (AnalyticsListener listener : listeners) {
            listener.onDrmKeysRemoved(eventTime);
        }
    }
}
