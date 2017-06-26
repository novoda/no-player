package com.novoda.noplayer.model;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

public class TextCues {

    private final List<Cue> cues;

    public TextCues(List<Cue> cues) {
        this.cues = cues;
    }

    public List<Cue> cues() {
        return cues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextCues)) {
            return false;
        }

        TextCues textCues = (TextCues) o;

        return cues.equals(textCues.cues);
    }

    @Override
    public int hashCode() {
        return cues.hashCode();
    }

    @Override
    public String toString() {
        return "TextCues{" +
                "cues=" + cues +
                '}';
    }
}
