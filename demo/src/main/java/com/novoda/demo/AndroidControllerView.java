package com.novoda.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.novoda.notils.exception.DeveloperError;

public class AndroidControllerView extends LinearLayout implements ControllerView {

    private ImageView playPauseButton;
    private TextView elapsedTimeView;
    private SeekBar progressView;
    private TextView timeRemainingView;

    public AndroidControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AndroidControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw new DeveloperError("This layout only supports horizontal orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_player_controls, this, true);

        playPauseButton = (ImageView) findViewById(R.id.player_controls_play_pause);
        elapsedTimeView = (TextView) findViewById(R.id.player_controls_elapsed_time);
        progressView = (SeekBar) findViewById(R.id.player_controls_progress);
        timeRemainingView = (TextView) findViewById(R.id.player_controls_time_remaining);
    }

    @Override
    public void setPaused() {
        playPauseButton.setImageResource(R.drawable.pause);
    }

    @Override
    public void setPlaying() {
        playPauseButton.setImageResource(R.drawable.play);
    }

    @Override
    public void updateProgress(int progress, int buffer) {
        progressView.setProgress(progress);
        progressView.setSecondaryProgress(buffer);
    }

    @Override
    public void updateElapsedTime(String elapsedTime) {
        elapsedTimeView.setText(elapsedTime);
    }

    @Override
    public void updateTimeRemaining(String timeRemaining) {
        timeRemainingView.setText(timeRemaining);
    }

    @Override
    public void setTogglePlayPauseAction(final TogglePlayPauseAction togglePlayPauseAction) {
        playPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPauseAction.perform();
            }
        });
    }
}
