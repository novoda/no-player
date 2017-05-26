package com.novoda.noplayer.exoplayer;

public class ExoPlayerBinder {

    private final ExoPlayerTwoFacade facade;

    ExoPlayerBinder(ExoPlayerTwoFacade facade) {
        this.facade = facade;
    }

    public void bind(ExoForwarder exoForwarder) {
        exoForwarder.bind(facade);
    }

}
