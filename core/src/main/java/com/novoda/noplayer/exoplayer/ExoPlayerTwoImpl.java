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

public class ExoPlayerTwoImpl extends PlayerListenersHolder implements Player {

    private final ExoPlayerTwoFacade facade;
    private final Heart heart;
    private VideoContainer videoContainer = VideoContainer.empty();

    public ExoPlayerTwoImpl(ExoPlayerTwoFacade facade) {
        this.facade = facade;
        Heart.Heartbeat<Player> onHeartbeat = new Heart.Heartbeat<>(getHeartbeatCallbacks(), this);
        heart = Heart.newInstance(onHeartbeat);
    }

    @Override
    public boolean isPlaying() {
        return facade.getPlayWhenReady();
    }

    @Override
    public int getVideoWidth() {
        return facade.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return facade.getVideoHeight();
    }

    @Override
    public VideoPosition getPlayheadPosition() {
        return VideoPosition.fromMillis(facade.getPlayheadPosition());
    }

    @Override
    public VideoDuration getMediaDuration() {
        return VideoDuration.fromMillis(facade.getMediaDuration());
    }

    @Override
    public int getBufferPercentage() {
        return facade.getBufferedPercentage();
    }

    @Override
    public void addErrorListener(ErrorListener errorListener) {

    }

    @Override
    public void removeErrorListener(ErrorListener errorListener) {

    }

    @Override
    public void addPreparedListener(PreparedListener preparedListener) {

    }

    @Override
    public void removePreparedListener(PreparedListener preparedListener) {

    }

    @Override
    public void addBufferStateListener(BufferStateListener bufferStateListener) {

    }

    @Override
    public void removeBufferStateListener(BufferStateListener bufferStateListener) {

    }

    @Override
    public void addCompletionListener(CompletionListener completionListener) {

    }

    @Override
    public void removeCompletionListener(CompletionListener completionListener) {

    }

    @Override
    public void addStateChangedListener(StateChangedListener stateChangedListener) {

    }

    @Override
    public void removeStateChangedListener(StateChangedListener stateChangedListener) {

    }

    @Override
    public void addBitrateChangedListener(BitrateChangedListener bitrateChangedListener) {

    }

    @Override
    public void removeBitrateChangedListener(BitrateChangedListener bitrateChangedListener) {

    }

    @Override
    public void setPreReleaseListener(PreReleaseListener playerReleaseListener) {

    }

    @Override
    public void addHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback) {

    }

    @Override
    public void removeHeartbeatCallback(Heart.Heartbeat.Callback<Player> callback) {

    }

    @Override
    public void addVideoSizeChangedListener(VideoSizeChangedListener videoSizeChangedListener) {

    }

    @Override
    public void removeVideoSizeChangedListener(VideoSizeChangedListener videoSizeChangedListener) {

    }

    @Override
    public void play() {
        showContainer();
        heart.startBeatingHeart();
        facade.setPlayWhenReady(true);
        getStateChangedListeners().onVideoPlaying();
    }

    @Override
    public void play(VideoPosition position) {
        seekTo(position);
        play();
    }

    @Override
    public void pause() {
        facade.setPlayWhenReady(false);
        getStateChangedListeners().onVideoPaused();
        if (heart.isBeating()) {
            heart.stopBeatingHeart();
            heart.forceBeat();
        }
    }

    @Override
    public void seekTo(VideoPosition position) {
        facade.seekTo(position.inMillis());
    }

    @Override
    public void reset() {
        // TODO
    }

    @Override
    public void release() {
        getPlayerReleaseListener().onPlayerPreRelease(this);
        getStateChangedListeners().onVideoReleased();
        heart.stopBeatingHeart();
        facade.release();
        videoContainer.hide();
    }

    @Override
    public void loadVideo(Uri uri, ContentType contentType) {
//        preparerForwarder.reset();
        showContainer();
        facade.prepare(uri, contentType);
    }

    @Override
    public void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadVideo(uri, contentType);
    }

    @Override
    public PlayerInformation getPlayerInformation() {
        return new ExoPlayerInformation();
    }

    @Override
    public void attach(PlayerView playerView) {
        videoContainer = VideoContainer.with(playerView.getContainerView());
        facade.setSurfaceHolderRequester(playerView.getSurfaceHolderRequester());
        facade.setPlayer(playerView.simplePlayerView());
        addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        // TODO : Set SubtitleView
        // facade.setSubtitleView(playerView.());
    }

    private void showContainer() {
        videoContainer.show();
    }

    public ExoPlayerTwoFacade getInternalExoPlayer() {
        return facade;
    }
}
