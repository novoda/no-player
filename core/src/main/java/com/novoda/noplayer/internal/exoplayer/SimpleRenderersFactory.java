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
import android.support.annotation.IntDef;
import android.util.Log;

import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.NoPlayer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link RenderersFactory} implementation.
 */
class SimpleRenderersFactory implements RenderersFactory {

    private static final boolean DO_NOT_PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = false;
    private static final boolean INIT_ARGS = true;
    private static final boolean PLAY_CLEAR_SAMPLES_WITHOUT_KEYS = true;

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

    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;

    @ExtensionRendererMode
    private final int extensionRendererMode;

    private final long allowedVideoJoiningTimeMs;
    private final MediaCodecSelector mediaCodecSelector;
    private final FramesPerSecondCalculator framesPerSecondCalculator;
    private final NoPlayer.FramesPerSecondChangedListener framesPerSecondChangedListeners;

    /**
     * @param context                         A {@link Context}.
     * @param drmSessionManager               An optional {@link DrmSessionManager}. May be null if DRM protected
     *                                        playbacks are not required..
     * @param extensionRendererMode           The extension renderer mode, which determines if and how
     *                                        available extension renderers are used. Note that extensions must be included in the
     *                                        application build for them to be considered available.
     * @param allowedVideoJoiningTimeMs       The maximum duration for which video renderers can attempt
     *                                        to seamlessly join an ongoing playback.
     * @param mediaCodecSelector              Used for selecting the codec for the video renderer.
     * @param framesPerSecondCalculator       Used to calculate the frames per second based on the output buffer timing.
     * @param framesPerSecondChangedListeners
     */
    SimpleRenderersFactory(Context context,
                           DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                           @ExtensionRendererMode int extensionRendererMode,
                           long allowedVideoJoiningTimeMs,
                           MediaCodecSelector mediaCodecSelector,
                           FramesPerSecondCalculator framesPerSecondCalculator,
                           NoPlayer.FramesPerSecondChangedListener framesPerSecondChangedListeners) {
        this.context = context;
        this.drmSessionManager = drmSessionManager;
        this.extensionRendererMode = extensionRendererMode;
        this.allowedVideoJoiningTimeMs = allowedVideoJoiningTimeMs;
        this.mediaCodecSelector = mediaCodecSelector;
        this.framesPerSecondCalculator = framesPerSecondCalculator;
        this.framesPerSecondChangedListeners = framesPerSecondChangedListeners;
    }

    @Override
    public Renderer[] createRenderers(Handler eventHandler,
                                      VideoRendererEventListener videoRendererEventListener,
                                      AudioRendererEventListener audioRendererEventListener,
                                      TextOutput textRendererOutput,
                                      MetadataOutput metadataRendererOutput) {
        ArrayList<Renderer> renderersList = new ArrayList<>();
        buildVideoRenderers(context, drmSessionManager, allowedVideoJoiningTimeMs,
                            eventHandler, videoRendererEventListener, extensionRendererMode, renderersList
        );
        buildAudioRenderers(context, drmSessionManager, buildAudioProcessors(),
                            eventHandler, audioRendererEventListener, extensionRendererMode, renderersList
        );
        buildTextRenderers(textRendererOutput, eventHandler.getLooper(),
                           renderersList
        );
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
        outRenderers.add(new FramesPerSecondReportingMediaCodecVideoRenderer(
                context,
                mediaCodecSelector,
                allowedVideoJoiningTimeMs,
                drmSessionManager,
                DO_NOT_PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                eventHandler,
                eventListener,
                MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY,
                framesPerSecondCalculator,
                framesPerSecondChangedListeners
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
        outRenderers.add(new MediaCodecAudioRenderer(
                mediaCodecSelector,
                drmSessionManager,
                PLAY_CLEAR_SAMPLES_WITHOUT_KEYS,
                eventHandler,
                eventListener,
                AudioCapabilities.getCapabilities(context),
                audioProcessors
        ));

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
     * @param output       An output for the renderers.
     * @param outputLooper The looper associated with the thread on which the output should be
     *                     called.
     * @param outRenderers An array to which the built renderers should be appended.
     */
    private void buildTextRenderers(TextOutput output, Looper outputLooper, List<Renderer> outRenderers) {
        outRenderers.add(new TextRenderer(output, outputLooper));
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
}
