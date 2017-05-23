package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;

public class AspectRatioChangeForwarder implements ExoPlayerTwoFacade.Forwarder {

    private final Listener listener;

    public AspectRatioChangeForwarder(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void forwardPlayerStateChanged(boolean playWhenReady, int playbackState) {
        //no-op

    }

    @Override
    public void forwardPlayerError(ExoPlaybackException error) {
        //no-op
    }

    @Override
    public void forwardVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        float aspectRatio = determineAspectRatio(width, height, pixelWidthHeightRatio);
        listener.onNewAspectRatio(aspectRatio);
    }

    private float determineAspectRatio(int videoWidth, int videoHeight, float pixelWidthHeightRatio) {
        if (videoHeight == 0) {
            return 1;
        }
        return (videoWidth * pixelWidthHeightRatio) / videoHeight;
    }

    public interface Listener {

        void onNewAspectRatio(float aspectRatio);

    }
}
