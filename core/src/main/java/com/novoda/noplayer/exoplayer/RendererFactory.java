package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer.util.Util;
import com.novoda.noplayer.ContentType;
import com.novoda.notils.exception.DeveloperError;

public class RendererFactory {

    private final Context context;
    private final DrmSessionCreator drmSessionCreator;

    public RendererFactory(Context context, DrmSessionCreator drmSessionCreator) {
        this.context = context;
        this.drmSessionCreator = drmSessionCreator;
    }

    public RendererCreator createRenderer(ContentType contentType) {
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");

        Handler mainThreadHandler = new Handler();

        switch (contentType) {
            case DASH:
                return new DashRendererCreator(context, userAgent, mainThreadHandler, drmSessionCreator);
            case H264:
                return new ExtractorRendererCreator(context, userAgent, mainThreadHandler);
            case HLS:
                return new HlsRendererCreator(context, userAgent, mainThreadHandler);
            default:
                throw new DeveloperError("Unhandled content type : " + contentType);
        }
    }
}
