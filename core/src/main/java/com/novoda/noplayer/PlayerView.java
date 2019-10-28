package com.novoda.noplayer;

import android.view.View;

import com.novoda.noplayer.model.TextCues;

import androidx.annotation.NonNull;

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

    void setResizeMode(@NonNull ResizeMode mode);

    @NonNull
    ResizeMode getResizeMode();

    enum ResizeMode {
        FIT,
        FIXED_WIDTH,
        FIXED_HEIGHT,
        FILL,
        ZOOM
    }
}
