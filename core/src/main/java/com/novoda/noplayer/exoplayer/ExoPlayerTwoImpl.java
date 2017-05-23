package com.novoda.noplayer.exoplayer;

import android.net.Uri;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoContainer;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.player.PlayerInformation;

import java.util.Collections;
import java.util.List;

public class ExoPlayerTwoImpl extends PlayerListenersHolder implements Player {

    private final ExoPlayerTwoFacade facade;
    private final Heart heart;
    private final OnPrepareForwarder prepareForwarder;
    private VideoContainer videoContainer = VideoContainer.empty();

    public ExoPlayerTwoImpl(ExoPlayerTwoFacade facade) {
        this.facade = facade;
        Heart.Heartbeat<Player> onHeartbeat = new Heart.Heartbeat<>(getHeartbeatCallbacks(), this);
        heart = Heart.newInstance(onHeartbeat);

        prepareForwarder = new OnPrepareForwarder(getPreparedListeners(), this);
        facade.addListener(prepareForwarder);
        facade.addListener(new OnCompletionForwarder(getCompletionListeners()));
        facade.addListener(new OnErrorForwarder(this, getErrorListeners()));
        facade.addListener(new BufferStateForwarder(getBufferStateListeners()));
        facade.addListener(new VideoSizeChangedForwarder(getVideoSizeChangedListeners()));
        facade.setInfoListeners(getInfoListeners());
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
    public void stop() {
        facade.stop();
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
        prepareForwarder.reset();
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
        facade.setPlayer(playerView.simplePlayerView());
        addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        // TODO : Set SubtitleView
        // facade.setSubtitleView(playerView.());
    }

    @Override
    public void selectAudioTrack(int audioTrackIndex) {
        //TODO: actually select the audio track.
    }

    @Override
    public List<PlayerAudioTrack> getAudioTracks() {
        //TODO : return audio tracks.
        return Collections.emptyList();
    }

    private void showContainer() {
        videoContainer.show();
    }

    public ExoPlayerTwoFacade getInternalExoPlayer() {
        return facade;
    }

}
