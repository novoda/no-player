package com.novoda.noplayer;

import android.view.View;
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
     * whether to use the styles set by the user in the accesibility settings instead of the style
     * that comes from the video's subtitle cues.
     *
     * Default: Enabled (true)
     */
    void setAccessibilityCaptionsStyleEnabled(boolean enabled);

}
