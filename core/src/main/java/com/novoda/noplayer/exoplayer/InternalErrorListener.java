package com.novoda.noplayer.exoplayer;

import android.media.MediaCodec;

import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;

import java.io.IOException;

/**
 * A listener for internal errors.
 * <p/>
 * These errors are not visible to the user, and hence this listener is provided for
 * informational purposes only. Note however that an internal error may cause a fatal
 * error if the player fails to recover. If this happens, {@link ExoPlayerFacade.Listener#onError(Exception)}
 * will be invoked.
 */
public interface InternalErrorListener {
    void onRendererInitializationError(Exception e);

    void onAudioTrackInitializationError(AudioTrack.InitializationException e);

    void onAudioTrackWriteError(AudioTrack.WriteException e);

    void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e);

    void onCryptoError(MediaCodec.CryptoException e);

    void onLoadError(int sourceId, IOException e);

    void onDrmSessionManagerError(Exception e);
}
