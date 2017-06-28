package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.text.Cue;
import com.novoda.noplayer.model.NoPlayerCue;
import com.novoda.noplayer.model.TextCues;

import java.util.ArrayList;
import java.util.List;

final class ExoPlayerCueMapper {

    private ExoPlayerCueMapper() {
        // static class.
    }

    static TextCues map(List<Cue> cues) {
        List<NoPlayerCue> noPlayerCues = new ArrayList<>(cues.size());

        for (Cue cue : cues) {
            NoPlayerCue noPlayerCue = new NoPlayerCue(
                    cue.text,
                    cue.textAlignment,
                    cue.bitmap,
                    cue.line,
                    cue.lineType,
                    cue.lineAnchor,
                    cue.position,
                    cue.positionAnchor,
                    cue.size,
                    cue.bitmapHeight,
                    cue.windowColorSet,
                    cue.windowColor
            );
            noPlayerCues.add(noPlayerCue);
        }
        return new TextCues(noPlayerCues);
    }
}
