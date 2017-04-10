package com.novoda.noplayer.exoplayer;

import android.media.MediaCodec.CryptoException;
import android.net.Uri;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer.DecoderInitializationException;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.metadata.MetadataTrackRenderer.MetadataRenderer;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.SubtitleLayout;
import com.google.android.exoplayer.text.TextRenderer;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExoPlayerFacade implements ChunkSampleSource.EventListener,
        MediaCodecVideoTrackRenderer.EventListener,
        MediaCodecAudioTrackRenderer.EventListener,
        StreamingDrmSessionManager.EventListener,
        DashChunkSource.EventListener,
        MetadataRenderer<Map<String, Object>>,
        TextRenderer {

    private static final int MIN_BUFFER_MS = 1000;
    private static final int MIN_REBUFFER_MS = 5000;

    private final RendererState rendererState;

    private int videoWidth;
    private int videoHeight;
    private RendererCreator rendererCreator;
    private SubtitleLayout subtitleLayout;

    public interface Listener {

        void onStateChanged(boolean playWhenReady, int playbackState);

        void onError(Exception e);

        void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                float pixelWidthHeightRatio);
    }

    private final RendererFactory rendererFactory;

    private final CopyOnWriteArrayList<Listener> listeners;
    private ExoPlayer player;

    private PlayerStateNotifier playerStateNotifier;
    private TrackRenderer videoRenderer;

    private InternalErrorListener internalErrorListener;

    private InfoListener infoListener;
    private SurfaceHolderRequester surfaceHolderRequester;

    public ExoPlayerFacade(RendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
        listeners = new CopyOnWriteArrayList<>();
        rendererState = new RendererState();
    }

    private final ExoPlayer.Listener exoPlayerListener = new ExoPlayer.Listener() {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int state) {
            playerStateNotifier.maybeReportPlayerState(listeners);
        }

        @Override
        public void onPlayerError(ExoPlaybackException exception) {
            rendererState.setIdle();
            for (Listener listener : listeners) {
                listener.onError(exception);
            }
        }

        @Override
        public void onPlayWhenReadyCommitted() {
            // Do nothing.
        }
    };

    public void setSurfaceHolderRequester(SurfaceHolderRequester surfaceHolderRequester) {
        this.surfaceHolderRequester = surfaceHolderRequester;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void setInternalErrorListener(InternalErrorListener listener) {
        internalErrorListener = listener;
    }

    public void setInfoListener(InfoListener listener) {
        infoListener = listener;
    }

    public void prepare(Uri contentUri, ContentType contentType) {
        if (hasPlayer()) {
            release();
        }

        videoRenderer = null;
        rendererState.setBuilding();
        rendererCreator = rendererFactory.createRenderer(contentType);
        initialise(rendererCreator.getRendererCount());

        rendererCreator.create(contentUri, this, rendererCallback);
    }

    private final RendererCreator.Callback rendererCallback = new RendererCreator.Callback() {
        @Override
        public void onCreated(Renderers renderers, BandwidthMeter bandwidthMeter) {
            ExoPlayerFacade.this.videoRenderer = renderers.getVideoRenderer();
            pushSurface();
            player.prepare(renderers.asArray());
            rendererState.setBuilt();
        }

        @Override
        public void onError(Exception e) {
            if (internalErrorListener != null) {
                internalErrorListener.onRendererInitializationError(e);
            }
            for (Listener listener : listeners) {
                listener.onError(e);
            }
            rendererState.setIdle();
            playerStateNotifier.maybeReportPlayerState(listeners);
        }
    };

    private void initialise(int rendererCount) {
        player = ExoPlayer.Factory.newInstance(rendererCount, MIN_BUFFER_MS, MIN_REBUFFER_MS);
        player.addListener(exoPlayerListener);
        playerStateNotifier = new PlayerStateNotifier(player, rendererState);
        playerStateNotifier.maybeReportPlayerState(listeners);
    }

    private void pushSurface() {
        if (surfaceHolderRequester == null) {
            Log.w(String.format("Attempt to %s has been ignored because the Player has not been attached to a PlayerView", "pushSurface()"));
            return;
        }
        surfaceHolderRequester.requestSurfaceHolder(new SurfaceHolderRequester.Callback() {
            @Override
            public void onSurfaceHolderReady(SurfaceHolder surfaceHolder) {
                surfaceHolder.removeCallback(clearingSurfaceCallback);
                surfaceHolder.addCallback(clearingSurfaceCallback);

                if (videoRenderer == null) {
                    return;
                }
                Surface surface = surfaceHolder.getSurface();
                player.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
            }
        });
    }

    private final SurfaceHolder.Callback clearingSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // do nothing
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // do nothing
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            holder.removeCallback(this);
            clearSurface();
        }
    };

    public void setPlayWhenReady(final boolean playWhenReady) {
        if (hasPlayer()) {
            if (playWhenReady) {
                pushSurface();
            }
            player.setPlayWhenReady(playWhenReady);
        }
    }

    public void seekTo(long positionMs) {
        if (hasPlayer()) {
            player.seekTo(positionMs);
        }
    }

    public void release() {
        if (rendererState.isBuilding()) {
            rendererCreator.release();
        }

        rendererState.setIdle();
        if (hasPlayer()) {
            clearSurface();
            player.release();
        }
    }

    private void clearSurface() {
        if (videoRenderer == null) {
            return;
        }
        player.blockingSendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, null);
    }

    public long getCurrentPosition() {
        if (hasPlayer()) {
            return player.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public long getDuration() {
        if (hasPlayer()) {
            return player.getDuration();
        } else {
            return 0;
        }
    }

    public int getBufferedPercentage() {
        if (hasPlayer()) {
            return player.getBufferedPercentage();
        } else {
            return 0;
        }
    }

    public Looper getPlaybackLooper() {
        if (hasPlayer()) {
            return player.getPlaybackLooper();
        } else {
            throw new DeveloperError("We're not ready yet, fix me");
        }
    }

    public boolean isPlaying() {
        if (hasPlayer()) {
            return player.getPlayWhenReady();
        } else {
            return false;
        }
    }

    private boolean hasPlayer() {
        return player != null;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio) {
        videoWidth = width;
        videoHeight = height;

        for (Listener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        if (infoListener == null) {
            return;
        }
        infoListener.onDroppedFrames(count, elapsed);
    }

    @Override
    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger,
                                          long mediaTimeMs) {
        if (infoListener == null) {
            return;
        }
        infoListener.onDownstreamFormatChanged(sourceId, format, trigger, mediaTimeMs);
    }

    @Override
    public void onDrmKeysLoaded() {
        // Do nothing.
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        if (internalErrorListener == null) {
            return;
        }
        internalErrorListener.onDrmSessionManagerError(e);
    }

    @Override
    public void onDecoderInitializationError(DecoderInitializationException e) {
        if (internalErrorListener == null) {
            return;
        }
        internalErrorListener.onDecoderInitializationError(e);
    }

    @Override
    public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {
        if (internalErrorListener == null) {
            return;
        }
        internalErrorListener.onAudioTrackInitializationError(e);
    }

    @Override
    public void onAudioTrackWriteError(AudioTrack.WriteException e) {
        if (internalErrorListener == null) {
            return;
        }
        internalErrorListener.onAudioTrackWriteError(e);
    }

    @Override
    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        if (infoListener == null) {
            return;
        }
        infoListener.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
    }

    @Override
    public void onAvailableRangeChanged(int sourceId, TimeRange availableRange) {
        if (infoListener == null) {
            return;
        }
        infoListener.onAvailableRangeChanged(sourceId, availableRange);
    }

    @Override
    public void onCryptoError(CryptoException e) {
        if (internalErrorListener == null) {
            return;
        }
        internalErrorListener.onCryptoError(e);
    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        if (infoListener == null) {
            return;
        }
        infoListener.onDecoderInitialized(decoderName, elapsedRealtimeMs, initializationDurationMs);
    }

    @Override
    public void onLoadError(int sourceId, IOException e) {
        if (internalErrorListener == null) {
            return;
        }
        internalErrorListener.onLoadError(sourceId, e);
    }

    @Override
    public void onMetadata(Map<String, Object> metadata) {
        // do nothing
    }

    @Override
    public void onDrawnToSurface(Surface surface) {
        // Do nothing.
    }

    @Override
    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
        if (infoListener == null) {
            return;
        }
        infoListener.onLoadStarted(sourceId, length, type, trigger, format, mediaStartTimeMs, mediaEndTimeMs);
    }

    @Override
    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format,
                                long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
        if (infoListener == null) {
            return;
        }
        infoListener.onLoadCompleted(
                sourceId,
                bytesLoaded,
                type,
                trigger,
                format,
                mediaStartTimeMs,
                mediaEndTimeMs,
                elapsedRealtimeMs,
                loadDurationMs
        );
    }

    @Override
    public void onLoadCanceled(int sourceId, long bytesLoaded) {
        // Do nothing.
    }

    @Override
    public void onUpstreamDiscarded(int sourceId, long mediaStartTimeMs, long mediaEndTimeMs) {
        // Do nothing.
    }

    public void selectAudioTrack(int audioTrackIndex) {
        if (player == null) {
            throw new NullPointerException("You can only call selectAudioTrack() when video is prepared.");
        }

        int trackCount = player.getTrackCount(Renderers.AUDIO_RENDERER_ID);

        if (audioTrackIndex < 0 || audioTrackIndex > trackCount - 1) {
            Log.e(String.format(
                    "Attempt to %s has been ignored because an invalid position was specified: %s, total: %s",
                    "selectAudioTrack()",
                    audioTrackIndex,
                    trackCount
                  )
            );
            return;
        }

        player.setSelectedTrack(Renderers.AUDIO_RENDERER_ID, audioTrackIndex);
    }

    public List<PlayerAudioTrack> getAudioTracks() {
        if (player == null) {
            throw new NullPointerException("You can only call getAudioTracks() when video is prepared.");
        }

        List<PlayerAudioTrack> tracks = new ArrayList<>();
        for (int i = 0; i < player.getTrackCount(Renderers.AUDIO_RENDERER_ID); i++) {
            MediaFormat track = player.getTrackFormat(Renderers.AUDIO_RENDERER_ID, i);
            PlayerAudioTrack playerAudioTrack = new PlayerAudioTrack(
                    track.trackId,
                    track.language,
                    track.mimeType,
                    track.channelCount,
                    track.bitrate
            );
            tracks.add(playerAudioTrack);
        }
        return tracks;
    }

    public void setSubtitleLayout(SubtitleLayout subtitleLayout) {
        this.subtitleLayout = subtitleLayout;
    }

    @Override
    public void onCues(List<Cue> cues) {
        if (subtitleLayout == null) {
            return;
        }
        subtitleLayout.setCues(cues);
    }
}
