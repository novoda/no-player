package com.novoda.noplayer;

import android.view.SurfaceView;
import android.view.View;
import com.novoda.noplayer.model.TextCues;

public interface PlayerView {

    View getContainerView();

    SurfaceContainer getSurfaceContainer();

    NoPlayer.VideoSizeChangedListener getVideoSizeChangedListener();

    NoPlayer.StateChangedListener getStateChangedListener();

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(TextCues textCues);

    class SurfaceContainer {

        private final SurfaceView surfaceView;
        private final PlayerViewSurfaceHolder playerViewSurfaceHolder;

        public SurfaceContainer(SurfaceView surfaceView) {
            this.surfaceView = surfaceView;
            playerViewSurfaceHolder = new PlayerViewSurfaceHolder();
            surfaceView.getHolder().addCallback(playerViewSurfaceHolder);
        }

        public SurfaceView getSurfaceView() {
            return surfaceView;
        }

        public SurfaceHolderRequester getSurfaceHolderRequester() {
            return playerViewSurfaceHolder;
        }
    }
}
