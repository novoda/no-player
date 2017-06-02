package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoContainer;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.player.PlayerInformation;
import com.novoda.notils.logger.simple.Log;

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

    private int videoWidth;
    private int videoHeight;

    private VideoPosition seekToPosition = NO_SEEK_TO_POSITION;
    private boolean seekingWithIntentToPlay;

    private VideoContainer videoContainer = VideoContainer.empty();
    private VideoSizeChangedListener videoSizeChangedListener;
    private StateChangedListener stateChangedListener;
    private CheckBufferHeartbeatCallback heartbeatCallback;

    public static AndroidMediaPlayerImpl newInstance(Context context) {
        AndroidMediaPlayerFacade androidMediaPlayer = AndroidMediaPlayerFacade.newInstance(context);
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        Heart heart = Heart.newInstance();
        PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
        return new AndroidMediaPlayerImpl(androidMediaPlayer, listenersHolder, new MediaPlayerForwarder(), loadTimeout, heart);
    }

    AndroidMediaPlayerImpl(final AndroidMediaPlayerFacade mediaPlayer,
                           PlayerListenersHolder listenersHolder,
                           MediaPlayerForwarder forwarder,
                           LoadTimeout loadTimeoutParam,
                           Heart heart) {
        this.mediaPlayer = mediaPlayer;
        this.listenersHolder = listenersHolder;
        this.loadTimeout = loadTimeoutParam;
        this.heart = heart;
        heart.bind(new Heart.Heartbeat<>(listenersHolder.getHeartbeatCallbacks(), this));
        handler = new Handler(Looper.getMainLooper());

        forwarder.bind(listenersHolder.getPreparedListeners(), this);
        forwarder.bind(listenersHolder.getBufferStateListeners(), listenersHolder.getErrorListeners(), this);
        forwarder.bind(listenersHolder.getCompletionListeners());
        forwarder.bind(listenersHolder.getVideoSizeChangedListeners());
        forwarder.bind(listenersHolder.getInfoListeners());

        mediaPlayer.setOnPreparedListener(forwarder.onPreparedListener());
        listenersHolder.addHeartbeatCallback(new CheckBufferHeartbeatCallback(forwarder.onHeartbeatListener()));
        mediaPlayer.setOnCompletionListener(forwarder.onCompletionListener());
        mediaPlayer.setOnErrorListener(forwarder.onErrorListener());
        mediaPlayer.setOnSizeChangedListener(forwarder.onSizeChangedListener());

        listenersHolder.addPreparedListener(new PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();
                mediaPlayer.setOnSeekCompleteListener(seekToResettingSeekListener);
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
        videoContainer.show();
        heart.startBeatingHeart();
        mediaPlayer.start();
        listenersHolder.getStateChangedListeners().onVideoPlaying();
    }

    @Override
    public void play(VideoPosition position) {
        if (getPlayheadPosition().equals(position)) {
            play();
        } else {
            initialSeekWorkaround(position);
        }
    }

    /**
     * Workaround to fix some devices (nexus 7 2013 in particular) from natively crashing the mediaplayer
     * by starting the mediaplayer before seeking it.
     *
     * @param initialPlayPosition
     */
    private void initialSeekWorkaround(final VideoPosition initialPlayPosition) {
        videoContainer.show();
        listenersHolder.getBufferStateListeners().onBufferStarted();
        initialisePlaybackForSeeking();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                seekWithIntentToPlay(initialPlayPosition);
            }
        }, INITIAL_PLAY_SEEK_DELAY_IN_MILLIS);
    }

    private void initialisePlaybackForSeeking() {
        mediaPlayer.start();
        mediaPlayer.pause();
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
    public void loadVideo(Uri uri, ContentType contentType) {
        videoContainer.show();
        listenersHolder.getBufferStateListeners().onBufferStarted();
        mediaPlayer.prepareVideo(uri);
    }

    @Override
    public void loadVideoWithTimeout(Uri uri, ContentType contentType, Timeout timeout, LoadTimeoutCallback loadTimeoutCallback) {
        loadTimeout.start(timeout, loadTimeoutCallback);
        loadVideo(uri, contentType);
    }

    @Override
    public VideoPosition getPlayheadPosition() {
        try {
            return isSeeking() ? seekToPosition : VideoPosition.fromMillis(mediaPlayer.getCurrentPosition());
        } catch (IllegalStateException e) {
            Log.e(e, "Cannot get current position:");
            return VideoPosition.INVALID;
        }
    }

    @Override
    public VideoDuration getMediaDuration() {
        try {
            return VideoDuration.fromMillis(mediaPlayer.getDuration());
        } catch (IllegalStateException e) {
            Log.e(e, "Cannot get duration: ");
            return VideoDuration.INVALID;
        }
    }

    @Override
    public int getBufferPercentage() {
        return mediaPlayer.getBufferPercentage();
    }

    private boolean isSeeking() {
        return !seekToPosition.equals(NO_SEEK_TO_POSITION);
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
        videoContainer = VideoContainer.with(playerView.getContainerView());
        mediaPlayer.setSurfaceHolderRequester(playerView.getSurfaceHolderRequester());
        BuggyVideoDriverPreventer.newInstance(playerView.getContainerView(), this).preventVideoDriverBug();
        videoSizeChangedListener = playerView.getVideoSizeChangedListener();
        listenersHolder.addVideoSizeChangedListener(videoSizeChangedListener);
        stateChangedListener = playerView.getStateChangedListener();
        listenersHolder.getStateChangedListeners().add(stateChangedListener);
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
    public PlayerListenersHolder getListenersHolder() {
        return listenersHolder;
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void reset() {
        release();
        videoContainer.show();
    }

    @Override
    public void release() {
        loadTimeout.cancel();
        heart.stopBeatingHeart();
        listenersHolder.getPlayerReleaseListener().onPlayerPreRelease(this);
        mediaPlayer.release();
        listenersHolder.getStateChangedListeners().onVideoReleased();
        listenersHolder.removeVideoSizeChangedListener(videoSizeChangedListener);
        listenersHolder.removeStateChangedListener(stateChangedListener);
        listenersHolder.removeHeartbeatCallback(heartbeatCallback);
        videoSizeChangedListener = null;
        stateChangedListener = null;
        heartbeatCallback = null;
        videoContainer.hide();
    }
}
