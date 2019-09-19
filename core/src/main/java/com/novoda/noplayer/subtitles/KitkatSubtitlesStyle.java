package com.novoda.noplayer.subtitles;

import android.support.annotation.RequiresApi;
import android.view.accessibility.CaptioningManager;

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
        return captionStyle.backgroundColor;
    }

    @Override
    public int foregroundColorOr(int fallbackColor) {
        return captionStyle.foregroundColor;
    }

    @Override
    public float scaleTextSize(float textSize) {
        return textSize * fontScale;
    }

}
