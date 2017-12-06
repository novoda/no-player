package com.novoda.noplayer.model;

import android.graphics.Bitmap;
import android.text.Layout.Alignment;

public class NoPlayerCue {

    private final CharSequence text;
    private final Alignment textAlignment;
    private final Bitmap bitmap;
    private final float line;
    private final int lineType;
    private final int lineAnchor;
    private final float position;
    private final int positionAnchor;
    private final float size;
    private final float bitmapHeight;
    private final boolean windowColorSet;
    private final int windowColor;

    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})     // TODO group parameters into classes
    public NoPlayerCue(CharSequence text,
                       Alignment textAlignment,
                       Bitmap bitmap,
                       float line,
                       int lineType,
                       int lineAnchor,
                       float position,
                       int positionAnchor,
                       float size,
                       float bitmapHeight,
                       boolean windowColorSet,
                       int windowColor) {
        this.text = text;
        this.textAlignment = textAlignment;
        this.bitmap = bitmap;
        this.line = line;
        this.lineType = lineType;
        this.lineAnchor = lineAnchor;
        this.position = position;
        this.positionAnchor = positionAnchor;
        this.size = size;
        this.bitmapHeight = bitmapHeight;
        this.windowColorSet = windowColorSet;
        this.windowColor = windowColor;
    }

    public CharSequence text() {
        return text;
    }

    public Alignment textAlignment() {
        return textAlignment;
    }

    public Bitmap bitmap() {
        return bitmap;
    }

    public float line() {
        return line;
    }

    public int lineType() {
        return lineType;
    }

    public int lineAnchor() {
        return lineAnchor;
    }

    public float position() {
        return position;
    }

    public int positionAnchor() {
        return positionAnchor;
    }

    public float size() {
        return size;
    }

    public float bitmapHeight() {
        return bitmapHeight;
    }

    public boolean windowColorSet() {
        return windowColorSet;
    }

    public int windowColor() {
        return windowColor;
    }

    @Override
    public String toString() {
        return "NoPlayerCue{"
                + "text=" + text
                + ", textAlignment=" + textAlignment
                + ", bitmap=" + bitmap
                + ", line=" + line
                + ", lineType=" + lineType
                + ", lineAnchor=" + lineAnchor
                + ", position=" + position
                + ", positionAnchor=" + positionAnchor
                + ", size=" + size
                + ", bitmapHeight=" + bitmapHeight
                + ", windowColorSet=" + windowColorSet
                + ", windowColor=" + windowColor
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NoPlayerCue that = (NoPlayerCue) o;

        if (Float.compare(that.line, line) != 0) {
            return false;
        }
        if (lineType != that.lineType) {
            return false;
        }
        if (lineAnchor != that.lineAnchor) {
            return false;
        }
        if (Float.compare(that.position, position) != 0) {
            return false;
        }
        if (positionAnchor != that.positionAnchor) {
            return false;
        }
        if (Float.compare(that.size, size) != 0) {
            return false;
        }
        if (Float.compare(that.bitmapHeight, bitmapHeight) != 0) {
            return false;
        }
        if (windowColorSet != that.windowColorSet) {
            return false;
        }
        if (windowColor != that.windowColor) {
            return false;
        }
        if (text != null ? !text.equals(that.text) : that.text != null) {
            return false;
        }
        if (textAlignment != that.textAlignment) {
            return false;
        }
        return bitmap != null ? bitmap.equals(that.bitmap) : that.bitmap == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (textAlignment != null ? textAlignment.hashCode() : 0);
        result = 31 * result + (bitmap != null ? bitmap.hashCode() : 0);
        result = 31 * result + (line != +0.0f ? Float.floatToIntBits(line) : 0);
        result = 31 * result + lineType;
        result = 31 * result + lineAnchor;
        result = 31 * result + (position != +0.0f ? Float.floatToIntBits(position) : 0);
        result = 31 * result + positionAnchor;
        result = 31 * result + (size != +0.0f ? Float.floatToIntBits(size) : 0);
        result = 31 * result + (bitmapHeight != +0.0f ? Float.floatToIntBits(bitmapHeight) : 0);
        result = 31 * result + (windowColorSet ? 1 : 0);
        result = 31 * result + windowColor;
        return result;
    }
}
