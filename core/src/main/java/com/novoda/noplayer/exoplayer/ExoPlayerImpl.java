package com.novoda.noplayer.exoplayer;

import android.net.Uri;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoContainer;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.player.PlayerInformation;

// TODO Temp name until we rename/hide exoplayer
public class ExoPlayerImpl extends PlayerListenersHolder implements Player {

    private static final ExoPlayerInformation EXO_PLAYER_INFORMATION = new ExoPlayerInformation();

    private final ExoPlayerFacade exoPlayer;
    private final OnPreparedForwarder preparedForwarder;
    private final Heart heart;

    private VideoContainer videoContainer = VideoContainer.empty();

    public ExoPlayerImpl(ExoPlayerFacade exoPlayer) {
        this.exoPlayer = exoPlayer;

        preparedForwarder = new OnPreparedForwarder(this, getPreparedListeners());

        Heart.Heartbeat<Player> onHeartbeat = new Heart.Heartbeat<>(getHeartbeatCallbacks(), this);
        heart = Heart.newInstance(onHeartbeat);

        addErrorForwarder(exoPlayer);
        addPreparedForwarder(exoPlayer);
        addBufferStateForwarder(exoPlayer);
        addCompletionForwarder(exoPlayer);
        addInfoForwarder(exoPlayer);
        addVideoSizeChangedForwarder(exoPlayer);
    }

    private void addErrorForwarder(ExoPlayerFacade exoPlayer) {
        exoPlayer.addListener(new ErrorForwarder(this, getErrorListeners()));
    }

    private void addPreparedForwarder(ExoPlayerFacade exoPlayer) {
        exoPlayer.addListener(preparedForwarder);
    }

    private void addBufferStateForwarder(ExoPlayerFacade exoPlayer) {
        exoPlayer.addListener(new BufferStateForwarder(getBufferStateListeners()));
    }

    private void addCompletionForwarder(ExoPlayerFacade exoPlayer) {
        exoPlayer.addListener(new OnCompletionForwarder(getCompletionListeners()));
    }

    private void addInfoForwarder(ExoPlayerFacade exoPlayer) {
        exoPlayer.setInfoListener(new InfoForwarder(getBitrateChangedListeners()));
    }

    private void addVideoSizeChangedForwarder(ExoPlayerFacade exoPlayer) {
        exoPlayer.addListener(new VideoSizeChangedForwarder(getVideoSizeChangedListeners()));
    }

    @Override
    public boolean isPlaying() {
        return exoPlayer.isPlaying();
    }

    @Override
    public VideoDuration getMediaDuration() {
        return VideoDuration.fromMillis(exoPlayer.getDuration());
    }

    @Override
    public int getBufferPercentage() {
        return exoPlayer.getBufferedPercentage();
    }

    @Override
    public VideoPosition getPlayheadPosition() {
        return VideoPosition.fromMillis(exoPlayer.getCurrentPosition());
    }

    @Override
    public int getVideoWidth() {
        return exoPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return exoPlayer.getVideoHeight();
    }

    @Override
    public void play() {
        showContainer();
        heart.startBeatingHeart();
        exoPlayer.setPlayWhenReady(true);
        getStateChangedListeners().onVideoPlaying();
    }

    private void showContainer() {
        videoContainer.show();
    }

    @Override
    public void play(VideoPosition position) {
        seekTo(position);
        play();
    }

    @Override
    public void pause() {
        exoPlayer.setPlayWhenReady(false);
        getStateChangedListeners().onVideoPaused();
        if (heart.isBeating()) {
            stopBeatingHeart();
            heart.forceBeat();
        }
    }

    @Override
    public void seekTo(VideoPosition position) {
        exoPlayer.seekTo(position.inMillis());
    }

    @Override
    public void reset() {
        // TODO
    }

    @Override
    public void release() {
        getPlayerReleaseListener().onPlayerPreRelease(this); // remove player passing, and only pass the current position before releasing
        getStateChangedListeners().onVideoReleased();
        stopBeatingHeart();
        exoPlayer.release();
        hideContainer();
    }

    private void hideContainer() {
        videoContainer.hide();
    }

    private void stopBeatingHeart() {
        heart.stopBeatingHeart();
    }

    @Override
    public void loadVideo(Uri uri, ContentType contentType) {
        preparedForwarder.reset();
        showContainer();
        exoPlayer.prepare(uri, contentType);
    }

    @Override
    public void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        // TODO do the timeout!
        loadVideo(uri, contentType);
    }

    @Override
    public PlayerInformation getPlayerInformation() {
        return EXO_PLAYER_INFORMATION;
    }

    @Override
    public void attach(PlayerView playerView) {
        videoContainer = VideoContainer.with(playerView.getContainerView());
        exoPlayer.setSurfaceHolderRequester(playerView.getSurfaceHolderRequester());
        exoPlayer.setSubtitleLayout(playerView.getSubtitleLayout());
    }

    // Used by the infinite modular video activity, purely for logging purposes
    public ExoPlayerFacade getInternalExoPlayer() {
        return exoPlayer;
    }
}
