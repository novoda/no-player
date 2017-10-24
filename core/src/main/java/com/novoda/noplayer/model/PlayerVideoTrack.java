package com.novoda.noplayer.model;

import com.novoda.noplayer.ContentType;

public class PlayerVideoTrack {

    private final ContentType contentType;
    private final int width;
    private final int height;
    private final int fps;
    private final int bitrate;

    public PlayerVideoTrack(ContentType contentType, int width, int height, int fps, int bitrate) {
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.bitrate = bitrate;
    }

    public ContentType contentType() {
        return contentType;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int fps() {
        return fps;
    }

    public int bitrate() {
        return bitrate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerVideoTrack that = (PlayerVideoTrack) o;

        if (width != that.width) {
            return false;
        }
        if (height != that.height) {
            return false;
        }
        if (fps != that.fps) {
            return false;
        }
        if (bitrate != that.bitrate) {
            return false;
        }
        return contentType == that.contentType;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + fps;
        result = 31 * result + bitrate;
        return result;
    }

    @Override
    public String toString() {
        return "PlayerVideoTrack{" +
                "contentType=" + contentType +
                ", width=" + width +
                ", height=" + height +
                ", fps=" + fps +
                ", bitrate=" + bitrate +
                '}';
    }
}
