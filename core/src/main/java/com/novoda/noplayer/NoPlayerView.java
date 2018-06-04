package com.novoda.noplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.novoda.noplayer.model.TextCues;

public class NoPlayerView extends FrameLayout implements AspectRatioChangeCalculator.Listener, PlayerView {

    private final AspectRatioChangeCalculator aspectRatioChangeCalculator;

    private AspectRatioFrameLayout videoFrame;
    private SurfaceView surfaceView;
    private SubtitleView subtitleView;
    private View shutterView;
    private PlayerSurfaceHolder playerSurfaceHolder;

    public NoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        aspectRatioChangeCalculator = new AspectRatioChangeCalculator(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.noplayer_view, this);
        videoFrame = findViewById(R.id.video_frame);
        shutterView = findViewById(R.id.shutter);
        surfaceView = findViewById(R.id.surface_view);
        subtitleView = findViewById(R.id.subtitles_layout);
        playerSurfaceHolder = PlayerSurfaceHolder.create(surfaceView);
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
    public PlayerSurfaceHolder getPlayerSurfaceHolder() {
        return playerSurfaceHolder;
    }

    @Override
    public NoPlayer.VideoSizeChangedListener getVideoSizeChangedListener() {
        return videoSizeChangedListener;
    }

    @Override
    public NoPlayer.StateChangedListener getStateChangedListener() {
        return stateChangedListener;
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
    public void setSubtitleCue(TextCues textCues) {
        subtitleView.setCues(textCues);
    }

    private final NoPlayer.VideoSizeChangedListener videoSizeChangedListener = new NoPlayer.VideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            aspectRatioChangeCalculator.onVideoSizeChanged(width, height, pixelWidthHeightRatio);
        }
    };

    private final NoPlayer.StateChangedListener stateChangedListener = new NoPlayer.StateChangedListener() {
        @Override
        public void onVideoPlaying() {
            shutterView.setVisibility(INVISIBLE);
        }

        @Override
        public void onVideoPaused() {
            // We don't care
        }

        @Override
        public void onVideoStopped() {
            shutterView.setVisibility(VISIBLE);
        }
    };
}
