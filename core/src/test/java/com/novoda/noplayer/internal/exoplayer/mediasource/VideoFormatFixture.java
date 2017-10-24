package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;

import java.util.Collections;
import java.util.List;

public class VideoFormatFixture {

    private String id = "id";
    private String sampleMimeType = "mime_type";
    private String codecs = "codecs";
    private int bitrate = 100;
    private int maxInputSize = 200;
    private int width = 1920;
    private int height = 1080;
    private float frameRate;
    private List<byte[]> initializationData = Collections.emptyList();
    private DrmInitData drmInitData = new DrmInitData(Collections.<DrmInitData.SchemeData>emptyList());

    public static VideoFormatFixture aVideoFormat() {
        return new VideoFormatFixture();
    }

    private VideoFormatFixture() {
        // Uses static factory method.
    }

    public VideoFormatFixture withId(String id) {
        this.id = id;
        return this;
    }

    public VideoFormatFixture withSampleMimeType(String sampleMimeType) {
        this.sampleMimeType = sampleMimeType;
        return this;
    }

    public VideoFormatFixture withCodecs(String codecs) {
        this.codecs = codecs;
        return this;
    }

    public VideoFormatFixture withBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public VideoFormatFixture withMaxInputSize(int maxInputSize) {
        this.maxInputSize = maxInputSize;
        return this;
    }

    public VideoFormatFixture withWidth(int width) {
        this.width = width;
        return this;
    }

    public VideoFormatFixture withHeight(int height) {
        this.height = height;
        return this;
    }

    public VideoFormatFixture withFrameRate(float frameRate) {
        this.frameRate = frameRate;
        return this;
    }

    public VideoFormatFixture withInitializationData(List<byte[]> initializationData) {
        this.initializationData = initializationData;
        return this;
    }

    public VideoFormatFixture withDrmInitData(DrmInitData drmInitData) {
        this.drmInitData = drmInitData;
        return this;
    }

    public Format build() {
        return Format.createVideoSampleFormat(
                id,
                sampleMimeType,
                codecs,
                bitrate,
                maxInputSize,
                width,
                height,
                frameRate,
                initializationData,
                drmInitData
        );
    }
}
