package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.Representation;

import java.util.ArrayList;
import java.util.List;

class ManifestAssetFinder {

    public List<String> findAssetUrls(MediaPresentationDescription manifest) {
        List<String> assetUrls = new ArrayList<>();
        for (int index = 0; index < manifest.getPeriodCount(); index++) {
            Period period = manifest.getPeriod(index);
            assetUrls.addAll(findAssetUrlsForPeriod(period));
        }
        return assetUrls;
    }

    private List<String> findAssetUrlsForPeriod(Period period) {
        List<String> assetUrls = new ArrayList<>();
        for (AdaptationSet adaptionSet : period.adaptationSets) {
            assetUrls.addAll(findAssetUrlsForAdaptionSet(adaptionSet));
        }
        return assetUrls;
    }

    private List<String> findAssetUrlsForAdaptionSet(AdaptationSet adaptionSet) {
        List<String> assetUrls = new ArrayList<>();
        for (Representation representation : adaptionSet.representations) {
            if (isMissingAssetUri(representation)) {
                continue;
            }
            assetUrls.add(findAssetUrlsForRepresentation(representation));
        }
        return assetUrls;
    }

    private boolean isMissingAssetUri(Representation representation) {
        return representation.getIndexUri() == null;
    }

    private String findAssetUrlsForRepresentation(Representation representation) {
        return representation.getIndexUri().getUri().toString();
    }
}
