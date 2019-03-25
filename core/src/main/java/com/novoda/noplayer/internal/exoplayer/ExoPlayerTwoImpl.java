package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerListener;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;
import com.novoda.noplayer.model.Timeout;

import java.util.List;

// Not much we can do, wrapping ExoPlayer is a lot of work
@SuppressWarnings("PMD.GodClass")
class ExoPlayerTwoImpl implements NoPlayer {

    private final ExoPlayerFacade exoPlayer;
    private final PlayerListenersHolder listenersHolder;
    private final ExoPlayerListener exoPlayerListener;
    private final Heart heart;
    private final DrmSessionCreator drmSessionCreator;
    private final MediaCodecSelector mediaCodecSelector;
    private final LoadTimeout loadTimeout;

    @Nullable
    private PlayerView playerView;

    private int videoWidth;
    private int videoHeight;
    private TextRendererOutput textRendererOutput;

    ExoPlayerTwoImpl(ExoPlayerFacade exoPlayer,
                     PlayerListenersHolder listenersHolder,
                     ExoPlayerListener exoPlayerListener,
                     LoadTimeout loadTimeoutParam,
                     Heart heart,
                     DrmSessionCreator drmSessionCreator,
                     MediaCodecSelector mediaCodecSelector) {
        this.exoPlayer = exoPlayer;
        this.listenersHolder = listenersHolder;
        this.loadTimeout = loadTimeoutParam;
        this.exoPlayerListener = exoPlayerListener;
        this.heart = heart;
        this.drmSessionCreator = drmSessionCreator;
        this.mediaCodecSelector = mediaCodecSelector;
    }

    void initialise() {
        heart.bind(new Heart.Heartbeat(listenersHolder.getHeartbeatCallbacks(), this));
        exoPlayerListener.bind(listenersHolder.getPreparedListeners(), this);
        exoPlayerListener.bind(listenersHolder.getCompletionListeners(), listenersHolder.getStateChangedListeners());
        exoPlayerListener.bind(listenersHolder.getErrorListeners());
        exoPlayerListener.bind(listenersHolder.getBufferStateListeners());
        exoPlayerListener.bind(listenersHolder.getVideoSizeChangedListeners());
        exoPlayerListener.bind(listenersHolder.getBitrateChangedListeners());
        exoPlayerListener.bind(listenersHolder.getInfoListeners());
        exoPlayerListener.bind(listenersHolder.getDroppedVideoFramesListeners());
        listenersHolder.addPreparedListener(new PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();
            }
        });
        listenersHolder.addErrorListener(new ErrorListener() {
            @Override
            public void onError(PlayerError error) {
                reset();
            }
        });
        listenersHolder.addVideoSizeChangedListener(new VideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                videoWidth = width;
                videoHeight = height;
            }
        });
    }

    @Override
    public boolean isPlaying() {
        return exoPlayer.isPlaying();
    }

    @Override
    public int videoWidth() {
        return videoWidth;
    }

    @Override
    public int videoHeight() {
        return videoHeight;
    }

    @Override
    public long playheadPositionInMillis() throws IllegalStateException {
        return exoPlayer.playheadPositionInMillis();
    }

    @Override
    public long mediaDurationInMillis() throws IllegalStateException {
        return exoPlayer.mediaDurationInMillis();
    }

    @Override
    public int bufferPercentage() throws IllegalStateException {
        return exoPlayer.bufferPercentage();
    }

    @Override
    public void setRepeating(boolean repeating) {
        exoPlayer.setRepeating(repeating);
    }

    @Override
    public void setVolume(float volume) {
        exoPlayer.setVolume(volume);
    }

    @Override
    public float getVolume() {
        return exoPlayer.getVolume();
    }

    @Override
    public Listeners getListeners() {
        return listenersHolder;
    }

    @Override
    public void play() throws IllegalStateException {
        heart.startBeatingHeart();
        exoPlayer.play();
        listenersHolder.getStateChangedListeners().onVideoPlaying();
    }

    @Override
    public void playAt(long positionInMillis) throws IllegalStateException {
        seekTo(positionInMillis);
        play();
    }

    @Override
    public void pause() throws IllegalStateException {
        exoPlayer.pause();
        listenersHolder.getStateChangedListeners().onVideoPaused();
        if (heart.isBeating()) {
            heart.stopBeatingHeart();
            heart.forceBeat();
        }
    }

    @Override
    public void seekTo(long positionInMillis) throws IllegalStateException {
        exoPlayer.seekTo(positionInMillis);
    }

    @Override
    public void stop() {
        reset();
        listenersHolder.getStateChangedListeners().onVideoStopped();
    }

    @Override
    public void release() {
        stop();
        listenersHolder.clear();
    }

    private void reset() {
        listenersHolder.resetState();
        loadTimeout.cancel();
        heart.stopBeatingHeart();
        exoPlayer.release();
        destroySurfaceByHidingVideoContainer();
    }

    private void destroySurfaceByHidingVideoContainer() {
        if (playerView != null) {
            playerView.getContainerView().setVisibility(View.GONE);
        }
    }

    @Override
    public void loadVideo(final Uri uri, final Options options) {
        if (exoPlayer.hasPlayedContent()) {
            stop();
        }
        assertPlayerViewIsAttached();
        exoPlayer.loadVideo(playerView.getPlayerSurfaceHolder(), drmSessionCreator, uri, options, exoPlayerListener, mediaCodecSelector);
        createSurfaceByShowingVideoContainer();
    }

    private void assertPlayerViewIsAttached() {
        if (playerView == null) {
            throw new IllegalStateException("A PlayerView must be attached in order to loadVideo");
        }
    }

    private void createSurfaceByShowingVideoContainer() {
        playerView.getContainerView().setVisibility(View.VISIBLE);
    }

    @Override
    public void loadVideoWithTimeout(Uri uri, Options options, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadTimeout.start(timeout, loadTimeoutCallback);
        loadVideo(uri, options);
    }

    @Override
    public PlayerInformation getPlayerInformation() {
        return new ExoPlayerInformation();
    }

    @Override
    public void attach(PlayerView playerView) {
        this.playerView = playerView;
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
    }

    @Override
    public void detach(PlayerView playerView) {
        listenersHolder.removeStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.removeVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        removeSubtitleRenderer();
        this.playerView = null;
    }

    @Override
    public boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        return exoPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public boolean clearAudioTrackSelection() throws IllegalStateException {
        return exoPlayer.clearAudioTrackSelection();
    }

    @Override
    public boolean showSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        setSubtitleRendererOutput();
        playerView.showSubtitles();
        return exoPlayer.selectSubtitleTrack(subtitleTrack);
    }

    private void setSubtitleRendererOutput() throws IllegalStateException {
        removeSubtitleRenderer();
        textRendererOutput = new TextRendererOutput(playerView);
        exoPlayer.setSubtitleRendererOutput(textRendererOutput);
    }

    @Override
    public boolean hideSubtitleTrack() throws IllegalStateException {
        playerView.hideSubtitles();
        removeSubtitleRenderer();
        return exoPlayer.clearSubtitleTrackSelection();
    }

    private void removeSubtitleRenderer() {
        if (textRendererOutput != null) {
            exoPlayer.removeSubtitleRendererOutput(textRendererOutput);
        }
    }

    @Override
    public AudioTracks getAudioTracks() throws IllegalStateException {
        return exoPlayer.getAudioTracks();
    }

    @Override
    public boolean selectVideoTrack(PlayerVideoTrack videoTrack) throws IllegalStateException {
        return exoPlayer.selectVideoTrack(videoTrack);
    }

    @Override
    public Optional<PlayerVideoTrack> getSelectedVideoTrack() throws IllegalStateException {
        return exoPlayer.getSelectedVideoTrack();
    }

    @Override
    public boolean clearVideoTrackSelection() throws IllegalStateException {
        return exoPlayer.clearVideoTrackSelection();
    }

    @Override
    public List<PlayerVideoTrack> getVideoTracks() throws IllegalStateException {
        return exoPlayer.getVideoTracks();
    }

    @Override
    public List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        return exoPlayer.getSubtitleTracks();
    }
}
