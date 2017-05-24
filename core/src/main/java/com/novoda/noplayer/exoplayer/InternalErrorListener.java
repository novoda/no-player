package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;

import java.io.IOException;

/**
 * A listener for internal errors.
 * <p>
 * These errors are not visible to the user, and hence this listener is provided for
 * informational purposes only. Note however that an internal error may cause a fatal
 * error if the player fails to recover. If this happens, {@link ExoPlayerTwoFacade.Listener#onPlayerError(ExoPlaybackException)}
 * will be invoked.
 */
public interface InternalErrorListener {

    void onLoadError(IOException e);

    // TODO: Add additional logging when developing DRM.

}
