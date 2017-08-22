package com.novoda.noplayer.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AudioTracks implements Iterable<PlayerAudioTrack> {

    private final List<PlayerAudioTrack> audioTracks;

    public static AudioTracks from(List<PlayerAudioTrack> audioTracks) {
        return new AudioTracks(Collections.unmodifiableList(audioTracks));
    }

    private AudioTracks(List<PlayerAudioTrack> audioTracks) {
        this.audioTracks = audioTracks;
    }

    public PlayerAudioTrack get(int index) {
        return audioTracks.get(index);
    }

    public int size() {
        return audioTracks.size();
    }

    @Override
    public Iterator<PlayerAudioTrack> iterator() {
        return audioTracks.iterator();
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

        return audioTracks != null ? audioTracks.equals(that.audioTracks) : that.audioTracks == null;
    }

    @Override
    public int hashCode() {
        return audioTracks != null ? audioTracks.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AudioTracks{" +
                "audioTracks=" + audioTracks +
                '}';
    }
}
