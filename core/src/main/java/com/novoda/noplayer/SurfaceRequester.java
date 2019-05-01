package com.novoda.noplayer;

import android.view.Surface;
import android.view.SurfaceHolder;
import com.novoda.noplayer.model.Either;

public interface SurfaceRequester {

    void requestSurface(Callback callback);

    void removeCallback(Callback callback);

    interface Callback {

        void onSurfaceReady(Either<Surface, SurfaceHolder> surface);
    }

}
