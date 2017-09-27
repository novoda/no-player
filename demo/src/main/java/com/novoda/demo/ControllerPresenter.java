package com.novoda.demo;

import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ControllerPresenter {

    private static final int MAX_PROGRESS_INCREMENTS = 1000;
    private static final int MAX_PROGRESS_PERCENT = 1000;

    private final ControllerView controllerView;

    public ControllerPresenter(ControllerView controllerView) {
        this.controllerView = controllerView;
    }

    public void update(VideoPosition position, VideoDuration duration, int bufferPercentage) {
        controllerView.setTogglePlayPauseAction(onTogglePlayPause);
        updateProgress(position, duration, bufferPercentage);
        updateTiming(position, duration);
    }

    private final ControllerView.TogglePlayPauseAction onTogglePlayPause = new ControllerView.TogglePlayPauseAction() {
        @Override
        public void perform() {
            // Check current state and perform toggle.
        }
    };

    private void updateProgress(VideoPosition position, VideoDuration duration, int bufferPercentage) {
        float proportionOfDuration = position.inMillis() / (float) duration.inMillis();
        int progress = (int) (MAX_PROGRESS_INCREMENTS * proportionOfDuration);

        int buffer = (bufferPercentage * MAX_PROGRESS_INCREMENTS) / MAX_PROGRESS_PERCENT;

        controllerView.updateProgress(progress, buffer);
    }

    private void updateTiming(VideoPosition position, VideoDuration duration) {
        VideoDuration elapsedDuration = VideoDuration.fromMillis(position.inMillis());
        VideoDuration remainingDuration = VideoDuration.fromMillis(duration.inMillis()).minus(elapsedDuration);

        controllerView.updateElapsedTime(stringFor(elapsedDuration.inImpreciseSeconds()));
        controllerView.updateTimeRemaining(stringFor(remainingDuration.inImpreciseSeconds()));
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
