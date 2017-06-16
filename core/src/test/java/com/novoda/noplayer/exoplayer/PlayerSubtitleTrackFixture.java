package com.novoda.noplayer.exoplayer;

import com.novoda.noplayer.PlayerSubtitleTrack;

public class PlayerSubtitleTrackFixture {

    private int groupIndex = 0;
    private int formatIndex = 0;
    private String trackId = "trackId";
    private String language = "language";
    private String mimeType = "text/vtt";
    private int numberOfChannels = 1;
    private int frequency = 4;

    private PlayerSubtitleTrackFixture() {
        // use anInstance() to get an instance
    }

    public static PlayerSubtitleTrackFixture anInstance() {
        return new PlayerSubtitleTrackFixture();
    }

    public PlayerSubtitleTrackFixture withGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
        return this;
    }

    public PlayerSubtitleTrackFixture withFormatIndex(int formatIndex) {
        this.formatIndex = formatIndex;
        return this;
    }

    public PlayerSubtitleTrackFixture withTrackId(String trackId) {
        this.trackId = trackId;
        return this;
    }

    public PlayerSubtitleTrackFixture withLanguage(String language) {
        this.language = language;
        return this;
    }

    public PlayerSubtitleTrackFixture withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public PlayerSubtitleTrackFixture withNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        return this;
    }

    public PlayerSubtitleTrackFixture withFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public PlayerSubtitleTrack build() {
        return new PlayerSubtitleTrack(groupIndex, formatIndex, trackId, language, mimeType, numberOfChannels, frequency);
    }
}
