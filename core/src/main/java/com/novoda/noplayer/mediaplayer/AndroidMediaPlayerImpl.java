package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.Listeners;
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
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.player.PlayerInformation;

import java.util.List;

public final class AndroidMediaPlayerImpl implements Player {

    private static final VideoPosition NO_SEEK_TO_POSITION = VideoPosition.INVALID;
    private static final MediaPlayerInformation MEDIA_PLAYER_INFORMATION = new MediaPlayerInformation();
    private static final int INITIAL_PLAY_SEEK_DELAY_IN_MILLIS = 500;

    private final AndroidMediaPlayerFacade mediaPlayer;

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

    public static AndroidMediaPlayerImpl newInstance(Context context) {
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        MediaPlayerForwarder forwarder = new MediaPlayerForwarder();
        AndroidMediaPlayerFacade facade = AndroidMediaPlayerFacade.newInstance(context, forwarder);
        PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
        CheckBufferHeartbeatCallback bufferHeartbeatCallback = new CheckBufferHeartbeatCallback();
        Heart heart = Heart.newInstance();
        BuggyVideoDriverPreventer buggyVideoDriverPreventer = BuggyVideoDriverPreventer.newInstance();
        Handler handler = new Handler(Looper.getMainLooper());

        return newInstance(loadTimeout, forwarder, facade, listenersHolder, bufferHeartbeatCallback, heart, buggyVideoDriverPreventer, handler);
    }

    static AndroidMediaPlayerImpl newInstance(LoadTimeout loadTimeout,
                                              MediaPlayerForwarder forwarder,
                                              AndroidMediaPlayerFacade facade,
                                              PlayerListenersHolder listenersHolder,
                                              CheckBufferHeartbeatCallback bufferHeartbeatCallback,
                                              Heart heart,
                                              BuggyVideoDriverPreventer preventer,
                                              Handler handler) {
        AndroidMediaPlayerImpl mediaPlayer = new AndroidMediaPlayerImpl(facade, listenersHolder, loadTimeout, heart, handler, preventer);
        bindListeners(loadTimeout, forwarder, facade, listenersHolder, bufferHeartbeatCallback, heart, mediaPlayer);

        return mediaPlayer;
    }

    private static void bindListeners(final LoadTimeout loadTimeout,
                                      MediaPlayerForwarder forwarder,
                                      final AndroidMediaPlayerFacade facade,
                                      PlayerListenersHolder listenersHolder,
                                      CheckBufferHeartbeatCallback bufferHeartbeatCallback,
                                      Heart heart,
                                      final AndroidMediaPlayerImpl mediaPlayer) {
        forwarder.bind(listenersHolder.getPreparedListeners(), mediaPlayer);
        forwarder.bind(listenersHolder.getBufferStateListeners(), listenersHolder.getErrorListeners(), mediaPlayer);
        forwarder.bind(listenersHolder.getCompletionListeners(), listenersHolder.getStateChangedListeners());
        forwarder.bind(listenersHolder.getVideoSizeChangedListeners());
        forwarder.bind(listenersHolder.getInfoListeners());
        bufferHeartbeatCallback.bind(forwarder.onHeartbeatListener());
        heart.bind(new Heart.Heartbeat<>(listenersHolder.getHeartbeatCallbacks(), mediaPlayer));
        listenersHolder.addHeartbeatCallback(bufferHeartbeatCallback);
        listenersHolder.addPreparedListener(new PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();
                facade.setOnSeekCompleteListener(mediaPlayer.seekToResettingSeekListener);
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
                mediaPlayer.videoWidth = width;
                mediaPlayer.videoHeight = height;
            }
        });
    }

    AndroidMediaPlayerImpl(AndroidMediaPlayerFacade mediaPlayer,
                           PlayerListenersHolder listenersHolder,
                           LoadTimeout loadTimeoutParam,
                           Heart heart,
                           Handler handler,
                           BuggyVideoDriverPreventer buggyVideoDriverPreventer) {
        this.mediaPlayer = mediaPlayer;
        this.listenersHolder = listenersHolder;
        this.loadTimeout = loadTimeoutParam;
        this.heart = heart;
        this.handler = handler;
        this.buggyVideoDriverPreventer = buggyVideoDriverPreventer;
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
    public void play() {
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
    public void play(final VideoPosition position) {
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
    private void initialSeekWorkaround(SurfaceHolder surfaceHolder, final VideoPosition initialPlayPosition) {
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
        surfaceHolderRequester.requestSurfaceHolder(callback);
    }

    private void seekWithIntentToPlay(VideoPosition position) {
        seekingWithIntentToPlay = true;
        seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(VideoPosition position) {
        seekToPosition = position;
        mediaPlayer.seekTo(position.inImpreciseMillis());
    }

    @Override
    public void pause() {
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
            reset();
        }
        listenersHolder.getBufferStateListeners().onBufferStarted();
        requestSurface(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                mediaPlayer.prepareVideo(uri, surfaceHolder);
            }
        });
    }

    @Override
    public void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadTimeout.start(timeout, loadTimeoutCallback);
        loadVideo(uri, contentType);
    }

    @Override
    public VideoPosition getPlayheadPosition() {
        return isSeeking() ? seekToPosition : VideoPosition.fromMillis(mediaPlayer.getCurrentPosition());
    }

    private boolean isSeeking() {
        return !seekToPosition.equals(NO_SEEK_TO_POSITION);
    }

    @Override
    public VideoDuration getMediaDuration() {
        return VideoDuration.fromMillis(mediaPlayer.getDuration());
    }

    @Override
    public int getBufferPercentage() {
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
        return MEDIA_PLAYER_INFORMATION;
    }

    @Override
    public void attach(PlayerView playerView) {
        surfaceHolderRequester = playerView.getSurfaceHolderRequester();
        buggyVideoDriverPreventer.preventVideoDriverBug(this, playerView.getContainerView());
        listenersHolder.addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        listenersHolder.addStateChangedListener(playerView.getStateChangedListener());
    }

    @Override
    public void detach(PlayerView playerView) {
        surfaceHolderRequester = null;
        listenersHolder.removeStateChangedListener(playerView.getStateChangedListener());
        listenersHolder.removeVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        buggyVideoDriverPreventer.clear(playerView.getContainerView());
    }

    @Override
    public void selectAudioTrack(PlayerAudioTrack audioTrack) {
        mediaPlayer.selectAudioTrack(audioTrack);
    }

    @Override
    public List<PlayerAudioTrack> getAudioTracks() {
        return mediaPlayer.getAudioTracks();
    }

    @Override
    public Listeners getListeners() {
        return listenersHolder;
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
        loadTimeout.cancel();
        heart.stopBeatingHeart();
        mediaPlayer.release();
        listenersHolder.getStateChangedListeners().onVideoStopped();
    }
}
