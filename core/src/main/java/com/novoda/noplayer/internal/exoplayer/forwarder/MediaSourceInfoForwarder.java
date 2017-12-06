package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.HashMap;

@SuppressWarnings("PMD.UnusedImports") // These two
class MediaSourceInfoForwarder implements AdaptiveMediaSourceEventListener {

    private final NoPlayer.InfoListener infoListener;

    MediaSourceInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"}) // This implements an interface method defined by ExoPlayer
    @Override
    public void onLoadStarted(DataSpec dataSpec,
                              int dataType,
                              int trackType,
                              Format trackFormat,
                              int trackSelectionReason,
                              Object trackSelectionData,
                              long mediaStartTimeMs,
                              long mediaEndTimeMs,
                              long elapsedRealtimeMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DATA_SPEC, String.valueOf(dataSpec));
        callingMethodParameters.put(Parameters.DATA_TYPE, String.valueOf(dataType));
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.TRACK_FORMAT, String.valueOf(trackFormat));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_REASON, String.valueOf(trackSelectionReason));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_DATA, String.valueOf(trackSelectionData));
        callingMethodParameters.put(Parameters.MEDIA_START_TIME_MS, String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put(Parameters.MEDIA_END_TIME_MS, String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put(Parameters.ELAPSED_REALTIME_MS, String.valueOf(elapsedRealtimeMs));

        infoListener.onNewInfo(Methods.ON_LOAD_STARTED, callingMethodParameters);
    }

    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"}) // This implements an interface method defined by ExoPlayer
    @Override
    public void onLoadCompleted(DataSpec dataSpec,
                                int dataType,
                                int trackType,
                                Format trackFormat,
                                int trackSelectionReason,
                                Object trackSelectionData,
                                long mediaStartTimeMs,
                                long mediaEndTimeMs,
                                long elapsedRealtimeMs,
                                long loadDurationMs,
                                long bytesLoaded) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DATA_SPEC, String.valueOf(dataSpec));
        callingMethodParameters.put(Parameters.DATA_TYPE, String.valueOf(dataType));
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.TRACK_FORMAT, String.valueOf(trackFormat));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_REASON, String.valueOf(trackSelectionReason));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_DATA, String.valueOf(trackSelectionData));
        callingMethodParameters.put(Parameters.MEDIA_START_TIME_MS, String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put(Parameters.MEDIA_END_TIME_MS, String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put(Parameters.ELAPSED_REALTIME_MS, String.valueOf(elapsedRealtimeMs));
        callingMethodParameters.put(Parameters.LOAD_DURATION_MS, String.valueOf(loadDurationMs));
        callingMethodParameters.put(Parameters.BYTES_LOADED, String.valueOf(bytesLoaded));

        infoListener.onNewInfo(Methods.ON_LOAD_COMPLETED, callingMethodParameters);
    }

    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"}) // This implements an interface method defined by ExoPlayer
    @Override
    public void onLoadCanceled(DataSpec dataSpec,
                               int dataType,
                               int trackType,
                               Format trackFormat,
                               int trackSelectionReason,
                               Object trackSelectionData,
                               long mediaStartTimeMs,
                               long mediaEndTimeMs,
                               long elapsedRealtimeMs,
                               long loadDurationMs,
                               long bytesLoaded) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DATA_SPEC, String.valueOf(dataSpec));
        callingMethodParameters.put(Parameters.DATA_TYPE, String.valueOf(dataType));
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.TRACK_FORMAT, String.valueOf(trackFormat));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_REASON, String.valueOf(trackSelectionReason));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_DATA, String.valueOf(trackSelectionData));
        callingMethodParameters.put(Parameters.MEDIA_START_TIME_MS, String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put(Parameters.MEDIA_END_TIME_MS, String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put(Parameters.ELAPSED_REALTIME_MS, String.valueOf(elapsedRealtimeMs));
        callingMethodParameters.put(Parameters.LOAD_DURATION_MS, String.valueOf(loadDurationMs));
        callingMethodParameters.put(Parameters.BYTES_LOADED, String.valueOf(bytesLoaded));

        infoListener.onNewInfo(Methods.ON_LOAD_CANCELED, callingMethodParameters);
    }

    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"}) // This implements an interface method defined by ExoPlayer
    @Override
    public void onLoadError(DataSpec dataSpec,
                            int dataType,
                            int trackType,
                            Format trackFormat,
                            int trackSelectionReason,
                            Object trackSelectionData,
                            long mediaStartTimeMs,
                            long mediaEndTimeMs,
                            long elapsedRealtimeMs,
                            long loadDurationMs,
                            long bytesLoaded,
                            IOException error,
                            boolean wasCanceled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.DATA_SPEC, String.valueOf(dataSpec));
        callingMethodParameters.put(Parameters.DATA_TYPE, String.valueOf(dataType));
        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.TRACK_FORMAT, String.valueOf(trackFormat));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_REASON, String.valueOf(trackSelectionReason));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_DATA, String.valueOf(trackSelectionData));
        callingMethodParameters.put(Parameters.MEDIA_START_TIME_MS, String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put(Parameters.MEDIA_END_TIME_MS, String.valueOf(mediaEndTimeMs));
        callingMethodParameters.put(Parameters.ELAPSED_REALTIME_MS, String.valueOf(elapsedRealtimeMs));
        callingMethodParameters.put(Parameters.LOAD_DURATION_MS, String.valueOf(loadDurationMs));
        callingMethodParameters.put(Parameters.BYTES_LOADED, String.valueOf(bytesLoaded));
        callingMethodParameters.put(Parameters.IO_EXCEPTION, String.valueOf(error));
        callingMethodParameters.put(Parameters.WAS_CANCELED, String.valueOf(wasCanceled));

        infoListener.onNewInfo(Methods.ON_LOAD_ERROR, callingMethodParameters);
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.MEDIA_START_TIME_MS, String.valueOf(mediaStartTimeMs));
        callingMethodParameters.put(Parameters.MEDIA_END_TIME_MS, String.valueOf(mediaEndTimeMs));

        infoListener.onNewInfo(Methods.ON_UPSTREAM_DISCARDED, callingMethodParameters);
    }

    @Override
    public void onDownstreamFormatChanged(int trackType,
                                          Format trackFormat,
                                          int trackSelectionReason,
                                          Object trackSelectionData,
                                          long mediaTimeMs) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.TRACK_TYPE, String.valueOf(trackType));
        callingMethodParameters.put(Parameters.TRACK_FORMAT, String.valueOf(trackFormat));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_REASON, String.valueOf(trackSelectionReason));
        callingMethodParameters.put(Parameters.TRACK_SELECTION_DATA, String.valueOf(trackSelectionData));
        callingMethodParameters.put(Parameters.MEDIA_TIME_MS, String.valueOf(mediaTimeMs));

        infoListener.onNewInfo(Methods.ON_DOWNSTREAM_FORMAT_CHANGED, callingMethodParameters);
    }

    static final class Parameters {

        private Parameters() {
            // Not instantiable
        }

        static final String DATA_SPEC = "dataSpec";
        static final String DATA_TYPE = "dataType";
        static final String TRACK_TYPE = "trackType";
        static final String TRACK_FORMAT = "trackFormat";
        static final String TRACK_SELECTION_REASON = "trackSelectionReason";
        static final String TRACK_SELECTION_DATA = "trackSelectionData";
        static final String MEDIA_START_TIME_MS = "mediaStartTimeMs";
        static final String MEDIA_END_TIME_MS = "mediaEndTimeMs";
        static final String ELAPSED_REALTIME_MS = "elapsedRealtimeMs";
        static final String LOAD_DURATION_MS = "loadDurationMs";
        static final String BYTES_LOADED = "bytesLoaded";
        static final String IO_EXCEPTION = "IOException";
        static final String WAS_CANCELED = "wasCanceled";
        static final String MEDIA_TIME_MS = "mediaTimeMs";
    }

    static final class Methods {

        private Methods() {
            // Not instantiable
        }

        static final String ON_LOAD_STARTED = "onLoadStarted";
        static final String ON_LOAD_COMPLETED = "onLoadCompleted";
        static final String ON_LOAD_CANCELED = "onLoadCanceled";
        static final String ON_LOAD_ERROR = "onLoadError";
        static final String ON_UPSTREAM_DISCARDED = "onUpstreamDiscarded";
        static final String ON_DOWNSTREAM_FORMAT_CHANGED = "onDownstreamFormatChanged";
    }
}
