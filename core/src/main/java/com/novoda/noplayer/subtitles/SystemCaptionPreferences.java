package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.accessibility.CaptioningManager;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.view.accessibility.CaptioningManager.CaptionStyle;
import static java.util.Objects.requireNonNull;

public class SystemCaptionPreferences {

    private final Context context;

    public SystemCaptionPreferences(Context context) {
        this.context = context;
    }

    public Style getStyle() {
        return getStyle(context);
    }

    public interface Style {

        int backgroundColorOr(int fallbackColor);

        int foregroundColorOr(int fallbackColor);

    }

    private static Style getStyle(Context context) {
        if (VERSION.SDK_INT >= KITKAT) {
            final CaptioningManager captioningManager = requireCaptionManager(context);
            CaptionStyle systemStyle = captioningManager.getUserStyle();
            return new KitkatStyle(systemStyle);
        } else {
            return new NoStyle();
        }
    }

    @RequiresApi(api = KITKAT)
    @NonNull
    private static CaptioningManager requireCaptionManager(Context context) {
        Object service = context.getSystemService(Context.CAPTIONING_SERVICE);
        return requireNonNull((CaptioningManager) service);
    }

    private static class NoStyle implements Style {

        @Override
        public int backgroundColorOr(int fallbackColor) {
            return fallbackColor;
        }

        @Override
        public int foregroundColorOr(int fallbackColor) {
            return fallbackColor;
        }

    }

    @RequiresApi(api = KITKAT)
    private static class KitkatStyle implements Style {

        final CaptionStyle captionStyle;

        KitkatStyle(CaptionStyle captionStyle) {
            this.captionStyle = captionStyle;
        }

        @Override
        public int backgroundColorOr(int fallbackColor) {
            return captionStyle.backgroundColor;
        }

        @Override
        public int foregroundColorOr(int fallbackColor) {
            return captionStyle.foregroundColor;
        }

    }

}
