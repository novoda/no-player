package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.os.Build;
import android.view.accessibility.CaptioningManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION_CODES.KITKAT;
import static com.google.android.exoplayer2.text.CaptionStyleCompat.createFromCaptionStyle;
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
            return new KitkatSubtitlesStyle(
                createFromCaptionStyle(captioningManager.getUserStyle()),
                captioningManager.getFontScale()
            );
        } else {
            return new PreKitkatSubtytlesStyle();
        }
    }

    @NonNull
    @RequiresApi(api = KITKAT)
    private CaptioningManager requireCaptionManager() {
        Object service = context.getSystemService(Context.CAPTIONING_SERVICE);
        return requireNonNull((CaptioningManager) service);
    }

}
