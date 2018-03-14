package com.novoda.noplayer;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class PlayerViewSurfaceTextureProvider implements TextureView.SurfaceTextureListener, SurfaceTextureRequester {

    private final List<Callback> callbacks = new ArrayList<>();
    private SurfaceTexture surfaceTexture;

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surfaceTexture = surface;
        notifyListeners(surface);
        callbacks.clear();
    }

    private void notifyListeners(SurfaceTexture surfaceTexture) {
        for (Callback callback : callbacks) {
            callback.onSurfaceTextureReady(surfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Do nothing.
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        setSurfaceHolderNotReady();
        callbacks.clear();
        return true;
    }

    private static final int NUMBER_OF_FRAMES_TO_CAPTURE = 100;
    private static final double ONE_SECOND_IN_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private final LinkedList<Long> frameTimes = new LinkedList<Long>() {{
        add(System.currentTimeMillis());
    }};

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.e("TAG", "FPS: " + calculateFramesPerSecond());
    }

    private double calculateFramesPerSecond() {
        long currrentTimeInMillis = System.currentTimeMillis();
        Long startTimeInMillis = frameTimes.getFirst();
        double numberOfSecondsElapsed = (currrentTimeInMillis - startTimeInMillis) / ONE_SECOND_IN_MILLIS;

        frameTimes.addLast(currrentTimeInMillis);
        int size = frameTimes.size();
        if (size > NUMBER_OF_FRAMES_TO_CAPTURE) {
            frameTimes.removeFirst();
        }

        return frameTimes.size() / numberOfSecondsElapsed;
    }

    private void setSurfaceHolderNotReady() {
        surfaceTexture = null;
    }

    @Override
    public void requestSurfaceTexture(Callback callback) {
        if (isSurfaceHolderReady()) {
            callback.onSurfaceTextureReady(surfaceTexture);
        } else {
            callbacks.add(callback);
        }
    }

    private boolean isSurfaceHolderReady() {
        return surfaceTexture != null;
    }

    @Override
    public void removeCallback(Callback callback) {
        callbacks.remove(callback);
    }

}
