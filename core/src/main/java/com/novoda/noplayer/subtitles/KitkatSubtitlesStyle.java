package com.novoda.noplayer.subtitles;

import android.graphics.Typeface;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.text.CaptionStyleCompat;

import static android.os.Build.VERSION_CODES.KITKAT;

@RequiresApi(api = KITKAT)
class KitkatSubtitlesStyle implements SubtitlesStyle {

    private final CaptionStyleCompat captionStyle;
    private final float fontScale;

    KitkatSubtitlesStyle(CaptionStyleCompat captionStyle, float fontScale) {
        this.captionStyle = captionStyle;
        this.fontScale = fontScale;
    }

    @Override
    public int backgroundColorOr(int fallbackColor) {
        return captionStyle.backgroundColor;
    }

    @Override
    public int foregroundColorOr(int fallbackColor) {
        return captionStyle.foregroundColor;
    }

    @Override
    public int windowColorOr(int fallbackColor) {
        return captionStyle.windowColor;
    }

    @Override
    public float scaleTextSize(float textSize) {
        return textSize * fontScale;
    }

    @Nullable
    @Override
    public Typeface typeface() {
        return captionStyle.typeface;
    }

    @Override
    public int edgeTypeOr(int fallbackEdgeType) {
        return captionStyle.edgeType;
    }

    @Override
    public int edgeColorOr(int fallbackColor) {
        return captionStyle.edgeColor;
    }

}
