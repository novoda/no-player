package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;

import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;

public class AndroidMediaPlayerImplCreator {
    public AndroidMediaPlayerImpl create(Context context) {
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        MediaPlayerForwarder forwarder = new MediaPlayerForwarder();
        AndroidMediaPlayerFacade facade = AndroidMediaPlayerFacade.newInstance(context, forwarder);
        PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
        CheckBufferHeartbeatCallback bufferHeartbeatCallback = new CheckBufferHeartbeatCallback();
        Heart heart = Heart.newInstance();
        BuggyVideoDriverPreventer buggyVideoDriverPreventer = BuggyVideoDriverPreventer.newInstance();
        Handler handler = new Handler(Looper.getMainLooper());

        return create(loadTimeout, forwarder, facade, listenersHolder, bufferHeartbeatCallback, heart, buggyVideoDriverPreventer, handler);
    }

    @VisibleForTesting
    AndroidMediaPlayerImpl create(LoadTimeout loadTimeout,
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

    private void bindListeners(final LoadTimeout loadTimeout,
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
        final MediaPlayer.OnSeekCompleteListener onSeekCompletedListener = new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mediaPlayer.resetSeek();
            }
        };
        listenersHolder.addPreparedListener(new Player.PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                loadTimeout.cancel();
                facade.setOnSeekCompleteListener(onSeekCompletedListener);
            }
        });
        listenersHolder.addErrorListener(new Player.ErrorListener() {
            @Override
            public void onError(Player player, Player.PlayerError error) {
                loadTimeout.cancel();
            }
        });
        listenersHolder.addVideoSizeChangedListener(new Player.VideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                mediaPlayer.updateVideoSize(width, height);
            }
        });
    }
}
