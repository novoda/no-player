package com.novoda.noplayer;

import android.view.SurfaceHolder;

public interface SurfaceHolderRequester {

    void requestSurfaceHolder(Callback callback);

    void removeCallback(Callback callback);

    interface Callback {

        void onSurfaceHolderReady(SurfaceHolder surfaceHolder);

    }

}
