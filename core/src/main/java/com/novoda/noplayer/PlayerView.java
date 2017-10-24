package com.novoda.noplayer;

import android.view.View;

import com.novoda.noplayer.model.TextCues;
import com.novoda.noplayer.model.PlayerVideoTrack;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    NoPlayer.VideoSizeChangedListener getVideoSizeChangedListener();

    NoPlayer.StateChangedListener getStateChangedListener();

    void updateVideoFormat(PlayerVideoTrack videoFormat);

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(TextCues textCues);
}
