package com.novoda.noplayer;

import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

class PlayerViewSurfaceProvider implements SurfaceHolder.Callback, SurfaceRequester {

    private final List<Callback> callbacks = new ArrayList<>();
    private Surface surface;

    private boolean isSurfaceHolderReady() {
        return surface != null;
    }

    @Override
    public void requestSurface(Callback callback) {
        if (isSurfaceHolderReady()) {
            callback.onSurfaceReady(surface);
        } else {
            callbacks.add(callback);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surface = surfaceHolder.getSurface();
        notifyListeners(surface);
        callbacks.clear();
    }

    private void notifyListeners(Surface surface) {
        for (Callback callback : callbacks) {
            callback.onSurfaceReady(surface);
        }
    }

    private void setSurfaceHolderNotReady() {
        if (surface != null) {
            surface.release();
            surface = null;
        }
    }

    @Override
    public void removeCallback(Callback callback) {
        callbacks.remove(callback);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        setSurfaceHolderNotReady();
        callbacks.clear();
    }

}
