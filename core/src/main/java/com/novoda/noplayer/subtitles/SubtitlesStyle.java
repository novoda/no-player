package com.novoda.noplayer.subtitles;

public interface SubtitlesStyle {

    int backgroundColorOr(int fallbackColor);

    int foregroundColorOr(int fallbackColor);

    int windowColorOr(int fallbackColor);

    float scaleTextSize(float textSize);

}
