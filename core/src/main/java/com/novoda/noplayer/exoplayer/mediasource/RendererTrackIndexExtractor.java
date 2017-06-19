package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.exoplayer.RendererTypeRequester;

class RendererTrackIndexExtractor {

    public int get(TrackType trackType, int mappedTrackInfoLength, RendererTypeRequester rendererTypeRequester) {
        for (int i = 0; i < mappedTrackInfoLength; i++) {
            int rendererType = rendererTypeRequester.getRendererTypeFor(i);

            if ((trackType == TrackType.AUDIO && rendererType == C.TRACK_TYPE_AUDIO)
                    || (trackType == TrackType.VIDEO && rendererType == C.TRACK_TYPE_VIDEO)
                    || (trackType == TrackType.TEXT && rendererType == C.TRACK_TYPE_TEXT)) {
                return i;
            }
        }

        return -1;
    }
}
