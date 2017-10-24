package com.novoda.demo;

import android.net.Uri;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

class DemoPresenter {

    private static final int MAX_PROGRESS_INCREMENTS = 100;
    private static final int MAX_PROGRESS_PERCENT = 100;

    private final ControllerView controllerView;
    private final NoPlayer noPlayer;
    private final Listeners listeners;
    private final PlayerView playerView;

    DemoPresenter(ControllerView controllerView, NoPlayer noPlayer, Listeners listeners, PlayerView playerView) {
        this.controllerView = controllerView;
        this.noPlayer = noPlayer;
        this.listeners = listeners;
        this.playerView = playerView;
    }

    void startPresenting(Uri uri, ContentType contentType) {
        listeners.addPreparedListener(playOnPrepared());
        listeners.addStateChangedListener(notifyControllerView());
        listeners.addHeartbeatCallback(updateControllerView());

        controllerView.setTogglePlayPauseAction(onTogglePlayPause);
        controllerView.setSeekAction(onSeekPerformed);

        noPlayer.attach(playerView);
        noPlayer.loadVideo(uri, contentType);
    }

    private NoPlayer.PreparedListener playOnPrepared() {
        return new NoPlayer.PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                noPlayer.play();
            }
        };
    }

    private NoPlayer.StateChangedListener notifyControllerView() {
        return new NoPlayer.StateChangedListener() {
            @Override
            public void onVideoPlaying() {
                controllerView.setPlaying();
            }

            @Override
            public void onVideoPaused() {
                controllerView.setPaused();
            }

            @Override
            public void onVideoStopped() {

            }
        };
    }

    private NoPlayer.HeartbeatCallback updateControllerView() {
        return new NoPlayer.HeartbeatCallback() {
            @Override
            public void onBeat(NoPlayer player) {
                VideoPosition position = player.getPlayheadPosition();
                VideoDuration duration = player.getMediaDuration();
                int bufferPercentage = player.getBufferPercentage();

                updateProgress(position, duration, bufferPercentage);
                updateTiming(position, duration);
            }
        };
    }

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

    private final ControllerView.SeekAction onSeekPerformed = new ControllerView.SeekAction() {
        @Override
        public void perform(int progress, int max) {
            VideoPosition seekToPosition = calculateSeekToPosition(progress, max);
            noPlayer.seekTo(seekToPosition);
        }
    };

    private VideoPosition calculateSeekToPosition(int progress, int max) {
        VideoDuration duration = noPlayer.getMediaDuration();
        float progressMultiplier = (float) progress / max;
        return duration.positionAtPercentage(progressMultiplier);
    }

    void stopPresenting() {
        noPlayer.stop();
        noPlayer.release();
    }
}
