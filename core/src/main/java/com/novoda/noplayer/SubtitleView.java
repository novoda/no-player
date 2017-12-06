package com.novoda.noplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.novoda.noplayer.model.TextCues;

import java.util.ArrayList;
import java.util.List;

public final class SubtitleView extends View {

    private static final float DEFAULT_TEXT_SIZE_FRACTION = 0.0533f;
    private static final float DEFAULT_BOTTOM_PADDING_FRACTION = 0.08f;

    private static final boolean APPLY_EMBEDDED_STYLES = true;
    private static final boolean APPLY_EMBEDDED_FONT_STYLES = true;

    private static final int ZERO_PIXELS = 0;

    private final List<SubtitlePainter> painters;

    private TextCues textCues;

    public SubtitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        painters = new ArrayList<>();
    }

    public void setCues(TextCues textCues) {
        if (textCues.equals(this.textCues)) {
            return;
        }

        this.textCues = textCues;
        int cueCount = textCues.size();

        while (painters.size() < cueCount) {
            painters.add(new SubtitlePainter(getContext()));
        }

        invalidate();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (textCues == null || textCues.isEmpty()) {
            return;
        }

        int rawTop = getTop();
        int rawBottom = getBottom();

        int left = getLeft() + getPaddingLeft();
        int top = rawTop + getPaddingTop();
        int right = getRight() + getPaddingRight();
        int bottom = rawBottom - getPaddingBottom();

        if (bottom <= top || right <= left) {
            return;
        }

        float textSizeInPixels = DEFAULT_TEXT_SIZE_FRACTION * (bottom - top);

        if (textSizeInPixels <= ZERO_PIXELS) {
            return;
        }

        int cueCount = textCues.size();
        for (int i = 0; i < cueCount; i++) {
            painters.get(i).draw(
                    textCues.get(i),
                    APPLY_EMBEDDED_STYLES,
                    APPLY_EMBEDDED_FONT_STYLES,
                    textSizeInPixels,
                    DEFAULT_BOTTOM_PADDING_FRACTION,
                    canvas,
                    left,
                    top,
                    right,
                    bottom
            );
        }
    }
}
