package com.novoda.noplayer.model;

import com.novoda.noplayer.ContentType;

public class PlayerVideoTrackFixture {

    private int groupIndex = 0;
    private int formatIndex = 0;
    private String id = "id";
    private ContentType contentType = ContentType.DASH;
    private int width = 1920;
    private int height = 1080;
    private int fps = 30;
    private int bitrate = 180000;

    public static PlayerVideoTrackFixture aPlayerVideoTrack() {
        return new PlayerVideoTrackFixture();
    }

    private PlayerVideoTrackFixture() {
        // Uses static factory method.
    }

    public PlayerVideoTrackFixture withGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
        return this;
    }

    public PlayerVideoTrackFixture withFormatIndex(int formatIndex) {
        this.formatIndex = formatIndex;
        return this;
    }

    public PlayerVideoTrackFixture withId(String id) {
        this.id = id;
        return this;
    }

    public PlayerVideoTrackFixture withContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public PlayerVideoTrackFixture withWidth(int width) {
        this.width = width;
        return this;
    }

    public PlayerVideoTrackFixture withHeight(int height) {
        this.height = height;
        return this;
    }

    public PlayerVideoTrackFixture withFps(int fps) {
        this.fps = fps;
        return this;
    }

    public PlayerVideoTrackFixture withBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public PlayerVideoTrack build() {
        return new PlayerVideoTrack(groupIndex, formatIndex, id, contentType, width, height, fps, bitrate);
    }
}
