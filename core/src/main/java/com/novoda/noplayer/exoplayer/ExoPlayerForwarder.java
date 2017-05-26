package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

interface ExoPlayerForwarder extends AdaptiveMediaSourceEventListener,
        ExoPlayer.EventListener,
        VideoRendererEventListener,
        ExtractorMediaSource.EventListener {

}
