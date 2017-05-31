package com.novoda.noplayer.exoplayer.forwarder;

import java.util.Map;

/**
 * A listener for debugging information.
 */
public interface InfoListener {

    /**
     * All event listeners attached to implementations of Player will
     * forward information through this to provide debugging
     * information to client applications.
     *
     * @param callingMethod       The method name from where this call originated.
     * @param callingMethodParams Parameter name and value pairs from where this call originated.
     *                            Pass only string representations not whole objects.
     */
    void onNewInfo(String callingMethod, Map<String, String> callingMethodParams);

}
