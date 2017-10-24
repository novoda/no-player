package com.novoda.demo;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

class ControllerPresenter {

    private static final int MAX_PROGRESS_INCREMENTS = 1000;
    private static final int MAX_PROGRESS_PERCENT = 100;

    private final ControllerView controllerView;
    private final NoPlayer noPlayer;

    ControllerPresenter(ControllerView controllerView, NoPlayer noPlayer) {
        this.controllerView = controllerView;
        this.noPlayer = noPlayer;
    }

    void onVideoPlaying() {
        controllerView.setPlaying();
    }

    void onVideoPaused() {
        controllerView.setPaused();
    }

    void update(VideoPosition position, VideoDuration duration, int bufferPercentage) {
        updateProgress(position, duration, bufferPercentage);
        updateTiming(position, duration);
        controllerView.setTogglePlayPauseAction(onTogglePlayPause);
    }

    private final ControllerView.TogglePlayPauseAction onTogglePlayPause = new ControllerView.TogglePlayPauseAction() {
        @Override
        public void perform() {
            if (noPlayer.isPlaying()) {
                noPlayer.pause();
            } else {
                noPlayer.play();
            }
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
