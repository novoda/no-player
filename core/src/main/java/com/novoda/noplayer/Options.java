package com.novoda.noplayer;

import com.novoda.noplayer.internal.utils.Optional;

import java.util.Arrays;

import androidx.annotation.Nullable;

/**
 * Options to customise the underlying player.
 */
public class Options {

    private final ContentType contentType;
    private final int minDurationBeforeQualityIncreaseInMillis;
    private final int maxInitialBitrate;
    private final int maxVideoBitrate;
    private final Optional<Long> initialPositionInMillis;
    private final Optional<Long> initialAdvertBreakPositionInMillis;
    @Nullable
    private final byte[] keySetId;

    /**
     * Creates a {@link OptionsBuilder} from this Options.
     *
     * @return a new instance of {@link OptionsBuilder}.
     */
    public OptionsBuilder toOptionsBuilder() {
        OptionsBuilder optionsBuilder = new OptionsBuilder()
                .withContentType(contentType)
                .withMinDurationBeforeQualityIncreaseInMillis(minDurationBeforeQualityIncreaseInMillis)
                .withMaxInitialBitrate(maxInitialBitrate)
                .withMaxVideoBitrate(maxVideoBitrate)
                .withKeySetId(keySetId);

        if (initialPositionInMillis.isPresent()) {
            optionsBuilder.withInitialPositionInMillis(initialPositionInMillis.get());
        }
        if (initialAdvertBreakPositionInMillis.isPresent()) {
            optionsBuilder.withInitialAdvertBreakPositionInMillis(initialAdvertBreakPositionInMillis.get());
        }
        return optionsBuilder;
    }

    Options(ContentType contentType,
            int minDurationBeforeQualityIncreaseInMillis,
            int maxInitialBitrate,
            int maxVideoBitrate,
            Optional<Long> initialPositionInMillis,
            Optional<Long> initialAdvertBreakPositionInMillis,
            @Nullable byte[] keySetId) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
        this.maxInitialBitrate = maxInitialBitrate;
        this.maxVideoBitrate = maxVideoBitrate;
        this.initialPositionInMillis = initialPositionInMillis;
        this.initialAdvertBreakPositionInMillis = initialAdvertBreakPositionInMillis;
        this.keySetId = keySetId;
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

    public int maxVideoBitrate() {
        return maxVideoBitrate;
    }

    public Optional<Long> getInitialPositionInMillis() {
        return initialPositionInMillis;
    }

    public Optional<Long> getInitialAdvertBreakPositionInMillis() {
        return initialAdvertBreakPositionInMillis;
    }

    @Nullable
    public byte[] getKeySetId() {
        return keySetId;
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
        if (maxVideoBitrate != options.maxVideoBitrate) {
            return false;
        }
        if (contentType != options.contentType) {
            return false;
        }
        if (initialPositionInMillis != null ? !initialPositionInMillis.equals(options.initialPositionInMillis) : options.initialPositionInMillis != null) {
            return false;
        }
        if (initialAdvertBreakPositionInMillis != null ? !initialAdvertBreakPositionInMillis.equals(options.initialAdvertBreakPositionInMillis) : options.initialAdvertBreakPositionInMillis != null) {
            return false;
        }
        return Arrays.equals(keySetId, options.keySetId);
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + minDurationBeforeQualityIncreaseInMillis;
        result = 31 * result + maxInitialBitrate;
        result = 31 * result + maxVideoBitrate;
        result = 31 * result + (initialPositionInMillis != null ? initialPositionInMillis.hashCode() : 0);
        result = 31 * result + (initialAdvertBreakPositionInMillis != null ? initialAdvertBreakPositionInMillis.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(keySetId);
        return result;
    }

    @Override
    public String toString() {
        return "Options{" +
                "contentType=" + contentType +
                ", minDurationBeforeQualityIncreaseInMillis=" + minDurationBeforeQualityIncreaseInMillis +
                ", maxInitialBitrate=" + maxInitialBitrate +
                ", maxVideoBitrate=" + maxVideoBitrate +
                ", initialPositionInMillis=" + initialPositionInMillis +
                ", initialAdvertBreakPositionInMillis=" + initialAdvertBreakPositionInMillis +
                ", keySetId=" + Arrays.toString(keySetId) +
                '}';
    }
}
