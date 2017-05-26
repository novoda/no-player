package com.novoda.noplayer;

import com.novoda.noplayer.exoplayer.ExoForwarder;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoFacade;

public class Forwarder {

    private final ExoForwarder exoForwarder = new ExoForwarder();

    public void bind(ExoPlayerTwoFacade facade, PlayerListenersHolder playerListenersHolder, Player player) {
        exoForwarder.bind(playerListenersHolder.getPreparedListeners(), player);
        exoForwarder.bind(playerListenersHolder.getCompletionListeners());
        exoForwarder.bind(playerListenersHolder.getErrorListeners(), player);
        exoForwarder.bind(playerListenersHolder.getBufferStateListeners());
        exoForwarder.bind(playerListenersHolder.getVideoSizeChangedListeners());
        exoForwarder.bind(playerListenersHolder.getBitrateChangedListeners());
        exoForwarder.bind(playerListenersHolder.getInfoListeners());
        exoForwarder.bind(facade);
    }

    public void bind(MediaPlayerWrapper facade, PlayerListenersHolder playerListenersHolder, Player player) {
        //TODO bind here
    }

    public void resetPrepared() { //TODO see if we can avoid
        exoForwarder.resetPrepared();
    }
}
