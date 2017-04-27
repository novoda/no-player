package com.novoda.noplayer.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrioritizedPlayerTypes implements Iterable<PlayerType> {

    private final List<PlayerType> playerPriorities;

    public static PrioritizedPlayerTypes prioritizeExoPlayer() {
        List<PlayerType> players = new ArrayList<>();
        players.add(PlayerType.EXO_PLAYER);
        players.add(PlayerType.MEDIA_PLAYER);
        return new PrioritizedPlayerTypes(players);
    }

    public static PrioritizedPlayerTypes prioritizeMediaPlayer() {
        List<PlayerType> players = new ArrayList<>();
        players.add(PlayerType.MEDIA_PLAYER);
        players.add(PlayerType.EXO_PLAYER);
        return new PrioritizedPlayerTypes(players);
    }

    PrioritizedPlayerTypes(List<PlayerType> playerPriorities) {
        this.playerPriorities = playerPriorities;
    }

    @Override
    public Iterator<PlayerType> iterator() {
        return playerPriorities.iterator();
    }
}
