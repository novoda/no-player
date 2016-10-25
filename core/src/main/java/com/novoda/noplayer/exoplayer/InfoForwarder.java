package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.chunk.Format;
import com.novoda.noplayer.Player;

class InfoForwarder implements InfoListener {

    private final Player.BitrateChangedListener bitrateChangedListener;
    private Bitrate videoBitrate = Bitrate.fromBitsPerSecond(0);
    private Bitrate audioBitrate = Bitrate.fromBitsPerSecond(0);

    InfoForwarder(Player.BitrateChangedListener bitrateChangedListener) {
        this.bitrateChangedListener = bitrateChangedListener;
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        // do nothing
    }

    @Override
    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
        // do nothing
    }

    @Override
    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
        // do nothing
    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        // do nothing
    }

    @Override
    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        // do nothing
    }

    @Override
    public void onAvailableRangeChanged(int sourceId, TimeRange availableRange) {
        // do nothing
    }

    @Override
    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs) {
        if (sourceId == Renderers.VIDEO_RENDERER_ID) {
            videoBitrate = Bitrate.fromBitsPerSecond(format.bitrate);
            bitrateChangedListener.onBitrateChanged(audioBitrate, videoBitrate);
        } else if (sourceId == Renderers.AUDIO_RENDERER_ID) {
            audioBitrate = Bitrate.fromBitsPerSecond(format.bitrate);
            bitrateChangedListener.onBitrateChanged(audioBitrate, videoBitrate);
        }
    }
}
