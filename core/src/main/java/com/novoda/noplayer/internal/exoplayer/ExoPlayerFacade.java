package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.PlayerSurfaceHolder;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
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

    private final BandwidthMeterCreator bandwidthMeterCreator;
    private final AndroidDeviceVersion androidDeviceVersion;
    private final MediaSourceFactory mediaSourceFactory;
    private final CompositeTrackSelectorCreator trackSelectorCreator;
    private final ExoPlayerCreator exoPlayerCreator;
    private final RendererTypeRequesterCreator rendererTypeRequesterCreator;

    @Nullable
    private SimpleExoPlayer exoPlayer;
    @Nullable
    private CompositeTrackSelector compositeTrackSelector;
    @Nullable
    private RendererTypeRequester rendererTypeRequester;
    @Nullable
    private Options options;

    ExoPlayerFacade(BandwidthMeterCreator bandwidthMeterCreator,
                    AndroidDeviceVersion androidDeviceVersion,
                    MediaSourceFactory mediaSourceFactory,
                    CompositeTrackSelectorCreator trackSelectorCreator,
                    ExoPlayerCreator exoPlayerCreator,
                    RendererTypeRequesterCreator rendererTypeRequesterCreator) {
        this.bandwidthMeterCreator = bandwidthMeterCreator;
        this.androidDeviceVersion = androidDeviceVersion;
        this.mediaSourceFactory = mediaSourceFactory;
        this.trackSelectorCreator = trackSelectorCreator;
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

    void loadVideo(PlayerSurfaceHolder playerSurfaceHolder,
                   DrmSessionCreator drmSessionCreator,
                   Uri uri,
                   Options options,
                   ExoPlayerForwarder forwarder,
                   MediaCodecSelector mediaCodecSelector) {
        this.options = options;

        DefaultBandwidthMeter bandwidthMeter = bandwidthMeterCreator.create(options.maxInitialBitrate());

        compositeTrackSelector = trackSelectorCreator.create(options, bandwidthMeter);
        exoPlayer = exoPlayerCreator.create(
                drmSessionCreator,
                forwarder.drmSessionEventListener(),
                mediaCodecSelector,
                compositeTrackSelector.trackSelector()
        );
        rendererTypeRequester = rendererTypeRequesterCreator.createfrom(exoPlayer);
        exoPlayer.addListener(forwarder.exoPlayerEventListener());
        exoPlayer.addAnalyticsListener(forwarder.analyticsListener());
        exoPlayer.addVideoListener(forwarder.videoListener());

        setMovieAudioAttributes(exoPlayer);

        MediaSource mediaSource = mediaSourceFactory.create(
                options,
                uri,
                forwarder.mediaSourceEventListener(),
                bandwidthMeter
        );
        attachToSurface(playerSurfaceHolder);
        exoPlayer.prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
    }

    private void setMovieAudioAttributes(SimpleExoPlayer exoPlayer) {
        if (androidDeviceVersion.isLollipopTwentyOneOrAbove()) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build();
            exoPlayer.setAudioAttributes(audioAttributes);
        }
    }

    private void attachToSurface(PlayerSurfaceHolder playerSurfaceHolder) {
        playerSurfaceHolder.attach(exoPlayer);
    }

    AudioTracks getAudioTracks() throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.getAudioTracks(rendererTypeRequester);
    }

    boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester);
    }

    boolean clearAudioTrackSelection() {
        assertVideoLoaded();
        return compositeTrackSelector.clearAudioTrack(rendererTypeRequester);
    }

    boolean selectVideoTrack(PlayerVideoTrack playerVideoTrack) {
        assertVideoLoaded();
        return compositeTrackSelector.selectVideoTrack(playerVideoTrack, rendererTypeRequester);
    }

    Optional<PlayerVideoTrack> getSelectedVideoTrack() {
        assertVideoLoaded();
        return compositeTrackSelector.getSelectedVideoTrack(exoPlayer, rendererTypeRequester, options.contentType());
    }

    List<PlayerVideoTrack> getVideoTracks() {
        assertVideoLoaded();
        return compositeTrackSelector.getVideoTracks(rendererTypeRequester, options.contentType());
    }

    boolean clearVideoTrackSelection() {
        assertVideoLoaded();
        return compositeTrackSelector.clearVideoTrack(rendererTypeRequester);
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
        return compositeTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester);
    }

    List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.getSubtitleTracks(rendererTypeRequester);
    }

    boolean hasPlayedContent() {
        return exoPlayer != null;
    }

    boolean clearSubtitleTrackSelection() throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.clearSubtitleTrack(rendererTypeRequester);
    }

    void setRepeating(boolean repeating) {
        assertVideoLoaded();
        exoPlayer.setRepeatMode(repeating ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
    }

    void setVolume(float volume) {
        assertVideoLoaded();
        exoPlayer.setVolume(volume);
    }

    float getVolume() {
        assertVideoLoaded();
        return exoPlayer.getVolume();
    }

    private void assertVideoLoaded() {
        if (exoPlayer == null) {
            throw new IllegalStateException("Video must be loaded before trying to interact with the player");
        }
    }

}
