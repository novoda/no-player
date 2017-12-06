package com.novoda.noplayer.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AudioTracks implements Iterable<PlayerAudioTrack> {

    private final List<PlayerAudioTrack> playerAudioTracks;

    public static AudioTracks from(List<PlayerAudioTrack> audioTracks) {
        return new AudioTracks(Collections.unmodifiableList(audioTracks));
    }

    private AudioTracks(List<PlayerAudioTrack> playerAudioTracks) {
        this.playerAudioTracks = playerAudioTracks;
    }

    public PlayerAudioTrack get(int index) {
        return playerAudioTracks.get(index);
    }

    public int size() {
        return playerAudioTracks.size();
    }

    @Override
    public Iterator<PlayerAudioTrack> iterator() {
        return playerAudioTracks.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AudioTracks that = (AudioTracks) o;

        return playerAudioTracks != null ? playerAudioTracks.equals(that.playerAudioTracks) : that.playerAudioTracks == null;
    }

    @Override
    public int hashCode() {
        return playerAudioTracks != null ? playerAudioTracks.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AudioTracks{playerAudioTracks=" + playerAudioTracks + '}';
    }
}
