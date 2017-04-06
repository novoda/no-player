package com.novoda.noplayer;

public class PlayerAudioTrack {

    private final String trackId;

    public PlayerAudioTrack(String trackId) {
        this.trackId = trackId;
    }

    public String trackId() {
        return trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerAudioTrack that = (PlayerAudioTrack) o;

        return trackId != null ? trackId.equals(that.trackId) : that.trackId == null;

    }

    @Override
    public int hashCode() {
        return trackId != null ? trackId.hashCode() : 0;
    }

}
