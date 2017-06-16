package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerSubtitleTrack;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.player.PlayerInformation;

import java.util.List;
import java.util.Map;

public class ExoPlayerTwoImpl implements Player {

    private final ExoPlayerFacade exoPlayer;
    private final PlayerListenersHolder listenersHolder;
    private final ExoPlayerForwarder forwarder;
    private final Heart heart;
    private final ExoPlayerTrackSelector exoPlayerTrackSelector;
    private final LoadTimeout loadTimeout;

    private SurfaceHolderRequester surfaceHolderRequester;

    private int videoWidth;
    private int videoHeight;

    ExoPlayerTwoImpl(ExoPlayerFacade exoPlayer,
                     PlayerListenersHolder listenersHolder,
                     ExoPlayerForwarder exoPlayerForwarder,
                     LoadTimeout loadTimeoutParam,
                     Heart heart,
                     ExoPlayerTrackSelector exoPlayerTrackSelector) {
        this.exoPlayer = exoPlayer;
        this.listenersHolder = listenersHolder;
        this.loadTimeout = loadTimeoutParam;
        this.forwarder = exoPlayerForwarder;
        this.heart = heart;
        this.exoPlayerTrackSelector = exoPlayerTrackSelector;
    }

    public void initialise() {
        heart.bind(new Heart.Heartbeat<>(listenersHolder.getHeartbeatCallbacks(), this));
        forwarder.bind(listenersHolder.getPreparedListeners(), this);
        forwarder.bind(listenersHolder.getCompletionListeners(), listenersHolder.getStateChangedListeners());
        forwarder.bind(listenersHolder.getErrorListeners(), this);
        forwarder.bind(listenersHolder.getBufferStateListeners());
        forwarder.bind(listenersHolder.getVideoSizeChangedListeners());
        forwarder.bind(listenersHolder.getBitrateChangedListeners());
        forwarder.bind(listenersHolder.getInfoListeners());
        listenersHolder.addPreparedListener(new PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();

                RendererTrackIndexExtractor rendererTrackIndexExtractor = new RendererTrackIndexExtractor();
                Map<TrackType, Integer> trackTypeIndexMap = rendererTrackIndexExtractor.extractFrom(
                        new RendererTrackIndexExtractor.NoPlayerTrackCounter() {
                            @Override
                            public int numberOfTracks() {
                                return exoPlayerTrackSelector.trackInfo().length;
                            }
                        },
                        exoPlayer.getRawExoPlayer()
                );
                exoPlayerTrackSelector.setTrackRendererIndexes(trackTypeIndexMap);
            }
        });
        listenersHolder.addErrorListener(new ErrorListener() {
            @Override
            public void onError(Player player, PlayerError error) {
                loadTimeout.cancel();
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
    public VideoPosition getPlayheadPosition() {
        return exoPlayer.getPlayheadPosition();
    }

    @Override
    public VideoDuration getMediaDuration() {
        return exoPlayer.getMediaDuration();
    }

    @Override
    public int getBufferPercentage() {
        return exoPlayer.getBufferPercentage();
    }

    @Override
    public void play() {
        heart.startBeatingHeart();
        surfaceHolderRequester.requestSurfaceHolder(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                exoPlayer.play(surfaceHolder);
                listenersHolder.getStateChangedListeners().onVideoPlaying();
            }
        });
    }

    @Override
    public void play(VideoPosition position) {
        seekTo(position);
        play();
    }

    @Override
    public void pause() {
        exoPlayer.pause();
        listenersHolder.getStateChangedListeners().onVideoPaused();
        if (heart.isBeating()) {
            heart.stopBeatingHeart();
            heart.forceBeat();
        }
    }

    @Override
    public void seekTo(VideoPosition position) {
        exoPlayer.seekTo(position);
    }

    @Override
    public void stop() {
        reset();
    }

    @Override
    public void release() {
        reset();
        listenersHolder.clear();
    }

    private void reset() {
        listenersHolder.getStateChangedListeners().onVideoStopped();
        loadTimeout.cancel();
        heart.stopBeatingHeart();
        exoPlayer.release();
    }

    @Override
    public void loadVideo(Uri uri, ContentType contentType) {
        if (exoPlayer.hasPlayedContent()) {
            reset();
        }
        listenersHolder.getPreparedListeners().resetPreparedState();
        exoPlayer.loadVideo(uri, contentType, forwarder);
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
    public void attach(final PlayerView playerView) {
        surfaceHolderRequester = playerView.getSurfaceHolderRequester();
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        exoPlayer.setSubtitleRendererOutput(new TextRenderer.Output() {
            @Override
            public void onCues(List<Cue> list) {
                playerView.setSubtitleCue(list);
            }
        });
    }

    @Override
    public void detach(PlayerView playerView) {
        surfaceHolderRequester = null;
        listenersHolder.removeStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.removeVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
    }

    @Override
    public void selectAudioTrack(PlayerAudioTrack audioTrack) {
        exoPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public void selectSubtitleTrack(PlayerSubtitleTrack subtitleTrack) {
        exoPlayer.selectSubtitleTrack(subtitleTrack);
    }

    @Override
    public void clearSubtitleTrack() {
        exoPlayer.clearSubtitleTrack();
    }

    @Override
    public List<PlayerAudioTrack> getAudioTracks() {
        return exoPlayer.getAudioTracks();
    }

    @Override
    public List<PlayerSubtitleTrack> getSubtitleTracks() {
        return exoPlayer.getSubtitleTracks();
    }

    @Override
    public PlayerListenersHolder getListeners() {
        return listenersHolder;
    }
}
