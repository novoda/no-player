package com.novoda.noplayer.model;

import com.novoda.noplayer.internal.exoplayer.mediasource.AudioTrackType;

public class PlayerAudioTrack {

    private final int groupIndex;
    private final int formatIndex;
    private final String trackId;
    private final String language;
    private final String mimeType;
    private final int numberOfChannels;
    private final int frequency;
    private final AudioTrackType audioTrackType;

    @SuppressWarnings("checkstyle:ParameterNumber") // TODO group parameters into classes
    public PlayerAudioTrack(int groupIndex,
                            int formatIndex,
                            String trackId,
                            String language,
                            String mimeType,
                            int numberOfChannels,
                            int frequency,
                            AudioTrackType audioTrackType) {
        this.groupIndex = groupIndex;
        this.formatIndex = formatIndex;
        this.trackId = trackId;
        this.language = language;
        this.mimeType = mimeType;
        this.numberOfChannels = numberOfChannels;
        this.frequency = frequency;
        this.audioTrackType = audioTrackType;
    }

    public int groupIndex() {
        return groupIndex;
    }

    public int formatIndex() {
        return formatIndex;
    }

    public String trackId() {
        return trackId;
    }

    public String language() {
        return language;
    }

    public String mimeType() {
        return mimeType;
    }

    public int numberOfChannels() {
        return numberOfChannels;
    }

    public int frequency() {
        return frequency;
    }

    public AudioTrackType audioTrackType() {
        return audioTrackType;
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

        if (groupIndex != that.groupIndex) {
            return false;
        }
        if (formatIndex != that.formatIndex) {
            return false;
        }
        if (numberOfChannels != that.numberOfChannels) {
            return false;
        }
        if (frequency != that.frequency) {
            return false;
        }
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) {
            return false;
        }
        if (language != null ? !language.equals(that.language) : that.language != null) {
            return false;
        }
        if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) {
            return false;
        }
        return audioTrackType == that.audioTrackType;
    }

    @Override
    public int hashCode() {
        int result = groupIndex;
        result = 31 * result + formatIndex;
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + numberOfChannels;
        result = 31 * result + frequency;
        result = 31 * result + (audioTrackType != null ? audioTrackType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerAudioTrack{"
                + "groupIndex=" + groupIndex
                + ", formatIndex=" + formatIndex
                + ", trackId='" + trackId + '\''
                + ", language='" + language + '\''
                + ", mimeType='" + mimeType + '\''
                + ", numberOfChannels=" + numberOfChannels
                + ", frequency=" + frequency
                + ", audioTrackType=" + audioTrackType
                + '}';
    }
}
