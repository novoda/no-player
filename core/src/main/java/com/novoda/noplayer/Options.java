package com.novoda.noplayer;

public class Options {

    private final ContentType contentType;
    private final int minDurationBeforeQualityIncreaseInMillis;

    Options(ContentType contentType, int minDurationBeforeQualityIncreaseInMillis) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
    }

    public ContentType contentType() {
        return contentType;
    }

    public int minDurationBeforeQualityIncreaseInMillis() {
        return minDurationBeforeQualityIncreaseInMillis;
    }

}
