package com.novoda.noplayer.exoplayer;

import java.util.Map;

/**
 * A listener for debugging information.
 */
public interface InfoListener {

//    void onDroppedFrames(int count, long elapsed);
//
//    void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
//            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs);
//
//    void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
//            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded);
//
//    void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
//            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded);
//
//    void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
//            trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException
//                             error, boolean wasCanceled);
//
//    void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs);

    void onNewInfo(String callingMethod, Map<String, String> keyValuePairs);

}
