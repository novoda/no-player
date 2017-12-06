package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.model.TextCues;

import java.util.List;

class TextRendererOutput {

    private final PlayerView playerView;

    TextRendererOutput(PlayerView playerView) {
        this.playerView = playerView;
    }

    TextRenderer.Output output() {
        return new TextRenderer.Output() {
            @Override
            public void onCues(List<Cue> cues) {
                TextCues textCues = ExoPlayerCueMapper.map(cues);
                playerView.setSubtitleCue(textCues);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextRendererOutput)) {
            return false;
        }

        TextRendererOutput that = (TextRendererOutput) o;

        return playerView.equals(that.playerView);
    }

    @Override
    public int hashCode() {
        return playerView.hashCode();
    }

    @Override
    public String toString() {
        return "TextRendererOutput{"
                + "playerView=" + playerView
                + '}';
    }
}
