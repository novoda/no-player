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

    private static final boolean APPLY_EMBEDDED_STYLES = true;
    private static final boolean APPLY_EMBEDDED_FONT_STYLES = true;

    private static final int ZERO_PIXELS = 0;
    private static final int NO_CUES = 0;

    private final List<SubtitlePainter> painters;

    private List<NoPlayerCue> cues;

    public SubtitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        painters = new ArrayList<>();
    }

    public void setCues(List<NoPlayerCue> cues) {
        if (this.cues == cues) {
            return;
        }

        this.cues = cues;
        int cueCount = (cues == null) ? NO_CUES : cues.size();

        while (painters.size() < cueCount) {
            painters.add(new SubtitlePainter(getContext()));
        }

        invalidate();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        int cueCount = (cues == null) ? NO_CUES : cues.size();
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

        for (int i = 0; i < cueCount; i++) {
            painters.get(i).draw(
                    cues.get(i),
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
