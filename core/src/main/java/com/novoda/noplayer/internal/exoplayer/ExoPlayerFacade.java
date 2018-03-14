package com.novoda.noplayer.internal.exoplayer;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.Surface;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerVideoTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.List;

class ExoPlayerFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private final AndroidDeviceVersion androidDeviceVersion;
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

    ExoPlayerFacade(AndroidDeviceVersion androidDeviceVersion,
                    MediaSourceFactory mediaSourceFactory,
                    ExoPlayerAudioTrackSelector audioTrackSelector,
                    ExoPlayerSubtitleTrackSelector subtitleTrackSelector,
                    ExoPlayerVideoTrackSelector exoPlayerVideoTrackSelector,
                    ExoPlayerCreator exoPlayerCreator,
                    RendererTypeRequesterCreator rendererTypeRequesterCreator) {
        this.androidDeviceVersion = androidDeviceVersion;
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

    long mediaDurationInMillis() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getDuration();
    }

    int bufferPercentage() throws IllegalStateException {
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

    void loadVideo(SurfaceTexture surfaceTexture,
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

        setMovieAudioAttributes(exoPlayer);

        MediaSource mediaSource = mediaSourceFactory.create(
                contentType,
                uri,
                forwarder.extractorMediaSourceListener(),
                forwarder.mediaSourceEventListener()
        );
        attachToSurface(surfaceTexture);
        exoPlayer.prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
    }

    private void setMovieAudioAttributes(SimpleExoPlayer exoPlayer) {
        if (androidDeviceVersion.isLollipopTwentyOneOrAbove()) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build();
            exoPlayer.setAudioAttributes(audioAttributes);
        }
    }

    private void attachToSurface(SurfaceTexture surfaceTexture) {
        Surface surface = new Surface(surfaceTexture);
        exoPlayer.setVideoSurface(surface);
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
        exoPlayer.addTextOutput(textRendererOutput.output());
    }

    void removeSubtitleRendererOutput(TextRendererOutput textRendererOutput) throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.removeTextOutput(textRendererOutput.output());
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

    void setRepeating(boolean repeating) {
        assertVideoLoaded();
        exoPlayer.setRepeatMode(repeating ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
    }

    private void assertVideoLoaded() {
        if (exoPlayer == null) {
            throw new IllegalStateException("Video must be loaded before trying to interact with the player");
        }
    }

}
