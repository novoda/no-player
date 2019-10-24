package com.novoda.demo;

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

    void setZoomedIn();

    void setZoomedOut();

    void setToggleZoomedInOutAction(ToggleZoomedInOutAction zoomedInOutAction);

    interface TogglePlayPauseAction {

        void perform();
    }

    interface SeekAction {

        void perform(int progress, int max);
    }

    interface ToggleVolumeOnOffAction {

        void perform();
    }

    interface ToggleZoomedInOutAction {

        void perform();
    }
}
