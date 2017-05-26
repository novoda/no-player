package com.novoda.noplayer;

import com.novoda.noplayer.exoplayer.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.ExoPlayerBinder;

public class Forwarder {

    private final ExoPlayerForwarder exoPlayerForwarder = new ExoPlayerForwarder();

    public void bind(ExoPlayerBinder binder, PlayerListenersHolder playerListenersHolder, Player player) {
        exoPlayerForwarder.bind(playerListenersHolder.getPreparedListeners(), player);
        exoPlayerForwarder.bind(playerListenersHolder.getCompletionListeners());
        exoPlayerForwarder.bind(playerListenersHolder.getErrorListeners(), player);
        exoPlayerForwarder.bind(playerListenersHolder.getBufferStateListeners());
        exoPlayerForwarder.bind(playerListenersHolder.getVideoSizeChangedListeners());
        exoPlayerForwarder.bind(playerListenersHolder.getBitrateChangedListeners());
        exoPlayerForwarder.bind(playerListenersHolder.getInfoListeners());
        binder.bind(exoPlayerForwarder);
    }

    public void resetPrepared() { //TODO see if we can avoid
        exoPlayerForwarder.resetPrepared();
    }
}
