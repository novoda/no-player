package com.novoda.noplayer.mediaplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;

public class AndroidMediaPlayerImplFactory {

    public AndroidMediaPlayerImpl create(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
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
