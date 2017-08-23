package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Listeners;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.Timeout;
import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

import java.util.ArrayList;
import java.util.List;

class AndroidMediaPlayerImpl implements NoPlayer {

    private static final VideoPosition NO_SEEK_TO_POSITION = VideoPosition.INVALID;
    private static final int INITIAL_PLAY_SEEK_DELAY_IN_MILLIS = 500;

    private final List<SurfaceHolderRequester.Callback> surfaceHolderRequesterCallbacks = new ArrayList<>();

    private final MediaPlayerInformation mediaPlayerInformation;
    private final AndroidMediaPlayerFacade mediaPlayer;
    private final MediaPlayerForwarder forwarder;
    private final CheckBufferHeartbeatCallback bufferHeartbeatCallback;
    private final Handler handler;
    private final Heart heart;
    private final PlayerListenersHolder listenersHolder;
    private final LoadTimeout loadTimeout;
    private final BuggyVideoDriverPreventer buggyVideoDriverPreventer;

    private int videoWidth;
    private int videoHeight;
    private VideoPosition seekToPosition = NO_SEEK_TO_POSITION;

    private boolean seekingWithIntentToPlay;
    private SurfaceHolderRequester surfaceHolderRequester;
    private View containerView;

    AndroidMediaPlayerImpl(MediaPlayerInformation mediaPlayerInformation,
                           AndroidMediaPlayerFacade mediaPlayer,
                           MediaPlayerForwarder forwarder,
                           PlayerListenersHolder listenersHolder,
                           CheckBufferHeartbeatCallback bufferHeartbeatCallback,
                           LoadTimeout loadTimeout,
                           Heart heart,
                           Handler handler,
                           BuggyVideoDriverPreventer buggyVideoDriverPreventer) {
        this.mediaPlayerInformation = mediaPlayerInformation;
        this.mediaPlayer = mediaPlayer;
        this.forwarder = forwarder;
        this.listenersHolder = listenersHolder;
        this.bufferHeartbeatCallback = bufferHeartbeatCallback;
        this.loadTimeout = loadTimeout;
        this.heart = heart;
        this.handler = handler;
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
            seekToPosition = NO_SEEK_TO_POSITION;

            if (seekingWithIntentToPlay || isPlaying()) {
                seekingWithIntentToPlay = false;
                play();
            }
        }
    };

    @Override
    public void play() throws IllegalStateException {
        heart.startBeatingHeart();
        requestSurface(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                mediaPlayer.start(surfaceHolder);
                listenersHolder.getStateChangedListeners().onVideoPlaying();
            }
        });
    }

    @Override
    public void play(final VideoPosition position) throws IllegalStateException {
        if (getPlayheadPosition().equals(position)) {
            play();
        } else {
            requestSurface(new SurfaceHolderRequester.Callback() {
                @Override
                public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                    initialSeekWorkaround(surfaceHolder, position);
                }
            });
        }
    }

    /**
     * Workaround to fix some devices (nexus 7 2013 in particular) from natively crashing the mediaplayer
     * by starting the mediaplayer before seeking it.
     */
    private void initialSeekWorkaround(SurfaceHolder surfaceHolder, final VideoPosition initialPlayPosition) throws IllegalStateException {
        listenersHolder.getBufferStateListeners().onBufferStarted();
        initialisePlaybackForSeeking(surfaceHolder);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                seekWithIntentToPlay(initialPlayPosition);
            }
        }, INITIAL_PLAY_SEEK_DELAY_IN_MILLIS);
    }

    private void initialisePlaybackForSeeking(SurfaceHolder surfaceHolder) {
        mediaPlayer.start(surfaceHolder);
        mediaPlayer.pause();
    }

    private void requestSurface(SurfaceHolderRequester.Callback callback) {
        if (surfaceHolderRequester == null) {
            throw new IllegalStateException("Must attach a PlayerView before interacting with Player");
        }
        surfaceHolderRequesterCallbacks.add(callback);
        surfaceHolderRequester.requestSurfaceHolder(callback);
    }

    private void seekWithIntentToPlay(VideoPosition position) throws IllegalStateException {
        seekingWithIntentToPlay = true;
        seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(VideoPosition position) throws IllegalStateException {
        seekToPosition = position;
        mediaPlayer.seekTo(position.inImpreciseMillis());
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
    public void loadVideo(final Uri uri, ContentType contentType) {
        if (mediaPlayer.hasPlayedContent()) {
            stop();
        }
        assertPlayerViewIsAttached();
        createSurfaceByShowingVideoContainer();
        listenersHolder.getBufferStateListeners().onBufferStarted();
        requestSurface(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                mediaPlayer.prepareVideo(uri, surfaceHolder);
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
    public void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadTimeout.start(timeout, loadTimeoutCallback);
        loadVideo(uri, contentType);
    }

    @Override
    public VideoPosition getPlayheadPosition() throws IllegalStateException {
        return isSeeking() ? seekToPosition : VideoPosition.fromMillis(mediaPlayer.getCurrentPosition());
    }

    private boolean isSeeking() {
        return !seekToPosition.equals(NO_SEEK_TO_POSITION);
    }

    @Override
    public VideoDuration getMediaDuration() throws IllegalStateException {
        return VideoDuration.fromMillis(mediaPlayer.getDuration());
    }

    @Override
    public int getBufferPercentage() throws IllegalStateException {
        return mediaPlayer.getBufferPercentage();
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
    public PlayerInformation getPlayerInformation() {
        return mediaPlayerInformation;
    }

    @Override
    public void attach(PlayerView playerView) {
        containerView = playerView.getContainerView();
        buggyVideoDriverPreventer.preventVideoDriverBug(this, containerView);
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
        surfaceHolderRequester = playerView.getSurfaceHolderRequester();
    }

    @Override
    public void detach(PlayerView playerView) {
        clearSurfaceHolderCallbacks();
        listenersHolder.removeStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.removeVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        buggyVideoDriverPreventer.clear(playerView.getContainerView());
        surfaceHolderRequester = null;
        containerView = null;
    }

    private void clearSurfaceHolderCallbacks() {
        for (SurfaceHolderRequester.Callback callback : surfaceHolderRequesterCallbacks) {
            surfaceHolderRequester.removeCallback(callback);
        }
        surfaceHolderRequesterCallbacks.clear();
    }

    @Override
    public boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        return mediaPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public boolean showSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        return mediaPlayer.selectSubtitleTrack(subtitleTrack);
    }

    @Override
    public void hideSubtitleTrack() throws IllegalStateException {
        mediaPlayer.clearSubtitleTrack();
    }

    @Override
    public AudioTracks getAudioTracks() throws IllegalStateException {
        return mediaPlayer.getAudioTracks();
    }

    @Override
    public List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        return mediaPlayer.getSubtitleTracks();
    }

    @Override
    public Listeners getListeners() {
        return listenersHolder;
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
        mediaPlayer.release();
        destroySurfaceByHidingVideoContainer();
    }

    private void destroySurfaceByHidingVideoContainer() {
        if (containerView != null) {
            containerView.setVisibility(View.GONE);
        }
    }
}
