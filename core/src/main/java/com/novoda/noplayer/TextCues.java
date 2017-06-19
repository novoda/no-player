package com.novoda.noplayer;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

public class TextCues {

    private final List<Cue> cues;

    public TextCues(List<Cue> cues) {
        this.cues = cues;
    }

    List<Cue> cues() {
        return cues;
    }
}
