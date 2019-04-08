package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;

import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertView;

import java.util.ArrayList;
import java.util.List;

final class MainThreadAwareAdvertView implements AdvertView {

    private final AdvertView advertView;
    private final Handler handler;

    MainThreadAwareAdvertView(AdvertView advertView, Handler handler) {
        this.advertView = advertView;
        this.handler = handler;
    }

    @Override
    public void setup(final List<AdvertBreak> advertBreaks, final AdvertInteractionListener advertInteractionListener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                advertView.setup(new ArrayList<>(advertBreaks), advertInteractionListener);
            }
        });
    }

    @Override
    public void removeMarker(final AdvertBreak advertBreak) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                advertView.removeMarker(advertBreak); // TODO: Maybe we should create a copy?
            }
        });
    }
}
