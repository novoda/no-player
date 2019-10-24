package com.novoda.demo;

import com.novoda.noplayer.PlayerView;

interface ControllerView {

    void setPaused();

    void setPlaying();

    void updateContentProgress(int progress);

    void updateBufferProgress(int buffer);

    void updateElapsedTime(String elapsedTime);

    void updateTimeRemaining(String timeRemaining);

    void setTogglePlayPauseAction(TogglePlayPauseAction togglePlayPauseAction);

    void setSeekAction(SeekAction seekAction);

    void setVolumeOn();

    void setVolumeOff();

    void setToggleVolumeOnOffAction(ToggleVolumeOnOffAction toggleVolumeOnOffAction);

    void announceResizeMode(PlayerView.ResizeMode resizeMode);

    void setToggleResizeModeAction(ToggleResizeModeAction zoomedInOutAction);

    interface TogglePlayPauseAction {

        void perform();
    }

    interface SeekAction {

        void perform(int progress, int max);
    }

    interface ToggleVolumeOnOffAction {

        void perform();
    }

    interface ToggleResizeModeAction {

        void perform();
    }
}
