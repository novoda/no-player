package com.novoda.noplayer.exoplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.chunk.FormatEvaluator.AdaptiveEvaluator;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.dash.DefaultDashTrackSelector;
import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.UtcTimingElement;
import com.google.android.exoplayer.dash.mpd.UtcTimingElementResolver;
import com.google.android.exoplayer.dash.mpd.UtcTimingElementResolver.UtcTimingCallback;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.text.TextRenderer;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.upstream.UriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;

import java.io.IOException;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class DashRendererCreator implements RendererCreator {

    private static final String TAG = "DashRendererCreator";
    private static final DrmSessionManager NO_DRM_SESSION_MANAGER = null;

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int VIDEO_BUFFER_SEGMENTS = 200;
    private static final int AUDIO_BUFFER_SEGMENTS = 54;
    private static final int LIVE_EDGE_LATENCY_MS = 30000;
    private static final int TEXT_BUFFER_SIZE = 2 * BUFFER_SEGMENT_SIZE;

    private static final int SECURITY_LEVEL_UNKNOWN = -1;
    private static final int SECURITY_LEVEL_1 = 1;
    private static final int SECURITY_LEVEL_3 = 3;
    private static final int ALLOWED_JOINING_TIME_MS = 5000;
    private static final int MAX_DROPPED_FRAME_COUNT_TO_NOTIFY = 50;
    private static final boolean PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = true;
    private static final FormatEvaluator NO_ADAPTIVE_FORMAT_EVALUATOR = null;
    private static final DashChunkSource.EventListener NO_DASH_CHUNK_SOURCE_EVENT_LISTENER = null;
    private static final ChunkSampleSource.EventListener NO_DASH_CHUNK_SAMPLE_EVENT_LISTENER = null;

    private final Context context;
    private final String userAgent;
    private final Handler mainHandler;
    private final DrmSessionCreator drmSessionCreator;

    private AsyncRendererBuilder currentAsyncBuilder;

    DashRendererCreator(Context context,
                        String userAgent,
                        Handler mainHandler,
                        DrmSessionCreator drmSessionCreator) {
        this.context = context;
        this.userAgent = userAgent;
        this.mainHandler = mainHandler;
        this.drmSessionCreator = drmSessionCreator;
    }

    @Override
    public void create(Uri contentUri, ExoPlayerFacade player, Callback callback) {
        currentAsyncBuilder = new AsyncRendererBuilder(
                context,
                userAgent,
                contentUri,
                player,
                player,
                player,
                player,
                mainHandler,
                callback,
                drmSessionCreator.create(player),
                player
        );
        currentAsyncBuilder.init();
    }

    @Override
    public int getRendererCount() {
        return Renderers.VIDEO_AUDIO_TEXT_RENDERERS_SIZE;
    }

    @Override
    public void release() {
        if (currentAsyncBuilder == null) {
            throw new EagerReleaseException();
        }
        currentAsyncBuilder.cancel();
    }

    private static final class AsyncRendererBuilder {

        private static final String KEY_SECURITY_LEVEL = "securityLevel";

        private final Context context;
        private final String userAgent;
        private final DashChunkSource.EventListener dashSourceEventListener;
        private final ChunkSampleSource.EventListener chunkSourceEventListener;
        private final MediaCodecVideoTrackRenderer.EventListener videoCodecEventListener;
        private final MediaCodecAudioTrackRenderer.EventListener audioCodecEventListener;
        private final ManifestFetcher<MediaPresentationDescription> manifestFetcher;
        private final UriDataSource manifestDataSource;
        private final Handler mainHandler;
        private final Callback callback;

        private final DrmSessionManager drmSessionManager;
        private final TextRenderer textRenderer;

        private boolean canceled;
        private MediaPresentationDescription manifest;
        private long elapsedRealtimeOffset;

        AsyncRendererBuilder(Context context,
                             String userAgent,
                             Uri contentUri,
                             DashChunkSource.EventListener dashSourceEventListener,
                             ChunkSampleSource.EventListener chunkSourceEventListener,
                             MediaCodecVideoTrackRenderer.EventListener videoCodecEventListener,
                             MediaCodecAudioTrackRenderer.EventListener audioCodecEventListener,
                             Handler mainHandler,
                             Callback callback,
                             DrmSessionManager drmSessionManager,
                             TextRenderer textRenderer) {
            this.context = context;
            this.userAgent = userAgent;
            this.dashSourceEventListener = dashSourceEventListener;
            this.chunkSourceEventListener = chunkSourceEventListener;
            this.videoCodecEventListener = videoCodecEventListener;
            this.audioCodecEventListener = audioCodecEventListener;
            this.mainHandler = mainHandler;
            this.callback = callback;
            this.drmSessionManager = drmSessionManager;
            this.textRenderer = textRenderer;
            MediaPresentationDescriptionParser parser = new MediaPresentationDescriptionParser();
            manifestDataSource = new DefaultUriDataSource(context, userAgent);
            manifestFetcher = new ManifestFetcher<>(contentUri.toString(), manifestDataSource, parser);
        }

        public void init() {
            manifestFetcher.singleLoad(mainHandler.getLooper(), onManifestCallback);
        }

        private final ManifestFetcher.ManifestCallback<MediaPresentationDescription> onManifestCallback =
                new ManifestFetcher.ManifestCallback<MediaPresentationDescription>() {

                    @Override
                    public void onSingleManifest(MediaPresentationDescription manifest) {
                        if (canceled) {
                            return;
                        }

                        AsyncRendererBuilder.this.manifest = manifest;
                        if (manifest.dynamic && manifest.utcTiming != null) {
                            UtcTimingElementResolver.resolveTimingElement(
                                    manifestDataSource,
                                    manifest.utcTiming,
                                    manifestFetcher.getManifestLoadCompleteTimestamp(),
                                    onUtcTimingCallback
                            );
                        } else {
                            buildRenderers();
                        }
                    }

                    @Override
                    public void onSingleManifestError(IOException e) {
                        if (canceled) {
                            return;
                        }

                        callback.onError(e);
                    }
                };

        public void cancel() {
            canceled = true;
        }

        private final UtcTimingCallback onUtcTimingCallback = new UtcTimingCallback() {
            @Override
            public void onTimestampResolved(UtcTimingElement utcTiming, long elapsedRealtimeOffset) {
                if (canceled) {
                    return;
                }

                AsyncRendererBuilder.this.elapsedRealtimeOffset = elapsedRealtimeOffset;
                buildRenderers();
            }

            @Override
            public void onTimestampError(UtcTimingElement utcTiming, IOException e) {
                if (canceled) {
                    return;
                }

                Log.e(TAG, "Failed to resolve UtcTiming element [" + utcTiming + "]", e);
                // Be optimistic and continue in the hope that the device clock is correct.
                buildRenderers();
            }
        };

        private void buildRenderers() {
            DrmSessionManager drmSessionManager;
            boolean filterHdContent;

            try {
                Period period = manifest.getPeriod(0);
                drmSessionManager = createDrmSessionManager(hasContentProtection(period));
                filterHdContent = getWidevineSecurityLevel() != SECURITY_LEVEL_1;
            } catch (UnsupportedSchemeException | UnsupportedDrmException e) {
                callback.onError(e);
                return;
            }

            LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mainHandler, null);

            AndroidVersion androidVersion = AndroidVersion.newInstance();
            MediaCodecSelector mediaCodecSelector = MediaCodecSelectorFactory.newInstance(androidVersion).createSelector();
            TrackRenderer videoRenderer = createVideoRenderer(
                    mainHandler,
                    loadControl,
                    bandwidthMeter,
                    filterHdContent,
                    drmSessionManager,
                    mediaCodecSelector,
                    createUriDataSource(bandwidthMeter)
            );
            TrackRenderer audioRenderer = createAudioRenderer(
                    mainHandler,
                    loadControl,
                    drmSessionManager,
                    mediaCodecSelector,
                    createUriDataSource(bandwidthMeter)
            );
            TrackRenderer textTrackRenderer = createTextTrackRenderer(
                    loadControl,
                    textRenderer,
                    createUriDataSource(bandwidthMeter)
            );

            Renderers renderers = new Renderers(videoRenderer, audioRenderer, textTrackRenderer);

            callback.onCreated(renderers, bandwidthMeter);
        }

        private DefaultUriDataSource createUriDataSource(DefaultBandwidthMeter bandwidthMeter) {
            return new DefaultUriDataSource(context, bandwidthMeter, userAgent);
        }

        private DrmSessionManager createDrmSessionManager(boolean hasContentProtection) throws UnsupportedDrmException {
            if (hasContentProtection) {
                if (Util.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    throw new UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME);
                }
                return drmSessionManager;
            } else {
                return NO_DRM_SESSION_MANAGER;
            }
        }

        private boolean hasContentProtection(Period period) {
            boolean hasContentProtection = false;
            for (int i = 0; i < period.adaptationSets.size(); i++) {
                AdaptationSet adaptationSet = period.adaptationSets.get(i);
                if (adaptationSet.type != AdaptationSet.TYPE_UNKNOWN) {
                    hasContentProtection |= adaptationSet.hasContentProtection();
                }
            }
            return hasContentProtection;
        }

        private TrackRenderer createVideoRenderer(Handler mainHandler,
                                                  LoadControl loadControl,
                                                  DefaultBandwidthMeter bandwidthMeter,
                                                  boolean filterHdContent,
                                                  DrmSessionManager drmSessionManager,
                                                  MediaCodecSelector mediaCodecSelector,
                                                  DefaultUriDataSource defaultUriDataSource) {
            ChunkSource videoChunkSource = new DashChunkSource(
                    manifestFetcher,
                    DefaultDashTrackSelector.newVideoInstance(context, true, filterHdContent),
                    defaultUriDataSource,
                    new AdaptiveEvaluator(bandwidthMeter),
                    LIVE_EDGE_LATENCY_MS,
                    elapsedRealtimeOffset,
                    mainHandler,
                    dashSourceEventListener,
                    Renderers.VIDEO_RENDERER_ID
            );
            ChunkSampleSource videoSampleSource = new ChunkSampleSource(
                    videoChunkSource, loadControl,
                    VIDEO_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE, mainHandler, chunkSourceEventListener,
                    Renderers.VIDEO_RENDERER_ID
            );
            return new MediaCodecVideoTrackRenderer(
                    context,
                    videoSampleSource,
                    mediaCodecSelector,
                    MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                    ALLOWED_JOINING_TIME_MS,
                    drmSessionManager,
                    PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                    mainHandler,
                    videoCodecEventListener,
                    MAX_DROPPED_FRAME_COUNT_TO_NOTIFY
            );
        }

        private TrackRenderer createAudioRenderer(Handler mainHandler,
                                                  LoadControl loadControl,
                                                  DrmSessionManager drmSessionManager,
                                                  MediaCodecSelector mediaCodecSelector,
                                                  DefaultUriDataSource defaultUriDataSource) {
            ChunkSource audioChunkSource = new DashChunkSource(
                    manifestFetcher,
                    DefaultDashTrackSelector.newAudioInstance(),
                    defaultUriDataSource,
                    NO_ADAPTIVE_FORMAT_EVALUATOR,
                    LIVE_EDGE_LATENCY_MS,
                    elapsedRealtimeOffset,
                    mainHandler,
                    dashSourceEventListener,
                    Renderers.AUDIO_RENDERER_ID
            );
            ChunkSampleSource audioSampleSource = new ChunkSampleSource(
                    audioChunkSource, loadControl,
                    AUDIO_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE, mainHandler, chunkSourceEventListener,
                    Renderers.AUDIO_RENDERER_ID
            );
            return new MediaCodecAudioTrackRenderer(
                    audioSampleSource,
                    mediaCodecSelector,
                    drmSessionManager,
                    PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                    mainHandler,
                    audioCodecEventListener,
                    AudioCapabilities.getCapabilities(context),
                    AudioManager.STREAM_MUSIC
            );
        }

        // TODO move WidevineModularDeviceSecurityLevelFinder to notils and use that instead
        @SuppressWarnings("WrongConstant")      // In mediaDrm.getPropertyString(KEY_SECURITY_LEVEL), the constant is correct
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        private static int getWidevineSecurityLevel() throws UnsupportedSchemeException {
            MediaDrm mediaDrm = new MediaDrm(StreamingDrmSessionManager.WIDEVINE_UUID);
            String securityLevelProperty = mediaDrm.getPropertyString(KEY_SECURITY_LEVEL);
            mediaDrm.release();
            if ("L1".equals(securityLevelProperty)) {
                return SECURITY_LEVEL_1;
            } else if ("L3".equals(securityLevelProperty)) {
                return SECURITY_LEVEL_3;
            } else {
                return SECURITY_LEVEL_UNKNOWN;
            }
        }

        private TrackRenderer createTextTrackRenderer(LoadControl loadControl,
                                                      TextRenderer textRenderer,
                                                      DefaultUriDataSource defaultUriDataSource) {
            DashChunkSource dashChunkSource = new DashChunkSource(
                    manifestFetcher,
                    DefaultDashTrackSelector.newTextInstance(),
                    defaultUriDataSource,
                    NO_ADAPTIVE_FORMAT_EVALUATOR,
                    LIVE_EDGE_LATENCY_MS,
                    elapsedRealtimeOffset,
                    mainHandler,
                    NO_DASH_CHUNK_SOURCE_EVENT_LISTENER,
                    Renderers.TEXT_RENDERER_ID
            );

            ChunkSampleSource textSampleSource = new ChunkSampleSource(
                    dashChunkSource,
                    loadControl,
                    TEXT_BUFFER_SIZE,
                    mainHandler,
                    NO_DASH_CHUNK_SAMPLE_EVENT_LISTENER,
                    Renderers.TEXT_RENDERER_ID
            );
            return new TextTrackRenderer(textSampleSource, textRenderer, mainHandler.getLooper());
        }
    }
}
