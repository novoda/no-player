package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.internal.mediaplayer.CheckBufferHeartbeatCallback;

public class MediaPlayerForwarder {

    private final MediaPlayerPreparedListener preparedListener;
    private final HeartBeatListener heartBeatListener;
    private final MediaPlayerCompletionListener completionListener;
    private final MediaPlayerErrorListener errorListener;
    private final VideoSizeChangedListener videoSizeChangedListener;

    public MediaPlayerForwarder() {
        preparedListener = new MediaPlayerPreparedListener();
        heartBeatListener = new HeartBeatListener();
        completionListener = new MediaPlayerCompletionListener();
        errorListener = new MediaPlayerErrorListener();
        videoSizeChangedListener = new VideoSizeChangedListener();
    }

    public void bind(NoPlayer.PreparedListener preparedListener, PlayerState playerState) {
        this.preparedListener.add(new OnPreparedForwarder(preparedListener, playerState));
    }

    public void bind(NoPlayer.BufferStateListener bufferStateListener, NoPlayer.ErrorListener errorListener) {
        preparedListener.add(new BufferOnPreparedListener(bufferStateListener));
        heartBeatListener.add(new BufferHeartbeatListener(bufferStateListener));
        this.errorListener.add(new ErrorForwarder(bufferStateListener, errorListener));
    }

    public void bind(NoPlayer.CompletionListener completionListener, NoPlayer.StateChangedListener stateChangedListener) {
        this.completionListener.add(new CompletionForwarder(completionListener));
        this.completionListener.add(new CompletionStateChangedForwarder(stateChangedListener));
    }

    public void bind(NoPlayer.VideoSizeChangedListener videoSizeChangedListener) {
        this.videoSizeChangedListener.add(new VideoSizeChangedForwarder(videoSizeChangedListener));
    }

    public void bind(NoPlayer.InfoListener infoListener) {
        preparedListener.add(new OnPreparedInfoForwarder(infoListener));
        heartBeatListener.add(new BufferInfoForwarder(infoListener));
        completionListener.add(new CompletionInfoForwarder(infoListener));
        errorListener.add(new ErrorInfoForwarder(infoListener));
        videoSizeChangedListener.add(new VideoSizeChangedInfoForwarder(infoListener));
    }

    public MediaPlayer.OnPreparedListener onPreparedListener() {
        return preparedListener;
    }

    public CheckBufferHeartbeatCallback.BufferListener onHeartbeatListener() {
        return heartBeatListener;
    }

    public MediaPlayer.OnCompletionListener onCompletionListener() {
        return completionListener;
    }

    public MediaPlayer.OnErrorListener onErrorListener() {
        return errorListener;
    }

    public MediaPlayer.OnVideoSizeChangedListener onSizeChangedListener() {
        return videoSizeChangedListener;
    }
}
