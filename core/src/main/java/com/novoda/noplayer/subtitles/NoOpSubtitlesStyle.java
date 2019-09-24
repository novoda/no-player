package com.novoda.noplayer.subtitles;

import android.graphics.Typeface;

import androidx.annotation.Nullable;

class NoOpSubtitlesStyle implements SubtitlesStyle {

    @Override
    public int backgroundColorOr(int fallbackColor) {
        return fallbackColor;
    }

    @Override
    public int foregroundColorOr(int fallbackColor) {
        return fallbackColor;
    }

    @Override
    public int windowColorOr(int fallbackColor) {
        return fallbackColor;
    }

    @Override
    public float scaleTextSize(float textSize) {
        return textSize;
    }

    @Nullable
    @Override
    public Typeface typeface() {
        return null;
    }

    @Override
    public int edgeTypeOr(int fallbackEdgeType) {
        return fallbackEdgeType;
    }

    @Override
    public int edgeColorOr(int fallbackColor) {
        return fallbackColor;
    }

}
