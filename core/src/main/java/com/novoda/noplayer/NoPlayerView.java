package com.novoda.noplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.novoda.noplayer.exoplayer.AspectRatioChangeForwarder;
import com.novoda.notils.caster.Views;

public class NoPlayerView extends FrameLayout implements AspectRatioChangeForwarder.Listener, PlayerView {

    private final PlayerViewSurfaceHolder surfaceHolderProvider;
    private final AspectRatioChangeForwarder aspectRatioChangeForwarder;

    private AspectRatioFrameLayout videoFrame;
    private SimpleExoPlayerView playerView;
    private SurfaceView surfaceView;
    private SubtitleView subtitleView;
    private View shutterView;

    public NoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolderProvider = new PlayerViewSurfaceHolder();
        aspectRatioChangeForwarder = new AspectRatioChangeForwarder(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.noplayer_view, this);
        videoFrame = Views.findById(this, R.id.exo_content_frame);
        playerView = Views.findById(this, R.id.simple_player_view);
        shutterView = Views.findById(this, R.id.exo_shutter);
        surfaceView = (SurfaceView) playerView.getVideoSurfaceView();
        surfaceView.getHolder().addCallback(surfaceHolderProvider);
        subtitleView = playerView.getSubtitleView();
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
    public Player.StateChangedListener getStateChangedListener() {
        return stateChangedListener;
    }

    @Override
    public SimpleExoPlayerView simplePlayerView() {
        return playerView;
    }

    @Override
    public void showSubtitles() {
        subtitleView.setVisibility(VISIBLE);
    }

    @Override
    public void hideSubtitles() {
        subtitleView.setVisibility(GONE);
    }

    @Override
    public void removeControls() {
        playerView.setUseController(false);
    }

    private final Player.VideoSizeChangedListener videoSizeChangedListener = new Player.VideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            aspectRatioChangeForwarder.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    };

    private final Player.StateChangedListener stateChangedListener = new Player.StateChangedListener() {
        @Override
        public void onVideoPlaying() {
            shutterView.setVisibility(INVISIBLE);
        }

        @Override
        public void onVideoPaused() {
            //We don't care
        }

        @Override
        public void onVideoReleased() {
            shutterView.setVisibility(VISIBLE);
        }
    };
}
