package com.novoda.noplayer.model;

import com.novoda.noplayer.internal.exoplayer.mediasource.AudioTrackType;

public class PlayerAudioTrackFixture {

    private int groupIndex = 0;
    private int formatIndex = 0;
    private String trackId = "id";
    private String language = "english";
    private String mimeType = ".mp4";
    private int numberOfChannels = 1;
    private int frequency = 60;
    private AudioTrackType audioTrackType = AudioTrackType.MAIN;

    public static PlayerAudioTrackFixture aPlayerAudioTrack() {
        return new PlayerAudioTrackFixture();
    }

    public PlayerAudioTrackFixture withGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
        return this;
    }

    public PlayerAudioTrackFixture withFormatIndex(int formatIndex) {
        this.formatIndex = formatIndex;
        return this;
    }

    public PlayerAudioTrackFixture withTrackId(String trackId) {
        this.trackId = trackId;
        return this;
    }

    public PlayerAudioTrackFixture withLanguage(String language) {
        this.language = language;
        return this;
    }

    public PlayerAudioTrackFixture withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public PlayerAudioTrackFixture withNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        return this;
    }

    public PlayerAudioTrackFixture withFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public PlayerAudioTrackFixture withAudioTrackType(AudioTrackType audioTrackType) {
        this.audioTrackType = audioTrackType;
        return this;
    }

    public PlayerAudioTrack build() {
        return new PlayerAudioTrack(
                groupIndex,
                formatIndex,
                trackId,
                language,
                mimeType,
                numberOfChannels,
                frequency,
                audioTrackType
        );
    }
}
