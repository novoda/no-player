package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.accessibility.CaptioningManager;

import static android.os.Build.VERSION_CODES.KITKAT;
import static java.util.Objects.requireNonNull;

class SubtitlesStyleFactory {

    private static final SubtitlesStyle NO_STYLE = new SubtitlesStyle() {

        @Override
        public int backgroundColorOr(int fallbackColor) {
            return fallbackColor;
        }

        @Override
        public int foregroundColorOr(int fallbackColor) {
            return fallbackColor;
        }

        @Override
        public float scaleTextSize(float textSize) {
            return textSize;
        }

    };

    @NonNull
    static SubtitlesStyle create(Context context) {
        if (Build.VERSION.SDK_INT >= KITKAT) {
            final CaptioningManager captioningManager = requireCaptionManager(context);
            CaptioningManager.CaptionStyle systemStyle = captioningManager.getUserStyle();
            float fontScale = captioningManager.getFontScale();
            return new KitkatSubtitlesStyle(systemStyle, fontScale);
        } else {
            return NO_STYLE;
        }
    }

    @RequiresApi(api = KITKAT)
    @NonNull
    private static CaptioningManager requireCaptionManager(Context context) {
        Object service = context.getSystemService(Context.CAPTIONING_SERVICE);
        return requireNonNull((CaptioningManager) service);
    }

}
