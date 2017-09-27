package com.novoda.demo;

import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

public class ControllerPresenter {

    private static final int MAX_PROGRESS_INCREMENTS = 1000;
    private static final int MAX_PROGRESS_PERCENT = 100;

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
        double percentageOfDuration = position.asPercentageOf(duration);
        int progressAsIncrements = (int) (MAX_PROGRESS_INCREMENTS * percentageOfDuration);

        int bufferAsIncrements = (bufferPercentage * MAX_PROGRESS_INCREMENTS) / MAX_PROGRESS_PERCENT;

        controllerView.updateContentProgress(progressAsIncrements);
        controllerView.updateBufferProgress(bufferAsIncrements);
    }

    private void updateTiming(VideoPosition position, VideoDuration duration) {
        VideoDuration elapsedDuration = VideoDuration.fromMillis(position.inMillis());
        VideoDuration remainingDuration = VideoDuration.fromMillis(duration.inMillis()).minus(elapsedDuration);

        controllerView.updateElapsedTime(TimeFormatter.asHoursMinutesSeconds(elapsedDuration.inImpreciseSeconds()));
        controllerView.updateTimeRemaining(TimeFormatter.asHoursMinutesSeconds(remainingDuration.inImpreciseSeconds()));
    }
}
