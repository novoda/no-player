package com.novoda.noplayer.subtitles;

import android.view.accessibility.CaptioningManager;

import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION_CODES.KITKAT;

@RequiresApi(api = KITKAT)
class KitkatSubtitlesStyle implements SubtitlesStyle {

    private final CaptioningManager.CaptionStyle captionStyle;
    private final float fontScale;

    KitkatSubtitlesStyle(CaptioningManager.CaptionStyle captionStyle, float fontScale) {
        this.captionStyle = captionStyle;
        this.fontScale = fontScale;
    }

    @Override
    public int backgroundColorOr(int fallbackColor) {
        return colorOr(captionStyle.backgroundColor, fallbackColor);
    }

    @Override
    public int foregroundColorOr(int fallbackColor) {
        return colorOr(captionStyle.foregroundColor, fallbackColor);
    }

    @Override
    public float scaleTextSize(float textSize) {
        return textSize * fontScale;
    }

    private static int colorOr(int color, int fallback) {
        return hasColor(color) ? color : fallback;
    }

    /**
     * Copied from API 21 version of CaptionStyle
     */
    @SuppressWarnings("MagicNumber")
    private static boolean hasColor(int packedColor) {
        // Matches the color packing code from Settings. "Default" packed
        // colors are indicated by zero alpha and non-zero red/blue. The
        // cached alpha value used by Settings is stored in green.
        return (packedColor >>> 24) != 0 || (packedColor & 0xFFFF00) == 0;
    }

}
