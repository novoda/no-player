package com.novoda.demo.controller;

public interface ControllerView {

    void setPaused();

    void setPlaying();

    void updateContentProgress(int progress);

    void updateBufferProgress(int buffer);

    void updateElapsedTime(String elapsedTime);

    void updateTimeRemaining(String timeRemaining);

    void setTogglePlayPauseAction(TogglePlayPauseAction togglePlayPauseAction);

    interface TogglePlayPauseAction {

        void perform();
    }
}
