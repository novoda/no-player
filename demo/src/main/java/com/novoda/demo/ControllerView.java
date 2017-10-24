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

    interface TogglePlayPauseAction {

        void perform();
    }

    interface SeekAction {

        void perform(int progress, int max);
    }
}
