package com.novoda.noplayer;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

class ResizeModeMapper {

    @AspectRatioFrameLayout.ResizeMode
    int toValue(PlayerView.ResizeMode resizeMode) {
        switch (resizeMode) {
            case FIT:
                return AspectRatioFrameLayout.RESIZE_MODE_FIT;
            case FIXED_WIDTH:
                return AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH;
            case FIXED_HEIGHT:
                return AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;
            case FILL:
                return AspectRatioFrameLayout.RESIZE_MODE_FILL;
            case ZOOM:
                return AspectRatioFrameLayout.RESIZE_MODE_ZOOM;
            default:
                throw new IllegalArgumentException(resizeMode.name() + " is not handled");

        }
    }

    PlayerView.ResizeMode fromValue(@AspectRatioFrameLayout.ResizeMode int value) {
        switch (value) {
            case AspectRatioFrameLayout.RESIZE_MODE_FIT:
                return PlayerView.ResizeMode.FIT;
            case AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH:
                return PlayerView.ResizeMode.FIXED_WIDTH;
            case AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT:
                return PlayerView.ResizeMode.FIXED_HEIGHT;
            case AspectRatioFrameLayout.RESIZE_MODE_FILL:
                return PlayerView.ResizeMode.FILL;
            case AspectRatioFrameLayout.RESIZE_MODE_ZOOM:
                return PlayerView.ResizeMode.ZOOM;
            default:
                throw new IllegalArgumentException("No " + PlayerView.ResizeMode.class.getSimpleName() + " found for value: " + value);
        }
    }
}
