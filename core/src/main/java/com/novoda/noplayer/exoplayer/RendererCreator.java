package com.novoda.noplayer.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer.upstream.BandwidthMeter;

public interface RendererCreator {

    void create(Uri contentUri, ExoPlayerFacade player, Callback callback);

    int getRendererCount();

    void release();

    interface Callback {

        void onCreated(Renderers renderers, BandwidthMeter bandwidthMeter);

        void onError(Exception e);

    }

    class EagerReleaseException extends RuntimeException {

        EagerReleaseException() {
            super("Attempted to cancel a non started renderer creation");
        }

    }

}
