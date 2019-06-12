/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.SubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * Default {@link RenderersFactory} implementation.
 */
class SimpleRenderersFactory implements RenderersFactory {

    private static final boolean DO_NOT_PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = false;
    private static final boolean INIT_ARGS = true;
    private static final boolean PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = true;
    private static final boolean ENABLE_DECODER_FALLBACK = true;

    /**
     * Modes for using extension renderers.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EXTENSION_RENDERER_MODE_OFF, EXTENSION_RENDERER_MODE_ON,
            EXTENSION_RENDERER_MODE_PREFER})
    @interface ExtensionRendererMode {

    }

    /**
     * Do not allow use of extension renderers.
     */
    static final int EXTENSION_RENDERER_MODE_OFF = 0;

    /**
     * Allow use of extension renderers. Extension renderers are indexed after core renderers of the
     * same type. A {@link TrackSelector} that prefers the first suitable renderer will therefore
     * prefer to use a core renderer to an extension renderer in the case that both are able to play
     * a given track.
     */
    static final int EXTENSION_RENDERER_MODE_ON = 1;
    /**
     * Allow use of extension renderers. Extension renderers are indexed before core renderers of the
     * same type. A {@link TrackSelector} that prefers the first suitable renderer will therefore
     * prefer to use an extension renderer to a core renderer in the case that both are able to play
     * a given track.
     */
    static final int EXTENSION_RENDERER_MODE_PREFER = 2;
    private static final String TAG = "DefaultRenderersFactory";

    private static final int MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY = 50;

    private final Context context;

    @ExtensionRendererMode
    private final int extensionRendererMode;

    private final long allowedVideoJoiningTimeMs;
    private final SecurityDowngradingCodecSelector mediaCodecSelector;
    private final SubtitleDecoderFactory subtitleDecoderFactory;

    /**
     * @param context                   A {@link Context}.
     * @param extensionRendererMode     The extension renderer mode, which determines if and how
     *                                  available extension renderers are used. Note that extensions must be included in the
     *                                  application build for them to be considered available.
     * @param allowedVideoJoiningTimeMs The maximum duration for which video renderers can attempt
     *                                  to seamlessly join an ongoing playback.
     * @param mediaCodecSelector        Used for selecting the codec for the video renderer.
     * @param subtitleDecoderFactory    A factory from which to obtain {@link SubtitleDecoder} instances.
     */
    SimpleRenderersFactory(Context context,
                           @ExtensionRendererMode int extensionRendererMode,
                           long allowedVideoJoiningTimeMs,
                           SecurityDowngradingCodecSelector mediaCodecSelector,
                           SubtitleDecoderFactory subtitleDecoderFactory) {
        this.context = context;
        this.extensionRendererMode = extensionRendererMode;
        this.allowedVideoJoiningTimeMs = allowedVideoJoiningTimeMs;
        this.mediaCodecSelector = mediaCodecSelector;
        this.subtitleDecoderFactory = subtitleDecoderFactory;
    }

    @Override
    public Renderer[] createRenderers(Handler eventHandler,
                                      VideoRendererEventListener videoRendererEventListener,
                                      AudioRendererEventListener audioRendererEventListener,
                                      TextOutput textRendererOutput,
                                      MetadataOutput metadataRendererOutput,
                                      @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        ArrayList<Renderer> renderersList = new ArrayList<>();
        buildVideoRenderers(context, drmSessionManager, allowedVideoJoiningTimeMs,
                            eventHandler, videoRendererEventListener, extensionRendererMode, renderersList
        );
        buildAudioRenderers(context, drmSessionManager, buildAudioProcessors(),
                            eventHandler, audioRendererEventListener, extensionRendererMode, renderersList
        );
        buildTextRenderers(textRendererOutput, eventHandler.getLooper(), renderersList, subtitleDecoderFactory);
        buildMetadataRenderers(metadataRendererOutput, eventHandler.getLooper(),
                               renderersList
        );
        buildMiscellaneousRenderers();
        return renderersList.toArray(new Renderer[renderersList.size()]);
    }

    /**
     * Builds video renderers for use by the player.
     *
     * @param context                   The {@link Context} associated with the player.
     * @param drmSessionManager         An optional {@link DrmSessionManager}. May be null if the player
     *                                  will not be used for DRM protected playbacks.
     * @param allowedVideoJoiningTimeMs The maximum duration in milliseconds for which video
     *                                  renderers can attempt to seamlessly join an ongoing playback.
     * @param eventHandler              A handler associated with the main thread's looper.
     * @param eventListener             An event listener.
     * @param extensionRendererMode     The extension renderer mode.
     * @param outRenderers              An array to which the built renderers should be appended.
     */
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})   // Using reflection and these APIs mean we need to do it
    private void buildVideoRenderers(Context context,
                                     DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                     long allowedVideoJoiningTimeMs,
                                     Handler eventHandler,
                                     VideoRendererEventListener eventListener,
                                     @ExtensionRendererMode int extensionRendererMode,
                                     List<Renderer> outRenderers) {
        outRenderers.add(new TempMediaCodecVideoRenderer(
                context,
                mediaCodecSelector,
                allowedVideoJoiningTimeMs,
                drmSessionManager,
                DO_NOT_PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                ENABLE_DECODER_FALLBACK,
                eventHandler,
                eventListener,
                MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY
        ));

        if (extensionRendererMode == EXTENSION_RENDERER_MODE_OFF) {
            return;
        }
        int extensionRendererIndex = outRenderers.size();
        if (extensionRendererMode == EXTENSION_RENDERER_MODE_PREFER) {
            extensionRendererIndex--;
        }

        try {
            Class<?> clazz = Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer");
            Constructor<?> constructor = clazz.getConstructor(boolean.class, long.class, Handler.class, VideoRendererEventListener.class, int.class);
            Renderer renderer = (Renderer) constructor.newInstance(
                    INIT_ARGS,
                    allowedVideoJoiningTimeMs,
                    eventHandler,
                    eventListener,
                    MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY
            );
            outRenderers.add(extensionRendererIndex, renderer);
            Log.i(TAG, "Loaded LibvpxVideoRenderer.");
        } catch (ClassNotFoundException e) {
            // Expected if the app was built without the extension.
        } catch (Exception e) {
            throw new RendererInstantiationException("LibvpxVideoRenderer", e);
        }
    }

    /**
     * Builds audio renderers for use by the player.
     *
     * @param context               The {@link Context} associated with the player.
     * @param drmSessionManager     An optional {@link DrmSessionManager}. May be null if the player
     *                              will not be used for DRM protected playbacks.
     * @param audioProcessors       An array of {@link AudioProcessor}s that will process PCM audio
     *                              buffers before output. May be empty.
     * @param eventHandler          A handler to use when invoking event listeners and outputs.
     * @param eventListener         An event listener.
     * @param extensionRendererMode The extension renderer mode.
     * @param outRenderers          An array to which the built renderers should be appended.
     */
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})   // Using reflection and these APIs mean we need to do it
    private void buildAudioRenderers(Context context,
                                     DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                     AudioProcessor[] audioProcessors,
                                     Handler eventHandler,
                                     AudioRendererEventListener eventListener,
                                     @ExtensionRendererMode int extensionRendererMode,
                                     List<Renderer> outRenderers) {
        MediaCodecAudioRenderer mediaCodecAudioRenderer = new MediaCodecAudioRenderer(
                context,
                mediaCodecSelector,
                drmSessionManager,
                PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                eventHandler,
                eventListener,
                AudioCapabilities.getCapabilities(context),
                audioProcessors
        );

        outRenderers.add(mediaCodecAudioRenderer);

        if (extensionRendererMode == EXTENSION_RENDERER_MODE_OFF) {
            return;
        }
        int extensionRendererIndex = outRenderers.size();
        if (extensionRendererMode == EXTENSION_RENDERER_MODE_PREFER) {
            extensionRendererIndex--;
        }

        try {
            Class<?> clazz = Class.forName("com.google.android.exoplayer2.ext.opus.LibopusAudioRenderer");
            Constructor<?> constructor = clazz.getConstructor(Handler.class, AudioRendererEventListener.class, AudioProcessor[].class);
            Renderer renderer = (Renderer) constructor.newInstance(eventHandler, eventListener, audioProcessors);
            outRenderers.add(extensionRendererIndex++, renderer);
            Log.i(TAG, "Loaded LibopusAudioRenderer.");
        } catch (ClassNotFoundException e) {
            // Expected if the app was built without the extension.
        } catch (Exception e) {
            throw new RendererInstantiationException("LibopusAudioRenderer", e);
        }

        try {
            Class<?> clazz = Class.forName("com.google.android.exoplayer2.ext.flac.LibflacAudioRenderer");
            Constructor<?> constructor = clazz.getConstructor(Handler.class, AudioRendererEventListener.class, AudioProcessor[].class);
            Renderer renderer = (Renderer) constructor.newInstance(eventHandler, eventListener, audioProcessors);
            outRenderers.add(extensionRendererIndex++, renderer);
            Log.i(TAG, "Loaded LibflacAudioRenderer.");
        } catch (ClassNotFoundException e) {
            // Expected if the app was built without the extension.
        } catch (Exception e) {
            throw new RendererInstantiationException("LibflacAudioRenderer", e);
        }

        try {
            Class<?> clazz = Class.forName("com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer");
            Constructor<?> constructor = clazz.getConstructor(Handler.class, AudioRendererEventListener.class, AudioProcessor[].class);
            Renderer renderer = (Renderer) constructor.newInstance(eventHandler, eventListener, audioProcessors);
            outRenderers.add(extensionRendererIndex, renderer);
            Log.i(TAG, "Loaded FfmpegAudioRenderer.");
        } catch (ClassNotFoundException e) {
            // Expected if the app was built without the extension.
        } catch (Exception e) {
            throw new RendererInstantiationException("FfmpegAudioRenderer", e);
        }
    }

    /**
     * Builds text renderers for use by the player.
     *
     * @param output         An output for the renderers.
     * @param outputLooper   The looper associated with the thread on which the output should be
     *                       called.
     * @param outRenderers   An array to which the built renderers should be appended.
     * @param decoderFactory A factory from which to obtain {@link SubtitleDecoder} instances.
     */
    private void buildTextRenderers(TextOutput output, Looper outputLooper, List<Renderer> outRenderers, SubtitleDecoderFactory decoderFactory) {
        outRenderers.add(new TextRenderer(output, outputLooper, decoderFactory));
    }

    /**
     * Builds metadata renderers for use by the player.
     *
     * @param output       An output for the renderers.
     * @param outputLooper The looper associated with the thread on which the output should be
     *                     called.
     * @param outRenderers An array to which the built renderers should be appended.
     */
    private void buildMetadataRenderers(MetadataOutput output, Looper outputLooper, List<Renderer> outRenderers) {
        outRenderers.add(new MetadataRenderer(output, outputLooper));
    }

    /**
     * Builds any miscellaneous renderers used by the player.
     */
    private void buildMiscellaneousRenderers() {
        // Do nothing.
    }

    /**
     * Builds an array of {@link AudioProcessor}s that will process PCM audio before output.
     */
    private AudioProcessor[] buildAudioProcessors() {
        return new AudioProcessor[0];
    }

    public static class RendererInstantiationException extends RuntimeException {

        RendererInstantiationException(String rendererName, Throwable cause) {
            super("Unable to instantiate renderer " + rendererName, cause);
        }
    }

    class TempMediaCodecVideoRenderer extends MediaCodecVideoRenderer {

        private final SecurityDowngradingCodecSelector mediaCodecSelector;

        TempMediaCodecVideoRenderer(Context context, SecurityDowngradingCodecSelector mediaCodecSelector, long allowedJoiningTimeMs, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, boolean enableDecoderFallback, @Nullable Handler eventHandler, @Nullable VideoRendererEventListener eventListener, int maxDroppedFramesToNotify) {
            super(context, mediaCodecSelector, allowedJoiningTimeMs, drmSessionManager, playClearSamplesWithoutKeys, enableDecoderFallback, eventHandler, eventListener, maxDroppedFramesToNotify);
            this.mediaCodecSelector = mediaCodecSelector;
        }

        @Override
        protected int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws MediaCodecUtil.DecoderQueryException {
            DrmInitData drmInitData = format.drmInitData;
            if (drmInitData == null) {
                this.mediaCodecSelector.disableSecureCodecs();
            } else {
                this.mediaCodecSelector.enableSecureCodecs();
            }

            return super.supportsFormat(mediaCodecSelector, drmSessionManager, format);
        }
    }
}
