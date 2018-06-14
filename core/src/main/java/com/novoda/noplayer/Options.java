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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Options options = (Options) o;

        if (minDurationBeforeQualityIncreaseInMillis != options.minDurationBeforeQualityIncreaseInMillis) {
            return false;
        }
        if (maxInitialBitrate != options.maxInitialBitrate) {
            return false;
        }
        return contentType == options.contentType;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + minDurationBeforeQualityIncreaseInMillis;
        result = 31 * result + maxInitialBitrate;
        return result;
    }

    @Override
    public String toString() {
        return "Options{"
                + "contentType=" + contentType
                + ", minDurationBeforeQualityIncreaseInMillis=" + minDurationBeforeQualityIncreaseInMillis
                + ", maxInitialBitrate=" + maxInitialBitrate
                + '}';
    }
}
