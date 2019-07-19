package com.novoda.noplayer;

import com.novoda.noplayer.internal.utils.Optional;

import java.util.List;

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
    private final List<String> unsupportedVideoDecoders;
    private final Optional<Integer> hdQualityBitrateThreshold;

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
                .withMaxVideoBitrate(maxVideoBitrate);

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
            List<String> unsupportedVideoDecoders,
            Optional<Integer> hdQualityBitrateThreshold) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
        this.maxInitialBitrate = maxInitialBitrate;
        this.maxVideoBitrate = maxVideoBitrate;
        this.initialPositionInMillis = initialPositionInMillis;
        this.initialAdvertBreakPositionInMillis = initialAdvertBreakPositionInMillis;
        this.unsupportedVideoDecoders = unsupportedVideoDecoders;
        this.hdQualityBitrateThreshold = hdQualityBitrateThreshold;
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

    public List<String> getUnsupportedVideoDecoders() {
        return unsupportedVideoDecoders;
    }

    public Optional<Integer> getHdQualityBitrateThreshold() {
        return hdQualityBitrateThreshold;
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
        if (unsupportedVideoDecoders != null ? !unsupportedVideoDecoders.equals(options.unsupportedVideoDecoders) : options.unsupportedVideoDecoders != null) {
            return false;
        }
        return hdQualityBitrateThreshold != null ? hdQualityBitrateThreshold.equals(options.hdQualityBitrateThreshold) : options.hdQualityBitrateThreshold == null;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + minDurationBeforeQualityIncreaseInMillis;
        result = 31 * result + maxInitialBitrate;
        result = 31 * result + maxVideoBitrate;
        result = 31 * result + (initialPositionInMillis != null ? initialPositionInMillis.hashCode() : 0);
        result = 31 * result + (initialAdvertBreakPositionInMillis != null ? initialAdvertBreakPositionInMillis.hashCode() : 0);
        result = 31 * result + (unsupportedVideoDecoders != null ? unsupportedVideoDecoders.hashCode() : 0);
        result = 31 * result + (hdQualityBitrateThreshold != null ? hdQualityBitrateThreshold.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Options{"
                + "contentType=" + contentType
                + ", minDurationBeforeQualityIncreaseInMillis=" + minDurationBeforeQualityIncreaseInMillis
                + ", maxInitialBitrate=" + maxInitialBitrate
                + ", maxVideoBitrate=" + maxVideoBitrate
                + ", initialPositionInMillis=" + initialPositionInMillis
                + ", initialAdvertBreakPositionInMillis=" + initialAdvertBreakPositionInMillis
                + ", unsupportedVideoDecoders=" + unsupportedVideoDecoders
                + ", hdQualityBitrateThreshold=" + hdQualityBitrateThreshold
                + '}';
    }
}
