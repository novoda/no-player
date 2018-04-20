package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerVideoTrackSelector;

class CompositeTrackSelectorCreator {

    private final DefaultBandwidthMeter bandwidthMeter;

    CompositeTrackSelectorCreator(DefaultBandwidthMeter bandwidthMeter) {
        this.bandwidthMeter = bandwidthMeter;
    }

    CompositeTrackSelector create() {
        TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        ExoPlayerTrackSelector exoPlayerTrackSelector = ExoPlayerTrackSelector.newInstance(trackSelector);
        FixedTrackSelection.Factory trackSelectionFactory = new FixedTrackSelection.Factory();
        ExoPlayerAudioTrackSelector audioTrackSelector = new ExoPlayerAudioTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);
        ExoPlayerVideoTrackSelector videoTrackSelector = new ExoPlayerVideoTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);
        ExoPlayerSubtitleTrackSelector subtitleTrackSelector = new ExoPlayerSubtitleTrackSelector(
                exoPlayerTrackSelector,
                trackSelectionFactory
        );
        return new CompositeTrackSelector(defaultTrackSelector, audioTrackSelector, videoTrackSelector, subtitleTrackSelector);
    }

}
