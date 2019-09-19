package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.support.annotation.NonNull;

public class SystemCaptionPreferences {

    private final Context context;

    public SystemCaptionPreferences(Context context) {
        this.context = context;
    }

    @NonNull
    public SubtitlesStyle getStyle() {
        return getStyle(context);
    }

    @NonNull
    private static SubtitlesStyle getStyle(Context context) {
        return SubtitlesStyleFactory.create(context);
    }


}
