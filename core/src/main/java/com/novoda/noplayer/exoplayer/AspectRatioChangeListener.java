package com.novoda.noplayer.exoplayer;

public class AspectRatioChangeListener implements ExoPlayerFacade.Listener {

    private final Listener listener;

    public AspectRatioChangeListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        // no op
    }

    @Override
    public void onError(Exception e) {
        // no op
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
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
