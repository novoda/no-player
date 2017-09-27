package com.novoda.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;
import com.novoda.notils.exception.DeveloperError;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AndroidControllerView extends LinearLayout {

    private static final int MAX_PROGRESS_INCREMENTS = 1000;
    private static final int MAX_PROGRESS_PERCENT = 1000;

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

    public void setPaused() {
        playPauseButton.setImageResource(R.drawable.pause);
    }

    public void setPlaying() {
        playPauseButton.setImageResource(R.drawable.play);
    }

    public void updateProgress(VideoPosition position, VideoDuration duration, int bufferPercentage) {
        float proportionOfDuration = position.inMillis() / (float) duration.inMillis();
        long progress = (long) (MAX_PROGRESS_INCREMENTS * proportionOfDuration);

        int buffer = (bufferPercentage * MAX_PROGRESS_INCREMENTS) / MAX_PROGRESS_PERCENT;

        progressView.setProgress((int) progress);
        progressView.setSecondaryProgress(buffer);
    }

    public void updateTiming(VideoPosition position, VideoDuration duration) {
        VideoDuration elapsedDuration = VideoDuration.fromMillis(position.inMillis());
        VideoDuration remainingDuration = VideoDuration.fromMillis(duration.inMillis()).minus(elapsedDuration);

        elapsedTimeView.setText(stringFor(elapsedDuration.inImpreciseSeconds()));
        timeRemainingView.setText(stringFor(remainingDuration.inImpreciseSeconds()));
    }

    private String stringFor(int timeInSeconds) {
        int hours = (int) TimeUnit.SECONDS.toHours(timeInSeconds);
        int minutes = (int) TimeUnit.SECONDS.toMinutes(timeInSeconds - TimeUnit.HOURS.toSeconds(hours));
        int seconds = (int) (timeInSeconds - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes));

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }
}
