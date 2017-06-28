package com.novoda.noplayer;

class AspectRatioChangeCalculator {

    private final Listener listener;

    AspectRatioChangeCalculator(Listener listener) {
        this.listener = listener;
    }

    void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio) {
        float aspectRatio = determineAspectRatio(width, height, pixelWidthHeightRatio);
        listener.onNewAspectRatio(aspectRatio);
    }

    private float determineAspectRatio(int videoWidth, int videoHeight, float pixelWidthHeightRatio) {
        if (videoHeight == 0) {
            return 1;
        }
        return (videoWidth * pixelWidthHeightRatio) / videoHeight;
    }

    interface Listener {

        void onNewAspectRatio(float aspectRatio);
    }
}
