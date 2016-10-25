package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.ContentProtection;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.Representation;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static com.novoda.noplayer.dash.MediaPresentationDescriptionFixture.aMediaPresentationDescription;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManifestAssetFinderTest {

    private static final String IGNORED_PERIOD_ID = "";
    private static final int IGNORED_PERIOD_START = 0;
    private static final int IGNORED_ADAPTION_SET_ID = 0;
    private static final int IGNORED_ADAPTION_SET_TYPE = 0;
    private static final List<ContentProtection> IGNORED_CONTENT_PROTECTIONS = null;

    ManifestAssetFinder manifestAssetFinder;

    @Before
    public void setUp() {
        manifestAssetFinder = new ManifestAssetFinder();
    }

    @Test
    public void givenAManifestWithNoPeriod_whenFindingAssetUrls_thenAnEmptyListIsReturned() {
        MediaPresentationDescription manifest = aMediaPresentationDescription()
                .build();

        List<String> assetsToDownloadUrls = manifestAssetFinder.findAssetUrls(manifest);

        assertThat(assetsToDownloadUrls).isEmpty();
    }

    @Test
    public void givenAManifestWithNoAdaptionSet_whenFindingAssetUrls_thenAnEmptyListIsReturned() {
        MediaPresentationDescription manifest = aMediaPresentationDescription()
                .withPeriod(createPeriod(Collections.<AdaptationSet>emptyList()))
                .build();

        List<String> assetsToDownloadUrls = manifestAssetFinder.findAssetUrls(manifest);

        assertThat(assetsToDownloadUrls).isEmpty();
    }

    @Test
    public void givenAManifestWithNoIndexUri_whenFindingAssetUrls_thenAnEmptyListIsReturned() {
        MediaPresentationDescription manifest = aMediaPresentationDescription()
                .withPeriod(givenAdaptationSetWithNoIndexUri())
                .build();

        List<String> assetsToDownloadUrls = manifestAssetFinder.findAssetUrls(manifest);

        assertThat(assetsToDownloadUrls).isEmpty();
    }

    private Period givenAdaptationSetWithNoIndexUri() {
        Representation representation = mock(Representation.class);
        when(representation.getIndexUri()).thenReturn(null);
        AdaptationSet adaptationSet = createAdaptationSet(Collections.singletonList(representation));
        return createPeriod(Collections.singletonList(adaptationSet));
    }

    private AdaptationSet createAdaptationSet(List<Representation> representations) {
        return new AdaptationSet(
                IGNORED_ADAPTION_SET_ID,
                IGNORED_ADAPTION_SET_TYPE,
                representations,
                IGNORED_CONTENT_PROTECTIONS
        );
    }

    private Period createPeriod(List<AdaptationSet> adaptationSet) {
        return new Period(
                IGNORED_PERIOD_ID,
                IGNORED_PERIOD_START,
                adaptationSet
        );
    }
}
