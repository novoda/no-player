package com.novoda.noplayer.subtitles;

import android.graphics.Typeface;

import androidx.annotation.Nullable;

public interface SubtitlesStyle {

    int backgroundColorOr(int fallbackColor);

    int foregroundColorOr(int fallbackColor);

    int windowColorOr(int fallbackColor);

    float scaleTextSize(float textSize);

    @Nullable
    Typeface typeface();

    int edgeTypeOr(int fallbackEdgeType);

    int edgeColorOr(int fallbackColor);

    CharSequence formatText(CharSequence text);
}
