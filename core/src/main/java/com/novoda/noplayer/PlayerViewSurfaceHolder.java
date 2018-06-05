package com.novoda.noplayer;

import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.List;

class PlayerViewSurfaceHolder implements SurfaceHolder.Callback, TextureView.SurfaceTextureListener, SurfaceRequester {

    private final List<Callback> callbacks = new ArrayList<>();
    @Nullable
    private Surface surface;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surface = surfaceHolder.getSurface();
        notifyListeners(surface);
        callbacks.clear();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        setSurfaceNotReady();
        callbacks.clear();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        this.surface = new Surface(surfaceTexture);
        notifyListeners(surface);
        callbacks.clear();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // do nothing
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        setSurfaceNotReady();
        surface.release();
        callbacks.clear();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // do nothing
    }

    private void notifyListeners(Surface surface) {
        for (Callback callback : callbacks) {
            callback.onSurfaceReady(surface);
        }
    }

    private void setSurfaceNotReady() {
        surface = null;
    }

    @Override
    public void requestSurface(Callback callback) {
        if (isSurfaceHolderReady()) {
            callback.onSurfaceReady(surface);
        } else {
            callbacks.add(callback);
        }
    }

    private boolean isSurfaceHolderReady() {
        return surface != null;
    }

    @Override
    public void removeCallback(Callback callback) {
        callbacks.remove(callback);
    }
}
