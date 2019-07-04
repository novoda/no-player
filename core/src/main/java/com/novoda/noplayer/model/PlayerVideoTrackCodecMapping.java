package com.novoda.noplayer.model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds an association betwen the codec being used by a particular track
 * and the decoder that the engine assigns to this codec.
 *
 *
 */
public class PlayerVideoTrackCodecMapping {

    private static final PlayerVideoTrackCodecMapping INSTANCE = LazySingleton.INSTANCE;

    private static class LazySingleton {

        private static final PlayerVideoTrackCodecMapping INSTANCE = new PlayerVideoTrackCodecMapping();
    }

    public static PlayerVideoTrackCodecMapping getInstance() {
        return INSTANCE;
    }

    private final Map<String, String> trackCodecMap = new HashMap<>();

    private PlayerVideoTrackCodecMapping() {
        // private class
    }

    public void addTrackCodec(String track, String codec) {
        trackCodecMap.put(track, codec);
    }

    @Nullable
    public String getCodecFor(String track) {
        return trackCodecMap.get(track);
    }
}
