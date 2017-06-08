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
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        MediaPlayerForwarder forwarder = new MediaPlayerForwarder();
        AndroidMediaPlayerFacade facade = AndroidMediaPlayerFacade.newInstance(context, forwarder);
        PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
        CheckBufferHeartbeatCallback bufferHeartbeatCallback = new CheckBufferHeartbeatCallback();
        Heart heart = Heart.newInstance();
        BuggyVideoDriverPreventer preventer = BuggyVideoDriverPreventer.newInstance();
        Handler handler = new Handler(Looper.getMainLooper());

        return new AndroidMediaPlayerImpl(facade, forwarder, listenersHolder, bufferHeartbeatCallback, loadTimeout, heart, handler, preventer);
    }

}
