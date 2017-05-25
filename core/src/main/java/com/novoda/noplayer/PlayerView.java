package com.novoda.noplayer;

import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    Player.VideoSizeChangedListener getVideoSizeChangedListener();

    Player.StateChangedListener getStateChangedListener();

    SimpleExoPlayerView simplePlayerView();

    void showSubtitles();

    void hideSubtitles();

    void removeControls();

}
