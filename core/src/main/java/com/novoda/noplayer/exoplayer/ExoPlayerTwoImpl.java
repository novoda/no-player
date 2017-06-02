package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
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

public class ExoPlayerTwoImpl extends PlayerListenersHolder implements Player {

    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private final SimpleExoPlayer exoPlayer;
    private final MediaSourceFactory mediaSourceFactory;
    private final ExoPlayerForwarder forwarder;
    private final ExoPlayerAudioTrackSelector trackSelector;
    private final Heart heart;
    private final LoadTimeout loadTimeout;

    private SurfaceHolderRequester surfaceHolderRequester;

    private int videoWidth;
    private int videoHeight;

    public static ExoPlayerTwoImpl newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);
        Heart heart = Heart.newInstance();

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        ExoPlayerTrackSelector exoPlayerTrackSelector = new ExoPlayerTrackSelector(trackSelector);
        FixedTrackSelection.Factory trackSelectionFactory = new FixedTrackSelection.Factory();
        ExoPlayerAudioTrackSelector exoPlayerAudioTrackSelector = new ExoPlayerAudioTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector, new DefaultLoadControl());
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        return new ExoPlayerTwoImpl(
                exoPlayer,
                mediaSourceFactory,
                new ExoPlayerForwarder(),
                loadTimeout,
                exoPlayerAudioTrackSelector,
                heart
        );
    }

    ExoPlayerTwoImpl(SimpleExoPlayer exoPlayer,
                     MediaSourceFactory mediaSourceFactory,
                     ExoPlayerForwarder exoPlayerForwarder,
                     LoadTimeout loadTimeoutParam,
                     ExoPlayerAudioTrackSelector trackSelector,
                     Heart heart) {
        this.exoPlayer = exoPlayer;
        this.mediaSourceFactory = mediaSourceFactory;
        this.loadTimeout = loadTimeoutParam;
        this.forwarder = exoPlayerForwarder;
        this.trackSelector = trackSelector;
        this.heart = heart;

        heart.bind(new Heart.Heartbeat<>(getHeartbeatCallbacks(), this));
        forwarder.bind(getPreparedListeners(), this);
        forwarder.bind(getCompletionListeners());
        forwarder.bind(getErrorListeners(), this);
        forwarder.bind(getBufferStateListeners());
        forwarder.bind(getVideoSizeChangedListeners());
        forwarder.bind(getBitrateChangedListeners());
        forwarder.bind(getInfoListeners());
        addPreparedListener(new PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();
            }
        });
        addErrorListener(new ErrorListener() {
            @Override
            public void onError(Player player, PlayerError error) {
                loadTimeout.cancel();
            }
        });
        addVideoSizeChangedListener(new VideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                videoWidth = width;
                videoHeight = height;
            }
        });
    }

    @Override
    public boolean isPlaying() {
        return exoPlayer.getPlayWhenReady();
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
        return VideoPosition.fromMillis(exoPlayer.getCurrentPosition());
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
    public void play() {
        heart.startBeatingHeart();
        surfaceHolderRequester.requestSurfaceHolder(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                exoPlayer.clearVideoSurfaceHolder(surfaceHolder);
                exoPlayer.setVideoSurfaceHolder(surfaceHolder);
                exoPlayer.setPlayWhenReady(true);
                getStateChangedListeners().onVideoPlaying();
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
        exoPlayer.setPlayWhenReady(false);
        getStateChangedListeners().onVideoPaused();
        if (heart.isBeating()) {
            heart.stopBeatingHeart();
            heart.forceBeat();
        }
    }

    @Override
    public void seekTo(VideoPosition position) {
        exoPlayer.seekTo(position.inMillis());
    }

    @Override
    public void reset() {
        // TODO: Reset the player, so that it can be used by another video source.
    }

    @Override
    public void stop() {
        exoPlayer.stop();
    }

    @Override
    public void release() {
        getPlayerReleaseListener().onPlayerPreRelease(this);
        getStateChangedListeners().onVideoReleased();
        loadTimeout.cancel();
        heart.stopBeatingHeart();
        exoPlayer.release();
    }

    @Override
    public void loadVideo(Uri uri, ContentType contentType) {
        getPreparedListeners().reset();
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
        addStateChangedListener(playerView.getStateChangedListener());
        addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
    }

    @Override
    public void selectAudioTrack(PlayerAudioTrack audioTrack) {
        trackSelector.selectAudioTrack(audioTrack);
    }

    @Override
    public List<PlayerAudioTrack> getAudioTracks() {
        return trackSelector.getAudioTracks();
    }
}
