package com.novoda.demo;

import android.net.Uri;
import android.util.Log;

import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;

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

    void startPresenting(Uri uri, Options options) {
        listeners.addPreparedListener(playOnPrepared);
        listeners.addStateChangedListener(updatePlayPause);
        listeners.addHeartbeatCallback(updateProgress);
        listeners.addDrmSecurityLevelCallback(new NoPlayer.DrmSecurityLevelCallback() {
            @Override
            public void onSecurityLevelDetermined(String securityLevel) {
                Log.e("SecurityLevel", securityLevel);
            }
        });

        controllerView.setTogglePlayPauseAction(onTogglePlayPause);
        controllerView.setSeekAction(onSeekPerformed);
        controllerView.setToggleVolumeOnOffAction(onToggleVolume);

        noPlayer.attach(playerView);
        noPlayer.loadVideo(uri, options);
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

    private final NoPlayer.HeartbeatCallback updateProgress = new NoPlayer.HeartbeatCallback() {
        @Override
        public void onBeat(NoPlayer player) {
            long positionInMillis = player.playheadPositionInMillis();
            long durationInMillis = player.mediaDurationInMillis();
            int bufferPercentage = player.bufferPercentage();

            updateProgress(positionInMillis, durationInMillis, bufferPercentage);
            updateTiming(positionInMillis, durationInMillis);
        }
    };

    private void updateProgress(long positionInMillis, long durationInMillis, int bufferPercentage) {
        int progressAsIncrements = ProgressCalculator.progressAsIncrements(positionInMillis, durationInMillis);
        int bufferAsIncrements = ProgressCalculator.bufferAsIncrements(bufferPercentage);

        controllerView.updateContentProgress(progressAsIncrements);
        controllerView.updateBufferProgress(bufferAsIncrements);
    }

    private void updateTiming(long positionInMillis, long durationInMillis) {
        long remainingDuration = durationInMillis - positionInMillis;

        controllerView.updateElapsedTime(TimeFormatter.asHoursMinutesSeconds(positionInMillis));
        controllerView.updateTimeRemaining(TimeFormatter.asHoursMinutesSeconds(remainingDuration));
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
            long seekToPosition = ProgressCalculator.seekToPosition(noPlayer.mediaDurationInMillis(), progress, max);
            noPlayer.seekTo(seekToPosition);
        }
    };

    private final ControllerView.ToggleVolumeOnOffAction onToggleVolume = new ControllerView.ToggleVolumeOnOffAction() {
        @Override
        public void perform() {
            boolean isSoundOn = Float.compare(noPlayer.getVolume(), 0f) > 0;
            if (isSoundOn) {
                noPlayer.setVolume(0f);
                controllerView.setVolumeOff();
            } else {
                noPlayer.setVolume(1f);
                controllerView.setVolumeOn();
            }
        }
    };

    void stopPresenting() {
        noPlayer.stop();
        noPlayer.release();
    }
}
