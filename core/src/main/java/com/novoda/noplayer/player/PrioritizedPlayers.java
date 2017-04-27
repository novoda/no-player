package com.novoda.noplayer.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrioritizedPlayers implements Iterable<PlayerType> {

    private final List<PlayerType> playerPriorities;

    public static PrioritizedPlayers prioritizeExoPlayer() {
        List<PlayerType> players = new ArrayList<>();
        players.add(PlayerType.EXO_PLAYER);
        players.add(PlayerType.MEDIA_PLAYER);
        return new PrioritizedPlayers(players);
    }

    public static PrioritizedPlayers prioritizeMediaPlayer() {
        List<PlayerType> players = new ArrayList<>();
        players.add(PlayerType.MEDIA_PLAYER);
        players.add(PlayerType.EXO_PLAYER);
        return new PrioritizedPlayers(players);
    }

    PrioritizedPlayers(List<PlayerType> playerPriorities) {
        this.playerPriorities = playerPriorities;
    }

    @Override
    public Iterator<PlayerType> iterator() {
        return playerPriorities.iterator();
    }
}
