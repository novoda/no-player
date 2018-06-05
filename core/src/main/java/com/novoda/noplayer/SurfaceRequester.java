package com.novoda.noplayer;

import android.view.Surface;

public interface SurfaceRequester {

    void requestSurface(Callback callback);

    void removeCallback(Callback callback);

    interface Callback {

        void onSurfaceReady(Surface surface);

    }

}
