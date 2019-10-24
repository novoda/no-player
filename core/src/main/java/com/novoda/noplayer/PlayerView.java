package com.novoda.noplayer;

import android.view.View;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.novoda.noplayer.model.TextCues;

public interface PlayerView {

    View getContainerView();

    PlayerSurfaceHolder getPlayerSurfaceHolder();

    NoPlayer.VideoSizeChangedListener getVideoSizeChangedListener();

    NoPlayer.StateChangedListener getStateChangedListener();

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(TextCues textCues);

    /**
     * Whether to use the styles set by the user in the accessibility settings instead of the style
     * that comes from the video's subtitle cues.
     *
     * @param enabled Default: true
     */
    void setAccessibilityCaptionsStyleEnabled(boolean enabled);

    void setResizeMode(ResizeMode mode);

    ResizeMode getResizeMode();

    enum ResizeMode {
        FIT(AspectRatioFrameLayout.RESIZE_MODE_FIT),
        FIXED_WIDTH(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH),
        FIXED_HEIGHT(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT),
        FILL(AspectRatioFrameLayout.RESIZE_MODE_FILL),
        ZOOM(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        private final int value;

        ResizeMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        static ResizeMode fromValue(int value) {
            for (ResizeMode resizeMode : values()) {
                if (resizeMode.value == value) {
                    return resizeMode;
                }
            }
            throw new IllegalArgumentException("No " + ResizeMode.class.getSimpleName() + " found for value: " + value);
        }
    }

}
