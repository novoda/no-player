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

    private boolean enabled = true;

    public SystemCaptionPreferences(Context context) {
        this.context = context;
    }

    @NonNull
    public SubtitlesStyle getStyle() {
        if (enabled && Build.VERSION.SDK_INT >= KITKAT) {
            final CaptioningManager captioningManager = requireCaptionManager();
            if (captioningManager.isEnabled()) {
                return new KitKatSubtitlesStyle(
                    createFromCaptionStyle(captioningManager.getUserStyle()),
                    captioningManager.getFontScale()
                );
            }
        }
        return new NoSubtitlesStyle();
    }

    @NonNull
    @RequiresApi(api = KITKAT)
    private CaptioningManager requireCaptionManager() {
        Object service = context.getSystemService(Context.CAPTIONING_SERVICE);
        return requireNonNull((CaptioningManager) service);
    }

    public void setAccessibilityCaptionsStyleEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
