package com.novoda.noplayer;

import com.novoda.noplayer.internal.utils.Optional;

/**
 * Options to customise the underlying player.
 */
public class Options {

    private final ContentType contentType;
    private final int minDurationBeforeQualityIncreaseInMillis;
    private final int maxInitialBitrate;
    private final Optional<Long> initialPositionInMillis;

    /**
     * Creates a {@link OptionsBuilder} from this Options.
     *
     * @return a new instance of {@link OptionsBuilder}.
     */
    public OptionsBuilder toOptionsBuilder() {
        OptionsBuilder optionsBuilder = new OptionsBuilder()
                .withContentType(contentType)
                .withMinDurationBeforeQualityIncreaseInMillis(minDurationBeforeQualityIncreaseInMillis)
                .withMaxInitialBitrate(maxInitialBitrate);

        if (initialPositionInMillis.isPresent()) {
            optionsBuilder = optionsBuilder.withInitialPositionInMillis(initialPositionInMillis.get());
        }
        return optionsBuilder;
    }

    Options(ContentType contentType,
            int minDurationBeforeQualityIncreaseInMillis,
            int maxInitialBitrate,
            Optional<Long> initialPositionInMillis) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
        this.maxInitialBitrate = maxInitialBitrate;
        this.initialPositionInMillis = initialPositionInMillis;
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

    public Optional<Long> getInitialPositionInMillis() {
        return initialPositionInMillis;
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
        if (contentType != options.contentType) {
            return false;
        }
        return initialPositionInMillis != null
                ? initialPositionInMillis.equals(options.initialPositionInMillis) : options.initialPositionInMillis == null;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + minDurationBeforeQualityIncreaseInMillis;
        result = 31 * result + maxInitialBitrate;
        result = 31 * result + (initialPositionInMillis != null ? initialPositionInMillis.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Options{"
                + "contentType=" + contentType
                + ", minDurationBeforeQualityIncreaseInMillis=" + minDurationBeforeQualityIncreaseInMillis
                + ", maxInitialBitrate=" + maxInitialBitrate
                + ", initialPositionInMillis=" + initialPositionInMillis
                + '}';
    }
}
