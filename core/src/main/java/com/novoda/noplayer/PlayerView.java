package com.novoda.noplayer;

import android.view.View;

import com.google.android.exoplayer.text.SubtitleLayout;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    SubtitleLayout getSubtitleLayout();

    void showSubtitles();

    void hideSubtitles();
}
