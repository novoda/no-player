package com.novoda.noplayer.internal.mediaplayer;

import android.content.Context;
import android.os.Handler;

import com.novoda.noplayer.Heart;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.internal.mediaplayer.forwarder.MediaPlayerForwarder;

public class NoPlayerMediaPlayerCreator {

    private final InternalCreator internalCreator;

    public static NoPlayerMediaPlayerCreator newInstance(Handler handler) {
        InternalCreator internalCreator = new InternalCreator(handler);
        return new NoPlayerMediaPlayerCreator(internalCreator);
    }

    NoPlayerMediaPlayerCreator(InternalCreator internalCreator) {
        this.internalCreator = internalCreator;
    }

    public Player createMediaPlayer(Context context) {
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
            BuggyVideoDriverPreventer preventer = BuggyVideoDriverPreventer.newInstance();

            return new AndroidMediaPlayerImpl(facade, forwarder, listenersHolder, bufferHeartbeatCallback, loadTimeout, heart, handler, preventer);
        }
    }
}
