package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.SimpleExoPlayer;

class RendererTypeRequesterCreator {

    RendererTypeRequester createfrom(final SimpleExoPlayer exoPlayer) {
        return new RendererTypeRequester() {
            @Override
            public int getRendererTypeFor(int index) {
                return exoPlayer.getRendererType(index);
            }
        };
    }
}
