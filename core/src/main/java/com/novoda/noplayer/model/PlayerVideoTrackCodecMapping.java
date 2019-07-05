package com.novoda.noplayer.model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds an association betwen the codec being used by a particular track
 * and the decoder that the engine assigns to this codec.
 *
 * Bear in mind that this association is provisional and there is no guarantee that
 * the engine will not fallback to another decoder. This is due to that usually several
 * decoders are provided for a particual track. If the first decoder fails to initialise
 * then the engine will choose another decoder. This information is not easely accessible
 * and so we only provide the first association that the engine does.
 */
public final class PlayerVideoTrackCodecMapping {

    private static final Map<String, String> TRACK_CODEC_MAP = new HashMap<>();

    public void addTrackCodec(String track, String codec) {
        TRACK_CODEC_MAP.put(track, codec);
    }

    @Nullable
    public String getCodecFor(String track) {
        return TRACK_CODEC_MAP.get(track);
    }
}
