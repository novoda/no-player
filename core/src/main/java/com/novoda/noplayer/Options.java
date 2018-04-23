package com.novoda.noplayer;

public class Options {

    private final ContentType contentType;
    private final long minDurationBeforeQualityIncreaseInMillis;

    Options(ContentType contentType, long minDurationBeforeQualityIncreaseInMillis) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
    }

    public ContentType contentType() {
        return contentType;
    }

    public long minDurationBeforeQualityIncreaseInMillis() {
        return minDurationBeforeQualityIncreaseInMillis;
    }
}
