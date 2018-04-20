package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerVideoTrackSelector;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.List;

class ExoPlayerTrackSelector {

    private ExoPlayerAudioTrackSelector audioTrackSelector;
    private ExoPlayerVideoTrackSelector videoTrackSelector;
    private ExoPlayerSubtitleTrackSelector subtitleTrackSelector;

    ExoPlayerTrackSelector(ExoPlayerAudioTrackSelector audioTrackSelector,
                           ExoPlayerVideoTrackSelector videoTrackSelector,
                           ExoPlayerSubtitleTrackSelector subtitleTrackSelector) {
        this.audioTrackSelector = audioTrackSelector;
        this.videoTrackSelector = videoTrackSelector;
        this.subtitleTrackSelector = subtitleTrackSelector;
    }

    boolean selectAudioTrack(PlayerAudioTrack audioTrack, RendererTypeRequester rendererTypeRequester) {
        return audioTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester);
    }

    AudioTracks getAudioTracks(RendererTypeRequester rendererTypeRequester) {
        return audioTrackSelector.getAudioTracks(rendererTypeRequester);
    }

    boolean clearAudioTrack(RendererTypeRequester rendererTypeRequester) {
        return audioTrackSelector.clearAudioTrack(rendererTypeRequester);
    }

    boolean selectVideoTrack(PlayerVideoTrack videoTrack, RendererTypeRequester rendererTypeRequester) {
        return videoTrackSelector.selectVideoTrack(videoTrack, rendererTypeRequester);
    }

    List<PlayerVideoTrack> getVideoTracks(RendererTypeRequester rendererTypeRequester, ContentType contentType) {
        return videoTrackSelector.getVideoTracks(rendererTypeRequester, contentType);
    }

    Optional<PlayerVideoTrack> getSelectedVideoTrack(SimpleExoPlayer exoPlayer,
                                                     RendererTypeRequester rendererTypeRequester,
                                                     ContentType contentType) {
        return videoTrackSelector.getSelectedVideoTrack(exoPlayer, rendererTypeRequester, contentType);
    }

    boolean clearVideoTrack(RendererTypeRequester rendererTypeRequester) {
        return videoTrackSelector.clearVideoTrack(rendererTypeRequester);
    }

    boolean selectTextTrack(PlayerSubtitleTrack subtitleTrack, RendererTypeRequester rendererTypeRequester) {
        return subtitleTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester);
    }

    List<PlayerSubtitleTrack> getSubtitleTracks(RendererTypeRequester rendererTypeRequester) {
        return subtitleTrackSelector.getSubtitleTracks(rendererTypeRequester);
    }

    boolean clearSubtitleTrack(RendererTypeRequester rendererTypeRequester) {
        return subtitleTrackSelector.clearSubtitleTrack(rendererTypeRequester);
    }
}
