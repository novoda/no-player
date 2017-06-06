package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.player.PlayerInformation;

import java.util.List;

public class ExoPlayerTwoImpl implements Player {

    private final ExoPlayerFacade exoPlayer;
    private final PlayerListenersHolder listenersHolder;
    private final ExoPlayerForwarder forwarder;
    private final Heart heart;
    private final LoadTimeout loadTimeout;

    private SurfaceHolderRequester surfaceHolderRequester;

    private int videoWidth;
    private int videoHeight;

    public static ExoPlayerTwoImpl newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        ExoPlayerTrackSelector exoPlayerTrackSelector = new ExoPlayerTrackSelector(trackSelector);
        FixedTrackSelection.Factory trackSelectionFactory = new FixedTrackSelection.Factory();
        ExoPlayerAudioTrackSelector exoPlayerAudioTrackSelector = new ExoPlayerAudioTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);

        ExoPlayerCreator exoPlayerCreator = new ExoPlayerCreator(context, trackSelector);
        ExoPlayerFacade exoPlayerFacade = new ExoPlayerFacade(mediaSourceFactory, exoPlayerAudioTrackSelector, exoPlayerCreator);

        PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
        ExoPlayerForwarder exoPlayerForwarder = new ExoPlayerForwarder();
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        Heart heart = Heart.newInstance();

        return new ExoPlayerTwoImpl(
                exoPlayerFacade,
                listenersHolder,
                exoPlayerForwarder,
                loadTimeout,
                heart
        );
    }

    ExoPlayerTwoImpl(ExoPlayerFacade exoPlayer,
                     PlayerListenersHolder listenersHolder,
                     ExoPlayerForwarder exoPlayerForwarder,
                     LoadTimeout loadTimeoutParam,
                     Heart heart) {
        this.exoPlayer = exoPlayer;
        this.listenersHolder = listenersHolder;
        this.loadTimeout = loadTimeoutParam;
        this.forwarder = exoPlayerForwarder;
        this.heart = heart;

        heart.bind(new Heart.Heartbeat<>(listenersHolder.getHeartbeatCallbacks(), this));
        forwarder.bind(listenersHolder.getPreparedListeners(), this);
        forwarder.bind(listenersHolder.getCompletionListeners());
        forwarder.bind(listenersHolder.getErrorListeners(), this);
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
        exoPlayer.stop();
    }

    @Override
    public void release() {
        reset();
    }

    private void reset() {
        listenersHolder.getPlayerReleaseListener().onPlayerPreRelease(this);
        listenersHolder.getStateChangedListeners().onVideoReleased();
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
    public void attach(PlayerView playerView) {
        surfaceHolderRequester = playerView.getSurfaceHolderRequester();
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
    }

    @Override
    public void selectAudioTrack(PlayerAudioTrack audioTrack) {
        exoPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public List<PlayerAudioTrack> getAudioTracks() {
        return exoPlayer.getAudioTracks();
    }

    @Override
    public PlayerListenersHolder getListenersHolder() {
        return listenersHolder;
    }
}
