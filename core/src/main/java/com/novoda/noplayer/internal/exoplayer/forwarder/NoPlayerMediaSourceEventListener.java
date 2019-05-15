package com.novoda.noplayer.internal.exoplayer.forwarder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"}) // This implements an interface method defined by ExoPlayer
class NoPlayerMediaSourceEventListener implements MediaSourceEventListener {

    private final List<MediaSourceEventListener> listeners = new CopyOnWriteArrayList<>();

    public void add(MediaSourceEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onMediaPeriodCreated(windowIndex, mediaPeriodId);
        }
    }

    @Override
    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onMediaPeriodReleased(windowIndex, mediaPeriodId);
        }
    }

    @Override
    public void onLoadStarted(int windowIndex,
                              @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                              LoadEventInfo loadEventInfo,
                              MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onLoadStarted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCompleted(int windowIndex,
                                @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                LoadEventInfo loadEventInfo,
                                MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onLoadCompleted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadCanceled(int windowIndex,
                               @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                               LoadEventInfo loadEventInfo,
                               MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onLoadCanceled(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData);
        }
    }

    @Override
    public void onLoadError(int windowIndex,
                            @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                            LoadEventInfo loadEventInfo,
                            MediaLoadData mediaLoadData,
                            IOException error, boolean wasCanceled) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onLoadError(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData, error, wasCanceled);
        }
    }

    @Override
    public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onReadingStarted(windowIndex, mediaPeriodId);
        }
    }

    @Override
    public void onUpstreamDiscarded(int windowIndex,
                                    MediaSource.MediaPeriodId mediaPeriodId,
                                    MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onUpstreamDiscarded(windowIndex, mediaPeriodId, mediaLoadData);
        }
    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex,
                                          @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                          MediaLoadData mediaLoadData) {
        for (MediaSourceEventListener listener : listeners) {
            listener.onDownstreamFormatChanged(windowIndex, mediaPeriodId, mediaLoadData);
        }
    }
}
