package com.novoda.noplayer;

import android.view.View;

import com.novoda.notils.logger.simple.Log;

public class VideoContainer {

    private final View container;

    public static VideoContainer empty() {
        return new VideoContainer(null);
    }

    public static VideoContainer with(View container) {
        return new VideoContainer(container);
    }

    VideoContainer(View container) {
        this.container = container;
    }

    public void show() {
        if (isMissing()) {
            logContainerNotAttached("show()");
            return;
        }
        container.setVisibility(View.VISIBLE);
    }

    public void hide() {
        if (isMissing()) {
            logContainerNotAttached("hide()");
            return;
        }
        container.setVisibility(View.GONE);
    }

    private boolean isMissing() {
        return container == null;
    }

    private void logContainerNotAttached(String action) {
        Log.w(String.format("Attempt to %s the video container has been ignored because the Player has not been attached to a PlayerView", action));
    }
}
