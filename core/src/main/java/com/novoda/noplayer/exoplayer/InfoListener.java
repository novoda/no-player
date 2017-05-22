package com.novoda.noplayer.exoplayer;

/**
 * TODO: Figure out which ExoPlayer.EventListener events we want to listen for.
 * A listener for debugging information.
 */
public interface InfoListener {

    void onDroppedFrames(int count, long elapsed);

}
