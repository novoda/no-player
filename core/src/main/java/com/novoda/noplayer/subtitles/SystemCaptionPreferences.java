package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.accessibility.CaptioningManager;

import static android.os.Build.VERSION_CODES.KITKAT;
import static java.util.Objects.requireNonNull;

public class SystemCaptionPreferences {

    private final Context context;

    public SystemCaptionPreferences(Context context) {
        this.context = context;
    }

    @NonNull
    public SubtitlesStyle getStyle() {
        if (Build.VERSION.SDK_INT >= KITKAT) {
            final CaptioningManager captioningManager = requireCaptionManager();
            CaptioningManager.CaptionStyle systemStyle = captioningManager.getUserStyle();
            float fontScale = captioningManager.getFontScale();
            return new KitkatSubtitlesStyle(systemStyle, fontScale);
        } else {
            return NO_STYLE;
        }
    }

    @NonNull
    @RequiresApi(api = KITKAT)
    private CaptioningManager requireCaptionManager() {
        Object service = context.getSystemService(Context.CAPTIONING_SERVICE);
        return requireNonNull((CaptioningManager) service);
    }

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

}
