package com.novoda.noplayer;

import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.Dimension;

import java.util.List;

/**
 * Options to customise the underlying player.
 */
@SuppressWarnings("PMD.ExcessiveImports")
public class Options {

    private final ContentType contentType;
    private final int minDurationBeforeQualityIncreaseInMillis;
    private final int maxInitialBitrate;
    private final int maxVideoBitrate;
    private final Dimension maxVideoSize;
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

    // There's a lot of customisation here.
    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})
    Options(ContentType contentType,
            int minDurationBeforeQualityIncreaseInMillis,
            int maxInitialBitrate,
            int maxVideoBitrate,
            Dimension maxVideoSize,
            Optional<Long> initialPositionInMillis,
            Optional<Long> initialAdvertBreakPositionInMillis,
            List<String> unsupportedVideoDecoders,
            Optional<Integer> hdQualityBitrateThreshold) {
        this.contentType = contentType;
        this.minDurationBeforeQualityIncreaseInMillis = minDurationBeforeQualityIncreaseInMillis;
        this.maxInitialBitrate = maxInitialBitrate;
        this.maxVideoBitrate = maxVideoBitrate;
        this.maxVideoSize = maxVideoSize;
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

    public Dimension maxVideoSize() {
        return maxVideoSize;
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
        if (!(o instanceof Options)) {
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
        if (!maxVideoSize.equals(options.maxVideoSize)) {
            return false;
        }
        if (!initialPositionInMillis.equals(options.initialPositionInMillis)) {
            return false;
        }
        if (!initialAdvertBreakPositionInMillis.equals(options.initialAdvertBreakPositionInMillis)) {
            return false;
        }
        if (!unsupportedVideoDecoders.equals(options.unsupportedVideoDecoders)) {
            return false;
        }
        return hdQualityBitrateThreshold.equals(options.hdQualityBitrateThreshold);
    }

    @Override
    public int hashCode() {
        int result = contentType.hashCode();
        result = 31 * result + minDurationBeforeQualityIncreaseInMillis;
        result = 31 * result + maxInitialBitrate;
        result = 31 * result + maxVideoBitrate;
        result = 31 * result + maxVideoSize.hashCode();
        result = 31 * result + initialPositionInMillis.hashCode();
        result = 31 * result + initialAdvertBreakPositionInMillis.hashCode();
        result = 31 * result + unsupportedVideoDecoders.hashCode();
        result = 31 * result + hdQualityBitrateThreshold.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Options{"
                + "contentType=" + contentType
                + ", minDurationBeforeQualityIncreaseInMillis=" + minDurationBeforeQualityIncreaseInMillis
                + ", maxInitialBitrate=" + maxInitialBitrate
                + ", maxVideoBitrate=" + maxVideoBitrate
                + ", maxVideoSize=" + maxVideoSize
                + ", initialPositionInMillis=" + initialPositionInMillis
                + ", initialAdvertBreakPositionInMillis=" + initialAdvertBreakPositionInMillis
                + ", unsupportedVideoDecoders=" + unsupportedVideoDecoders
                + ", hdQualityBitrateThreshold=" + hdQualityBitrateThreshold
                + '}';
    }
}
