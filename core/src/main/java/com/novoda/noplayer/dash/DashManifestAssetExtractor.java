package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;

import java.util.List;

public class DashManifestAssetExtractor {

    private final ManifestLoader manifestLoader;
    private final ManifestAssetFinder manifestAssetFinder;

    public static DashManifestAssetExtractor newInstance() {
        MediaPresentationDescriptionParser mediaPresentationDescriptionParser = new MediaPresentationDescriptionParser();
        ManifestLoader manifestLoader = new ManifestLoader(mediaPresentationDescriptionParser);
        ManifestAssetFinder manifestAssetFinder = new ManifestAssetFinder();
        return new DashManifestAssetExtractor(manifestLoader, manifestAssetFinder);
    }

    DashManifestAssetExtractor(ManifestLoader manifestLoader, ManifestAssetFinder manifestAssetFinder) {
        this.manifestLoader = manifestLoader;
        this.manifestAssetFinder = manifestAssetFinder;
    }

    public List<String> extractAssets(String manifestUrl) {
        MediaPresentationDescription manifest = manifestLoader.load(manifestUrl);
        return manifestAssetFinder.findAssetUrls(manifest);
    }
}
