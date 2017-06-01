package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;

import java.util.Collections;
import java.util.List;

class AudioFormatFixture {

    private String id = "id";
    private String sampleMimeType = "mime_type";
    private String codecs = "codecs";
    private int bitrate = 100;
    private int maxInputSize = 200;
    private int channelCount = 2;
    private int sampleRate = 50;
    private List<byte[]> initializationData = Collections.emptyList();
    private DrmInitData drmInitData = new DrmInitData(Collections.<DrmInitData.SchemeData>emptyList());
    private int selectionFlags = 0;
    private String language = "english";

    public static AudioFormatFixture anAudioFormat() {
        return new AudioFormatFixture();
    }

    public AudioFormatFixture withId(String id) {
        this.id = id;
        return this;
    }

    public AudioFormatFixture withSampleMimeType(String sampleMimeType) {
        this.sampleMimeType = sampleMimeType;
        return this;
    }

    public AudioFormatFixture withCodecs(String codecs) {
        this.codecs = codecs;
        return this;
    }

    public AudioFormatFixture withBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public AudioFormatFixture withMaxInputSize(int maxInputSize) {
        this.maxInputSize = maxInputSize;
        return this;
    }

    public AudioFormatFixture withChannelCount(int channelCount) {
        this.channelCount = channelCount;
        return this;
    }

    public AudioFormatFixture withSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public AudioFormatFixture withInitializationData(List<byte[]> initializationData) {
        this.initializationData = initializationData;
        return this;
    }

    public AudioFormatFixture withDrmInitData(DrmInitData drmInitData) {
        this.drmInitData = drmInitData;
        return this;
    }

    public AudioFormatFixture withSelectionFlags(int selectionFlags) {
        this.selectionFlags = selectionFlags;
        return this;
    }

    public AudioFormatFixture withLanguage(String language) {
        this.language = language;
        return this;
    }

    Format build() {
        return Format.createAudioSampleFormat(
                id,
                sampleMimeType,
                codecs,
                bitrate,
                maxInputSize,
                channelCount,
                sampleRate,
                initializationData,
                drmInitData,
                selectionFlags,
                language
        );
    }

}
