package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerSubtitleTrack;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.MediaSourceFactory;

import java.util.List;

class ExoPlayerFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private final MediaSourceFactory mediaSourceFactory;
    private final ExoPlayerAudioTrackSelector audioTrackSelector;
    private final ExoPlayerSubtitleTrackSelector subtitleTrackSelector;
    private final ExoPlayerCreator exoPlayerCreator;
    private final RendererTypeRequesterCreator rendererTypeRequesterCreator;

    @Nullable
    private SimpleExoPlayer exoPlayer;
    @Nullable
    private RendererTypeRequester rendererTypeRequester;

    ExoPlayerFacade(MediaSourceFactory mediaSourceFactory,
                    ExoPlayerAudioTrackSelector audioTrackSelector,
                    ExoPlayerSubtitleTrackSelector subtitleTrackSelector,
                    ExoPlayerCreator exoPlayerCreator,
                    RendererTypeRequesterCreator rendererTypeRequesterCreator) {
        this.mediaSourceFactory = mediaSourceFactory;
        this.audioTrackSelector = audioTrackSelector;
        this.subtitleTrackSelector = subtitleTrackSelector;
        this.exoPlayerCreator = exoPlayerCreator;
        this.rendererTypeRequesterCreator = rendererTypeRequesterCreator;
    }

    boolean isPlaying() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    VideoPosition getPlayheadPosition() {
        assertVideoLoaded();
        return VideoPosition.fromMillis(exoPlayer.getCurrentPosition());
    }

    VideoDuration getMediaDuration() {
        assertVideoLoaded();
        return VideoDuration.fromMillis(exoPlayer.getDuration());
    }

    int getBufferPercentage() {
        assertVideoLoaded();
        return exoPlayer.getBufferedPercentage();
    }

    void play(SurfaceHolder surfaceHolder, VideoPosition position) {
        seekTo(position);
        play(surfaceHolder);
    }

    void play(SurfaceHolder surfaceHolder) {
        assertVideoLoaded();
        exoPlayer.clearVideoSurfaceHolder(surfaceHolder);
        exoPlayer.setVideoSurfaceHolder(surfaceHolder);
        exoPlayer.setPlayWhenReady(true);
    }

    void pause() {
        assertVideoLoaded();
        exoPlayer.setPlayWhenReady(false);
    }

    void seekTo(VideoPosition position) {
        assertVideoLoaded();
        exoPlayer.seekTo(position.inMillis());
    }

    void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    void stop() {
        assertVideoLoaded();
        exoPlayer.stop();
    }

    void loadVideo(DrmSessionCreator drmSessionCreator, Uri uri, ContentType contentType, ExoPlayerForwarder forwarder) {
        exoPlayer = exoPlayerCreator.create(drmSessionCreator);
        rendererTypeRequester = rendererTypeRequesterCreator.createfrom(exoPlayer);
        exoPlayer.addListener(forwarder.exoPlayerEventListener());
        exoPlayer.setVideoDebugListener(forwarder.videoRendererEventListener());
        MediaSource mediaSource = mediaSourceFactory.create(
                contentType,
                uri,
                forwarder.extractorMediaSourceListener(),
                forwarder.mediaSourceEventListener()
        );
        exoPlayer.prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
    }

    boolean selectAudioTrack(PlayerAudioTrack audioTrack) {
        assertVideoLoaded();
        return audioTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester);
    }

    List<PlayerAudioTrack> getAudioTracks() {
        assertVideoLoaded();
        return audioTrackSelector.getAudioTracks(rendererTypeRequester);
    }

    void setSubtitleRendererOutput(TextRendererOutput textRendererOutput) {
        assertVideoLoaded();
        exoPlayer.setTextOutput(textRendererOutput.output());
    }

    void removeSubtitleRendererOutput() {
        assertVideoLoaded();
        exoPlayer.setTextOutput(null);
    }

    boolean selectSubtitleTrack(PlayerSubtitleTrack subtitleTrack) {
        assertVideoLoaded();
        return subtitleTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester);
    }

    List<PlayerSubtitleTrack> getSubtitleTracks() {
        assertVideoLoaded();
        return subtitleTrackSelector.getSubtitleTracks(rendererTypeRequester);
    }

    boolean hasPlayedContent() {
        return exoPlayer != null;
    }

    void clearSubtitleTrack() {
        assertVideoLoaded();
        subtitleTrackSelector.clearSubtitleTrack(rendererTypeRequester);
    }

    private void assertVideoLoaded() {
        if (exoPlayer == null) {
            throw new IllegalStateException("Video must be loaded before trying to interact with the player");
        }
    }
}
