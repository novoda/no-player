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
        facade.addForwarder(prepareForwarder);
        facade.addForwarder(new OnCompletionForwarder(getCompletionListeners()));
        facade.addForwarder(new OnErrorForwarder(this, getErrorListeners()));
        facade.addForwarder(new BufferStateForwarder(getBufferStateListeners()));
        facade.addForwarder(new VideoSizeChangedForwarder(getVideoSizeChangedListeners()));
        facade.addForwarder(new BitrateForwarder(getBitrateChangedListeners()));
        facade.addForwarder(new InfoForwarder(getInfoListeners()));
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
        // TODO: Reset the player, so that it can be used by another video source.
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
        // TODO : Set SubtitleView on the facade.
    }

    @Override
    public void selectAudioTrack(int audioTrackIndex) {
        //TODO: Select the audio track on the facade.
    }

    @Override
    public List<PlayerAudioTrack> getAudioTracks() {
        //TODO : Get a list of audio tracks from the facade.
        return Collections.emptyList();
    }

    private void showContainer() {
        videoContainer.show();
    }

}
