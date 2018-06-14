package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerSurfaceHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceRequester;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.Either;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;
import com.novoda.noplayer.model.Timeout;

import java.util.ArrayList;
import java.util.List;

// Not much we can do, wrapping MediaPlayer is a lot of work
@SuppressWarnings("PMD.GodClass")
class AndroidMediaPlayerImpl implements NoPlayer {

    private static final long NO_SEEK_TO_POSITION = -1;
    private static final long INITIAL_PLAY_SEEK_DELAY_IN_MILLIS = 500;

    private final List<SurfaceRequester.Callback> surfaceHolderRequesterCallbacks = new ArrayList<>();

    private final MediaPlayerInformation mediaPlayerInformation;
    private final AndroidMediaPlayerFacade mediaPlayer;
    private final MediaPlayerForwarder forwarder;
    private final CheckBufferHeartbeatCallback bufferHeartbeatCallback;
    private final DelayedActionExecutor delayedActionExecutor;
    private final Heart heart;
    private final PlayerListenersHolder listenersHolder;
    private final LoadTimeout loadTimeout;
    private final BuggyVideoDriverPreventer buggyVideoDriverPreventer;

    private int videoWidth;
    private int videoHeight;
    private long seekToPositionInMillis = NO_SEEK_TO_POSITION;

    private boolean seekingWithIntentToPlay;
    private SurfaceRequester surfaceRequester;
    private View containerView;

    @SuppressWarnings("checkstyle:ParameterNumber")
        // We cannot really group these any further
    AndroidMediaPlayerImpl(MediaPlayerInformation mediaPlayerInformation,
                           AndroidMediaPlayerFacade mediaPlayer,
                           MediaPlayerForwarder forwarder,
                           PlayerListenersHolder listenersHolder,
                           CheckBufferHeartbeatCallback bufferHeartbeatCallback,
                           LoadTimeout loadTimeout,
                           Heart heart,
                           DelayedActionExecutor delayedActionExecutor,
                           BuggyVideoDriverPreventer buggyVideoDriverPreventer) {
        this.mediaPlayerInformation = mediaPlayerInformation;
        this.mediaPlayer = mediaPlayer;
        this.forwarder = forwarder;
        this.listenersHolder = listenersHolder;
        this.bufferHeartbeatCallback = bufferHeartbeatCallback;
        this.loadTimeout = loadTimeout;
        this.heart = heart;
        this.delayedActionExecutor = delayedActionExecutor;
        this.buggyVideoDriverPreventer = buggyVideoDriverPreventer;
    }

    void initialise() {
        forwarder.bind(listenersHolder.getPreparedListeners(), this);
        forwarder.bind(listenersHolder.getBufferStateListeners(), listenersHolder.getErrorListeners());
        forwarder.bind(listenersHolder.getCompletionListeners(), listenersHolder.getStateChangedListeners());
        forwarder.bind(listenersHolder.getVideoSizeChangedListeners());
        forwarder.bind(listenersHolder.getInfoListeners());

        bufferHeartbeatCallback.bind(forwarder.onHeartbeatListener());

        heart.bind(new Heart.Heartbeat(listenersHolder.getHeartbeatCallbacks(), this));

        listenersHolder.addHeartbeatCallback(bufferHeartbeatCallback);
        listenersHolder.addPreparedListener(new PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();
                mediaPlayer.setOnSeekCompleteListener(seekToResettingSeekListener);
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

    private final MediaPlayer.OnSeekCompleteListener seekToResettingSeekListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            seekToPositionInMillis = NO_SEEK_TO_POSITION;

            if (seekingWithIntentToPlay || isPlaying()) {
                seekingWithIntentToPlay = false;
                play();
            }
        }
    };

    @Override
    public void setRepeating(boolean repeating) {
        mediaPlayer.setRepeating(repeating);
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume);
    }

    @Override
    public float getVolume() {
        return mediaPlayer.getVolume();
    }

    @Override
    public Listeners getListeners() {
        return listenersHolder;
    }

    @Override
    public void play() throws IllegalStateException {
        heart.startBeatingHeart();
        requestSurface(new SurfaceRequester.Callback() {
            @Override
            public void onSurfaceReady(Either<Surface, SurfaceHolder> surface) {
                mediaPlayer.start(surface);
                listenersHolder.getStateChangedListeners().onVideoPlaying();
            }
        });
    }

    @Override
    public void playAt(final long positionInMillis) throws IllegalStateException {
        if (playheadPositionInMillis() == positionInMillis) {
            play();
        } else {
            requestSurface(new SurfaceRequester.Callback() {
                @Override
                public void onSurfaceReady(Either<Surface, SurfaceHolder> surface) {
                    initialSeekWorkaround(surface, positionInMillis);
                }
            });
        }
    }

    /**
     * Workaround to fix some devices (nexus 7 2013 in particular) from natively crashing the mediaplayer
     * by starting the mediaplayer before seeking it.
     */
    private void initialSeekWorkaround(Either<Surface, SurfaceHolder> surface, final long initialPlayPositionInMillis) throws IllegalStateException {
        listenersHolder.getBufferStateListeners().onBufferStarted();
        initialisePlaybackForSeeking(surface);
        delayedActionExecutor.performAfterDelay(new DelayedActionExecutor.Action() {
            @Override
            public void perform() {
                seekWithIntentToPlay(initialPlayPositionInMillis);
            }
        }, INITIAL_PLAY_SEEK_DELAY_IN_MILLIS);
    }

    private void initialisePlaybackForSeeking(Either<Surface, SurfaceHolder> surface) {
        mediaPlayer.start(surface);
        mediaPlayer.pause();
    }

    private void requestSurface(SurfaceRequester.Callback callback) {
        if (surfaceRequester == null) {
            throw new IllegalStateException("Must attach a PlayerView before interacting with Player");
        }
        surfaceHolderRequesterCallbacks.add(callback);
        surfaceRequester.requestSurface(callback);
    }

    private void seekWithIntentToPlay(long positionInMillis) throws IllegalStateException {
        seekingWithIntentToPlay = true;
        seekTo(positionInMillis);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long positionInMillis) throws IllegalStateException {
        seekToPositionInMillis = positionInMillis;
        mediaPlayer.seekTo(positionInMillis);
    }

    @Override
    public void pause() throws IllegalStateException {
        mediaPlayer.pause();
        if (heart.isBeating()) {
            heart.stopBeatingHeart();
            heart.forceBeat();
        }
        listenersHolder.getStateChangedListeners().onVideoPaused();
    }

    @Override
    public void loadVideo(final Uri uri, final Options options) {
        if (mediaPlayer.hasPlayedContent()) {
            stop();
        }
        assertPlayerViewIsAttached();
        createSurfaceByShowingVideoContainer();
        listenersHolder.getBufferStateListeners().onBufferStarted();
        requestSurface(new SurfaceRequester.Callback() {
            @Override
            public void onSurfaceReady(Either<Surface, SurfaceHolder> surface) {
                mediaPlayer.prepareVideo(uri, surface);
            }
        });
    }

    private void createSurfaceByShowingVideoContainer() {
        containerView.setVisibility(View.VISIBLE);
    }

    private void assertPlayerViewIsAttached() {
        if (containerView == null) {
            throw new IllegalStateException("A PlayerView must be attached in order to loadVideo");
        }
    }

    @Override
    public void loadVideoWithTimeout(Uri uri, Options options, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadTimeout.start(timeout, loadTimeoutCallback);
        loadVideo(uri, options);
    }

    @Override
    public long playheadPositionInMillis() throws IllegalStateException {
        return isSeeking() ? seekToPositionInMillis : mediaPlayer.currentPositionInMillis();
    }

    private boolean isSeeking() {
        return seekToPositionInMillis != NO_SEEK_TO_POSITION;
    }

    @Override
    public long mediaDurationInMillis() throws IllegalStateException {
        return mediaPlayer.mediaDurationInMillis();
    }

    @Override
    public int bufferPercentage() throws IllegalStateException {
        return mediaPlayer.getBufferPercentage();
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
    public PlayerInformation getPlayerInformation() {
        return mediaPlayerInformation;
    }

    @Override
    public void attach(PlayerView playerView) {
        containerView = playerView.getContainerView();
        buggyVideoDriverPreventer.preventVideoDriverBug(this, containerView);
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
        PlayerSurfaceHolder playerSurfaceHolder = playerView.getPlayerSurfaceHolder();
        surfaceRequester = playerSurfaceHolder.getSurfaceRequester();
    }

    @Override
    public void detach(PlayerView playerView) {
        clearSurfaceHolderCallbacks();
        listenersHolder.removeStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.removeVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        buggyVideoDriverPreventer.clear(playerView.getContainerView());
        surfaceRequester = null;
        containerView = null;
    }

    private void clearSurfaceHolderCallbacks() {
        for (SurfaceRequester.Callback callback : surfaceHolderRequesterCallbacks) {
            surfaceRequester.removeCallback(callback);
        }
        surfaceHolderRequesterCallbacks.clear();
    }

    @Override
    public boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        return mediaPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public boolean clearAudioTrackSelection() throws IllegalStateException {
        return mediaPlayer.clearAudioTrackSelection();
    }

    @Override
    public boolean showSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        return mediaPlayer.selectSubtitleTrack(subtitleTrack);
    }

    @Override
    public boolean hideSubtitleTrack() throws IllegalStateException {
        return mediaPlayer.clearSubtitleTrack();
    }

    @Override
    public AudioTracks getAudioTracks() throws IllegalStateException {
        return mediaPlayer.getAudioTracks();
    }

    @Override
    public boolean selectVideoTrack(PlayerVideoTrack videoTrack) throws IllegalStateException {
        return mediaPlayer.selectVideoTrack(videoTrack);
    }

    @Override
    public Optional<PlayerVideoTrack> getSelectedVideoTrack() throws IllegalStateException {
        return mediaPlayer.getSelectedVideoTrack();
    }

    @Override
    public boolean clearVideoTrackSelection() throws IllegalStateException {
        return mediaPlayer.clearVideoTrackSelection();
    }

    @Override
    public List<PlayerVideoTrack> getVideoTracks() throws IllegalStateException {
        return mediaPlayer.getVideoTracks();
    }

    @Override
    public List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        return mediaPlayer.getSubtitleTracks();
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
        delayedActionExecutor.clearAllActions();
        listenersHolder.resetState();
        loadTimeout.cancel();
        heart.stopBeatingHeart();
        mediaPlayer.release();
        destroySurfaceByHidingVideoContainer();
    }

    private void destroySurfaceByHidingVideoContainer() {
        if (containerView != null) {
            containerView.setVisibility(View.GONE);
        }
    }
}
