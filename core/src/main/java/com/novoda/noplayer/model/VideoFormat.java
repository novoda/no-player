package com.novoda.noplayer.model;

import com.novoda.noplayer.ContentType;

public class VideoFormat {

    private final ContentType contentType;
    private final String quality;
    private final int fps;
    private final int bitrate;

    public VideoFormat(ContentType contentType, String quality, int fps, int bitrate) {
        this.contentType = contentType;
        this.quality = quality;
        this.fps = fps;
        this.bitrate = bitrate;
    }

    public ContentType contentType() {
        return contentType;
    }

    public String quality() {
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

        VideoFormat that = (VideoFormat) o;

        if (fps != that.fps) {
            return false;
        }
        if (bitrate != that.bitrate) {
            return false;
        }
        if (contentType != that.contentType) {
            return false;
        }
        return quality != null ? quality.equals(that.quality) : that.quality == null;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + (quality != null ? quality.hashCode() : 0);
        result = 31 * result + fps;
        result = 31 * result + bitrate;
        return result;
    }
}
