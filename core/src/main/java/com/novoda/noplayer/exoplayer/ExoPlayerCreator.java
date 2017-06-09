package com.novoda.noplayer.exoplayer;

import android.content.Context;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.novoda.noplayer.drm.DrmSessionCreator;

class ExoPlayerCreator {

    private final Context context;
    private final DefaultTrackSelector trackSelector;

    ExoPlayerCreator(Context context, DefaultTrackSelector trackSelector) {
        this.context = context;
        this.trackSelector = trackSelector;
    }

    public SimpleExoPlayer create(DrmSessionCreator drmSessionCreator) {
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context, drmSessionCreator.create());
        DefaultLoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }
}
