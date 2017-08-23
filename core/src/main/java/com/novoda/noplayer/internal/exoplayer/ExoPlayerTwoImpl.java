package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.View;

import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.Timeout;
import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

import java.util.List;

class ExoPlayerTwoImpl implements NoPlayer {

    private final ExoPlayerFacade exoPlayer;
    private final PlayerListenersHolder listenersHolder;
    private final ExoPlayerForwarder forwarder;
    private final Heart heart;
    private final DrmSessionCreator drmSessionCreator;
    private final MediaCodecSelector mediaCodecSelector;
    private final LoadTimeout loadTimeout;

    @Nullable
    private PlayerView playerView;
    @Nullable
    private SurfaceHolderRequester surfaceHolderRequester;

    private int videoWidth;
    private int videoHeight;
    private SurfaceHolderRequester.Callback onSurfaceReadyCallback;

    ExoPlayerTwoImpl(ExoPlayerFacade exoPlayer,
                     PlayerListenersHolder listenersHolder,
                     ExoPlayerForwarder exoPlayerForwarder,
                     LoadTimeout loadTimeoutParam,
                     Heart heart,
                     DrmSessionCreator drmSessionCreator,
                     MediaCodecSelector mediaCodecSelector) {
        this.exoPlayer = exoPlayer;
        this.listenersHolder = listenersHolder;
        this.loadTimeout = loadTimeoutParam;
        this.forwarder = exoPlayerForwarder;
        this.heart = heart;
        this.drmSessionCreator = drmSessionCreator;
        this.mediaCodecSelector = mediaCodecSelector;
    }

    void initialise() {
        heart.bind(new Heart.Heartbeat(listenersHolder.getHeartbeatCallbacks(), this));
        forwarder.bind(listenersHolder.getPreparedListeners(), this);
        forwarder.bind(listenersHolder.getCompletionListeners(), listenersHolder.getStateChangedListeners());
        forwarder.bind(listenersHolder.getErrorListeners());
        forwarder.bind(listenersHolder.getBufferStateListeners());
        forwarder.bind(listenersHolder.getVideoSizeChangedListeners());
        forwarder.bind(listenersHolder.getBitrateChangedListeners());
        forwarder.bind(listenersHolder.getInfoListeners());
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
    public int getVideoWidth() {
        return videoWidth;
    }

    @Override
    public int getVideoHeight() {
        return videoHeight;
    }

    @Override
    public VideoPosition getPlayheadPosition() throws IllegalStateException {
        return exoPlayer.getPlayheadPosition();
    }

    @Override
    public VideoDuration getMediaDuration() throws IllegalStateException {
        return exoPlayer.getMediaDuration();
    }

    @Override
    public int getBufferPercentage() throws IllegalStateException {
        return exoPlayer.getBufferPercentage();
    }

    @Override
    public void play() throws IllegalStateException {
        heart.startBeatingHeart();
        exoPlayer.play();
        listenersHolder.getStateChangedListeners().onVideoPlaying();
    }

    @Override
    public void play(VideoPosition position) throws IllegalStateException {
        seekTo(position);
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
    public void seekTo(VideoPosition position) throws IllegalStateException {
        exoPlayer.seekTo(position);
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
        listenersHolder.resetPreparedState();
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
    public void loadVideo(final Uri uri, final ContentType contentType) {
        if (exoPlayer.hasPlayedContent()) {
            stop();
        }
        surfaceHolderRequester.removeCallback(onSurfaceReadyCallback);
        onSurfaceReadyCallback = new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                exoPlayer.loadVideo(surfaceHolder, drmSessionCreator, uri, contentType, forwarder, mediaCodecSelector);
            }
        };

        assertPlayerViewIsAttached();
        createSurfaceByShowingVideoContainer();
        surfaceHolderRequester.requestSurfaceHolder(onSurfaceReadyCallback);
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
    public void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadTimeout.start(timeout, loadTimeoutCallback);
        loadVideo(uri, contentType);
    }

    @Override
    public PlayerInformation getPlayerInformation() {
        return new ExoPlayerInformation();
    }

    @Override
    public void attach(PlayerView playerView) {
        this.playerView = playerView;
        surfaceHolderRequester = playerView.getSurfaceHolderRequester();
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
    }

    @Override
    public void detach(PlayerView playerView) {
        listenersHolder.removeStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.removeVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        exoPlayer.removeSubtitleRendererOutput();
        surfaceHolderRequester.removeCallback(onSurfaceReadyCallback);
        surfaceHolderRequester = null;
        this.playerView = null;
    }

    @Override
    public boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        return exoPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public boolean showSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        setSubtitleRendererOutput();
        playerView.showSubtitles();
        return exoPlayer.selectSubtitleTrack(subtitleTrack);
    }

    private void setSubtitleRendererOutput() throws IllegalStateException {
        TextRendererOutput textRendererOutput = new TextRendererOutput(playerView);
        exoPlayer.setSubtitleRendererOutput(textRendererOutput);
    }

    @Override
    public void hideSubtitleTrack() throws IllegalStateException {
        exoPlayer.clearSubtitleTrack();
        playerView.hideSubtitles();
        exoPlayer.removeSubtitleRendererOutput();
    }

    @Override
    public AudioTracks getAudioTracks() throws IllegalStateException {
        return exoPlayer.getAudioTracks();
    }

    @Override
    public List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        return exoPlayer.getSubtitleTracks();
    }

    @Override
    public PlayerListenersHolder getListeners() {
        return listenersHolder;
    }
}
