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
    private final String codecName;
    private final RendererSupport rendererCapability;

    @SuppressWarnings("checkstyle:ParameterNumber") // TODO group parameters into classes
    public PlayerVideoTrack(int groupIndex,
                            int formatIndex,
                            String id,
                            ContentType contentType,
                            int width,
                            int height,
                            int fps,
                            int bitrate,
                            String codecName,
                            RendererSupport rendererCapability) {
        this.groupIndex = groupIndex;
        this.formatIndex = formatIndex;
        this.id = id;
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.bitrate = bitrate;
        this.codecName = codecName;
        this.rendererCapability = rendererCapability;
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

    public RendererSupport rendererCapability() {
        return rendererCapability;
    }

    public String codecName() {
        return codecName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerVideoTrack)) {
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
        if (!id.equals(that.id)) {
            return false;
        }
        if (contentType != that.contentType) {
            return false;
        }
        if (codecName != null ? !codecName.equals(that.codecName) : that.codecName != null) {
            return false;
        }

        return rendererCapability == that.rendererCapability;
    }

    @Override
    public int hashCode() {
        int result = groupIndex;
        result = 31 * result + formatIndex;
        result = 31 * result + id.hashCode();
        result = 31 * result + contentType.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + fps;
        result = 31 * result + bitrate;
        result = 31 * result + (codecName != null ? codecName.hashCode() : 0);
        result = 31 * result + (rendererCapability != null ? rendererCapability.hashCode() : 0);
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
                + ", codecName='" + codecName + '\''
                + ", rendererCapability=" + rendererCapability
                + '}';
    }
}
