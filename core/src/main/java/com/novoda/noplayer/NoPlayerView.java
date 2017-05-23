package com.novoda.noplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.novoda.noplayer.exoplayer.AspectRatioChangeListener;
import com.novoda.notils.caster.Views;

public class NoPlayerView extends FrameLayout implements AspectRatioChangeListener.Listener, PlayerView {

    private final PlayerViewSurfaceHolder surfaceHolderProvider;
    private final AspectRatioChangeListener aspectRatioChangeListener;

    private AspectRatioFrameLayout videoFrame;
    private SimpleExoPlayerView playerView;
    private SurfaceView surfaceView;

    public NoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolderProvider = new PlayerViewSurfaceHolder();
        aspectRatioChangeListener = new AspectRatioChangeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.noplayer_view, this);
        videoFrame = Views.findById(this, R.id.video_frame);
        playerView = Views.findById(this, R.id.simple_player_view);
        surfaceView = (SurfaceView) playerView.getVideoSurfaceView();
        surfaceView.getHolder().addCallback(surfaceHolderProvider);
    }

    @Override
    public void onNewAspectRatio(float aspectRatio) {
        videoFrame.setAspectRatio(aspectRatio);
    }

    @Override
    public View getContainerView() {
        return surfaceView;
    }

    @Override
    public SurfaceHolderRequester getSurfaceHolderRequester() {
        return surfaceHolderProvider;
    }

    @Override
    public Player.VideoSizeChangedListener getVideoSizeChangedListener() {
        return videoSizeChangedListener;
    }

    @Override
    public SimpleExoPlayerView simplePlayerView() {
        return playerView;
    }

    @Override
    public void showSubtitles() {
        playerView.getSubtitleView().setVisibility(VISIBLE);
    }

    @Override
    public void hideSubtitles() {
        playerView.getSubtitleView().setVisibility(GONE);
    }

    @Override
    public void removeControls() {
        playerView.setUseController(false);
    }

    private final Player.VideoSizeChangedListener videoSizeChangedListener = new Player.VideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            aspectRatioChangeListener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    };
}
