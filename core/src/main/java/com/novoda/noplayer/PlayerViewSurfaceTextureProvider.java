package com.novoda.noplayer;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Do nothing.
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
