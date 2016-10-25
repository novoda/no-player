package com.novoda.noplayer.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrioritisedPlayers implements Iterable<PlayerType> {

    private final List<PlayerType> playerPriorities;

    public static PrioritisedPlayers prioritiseExoPlayer() {
        List<PlayerType> prioritisedPlayers = new ArrayList<>();
        prioritisedPlayers.add(PlayerType.EXO_PLAYER);
        prioritisedPlayers.add(PlayerType.MEDIA_PLAYER);
        return new PrioritisedPlayers(prioritisedPlayers);
    }

    public static PrioritisedPlayers prioritiseMediaPlayer() {
        List<PlayerType> prioritisedPlayers = new ArrayList<>();
        prioritisedPlayers.add(PlayerType.MEDIA_PLAYER);
        prioritisedPlayers.add(PlayerType.EXO_PLAYER);
        return new PrioritisedPlayers(prioritisedPlayers);
    }

    PrioritisedPlayers(List<PlayerType> playerPriorities) {
        this.playerPriorities = playerPriorities;
    }

    @Override
    public Iterator<PlayerType> iterator() {
        return playerPriorities.iterator();
    }
}
