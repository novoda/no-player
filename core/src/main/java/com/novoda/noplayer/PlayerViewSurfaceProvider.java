package com.novoda.noplayer;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.List;

class PlayerViewSurfaceProvider implements TextureView.SurfaceTextureListener, SurfaceHolder.Callback, SurfaceRequester {

    private final List<Callback> callbacks = new ArrayList<>();
    private final List<FPSCallback> fpsCallbacks = new ArrayList<>();
    private Surface surface;

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        this.surface = surface;
        notifyListeners(surface);
        callbacks.clear();
    }

    private void notifyListeners(Surface surface) {
        for (Callback callback : callbacks) {
            callback.onSurfaceReady(surface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        // Do nothing.
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        setSurfaceHolderNotReady();
        callbacks.clear();
        return true;
    }

    private final FramesPerSecondCalculator framesPerSecondCalculator = new FramesPerSecondCalculator();

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        double fps = framesPerSecondCalculator.calculate();
        for (FPSCallback callback : fpsCallbacks) {
            callback.onFpsUpdated(fps);
        }
    }

    private void setSurfaceHolderNotReady() {
        surface.release();
        surface = null;
    }

    @Override
    public void requestSurfaceTexture(Callback callback) {
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

    public void addFpsCallback(FPSCallback fpsCallback) {
        fpsCallbacks.add(fpsCallback);
    }

    public void removeFpsCallback(FPSCallback fpsCallback) {
        fpsCallbacks.remove(fpsCallback);
    }

    interface FPSCallback {
        void onFpsUpdated(double fps);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        notifyListeners(surfaceHolder.getSurface());
        callbacks.clear();
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
