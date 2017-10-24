package com.novoda.noplayer.model;

import com.novoda.noplayer.ContentType;

public class PlayerVideoTrack {

    private final ContentType contentType;
    private final int quality;
    private final int fps;
    private final int bitrate;

    public PlayerVideoTrack(ContentType contentType, int quality, int fps, int bitrate) {
        this.contentType = contentType;
        this.quality = quality;
        this.fps = fps;
        this.bitrate = bitrate;
    }

    public ContentType contentType() {
        return contentType;
    }

    public int quality() {
        return quality;
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

        if (quality != that.quality) {
            return false;
        }
        if (Float.compare(that.fps, fps) != 0) {
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
        result = 31 * result + quality;
        result = 31 * result + (fps != +0.0f ? Float.floatToIntBits(fps) : 0);
        result = 31 * result + bitrate;
        return result;
    }
}
