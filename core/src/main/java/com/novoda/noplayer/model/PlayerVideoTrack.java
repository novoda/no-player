package com.novoda.noplayer.model;

import com.novoda.noplayer.ContentType;

public class PlayerVideoTrack {

    private final int groupIndex;
    private final int formatIndex;
    private final String id;
    private final ContentType contentType;
    private final int width;
    private final int height;
    private final int fps;
    private final int bitrate;

    @SuppressWarnings("checkstyle:ParameterNumber") // TODO group parameters into classes
    public PlayerVideoTrack(int groupIndex, int formatIndex, String id, ContentType contentType, int width, int height, int fps, int bitrate) {
        this.groupIndex = groupIndex;
        this.formatIndex = formatIndex;
        this.id = id;
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.bitrate = bitrate;
    }

    public int groupIndex() {
        return groupIndex;
    }

    public int formatIndex() {
        return formatIndex;
    }

    public String id() {
        return id;
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

        if (groupIndex != that.groupIndex) {
            return false;
        }
        if (formatIndex != that.formatIndex) {
            return false;
        }
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
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return contentType == that.contentType;
    }

    @Override
    public int hashCode() {
        int result = groupIndex;
        result = 31 * result + formatIndex;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + fps;
        result = 31 * result + bitrate;
        return result;
    }

    @Override
    public String toString() {
        return "PlayerVideoTrack{"
                + "groupIndex=" + groupIndex
                + ", formatIndex=" + formatIndex
                + ", id='" + id + '\''
                + ", contentType=" + contentType
                + ", width=" + width
                + ", height=" + height
                + ", fps=" + fps
                + ", bitrate=" + bitrate
                + '}';
    }
}
