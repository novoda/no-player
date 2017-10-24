package com.novoda.noplayer;

import android.view.View;

import com.novoda.noplayer.model.TextCues;
import com.novoda.noplayer.model.VideoFormat;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    NoPlayer.VideoSizeChangedListener getVideoSizeChangedListener();

    NoPlayer.StateChangedListener getStateChangedListener();

    void updateVideoFormat(VideoFormat videoFormat);

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(TextCues textCues);
}
