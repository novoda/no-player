package com.novoda.noplayer;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import androidx.annotation.Nullable;

import com.novoda.noplayer.model.Either;

import java.util.ArrayList;
import java.util.List;

class PlayerViewSurfaceHolder implements SurfaceHolder.Callback, TextureView.SurfaceTextureListener, SurfaceRequester {

    private final List<Callback> callbacks = new ArrayList<>();
    @Nullable
    private Either<Surface, SurfaceHolder> eitherSurface;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.eitherSurface = Either.right(surfaceHolder);
        notifyListeners(eitherSurface);
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
        this.eitherSurface = Either.left(new Surface(surfaceTexture));
        notifyListeners(eitherSurface);
        callbacks.clear();
    }

    private void notifyListeners(Either<Surface, SurfaceHolder> either) {
        for (Callback callback : callbacks) {
            callback.onSurfaceReady(either);
        }
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

    private void setSurfaceNotReady() {
        eitherSurface = null;
    }

    @Override
    public void requestSurface(Callback callback) {
        if (isSurfaceReady()) {
            callback.onSurfaceReady(eitherSurface);
        } else {
            callbacks.add(callback);
        }
    }

    private boolean isSurfaceReady() {
        return eitherSurface != null;
    }

    @Override
    public void removeCallback(Callback callback) {
        callbacks.remove(callback);
    }
}
