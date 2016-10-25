package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.hls.DefaultHlsTrackSelector;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;

import java.io.IOException;

public class HlsRendererCreator implements RendererCreator {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENTS = 256;

    private static final int ALLOWED_JOINING_TIME_MS = 5000;
    private static final int MAX_DROPPED_FRAME_COUNT_TO_NOTIFY = 50;
    private static final boolean PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = true;
    private static final DrmSessionManager NO_DRM_SESSION_MANAGER = null;
    private static final boolean IS_MASTER = true;

    private final Context context;
    private final String userAgent;
    private final Handler mainHandler;
    private AsyncRendererBuilder currentAsyncBuilder;

    public HlsRendererCreator(Context context, String userAgent, Handler mainHandler) {
        this.context = context;
        this.userAgent = userAgent;
        this.mainHandler = mainHandler;
    }

    @Override
    public void create(Uri contentUri, ExoPlayerFacade player, Callback callback) {
        currentAsyncBuilder = new AsyncRendererBuilder(context, userAgent, contentUri.toString(), player, callback, mainHandler);
        currentAsyncBuilder.init();
    }

    @Override
    public int getRendererCount() {
        return Renderers.VIDEO_AUDIO_RENDERERS_SIZE;
    }

    @Override
    public void release() {
        if (currentAsyncBuilder == null) {
            throw new EagerReleaseException();
        }
        currentAsyncBuilder.cancel();
    }

    private static final class AsyncRendererBuilder implements ManifestFetcher.ManifestCallback<HlsPlaylist> {

        private final Context context;
        private final String userAgent;
        private final String url;
        private final ManifestFetcher<HlsPlaylist> playlistFetcher;
        private final Callback callback;
        private final Handler mainHandler;
        private final ExoPlayerFacade player;

        private boolean canceled;

        AsyncRendererBuilder(Context context, String userAgent, String url, ExoPlayerFacade player, Callback callback, Handler mainHandler) {
            this.context = context;
            this.userAgent = userAgent;
            this.url = url;
            this.player = player;
            this.callback = callback;
            this.mainHandler = mainHandler;
            HlsPlaylistParser parser = new HlsPlaylistParser();
            playlistFetcher = new ManifestFetcher<>(url, new DefaultUriDataSource(context, userAgent), parser);
        }

        public void init() {
            playlistFetcher.singleLoad(mainHandler.getLooper(), this);
        }

        public void cancel() {
            canceled = true;
        }

        @Override
        public void onSingleManifestError(IOException e) {
            if (canceled) {
                return;
            }
            callback.onError(e);
        }

        @Override
        public void onSingleManifest(HlsPlaylist manifest) {
            if (canceled) {
                return;
            }

            LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            PtsTimestampAdjusterProvider timestampAdjusterProvider = new PtsTimestampAdjusterProvider();
            DataSource dataSource = new DefaultUriDataSource(context, bandwidthMeter, userAgent);
            HlsChunkSource chunkSource = new HlsChunkSource(
                    IS_MASTER,
                    dataSource,
                    manifest,
                    DefaultHlsTrackSelector.newDefaultInstance(context),
                    bandwidthMeter,
                    timestampAdjusterProvider,
                    HlsChunkSource.ADAPTIVE_MODE_SPLICE
            );

            HlsSampleSource sampleSource = new HlsSampleSource(chunkSource, loadControl, BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE);
            MediaCodecVideoTrackRenderer videoRenderer = createVideoRenderer(sampleSource);
            MediaCodecAudioTrackRenderer audioRenderer = createAudioRenderer(sampleSource);

            Renderers renderers = new Renderers(videoRenderer, audioRenderer);
            callback.onCreated(renderers, bandwidthMeter);
        }

        private MediaCodecVideoTrackRenderer createVideoRenderer(HlsSampleSource sampleSource) {
            return new MediaCodecVideoTrackRenderer(
                    context,
                    sampleSource,
                    MediaCodecSelector.DEFAULT,
                    MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                    ALLOWED_JOINING_TIME_MS,
                    mainHandler,
                    player,
                    MAX_DROPPED_FRAME_COUNT_TO_NOTIFY
            );
        }

        private MediaCodecAudioTrackRenderer createAudioRenderer(HlsSampleSource sampleSource) {
            return new MediaCodecAudioTrackRenderer(
                    sampleSource,
                    MediaCodecSelector.DEFAULT,
                    NO_DRM_SESSION_MANAGER,
                    PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                    mainHandler,
                    player,
                    AudioCapabilities.getCapabilities(context),
                    AudioManager.STREAM_MUSIC
            );
        }

    }

}
