package com.novoda.noplayer;

import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.TextureView;

public class SurfaceContainer {

    @Nullable
    private final SurfaceView surfaceView;
    @Nullable
    private final TextureView textureView;
    private final PlayerViewSurfaceHolder surfaceHolder;

    public static SurfaceContainer create(SurfaceView surfaceView) {
        PlayerViewSurfaceHolder surfaceHolder = new PlayerViewSurfaceHolder();
        surfaceView.getHolder().addCallback(surfaceHolder);
        return new SurfaceContainer(surfaceView, null, surfaceHolder);
    }

    public static SurfaceContainer create(TextureView textureView) {
        PlayerViewSurfaceHolder surfaceHolder = new PlayerViewSurfaceHolder();
        textureView.setSurfaceTextureListener(surfaceHolder);
        return new SurfaceContainer(null, textureView, new PlayerViewSurfaceHolder());
    }

    SurfaceContainer(@Nullable SurfaceView surfaceView, @Nullable TextureView textureView, PlayerViewSurfaceHolder surfaceHolder) {
        this.surfaceView = surfaceView;
        this.textureView = textureView;
        this.surfaceHolder = surfaceHolder;
    }

    public boolean containsSurfaceView() {
        return surfaceView != null;
    }

    public SurfaceView getSurfaceView() {
        if (surfaceView == null) {
            throw new IllegalStateException("No SurfaceView available");
        }
        return surfaceView;
    }

    public boolean containsTextureView() {
        return textureView != null;
    }

    public TextureView getTextureView() {
        if (textureView == null) {
            throw new IllegalStateException("No TextureView available");
        }
        return textureView;
    }

    public SurfaceRequester getSurfaceRequester() {
        return surfaceHolder;
    }
}
