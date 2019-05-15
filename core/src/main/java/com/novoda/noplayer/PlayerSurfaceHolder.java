package com.novoda.noplayer;

import android.view.SurfaceView;
import android.view.TextureView;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;

public class PlayerSurfaceHolder {

    @Nullable
    private final SurfaceView surfaceView;
    @Nullable
    private final TextureView textureView;
    private final PlayerViewSurfaceHolder surfaceHolder;

    public static PlayerSurfaceHolder create(SurfaceView surfaceView) {
        PlayerViewSurfaceHolder surfaceHolder = new PlayerViewSurfaceHolder();
        surfaceView.getHolder().addCallback(surfaceHolder);
        return new PlayerSurfaceHolder(surfaceView, null, surfaceHolder);
    }

    public static PlayerSurfaceHolder create(TextureView textureView) {
        PlayerViewSurfaceHolder surfaceHolder = new PlayerViewSurfaceHolder();
        textureView.setSurfaceTextureListener(surfaceHolder);
        return new PlayerSurfaceHolder(null, textureView, surfaceHolder);
    }

    PlayerSurfaceHolder(@Nullable SurfaceView surfaceView, @Nullable TextureView textureView, PlayerViewSurfaceHolder surfaceHolder) {
        this.surfaceView = surfaceView;
        this.textureView = textureView;
        this.surfaceHolder = surfaceHolder;
    }

    public SurfaceRequester getSurfaceRequester() {
        return surfaceHolder;
    }

    public void attach(Player.VideoComponent videoPlayer) {
        if (containsSurfaceView()) {
            videoPlayer.setVideoSurfaceView(surfaceView);
        } else if (containsTextureView()) {
            videoPlayer.setVideoTextureView(textureView);
        } else {
            throw new IllegalArgumentException("Surface container does not contain any of the expected views");
        }
    }

    private boolean containsSurfaceView() {
        return surfaceView != null;
    }

    private boolean containsTextureView() {
        return textureView != null;
    }
}
