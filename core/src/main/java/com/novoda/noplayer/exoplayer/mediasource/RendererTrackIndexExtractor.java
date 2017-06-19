package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.exoplayer.RendererTypeRequester;
import com.novoda.utils.Optional;

class RendererTrackIndexExtractor {

    public Optional<Integer> get(TrackType trackType, int mappedTrackInfoLength, RendererTypeRequester typeRequester) {
        for (int i = 0; i < mappedTrackInfoLength; i++) {
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
