package com.novoda.noplayer.internal.mediaplayer;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.SystemClock;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.model.LoadTimeout;

import java.util.HashMap;

public class NoPlayerMediaPlayerCreator {

    private final InternalCreator internalCreator;

    public static NoPlayerMediaPlayerCreator newInstance(Handler handler) {
        InternalCreator internalCreator = new InternalCreator(handler);
        return new NoPlayerMediaPlayerCreator(internalCreator);
    }

    NoPlayerMediaPlayerCreator(InternalCreator internalCreator) {
        this.internalCreator = internalCreator;
    }

    public NoPlayer createMediaPlayer(Context context) {
        AndroidMediaPlayerImpl player = internalCreator.create(context);
        player.initialise();
        return player;
    }

    static class InternalCreator {

        private final Handler handler;

        InternalCreator(Handler handler) {
            this.handler = handler;
        }

        public AndroidMediaPlayerImpl create(Context context) {
            LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), handler);
            MediaPlayerForwarder forwarder = new MediaPlayerForwarder();
            AndroidMediaPlayerFacade facade = AndroidMediaPlayerFacade.newInstance(context, forwarder);
            PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
            CheckBufferHeartbeatCallback bufferHeartbeatCallback = new CheckBufferHeartbeatCallback();
            Heart heart = Heart.newInstance(handler);
            MediaPlayerTypeReader mediaPlayerTypeReader = new MediaPlayerTypeReader(new SystemProperties(), Build.VERSION.SDK_INT);
            DelayedActionExecutor delayedActionExecutor = new DelayedActionExecutor(handler, new HashMap<DelayedActionExecutor.Action, Runnable>());
            BuggyVideoDriverPreventer preventer = new BuggyVideoDriverPreventer(mediaPlayerTypeReader);
            MediaPlayerInformation mediaPlayerInformation = new MediaPlayerInformation(mediaPlayerTypeReader);
            return new AndroidMediaPlayerImpl(
                    mediaPlayerInformation,
                    facade,
                    forwarder,
                    listenersHolder,
                    bufferHeartbeatCallback,
                    loadTimeout,
                    heart,
                    delayedActionExecutor,
                    preventer
            );
        }
    }
}
