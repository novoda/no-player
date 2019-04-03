package com.novoda.noplayer;

public class Options {

    private final ContentType contentType;
    private final int minDurationBeforeQualityIncreaseInMillis;
    private final int maxInitialBitrate;
    private final boolean shouldResetPosition;
    private final boolean shouldResetState;

    Options(ContentType contentType,
            int minDurationBeforeQualityIncreaseInMillis,
            int maxInitialBitrate,
            boolean shouldResetPosition,
            boolean shouldResetState) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
        this.maxInitialBitrate = maxInitialBitrate;
        this.shouldResetPosition = shouldResetPosition;
        this.shouldResetState = shouldResetState;
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

    public boolean shouldResetposition() {
        return shouldResetPosition;
    }

    public boolean shouldResetState() {
        return shouldResetState;
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
        if (shouldResetPosition != options.shouldResetPosition) {
            return false;
        }
        if (shouldResetState != options.shouldResetState) {
            return false;
        }
        return contentType == options.contentType;
    }

    @Override
    public int hashCode() {
        int result = contentType.hashCode();
        result = 31 * result + minDurationBeforeQualityIncreaseInMillis;
        result = 31 * result + maxInitialBitrate;
        result = 31 * result + (shouldResetPosition ? 1 : 0);
        result = 31 * result + (shouldResetState ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Options{" +
                "contentType=" + contentType +
                ", minDurationBeforeQualityIncreaseInMillis=" + minDurationBeforeQualityIncreaseInMillis +
                ", maxInitialBitrate=" + maxInitialBitrate +
                ", shouldResetPosition=" + shouldResetPosition +
                ", shouldResetState=" + shouldResetState +
                '}';
    }
}
