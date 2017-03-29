package com.novoda.noplayer;

import android.view.View;

public interface PlayerView {

    View getContainerView();

    SurfaceHolderRequester getSurfaceHolderRequester();

    Player.VideoSizeChangedListener getVideoSizeChangedListener();
}
