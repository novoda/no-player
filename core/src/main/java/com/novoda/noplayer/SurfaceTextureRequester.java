package com.novoda.noplayer;

import android.graphics.SurfaceTexture;

public interface SurfaceTextureRequester {

    void requestSurfaceTexture(Callback callback);

    void removeCallback(Callback callback);

    interface Callback {

        void onSurfaceTextureReady(SurfaceTexture surfaceTexture);

    }

}
