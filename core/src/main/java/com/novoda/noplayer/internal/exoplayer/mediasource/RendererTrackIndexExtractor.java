package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.internal.utils.Optional;

class RendererTrackIndexExtractor {

    Optional<Integer> extract(TrackType trackType, int numberOfTracks, RendererTypeRequester typeRequester) {
        for (int i = 0; i < numberOfTracks; i++) {
            int rendererType = typeRequester.getRendererTypeFor(i);

            if ((trackType == TrackType.AUDIO && rendererType == C.TRACK_TYPE_AUDIO)
                    || (trackType == TrackType.VIDEO && rendererType == C.TRACK_TYPE_VIDEO)
                    || (trackType == TrackType.TEXT && rendererType == C.TRACK_TYPE_TEXT)) {
                return Optional.of(i);
            }
        }

        return Optional.absent();
    }
}
