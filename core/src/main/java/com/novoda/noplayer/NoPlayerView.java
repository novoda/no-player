package com.novoda.noplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.text.SubtitleLayout;
import com.novoda.noplayer.exoplayer.AspectRatioChangeListener;
import com.novoda.notils.caster.Views;

public class NoPlayerView extends FrameLayout implements AspectRatioChangeListener.Listener, PlayerView {

    private final PlayerViewSurfaceHolder surfaceHolderProvider;
    private final AspectRatioChangeListener aspectRatioChangeListener;

    private AspectRatioFrameLayout videoFrame;
    private SurfaceView surfaceView;
    private SubtitleLayout subtitleLayout;

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
        surfaceView = Views.findById(this, R.id.surface_view);
        subtitleLayout = Views.findById(this, R.id.subtitles_layout);

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
    public SubtitleLayout getSubtitleLayout() {
        return subtitleLayout;
    }

    @Override
    public void showSubtitles() {
        subtitleLayout.setVisibility(VISIBLE);
    }

    @Override
    public void hideSubtitles() {
        subtitleLayout.setVisibility(INVISIBLE);
    }

    @Override
    public Player.VideoSizeChangedListener getVideoSizeChangedListener() {
        return videoSizeChangedListener;
    }

    private final Player.VideoSizeChangedListener videoSizeChangedListener = new Player.VideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            aspectRatioChangeListener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    };
}
