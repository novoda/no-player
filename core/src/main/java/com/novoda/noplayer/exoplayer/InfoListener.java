package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.IOException;

/**
 * A listener for debugging information.
 */
public interface InfoListener {

    void onDroppedFrames(int count, long elapsed);

    void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs);

    void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded);

    void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded);

    void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException
                             error, boolean wasCanceled);

    void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs);

}
