package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.TrackRenderer;

import java.util.Arrays;

public class Renderers {

    public static final int VIDEO_RENDERER_ID = 0;
    public static final int AUDIO_RENDERER_ID = 1;
    public static final int TEXT_RENDERER_ID = 2;

    public static final int VIDEO_AUDIO_TEXT_RENDERERS_SIZE = 3;
    public static final int VIDEO_AUDIO_RENDERERS_SIZE = 2;

    private static final int VIDEO_RENDERER_INDEX = 0;
    private static final int AUDIO_RENDERER_INDEX = 1;
    private static final int TEXT_RENDERER_INDEX = 2;

    private final TrackRenderer[] renderers;

    public Renderers(TrackRenderer videoRenderer, TrackRenderer audioRenderer) {
        renderers = new TrackRenderer[VIDEO_AUDIO_RENDERERS_SIZE];
        renderers[VIDEO_RENDERER_INDEX] = videoRenderer;
        renderers[AUDIO_RENDERER_INDEX] = audioRenderer;
    }

    public Renderers(TrackRenderer videoRenderer, TrackRenderer audioRenderer, TrackRenderer textRenderer) {
        renderers = new TrackRenderer[VIDEO_AUDIO_TEXT_RENDERERS_SIZE];
        renderers[VIDEO_RENDERER_INDEX] = videoRenderer;
        renderers[AUDIO_RENDERER_INDEX] = audioRenderer;
        renderers[TEXT_RENDERER_INDEX] = textRenderer;
    }

    public TrackRenderer getVideoRenderer() {
        return renderers[VIDEO_RENDERER_INDEX];
    }

    public TrackRenderer getAudioRenderer() {
        return renderers[AUDIO_RENDERER_INDEX];
    }

    public TrackRenderer[] asArray() {
        return Arrays.copyOf(renderers, renderers.length);
    }
}
