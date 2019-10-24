package com.novoda.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AndroidControllerView extends LinearLayout implements ControllerView {

    private ImageView playPauseButton;
    private TextView elapsedTimeView;
    private SeekBar progressView;
    private TextView timeRemainingView;
    private ImageView volumeOnOffView;
    private ImageView zoomedInOutView;

    public AndroidControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AndroidControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw new IllegalAccessError("This layout only supports horizontal orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_player_controls, this, true);

        playPauseButton = (ImageView) findViewById(R.id.player_controls_play_pause);
        elapsedTimeView = (TextView) findViewById(R.id.player_controls_elapsed_time);
        progressView = (SeekBar) findViewById(R.id.player_controls_progress);
        timeRemainingView = (TextView) findViewById(R.id.player_controls_time_remaining);
        volumeOnOffView = findViewById(R.id.player_controls_volume_on_off);
        zoomedInOutView = findViewById(R.id.player_controls_zoom);
    }

    @Override
    public void setPaused() {
        playPauseButton.setImageResource(R.drawable.play);
    }

    @Override
    public void setPlaying() {
        playPauseButton.setImageResource(R.drawable.pause);
    }

    @Override
    public void updateContentProgress(int progress) {
        progressView.setProgress(progress);
    }

    @Override
    public void updateBufferProgress(int buffer) {
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

    @Override
    public void setSeekAction(final SeekAction seekAction) {
        progressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Not required.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not required.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int max = seekBar.getMax();
                seekAction.perform(progress, max);
            }
        });
    }

    @Override
    public void setVolumeOn() {
        volumeOnOffView.setImageResource(R.drawable.volume_on);
    }

    @Override
    public void setVolumeOff() {
        volumeOnOffView.setImageResource(R.drawable.volume_off);
    }

    @Override
    public void setToggleVolumeOnOffAction(final ToggleVolumeOnOffAction toggleVolumeOnOffAction) {
        volumeOnOffView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVolumeOnOffAction.perform();
            }
        });
    }

    @Override
    public void setZoomedIn() {
        zoomedInOutView.setImageResource(R.drawable.ic_fullscreen_on);
    }

    @Override
    public void setZoomedOut() {
        zoomedInOutView.setImageResource(R.drawable.ic_fullscreen_off);
    }

    @Override
    public void setToggleZoomedInOutAction(final ToggleZoomedInOutAction toggleZoomedInOutAction) {
        zoomedInOutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleZoomedInOutAction.perform();
            }
        });
    }
}
