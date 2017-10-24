package com.novoda.demo.controller;

import com.novoda.noplayer.NoPlayer;

public final class ControllerFactory {

    public static ControllerPresenter createControllerPresenter(ControllerView controllerView, NoPlayer noPlayer) {
        return new ControllerPresenter(controllerView, noPlayer);
    }
}
