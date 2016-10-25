package com.novoda.noplayer;

import android.view.SurfaceHolder;

public interface SurfaceHolderRequester {

    void requestSurfaceHolder(Callback callback);

    interface Callback {

        void onSurfaceHolderReady(SurfaceHolder surfaceHolder);

    }

}
