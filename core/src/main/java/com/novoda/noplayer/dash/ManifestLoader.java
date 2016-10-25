package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ManifestLoader {

    private final MediaPresentationDescriptionParser mediaPresentationDescriptionParser;

    public ManifestLoader(MediaPresentationDescriptionParser mediaPresentationDescriptionParser) {
        this.mediaPresentationDescriptionParser = mediaPresentationDescriptionParser;
    }

    public MediaPresentationDescription load(String manifestUrl) {
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(manifestUrl);
            return mediaPresentationDescriptionParser.parse(manifestUrl, inputStream);
        } catch (IOException e) {
            throw new ManifestLoadingError(e);
        } finally {
            closeStream(inputStream);
        }
    }

    // Visible for testing
    InputStream getInputStream(String manifestUrl) throws IOException {
        URL url = new URL(manifestUrl);
        return url.openStream();
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new ManifestLoadingError(e);
            }
        }
    }

    static class ManifestLoadingError extends RuntimeException {

        ManifestLoadingError(Exception cause) {
            super(cause);
        }
    }
}
