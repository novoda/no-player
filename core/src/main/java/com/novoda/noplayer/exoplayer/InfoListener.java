package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.chunk.Format;

/**
 * A listener for debugging information.
 */
public interface InfoListener {

    void onDroppedFrames(int count, long elapsed);

    void onLoadStarted(int sourceId, long length, int type, int trigger, Format format,
                       long mediaStartTimeMs, long mediaEndTimeMs);

    void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format,
                         long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs);

    void onDecoderInitialized(String decoderName, long elapsedRealtimeMs,
                              long initializationDurationMs);

    void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs);

    void onAvailableRangeChanged(int sourceId, TimeRange availableRange);

    void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs);
}
