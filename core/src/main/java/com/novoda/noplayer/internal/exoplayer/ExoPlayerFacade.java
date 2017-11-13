package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerVideoTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;
import com.novoda.noplayer.model.VideoDuration;
import com.novoda.utils.Optional;

import java.util.List;

class ExoPlayerFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private final MediaSourceFactory mediaSourceFactory;
    private final ExoPlayerAudioTrackSelector audioTrackSelector;
    private final ExoPlayerSubtitleTrackSelector subtitleTrackSelector;
    private final ExoPlayerVideoTrackSelector exoPlayerVideoTrackSelector;
    private final ExoPlayerCreator exoPlayerCreator;
    private final RendererTypeRequesterCreator rendererTypeRequesterCreator;

    @Nullable
    private SimpleExoPlayer exoPlayer;
    @Nullable
    private RendererTypeRequester rendererTypeRequester;
    @Nullable
    private ContentType contentType;

    ExoPlayerFacade(MediaSourceFactory mediaSourceFactory,
                    ExoPlayerAudioTrackSelector audioTrackSelector,
                    ExoPlayerSubtitleTrackSelector subtitleTrackSelector,
                    ExoPlayerVideoTrackSelector exoPlayerVideoTrackSelector,
                    ExoPlayerCreator exoPlayerCreator,
                    RendererTypeRequesterCreator rendererTypeRequesterCreator) {
        this.mediaSourceFactory = mediaSourceFactory;
        this.audioTrackSelector = audioTrackSelector;
        this.subtitleTrackSelector = subtitleTrackSelector;
        this.exoPlayerVideoTrackSelector = exoPlayerVideoTrackSelector;
        this.exoPlayerCreator = exoPlayerCreator;
        this.rendererTypeRequesterCreator = rendererTypeRequesterCreator;
    }

    boolean isPlaying() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    long playheadPositionInMillis() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getCurrentPosition();
    }

    VideoDuration getMediaDuration() throws IllegalStateException {
        assertVideoLoaded();
        return VideoDuration.fromMillis(exoPlayer.getDuration());
    }

    int getBufferPercentage() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getBufferedPercentage();
    }

    void play(long positionInMillis) throws IllegalStateException {
        seekTo(positionInMillis);
        play();
    }

    void play() throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.setPlayWhenReady(true);
    }

    void pause() throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.setPlayWhenReady(false);
    }

    void seekTo(long positionInMillis) throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.seekTo(positionInMillis);
    }

    void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    void loadVideo(SurfaceHolder surfaceHolder,
                   DrmSessionCreator drmSessionCreator,
                   Uri uri,
                   ContentType contentType,
                   ExoPlayerForwarder forwarder,
                   MediaCodecSelector mediaCodecSelector) {
        this.contentType = contentType;
        exoPlayer = exoPlayerCreator.create(drmSessionCreator, forwarder.drmSessionEventListener(), mediaCodecSelector);
        rendererTypeRequester = rendererTypeRequesterCreator.createfrom(exoPlayer);
        exoPlayer.addListener(forwarder.exoPlayerEventListener());
        exoPlayer.setVideoDebugListener(forwarder.videoRendererEventListener());
        MediaSource mediaSource = mediaSourceFactory.create(
                contentType,
                uri,
                forwarder.extractorMediaSourceListener(),
                forwarder.mediaSourceEventListener()
        );
        attachToSurface(surfaceHolder);
        exoPlayer.prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
    }

    private void attachToSurface(SurfaceHolder surfaceHolder) {
        exoPlayer.setVideoSurfaceHolder(surfaceHolder);
    }

    AudioTracks getAudioTracks() throws IllegalStateException {
        assertVideoLoaded();
        return audioTrackSelector.getAudioTracks(rendererTypeRequester);
    }

    boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        assertVideoLoaded();
        return audioTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester);
    }

    boolean clearAudioTrackSelection() {
        assertVideoLoaded();
        return audioTrackSelector.clearAudioTrack(rendererTypeRequester);
    }

    boolean selectVideoTrack(PlayerVideoTrack playerVideoTrack) {
        assertVideoLoaded();
        return exoPlayerVideoTrackSelector.selectVideoTrack(playerVideoTrack, rendererTypeRequester);
    }

    Optional<PlayerVideoTrack> getSelectedVideoTrack() {
        assertVideoLoaded();
        return exoPlayerVideoTrackSelector.getSelectedVideoTrack(exoPlayer, rendererTypeRequester, contentType);
    }

    List<PlayerVideoTrack> getVideoTracks() {
        assertVideoLoaded();
        return exoPlayerVideoTrackSelector.getVideoTracks(rendererTypeRequester, contentType);
    }

    boolean clearVideoTrackSelection() {
        assertVideoLoaded();
        return exoPlayerVideoTrackSelector.clearVideoTrack(rendererTypeRequester);
    }

    void setSubtitleRendererOutput(TextRendererOutput textRendererOutput) throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.setTextOutput(textRendererOutput.output());
    }

    void removeSubtitleRendererOutput() throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.setTextOutput(null);
    }

    boolean selectSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        assertVideoLoaded();
        return subtitleTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester);
    }

    List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        assertVideoLoaded();
        return subtitleTrackSelector.getSubtitleTracks(rendererTypeRequester);
    }

    boolean hasPlayedContent() {
        return exoPlayer != null;
    }

    boolean clearSubtitleTrackSelection() throws IllegalStateException {
        assertVideoLoaded();
        return subtitleTrackSelector.clearSubtitleTrack(rendererTypeRequester);
    }

    private void assertVideoLoaded() {
        if (exoPlayer == null) {
            throw new IllegalStateException("Video must be loaded before trying to interact with the player");
        }
    }
}
