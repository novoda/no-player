package com.novoda.noplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.novoda.noplayer.model.NoPlayerCue;

import java.util.ArrayList;
import java.util.List;

public final class SubtitleView extends View {

    private static final float DEFAULT_TEXT_SIZE_FRACTION = 0.0533f;
    private static final float DEFAULT_BOTTOM_PADDING_FRACTION = 0.08f;

    private static final int FRACTIONAL = 0;
    private static final int ABSOLUTE = 2;

    private final List<SubtitlePainter> painters;

    private List<NoPlayerCue> cues;
    private int textSizeType;
    private float textSize;
    private boolean applyEmbeddedStyles;
    private boolean applyEmbeddedFontSizes;
    private float bottomPaddingFraction;

    public SubtitleView(Context context) {
        this(context, null);
    }

    public SubtitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        painters = new ArrayList<>();
        textSizeType = FRACTIONAL;
        textSize = DEFAULT_TEXT_SIZE_FRACTION;
        applyEmbeddedStyles = true;
        applyEmbeddedFontSizes = true;
        bottomPaddingFraction = DEFAULT_BOTTOM_PADDING_FRACTION;
    }

    public void setCues(List<NoPlayerCue> cues) {
        if (this.cues == cues) {
            return;
        }
        this.cues = cues;
        int cueCount = (cues == null) ? 0 : cues.size();
        while (painters.size() < cueCount) {
            painters.add(new SubtitlePainter(getContext()));
        }

        invalidate();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        int cueCount = (cues == null) ? 0 : cues.size();
        int rawTop = getTop();
        int rawBottom = getBottom();

        int left = getLeft() + getPaddingLeft();
        int top = rawTop + getPaddingTop();
        int right = getRight() + getPaddingRight();
        int bottom = rawBottom - getPaddingBottom();
        if (bottom <= top || right <= left) {
            return;
        }

        float textSizePx = textSizeType == ABSOLUTE ? textSize
                : textSize * (textSizeType == FRACTIONAL ? (bottom - top) : (rawBottom - rawTop));
        if (textSizePx <= 0) {
            return;
        }

        for (int i = 0; i < cueCount; i++) {
            painters.get(i).draw(cues.get(i), applyEmbeddedStyles, applyEmbeddedFontSizes,
                    textSizePx, bottomPaddingFraction, canvas, left, top, right, bottom);
        }
    }
}
