package com.novoda.noplayer.exoplayer;

public class ExoPlayerBinder {

    private final ExoPlayerTwoFacade facade;

    ExoPlayerBinder(ExoPlayerTwoFacade facade) {
        this.facade = facade;
    }

    public void bind(ExoPlayerForwarder exoPlayerForwarder) {
        exoPlayerForwarder.bind(facade);
    }

}
