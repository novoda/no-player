package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DashManifestAssetExtractorTest {

    private static final String ANY_MANIFEST_URL = "";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    ManifestLoader manifestLoader;

    @Mock
    ManifestAssetFinder assetFinder;

    @InjectMocks
    DashManifestAssetExtractor dashManifestAssetExtractor;

    @Mock
    MediaPresentationDescription manifest;

    @Before
    public void setUp() {
        when(manifestLoader.load(ANY_MANIFEST_URL)).thenReturn(manifest);
    }

    @Test
    public void testName1() {
        dashManifestAssetExtractor.extractAssets(ANY_MANIFEST_URL);

        verify(manifestLoader).load(ANY_MANIFEST_URL);
    }

    @Test
    public void testName() {
        dashManifestAssetExtractor.extractAssets(ANY_MANIFEST_URL);

        verify(assetFinder).findAssetUrls(manifest);
    }
}
