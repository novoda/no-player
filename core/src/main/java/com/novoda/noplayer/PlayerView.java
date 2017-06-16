package com.novoda.noplayer;

import android.view.View;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    Player.VideoSizeChangedListener getVideoSizeChangedListener();

    Player.StateChangedListener getStateChangedListener();

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(List<Cue> cues);
}
