package com.novoda.noplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.novoda.noplayer.model.TextCues;

public class NoPlayerView extends FrameLayout implements AspectRatioChangeCalculator.Listener, PlayerView {

    private final PlayerViewSurfaceProvider surfaceHolderProvider;
    private final AspectRatioChangeCalculator aspectRatioChangeCalculator;

    private AspectRatioFrameLayout videoFrame;
    private SurfaceView textureView;
    private SubtitleView subtitleView;
    private View shutterView;
    private TextView fpsView;

    public NoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolderProvider = new PlayerViewSurfaceProvider();
        surfaceHolderProvider.addFpsCallback(fpsUpdatedListener);
        aspectRatioChangeCalculator = new AspectRatioChangeCalculator(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.noplayer_view, this);
        videoFrame = findViewById(R.id.video_frame);
        shutterView = findViewById(R.id.shutter);
        textureView = findViewById(R.id.texture_view);
        textureView.getHolder().addCallback(surfaceHolderProvider);
        subtitleView = findViewById(R.id.subtitles_layout);
        fpsView = findViewById(R.id.fps);
    }

    @Override
    public void onNewAspectRatio(float aspectRatio) {
        videoFrame.setAspectRatio(aspectRatio);
    }

    @Override
    public View getContainerView() {
        return textureView;
    }

    @Override
    public SurfaceRequester getSurfaceTextureRequester() {
        return surfaceHolderProvider;
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

    private final PlayerViewSurfaceProvider.FPSCallback fpsUpdatedListener = new PlayerViewSurfaceProvider.FPSCallback() {
        @Override
        public void onFpsUpdated(double fps) {
            String message = String.format("~ fps: %s", fps);
            fpsView.setText(message);
        }
    };
}
