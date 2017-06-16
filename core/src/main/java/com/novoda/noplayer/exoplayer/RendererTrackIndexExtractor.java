package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.EnumMap;
import java.util.Map;

class RendererTrackIndexExtractor {

    Map<TrackType, Integer> extractFrom(NoPlayerTrackCounter mappedTrackInfo,
                                        SimpleExoPlayer simpleExoPlayer) {
        Map<TrackType, Integer> trackIndex = new EnumMap<>(TrackType.class);

        int numberOfTracks = mappedTrackInfo.numberOfTracks();
        for (int i = 0; i < numberOfTracks; i++) {
            switch (simpleExoPlayer.getRendererType(i)) {
                case C.TRACK_TYPE_AUDIO:
                    trackIndex.put(TrackType.AUDIO, i);
                    break;
                case C.TRACK_TYPE_VIDEO:
                    trackIndex.put(TrackType.VIDEO, i);
                    break;
                case C.TRACK_TYPE_TEXT:
                    trackIndex.put(TrackType.TEXT, i);
                    break;
            }
        }

        return trackIndex;
    }

    interface NoPlayerTrackCounter {

        int numberOfTracks();
    }
}
