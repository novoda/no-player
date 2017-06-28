package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.Player;

import java.io.IOException;
import java.util.HashMap;

class ExtractorInfoForwarder implements ExtractorMediaSource.EventListener {

    private final Player.InfoListener infoListener;

    ExtractorInfoForwarder(Player.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onLoadError(IOException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("error", String.valueOf(error));

        infoListener.onNewInfo("onLoadError", callingMethodParameters);
    }
}
