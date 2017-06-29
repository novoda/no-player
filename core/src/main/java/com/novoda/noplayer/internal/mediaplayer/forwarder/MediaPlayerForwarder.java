package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;
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

    public void bind(Player.PreparedListener preparedListener, PlayerState playerState) {
        this.preparedListener.add(new OnPreparedForwarder(preparedListener, playerState));
    }

    public void bind(Player.BufferStateListener bufferStateListener, Player.ErrorListener errorListener, Player player) {
        preparedListener.add(new BufferOnPreparedListener(bufferStateListener));
        heartBeatListener.add(new BufferHeartbeatListener(bufferStateListener));
        this.errorListener.add(new ErrorForwarder(bufferStateListener, errorListener, player));
    }

    public void bind(Player.CompletionListener completionListener, Player.StateChangedListener stateChangedListener) {
        this.completionListener.add(new CompletionForwarder(completionListener));
        this.completionListener.add(new CompletionStateChangedForwarder(stateChangedListener));
    }

    public void bind(Player.VideoSizeChangedListener videoSizeChangedListener) {
        this.videoSizeChangedListener.add(new VideoSizeChangedForwarder(videoSizeChangedListener));
    }

    public void bind(Player.InfoListener infoListener) {
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
