package com.novoda.noplayer.model;

import java.util.Collections;
import java.util.List;

public final class TextCues {

    private final List<NoPlayerCue> cues;

    public static TextCues of(List<NoPlayerCue> cues) {
        return new TextCues(Collections.unmodifiableList(cues));
    }

    private TextCues(List<NoPlayerCue> cues) {
        this.cues = cues;
    }

    public int size() {
        return cues.size();
    }

    public boolean isEmpty() {
        return cues.isEmpty();
    }

    public NoPlayerCue get(int position) {
        return cues.get(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextCues textCues = (TextCues) o;

        return cues != null ? cues.equals(textCues.cues) : textCues.cues == null;
    }

    @Override
    public int hashCode() {
        return cues != null ? cues.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TextCues{" + "cues=" + cues + '}';
    }
}
