package com.novoda.noplayer;

import android.net.Uri;

/**
 * Builds instances of {@link Options} for {@link NoPlayer#loadVideo(Uri, Options)}.
 */
public class OptionsBuilder {

    private static final int DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS = 10000;
    private static final int DEFAULT_MAX_INITIAL_BITRATE = 800000;
    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private ContentType contentType = ContentType.H264;
    private int minDurationBeforeQualityIncreaseInMillis = DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
    private int maxInitialBitrate = DEFAULT_MAX_INITIAL_BITRATE;
    private boolean resetPosition = RESET_POSITION;
    private boolean resetState = DO_NOT_RESET_STATE;

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
     * @return {@link OptionsBuilder}
     */
    public OptionsBuilder withMaxInitialBitrate(int maxInitialBitrate) {
        this.maxInitialBitrate = maxInitialBitrate;
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with 'reset position' enabled. This will reset playback to the default
     * position..
     * @param resetPosition when false playback will start from the position for the last played video.
     * @return {@link OptionsBuilder}
     */
    public OptionsBuilder withResetPosition(boolean resetPosition) {
        this.resetPosition = resetPosition;
        return this;
    }

    /**
     * Sets {@link OptionsBuilder} to build {@link Options} with 'reset state' enabled. This will reset the timeline, manifests, tracks and tracks
     * selections.
     *
     * @param resetState should be true unless the player will load the same media that it was playing previously. (e.g if playback is being retried)
     * @return {@link OptionsBuilder}
     */
    public OptionsBuilder withResetState(boolean resetState) {
        this.resetState = resetState;
        return this;
    }

    /**
     * Builds a new {@link Options} instance.
     *
     * @return a {@link Options} instance.
     */
    public Options build() {
        return new Options(contentType, minDurationBeforeQualityIncreaseInMillis, maxInitialBitrate, resetPosition, resetState);
    }
}
