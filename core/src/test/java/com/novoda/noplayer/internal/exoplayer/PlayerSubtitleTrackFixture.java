package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.model.PlayerSubtitleTrack;

class PlayerSubtitleTrackFixture {

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

    static PlayerSubtitleTrackFixture anInstance() {
        return new PlayerSubtitleTrackFixture();
    }

    PlayerSubtitleTrackFixture withGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
        return this;
    }

    PlayerSubtitleTrackFixture withFormatIndex(int formatIndex) {
        this.formatIndex = formatIndex;
        return this;
    }

    PlayerSubtitleTrackFixture withTrackId(String trackId) {
        this.trackId = trackId;
        return this;
    }

    PlayerSubtitleTrackFixture withLanguage(String language) {
        this.language = language;
        return this;
    }

    PlayerSubtitleTrackFixture withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    PlayerSubtitleTrackFixture withNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        return this;
    }

    PlayerSubtitleTrackFixture withFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    PlayerSubtitleTrack build() {
        return new PlayerSubtitleTrack(groupIndex, formatIndex, trackId, language, mimeType, numberOfChannels, frequency);
    }
}
