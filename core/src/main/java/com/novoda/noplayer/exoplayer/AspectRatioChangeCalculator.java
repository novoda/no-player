package com.novoda.noplayer.exoplayer;

public class AspectRatioChangeCalculator {

    private final Listener listener;

    public AspectRatioChangeCalculator(Listener listener) {
        this.listener = listener;
    }

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
