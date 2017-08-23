package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.HashMap;

class ExtractorInfoForwarder implements ExtractorMediaSource.EventListener {

    private final NoPlayer.InfoListener infoListener;

    ExtractorInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onLoadError(IOException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("error", String.valueOf(error));

        infoListener.onNewInfo("onLoadError", callingMethodParameters);
    }
}
