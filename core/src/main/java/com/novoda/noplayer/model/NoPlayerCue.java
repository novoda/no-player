package com.novoda.noplayer.model;

import android.graphics.Bitmap;
import android.text.Layout.Alignment;

public class NoPlayerCue {

    public final CharSequence text;
    public final Alignment textAlignment;
    public final Bitmap bitmap;
    public final float line;
    public final int lineType;
    public final int lineAnchor;
    public final float position;
    public final int positionAnchor;
    public final float size;
    public final float bitmapHeight;
    public final boolean windowColorSet;
    public final int windowColor;

    private NoPlayerCue(CharSequence text,
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
}
