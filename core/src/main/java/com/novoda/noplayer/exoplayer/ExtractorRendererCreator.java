package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

public class ExtractorRendererCreator implements RendererCreator {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private static final int MAX_DROPPED_FRAME_COUNT_TO_NOTIFY = 50;
    private static final int ALLOWED_JOINING_TIME_MS = 5000;
    private static final DrmSessionManager NO_DRM_SESSION_MANAGER = null;
    private static final boolean PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = true;

    private final Context context;
    private final String userAgent;
    private final Handler mainThreadHandler;

    public ExtractorRendererCreator(Context context, String userAgent, Handler mainThreadHandler) {
        this.context = context;
        this.userAgent = userAgent;
        this.mainThreadHandler = mainThreadHandler;
    }

    @Override
    public void create(Uri contentUri, ExoPlayerFacade player, Callback callback) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mainThreadHandler, null);
        ExtractorSampleSource sampleSource = createSampleSource(contentUri, bandwidthMeter);
        MediaCodecVideoTrackRenderer videoRenderer = createVideoRenderer(player, sampleSource);
        MediaCodecAudioTrackRenderer audioRenderer = createAudioRenderer(player, sampleSource);

        Renderers renderers = new Renderers(videoRenderer, audioRenderer);
        callback.onCreated(renderers, bandwidthMeter);
    }

    @Override
    public int getRendererCount() {
        return Renderers.VIDEO_AUDIO_RENDERERS_SIZE;
    }

    @Override
    public void release() {
        // do nothing
    }

    private ExtractorSampleSource createSampleSource(Uri contentUri, DefaultBandwidthMeter bandwidthMeter) {
        int requestedBufferSize = BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE;
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DataSource dataSource = new DefaultUriDataSource(context, bandwidthMeter, userAgent);
        return new ExtractorSampleSource(contentUri, dataSource, allocator, requestedBufferSize);
    }

    private MediaCodecVideoTrackRenderer createVideoRenderer(ExoPlayerFacade player, ExtractorSampleSource sampleSource) {
        return new MediaCodecVideoTrackRenderer(
                context,
                sampleSource,
                MediaCodecSelector.DEFAULT,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                ALLOWED_JOINING_TIME_MS,
                mainThreadHandler,
                player,
                MAX_DROPPED_FRAME_COUNT_TO_NOTIFY
        );
    }

    private MediaCodecAudioTrackRenderer createAudioRenderer(ExoPlayerFacade player, ExtractorSampleSource sampleSource) {
        AudioCapabilities audioCapabilities = AudioCapabilities.getCapabilities(context);
        return new MediaCodecAudioTrackRenderer(
                sampleSource,
                MediaCodecSelector.DEFAULT,
                NO_DRM_SESSION_MANAGER,
                PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                mainThreadHandler,
                player,
                audioCapabilities,
                AudioManager.STREAM_MUSIC
        );
    }

}
