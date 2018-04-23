package com.novoda.noplayer;

import android.net.Uri;

/**
 * Builds instances of {@link Options} for {@link NoPlayer#loadVideo(Uri, ContentType)}.
 */
public class OptionsBuilder {

    private ContentType contentType = ContentType.H264;

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

    public Options build() {
        return new Options(contentType);
    }

}
