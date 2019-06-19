package com.novoda.noplayer;

import android.net.Uri;

import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.internal.utils.Optional;

import androidx.annotation.Nullable;

/**
 * Builds instances of {@link Options} for {@link NoPlayer#loadVideo(Uri, Options)}.
 */
public class OptionsBuilder {

    private static final int DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS = 10000;
    private static final int DEFAULT_MAX_INITIAL_BITRATE = 800000;
    private static final int DEFAULT_MAX_VIDEO_BITRATE = Integer.MAX_VALUE;

    private ContentType contentType = ContentType.H264;
    private int minDurationBeforeQualityIncreaseInMillis = DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
    private int maxInitialBitrate = DEFAULT_MAX_INITIAL_BITRATE;
    private int maxVideoBitrate = DEFAULT_MAX_VIDEO_BITRATE;
    private Optional<Long> initialPositionInMillis = Optional.absent();
    private Optional<Long> initialAdvertBreakPositionInMillis = Optional.absent();
    @Nullable
    private byte[] keySetId = null;
    private Optional<KeyRequestExecutor> keyRequestExecutor = Optional.absent();

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with a given {@link ContentType}.
     * This content type is passed to the underlying Player.
     *
     * @param contentType format of the content.
     * @return {@link OptionsBuilder}.
     */
    public OptionsBuilder withContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} so that the {@link NoPlayer}
     * switches to a higher quality video track after given time.
     *
     * @param minDurationInMillis time elapsed before switching to a higher quality video track.
     * @return {@link OptionsBuilder}.
     */
    public OptionsBuilder withMinDurationBeforeQualityIncreaseInMillis(int minDurationInMillis) {
        this.minDurationBeforeQualityIncreaseInMillis = minDurationInMillis;
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with given maximum initial bitrate in order to
     * control what is the quality with which {@link NoPlayer} starts the playback. Setting a higher value
     * allows the player to choose a higher quality video track at the beginning.
     *
     * @param maxInitialBitrate maximum bitrate that limits the initial track selection.
     * @return {@link OptionsBuilder}.
     */
    public OptionsBuilder withMaxInitialBitrate(int maxInitialBitrate) {
        this.maxInitialBitrate = maxInitialBitrate;
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with given maximum video bitrate in order to
     * control what is the maximum video quality with which {@link NoPlayer} starts the playback. Setting a higher value
     * allows the player to choose a higher quality video track.
     *
     * @param maxVideoBitrate maximum bitrate that limits the initial track selection.
     * @return {@link OptionsBuilder}
     */
    public OptionsBuilder withMaxVideoBitrate(int maxVideoBitrate) {
        this.maxVideoBitrate = maxVideoBitrate;
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with given initial position in millis in order
     * to specify the start position of the content that will be played. Omitting to set this will start
     * playback at the beginning of the content.
     *
     * @param initialPositionInMillis position that the content should begin playback at.
     * @return {@link OptionsBuilder}.
     */
    public OptionsBuilder withInitialPositionInMillis(long initialPositionInMillis) {
        this.initialPositionInMillis = Optional.of(initialPositionInMillis);
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with given initial position in millis in order
     * to specify the start position of the first advert break that will be played.
     *
     * @param initialAdvertBreakPositionInMillis position that the first advert break should begin playback at.
     * @return {@link OptionsBuilder}.
     */
    public OptionsBuilder withInitialAdvertBreakPositionInMillis(long initialAdvertBreakPositionInMillis) {
        this.initialAdvertBreakPositionInMillis = Optional.of(initialAdvertBreakPositionInMillis);
        return this;
    }

    public OptionsBuilder withKeySetId(byte[] keySetId) {
        this.keySetId = keySetId;
        return this;
    }

    public OptionsBuilder withKeySetExecutor(KeyRequestExecutor keyRequestExecutor) {
        this.keyRequestExecutor = Optional.of(keyRequestExecutor);
        return this;
    }

    /**
     * Builds a new {@link Options} instance.
     *
     * @return a {@link Options} instance.
     */
    public Options build() {
        return new Options(
                contentType,
                minDurationBeforeQualityIncreaseInMillis,
                maxInitialBitrate,
                maxVideoBitrate,
                initialPositionInMillis,
                initialAdvertBreakPositionInMillis,
                keySetId,
                keyRequestExecutor
        );
    }
}
