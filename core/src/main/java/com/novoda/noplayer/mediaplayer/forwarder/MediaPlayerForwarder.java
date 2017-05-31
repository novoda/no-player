package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;
import com.novoda.noplayer.mediaplayer.CheckBufferHeartbeatCallback;

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

    public void bind(PreparedListeners preparedListeners, final PlayerState playerState) {
        preparedListener.add(new OnPreparedForwarder(preparedListeners, playerState));
    }

    public void bind(BufferStateListeners bufferStateListeners, ErrorListeners errorListeners, Player player) {
        preparedListener.add(new BufferOnPreparedListener(bufferStateListeners));
        heartBeatListener.add(new BufferHeartbeatListener(bufferStateListeners));
        errorListener.add(new ErrorForwarder(bufferStateListeners, errorListeners, player));
    }

    public void bind(CompletionListeners completionListeners) {
        completionListener.add(new CompletionForwarder(completionListeners));
    }

    public void bind(VideoSizeChangedListeners videoSizeChangedListeners) {
        videoSizeChangedListener.add(new VideoSizeChangedForwarder(videoSizeChangedListeners));
    }

    public void bind(final InfoListeners infoListeners) {
        preparedListener.add(new OnPreparedInfoForwarder(infoListeners));
        heartBeatListener.add(new BufferInfoForwarder(infoListeners));
        completionListener.add(new CompletionInfoForwarder(infoListeners));
        errorListener.add(new ErrorInfoForwarder(infoListeners));
        videoSizeChangedListener.add(new VideoSizeChangedInfoForwarder(infoListeners));
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
