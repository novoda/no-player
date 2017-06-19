package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.novoda.noplayer.PlayerView;

import java.util.List;

class TextRendererOutput {

    private final PlayerView playerView;

    TextRendererOutput(PlayerView playerView) {
        this.playerView = playerView;
    }

    TextRenderer.Output output() {
        return new TextRenderer.Output() {
            @Override
            public void onCues(List<Cue> list) {
                playerView.setSubtitleCue(list);
            }
        };
    }
}
