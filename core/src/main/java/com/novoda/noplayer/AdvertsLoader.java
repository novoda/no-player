package com.novoda.noplayer;

import java.util.List;

public interface AdvertsLoader {

    Cancellable load(Callback callback);

    interface Callback {
        void onAdvertsLoaded(List<AdvertBreak> advertBreaks);

        void onAdvertsError(String message);
    }

    interface Cancellable {
        void cancel();
    }

}
