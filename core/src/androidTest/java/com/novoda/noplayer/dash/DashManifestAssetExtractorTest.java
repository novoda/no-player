package com.novoda.noplayer.dash;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DashManifestAssetExtractorTest {

    private static final String MANIFEST_URL = "manifestUrl.com/";

    @Test
    public void givenALocalDashManifestAssetExtractor_whenExtracting_thenTheValuesAreExpected() {
        DashManifestAssetExtractor dashManifestAssetExtractor = givenALocalDashManifestAssetExtractor();

        List<String> result = dashManifestAssetExtractor.extractAssets(MANIFEST_URL);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(MANIFEST_URL + "CH4_33_05_43_48956249001002_004_AudioOnly.mp4");
        assertThat(result.get(1)).isEqualTo(MANIFEST_URL + "CH4_33_05_43_48956249001002_004_1500.mp4");
    }

    private DashManifestAssetExtractor givenALocalDashManifestAssetExtractor() {
        return new DashManifestAssetExtractor(
                createLocalManifestLoader(),
                new ManifestAssetFinder()
        );
    }

    private ManifestLoader createLocalManifestLoader() {
        return new ManifestLoader(new MediaPresentationDescriptionParser()) {
            @Override
            protected InputStream getInputStream(String manifestUrl) throws IOException {
                return Thread.currentThread().getContextClassLoader().getResourceAsStream("manifest.mpd");
            }
        };
    }
}
