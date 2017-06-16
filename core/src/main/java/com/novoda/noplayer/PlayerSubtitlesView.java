package com.novoda.noplayer;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

public interface PlayerSubtitlesView {

    void showSubtitles();

    void hideSubtitles();

    void setSubtitleCue(List<Cue> cues);
}
