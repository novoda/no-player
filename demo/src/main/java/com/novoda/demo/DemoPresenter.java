package com.novoda.demo;

import android.net.Uri;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.model.VideoDuration;

class DemoPresenter {

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
        listeners.addPreparedListener(playOnPrepared);
        listeners.addStateChangedListener(updatePlayPause);
        listeners.addHeartbeatCallback(updateProgress);

        controllerView.setTogglePlayPauseAction(onTogglePlayPause);
        controllerView.setSeekAction(onSeekPerformed);

        noPlayer.attach(playerView);
        noPlayer.loadVideo(uri, contentType);
    }

    private final NoPlayer.PreparedListener playOnPrepared = new NoPlayer.PreparedListener() {
        @Override
        public void onPrepared(PlayerState playerState) {
            noPlayer.play();
        }
    };

    private final NoPlayer.StateChangedListener updatePlayPause = new NoPlayer.StateChangedListener() {
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
            // Not required.
        }
    };

    private NoPlayer.HeartbeatCallback updateProgress = new NoPlayer.HeartbeatCallback() {
        @Override
        public void onBeat(NoPlayer player) {
            long positionInMillis = player.playheadPositionInMillis();
            VideoDuration duration = player.mediaDuration();
            int bufferPercentage = player.bufferPercentage();

            updateProgress(positionInMillis, duration, bufferPercentage);
            updateTiming(positionInMillis, duration);
        }
    };

    private void updateProgress(long positionInMillis, VideoDuration duration, int bufferPercentage) {
        int progressAsIncrements = ProgressCalculator.progressAsIncrements(positionInMillis, duration);
        int bufferAsIncrements = ProgressCalculator.bufferAsIncrements(bufferPercentage);

        controllerView.updateContentProgress(progressAsIncrements);
        controllerView.updateBufferProgress(bufferAsIncrements);
    }

    private void updateTiming(long positionInMillis, VideoDuration duration) {
        VideoDuration elapsedDuration = VideoDuration.fromMillis(positionInMillis);
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
            long seekToPosition = ProgressCalculator.seekToPosition(noPlayer.mediaDuration(), progress, max);
            noPlayer.seekTo(seekToPosition);
        }
    };

    void stopPresenting() {
        noPlayer.stop();
        noPlayer.release();
    }
}
