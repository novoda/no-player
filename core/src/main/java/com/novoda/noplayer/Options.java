package com.novoda.noplayer;

public class Options {

    private final ContentType contentType;
    private final int minDurationBeforeQualityIncreaseInMillis;
    private final int maxInitialBitrate;

    Options(ContentType contentType, int minDurationBeforeQualityIncreaseInMillis, int maxInitialBitrate) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
        this.maxInitialBitrate = maxInitialBitrate;
    }

    public ContentType contentType() {
        return contentType;
    }

    public int minDurationBeforeQualityIncreaseInMillis() {
        return minDurationBeforeQualityIncreaseInMillis;
    }

    public int maxInitialBitrate() {
        return maxInitialBitrate;
    }
}
