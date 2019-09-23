package com.novoda.noplayer.subtitles;

class PreKitkatSubtytlesStyle implements SubtitlesStyle {

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

}
