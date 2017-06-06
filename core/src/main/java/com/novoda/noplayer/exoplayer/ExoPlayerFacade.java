package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.MediaSourceFactory;

import java.util.List;

class ExoPlayerFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private final MediaSourceFactory mediaSourceFactory;
    private final ExoPlayerAudioTrackSelector trackSelector;
    private final ExoPlayerCreator exoPlayerCreator;

    @Nullable
    private SimpleExoPlayer exoPlayer;

    ExoPlayerFacade(MediaSourceFactory mediaSourceFactory, ExoPlayerAudioTrackSelector trackSelector, ExoPlayerCreator exoPlayerCreator) {
        this.mediaSourceFactory = mediaSourceFactory;
        this.trackSelector = trackSelector;
        this.exoPlayerCreator = exoPlayerCreator;
    }

    public boolean isPlaying() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    public VideoPosition getPlayheadPosition() {
        assertVideoLoaded();
        return VideoPosition.fromMillis(exoPlayer.getCurrentPosition());
    }

    public VideoDuration getMediaDuration() {
        assertVideoLoaded();
        return VideoDuration.fromMillis(exoPlayer.getDuration());
    }

    public int getBufferPercentage() {
        assertVideoLoaded();
        return exoPlayer.getBufferedPercentage();
    }

    public void play(SurfaceHolder surfaceHolder, VideoPosition position) {
        seekTo(position);
        play(surfaceHolder);
    }

    public void play(SurfaceHolder surfaceHolder) {
        assertVideoLoaded();
        exoPlayer.clearVideoSurfaceHolder(surfaceHolder);
        exoPlayer.setVideoSurfaceHolder(surfaceHolder);
        exoPlayer.setPlayWhenReady(true);
    }

    public void pause() {
        assertVideoLoaded();
        exoPlayer.setPlayWhenReady(false);
    }

    public void seekTo(VideoPosition position) {
        assertVideoLoaded();
        exoPlayer.seekTo(position.inMillis());
    }

    public void reset() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    public void stop() {
        assertVideoLoaded();
        exoPlayer.stop();
    }

    private void assertVideoLoaded() {
        if (exoPlayer == null) {
            throw new IllegalStateException("Video must be loaded before trying to interact with the player");
        }
    }

    public void loadVideo(Uri uri, ContentType contentType, ExoPlayerForwarder forwarder) {
        exoPlayer = exoPlayerCreator.create();
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

    public void selectAudioTrack(PlayerAudioTrack audioTrack) {
        // TODO check if we can read tracks from exoplayer directly
        trackSelector.selectAudioTrack(audioTrack);
    }

    public List<PlayerAudioTrack> getAudioTracks() {
        return trackSelector.getAudioTracks();
    }
}
