package com.novoda.noplayer.internal.exoplayer.forwarder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.model.Bitrate;

import java.io.IOException;

class BitrateForwarder implements MediaSourceEventListener {

    private Bitrate videoBitrate = Bitrate.fromBitsPerSecond(0);
    private Bitrate audioBitrate = Bitrate.fromBitsPerSecond(0);

    private final NoPlayer.BitrateChangedListener bitrateChangedListener;

    BitrateForwarder(NoPlayer.BitrateChangedListener bitrateChangedListener) {
        this.bitrateChangedListener = bitrateChangedListener;
    }

    @Override
    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        // TODO: should we send?
    }

    @Override
    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        // TODO: should we send?
    }

    @Override
    public void onLoadStarted(int windowIndex,
                              @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                              LoadEventInfo loadEventInfo,
                              MediaLoadData mediaLoadData) {
        // TODO: should we send?
    }

    @Override
    public void onLoadCompleted(int windowIndex,
                                @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                LoadEventInfo loadEventInfo,
                                MediaLoadData mediaLoadData) {
        // TODO: should we send?
    }

    @Override
    public void onLoadCanceled(int windowIndex,
                               @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                               LoadEventInfo loadEventInfo,
                               MediaLoadData mediaLoadData) {
        // TODO: should we send?
    }

    @Override
    public void onLoadError(int windowIndex,
                            @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                            LoadEventInfo loadEventInfo,
                            MediaLoadData mediaLoadData,
                            IOException error,
                            boolean wasCanceled) {
        // TODO: should we send?
    }

    @Override
    public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        // TODO: should we send?
    }

    @Override
    public void onUpstreamDiscarded(int windowIndex,
                                    MediaSource.MediaPeriodId mediaPeriodId,
                                    MediaLoadData mediaLoadData) {
        // TODO: should we send?
    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex,
                                          @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                          MediaLoadData mediaLoadData) {
        if (mediaLoadData.trackFormat == null) {
            return;
        }

        if (mediaLoadData.trackType == C.TRACK_TYPE_VIDEO) {
            videoBitrate = Bitrate.fromBitsPerSecond(mediaLoadData.trackFormat.bitrate);
            bitrateChangedListener.onBitrateChanged(audioBitrate, videoBitrate);
        } else if (mediaLoadData.trackType == C.TRACK_TYPE_AUDIO) {
            audioBitrate = Bitrate.fromBitsPerSecond(mediaLoadData.trackFormat.bitrate);
            bitrateChangedListener.onBitrateChanged(audioBitrate, videoBitrate);
        }
    }
}
