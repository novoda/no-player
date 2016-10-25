package com.novoda.noplayer.dash;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class ManifestLoaderTest {

    private static final String ANY_MANIFEST_URL = "";

    @Test
    public void givenALocalManifestLoader_whenLoading_thenAManifestObjectIsReturned() {
        ManifestLoader manifestLoader = givenALocalManifestLoader();

        MediaPresentationDescription result = manifestLoader.load(ANY_MANIFEST_URL);

        assertThat(result).isNotNull();
    }

    private ManifestLoader givenALocalManifestLoader() {
        return new ManifestLoader(new MediaPresentationDescriptionParser()) {
            @Override
            protected InputStream getInputStream(String manifestUrl) throws IOException {
                return Thread.currentThread().getContextClassLoader().getResourceAsStream("manifest.mpd");
            }
        };
    }
}
