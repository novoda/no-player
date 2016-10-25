package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ExoPlayer;

import java.util.List;

import static com.google.android.exoplayer.ExoPlayer.STATE_IDLE;
import static com.google.android.exoplayer.ExoPlayer.STATE_PREPARING;

class PlayerStateNotifier {

    private final ExoPlayer player;
    private final RendererState rendererState;

    private int lastReportedPlaybackState;
    private boolean lastReportedPlayWhenReady;

    public PlayerStateNotifier(ExoPlayer player, RendererState rendererState) {
        this.player = player;
        this.rendererState = rendererState;
        lastReportedPlaybackState = STATE_IDLE;
    }

    public void maybeReportPlayerState(List<ExoPlayerFacade.Listener> listeners) {
        boolean playWhenReady = player.getPlayWhenReady();
        int playbackState = getPlaybackState();
        if (lastReportedPlayWhenReady != playWhenReady || lastReportedPlaybackState != playbackState) {
            for (ExoPlayerFacade.Listener listener : listeners) {
                listener.onStateChanged(playWhenReady, playbackState);
            }
            lastReportedPlayWhenReady = playWhenReady;
            lastReportedPlaybackState = playbackState;
        }
    }

    private int getPlaybackState() {
        if (rendererState.isBuilding()) {
            return STATE_PREPARING;
        }
        int playerState = player.getPlaybackState();
        if (rendererState.isBuilt() && playerState == STATE_IDLE) {
            // This is an edge case where the renderers are built, but are still being passed to the
            // player's playback thread.
            return STATE_PREPARING;
        }
        return playerState;
    }

}
