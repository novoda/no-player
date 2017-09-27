package com.novoda.demo;

interface ControllerView {

    void setPaused();

    void setPlaying();

    void updateProgress(int progress, int buffer);

    void updateElapsedTime(String elapsedTime);

    void updateTimeRemaining(String timeRemaining);
}
