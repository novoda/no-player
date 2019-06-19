package com.novoda.noplayer.internal.exoplayer.forwarder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.HashMap;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Methods;
import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Parameters;

// This implements an interface method defined by ExoPlayer
@SuppressWarnings({"PMD.UnusedImports", "checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})
class MediaSourceEventForwarder implements MediaSourceEventListener {

    private static final String NO_MEDIA_PERIOD_ID = null;

    private final NoPlayer.InfoListener infoListener;

    MediaSourceEventForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId.toString());

        infoListener.onNewInfo(Methods.ON_MEDIA_PERIOD_CREATED, callingMethodParameters);
    }

    @Override
    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId.toString());

        infoListener.onNewInfo(Methods.ON_MEDIA_PERIOD_RELEASED, callingMethodParameters);
    }

    @Override
    public void onLoadStarted(int windowIndex,
                              @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                              LoadEventInfo loadEventInfo,
                              MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId == null ? NO_MEDIA_PERIOD_ID : mediaPeriodId.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListener.onNewInfo(Methods.ON_LOAD_STARTED, callingMethodParameters);
    }

    @Override
    public void onLoadCompleted(int windowIndex,
                                @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                LoadEventInfo loadEventInfo,
                                MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId == null ? NO_MEDIA_PERIOD_ID : mediaPeriodId.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListener.onNewInfo(Methods.ON_LOAD_COMPLETED, callingMethodParameters);
    }

    @Override
    public void onLoadCanceled(int windowIndex,
                               @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                               LoadEventInfo loadEventInfo,
                               MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId == null ? NO_MEDIA_PERIOD_ID : mediaPeriodId.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListener.onNewInfo(Methods.ON_LOAD_CANCELED, callingMethodParameters);
    }

    @Override
    public void onLoadError(int windowIndex,
                            @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                            LoadEventInfo loadEventInfo,
                            MediaLoadData mediaLoadData,
                            IOException error,
                            boolean wasCanceled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId == null ? NO_MEDIA_PERIOD_ID : mediaPeriodId.toString());
        callingMethodParameters.put(Parameters.LOAD_EVENT_INFO, loadEventInfo.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA, mediaLoadData.toString());

        infoListener.onNewInfo(Methods.ON_LOAD_CANCELED, callingMethodParameters);
    }

    @Override
    public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID,  mediaPeriodId.toString());

        infoListener.onNewInfo(Methods.ON_READING_STARTED, callingMethodParameters);
    }

    @Override
    public void onUpstreamDiscarded(int windowIndex,
                                    MediaSource.MediaPeriodId mediaPeriodId,
                                    MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID,  mediaPeriodId.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA,  mediaLoadData.toString());

        infoListener.onNewInfo(Methods.ON_UPSTREAM_DISCARDED, callingMethodParameters);
    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex,
                                          @Nullable MediaSource.MediaPeriodId mediaPeriodId,
                                          MediaLoadData mediaLoadData) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.WINDOW_INDEX, String.valueOf(windowIndex));
        callingMethodParameters.put(Parameters.MEDIA_PERIOD_ID, mediaPeriodId == null ? NO_MEDIA_PERIOD_ID : mediaPeriodId.toString());
        callingMethodParameters.put(Parameters.MEDIA_LOAD_DATA,  mediaLoadData.toString());

        infoListener.onNewInfo(Methods.ON_DOWNSTREAM_FORMAT_CHANGED, callingMethodParameters);
    }
}
