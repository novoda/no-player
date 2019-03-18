package com.novoda.noplayer;

import java.util.List;

public interface AdvertsLoader {

    /**
     * Method called by NoPLayer to load what adverts to play.
     * This method can be called on the main thread.
     * The choice of what context to execute the loading on is left on the client.
     * @param callback callback to inform Noplayer of a successfull load or of an error.
     * @return A cancellable to allow for the load to be interupted. If cancel is called the loader should not interact with the callback anymore.
     */
    Cancellable load(Callback callback);

    interface Callback {
        void onAdvertsLoaded(List<AdvertBreak> advertBreaks);

        void onAdvertsError(String message);
    }

    interface Cancellable {
        void cancel();
    }

}
