package com.novoda.noplayer.exoplayer;

import java.util.Map;

/**
 * A listener for debugging information.
 */
public interface InfoListener {

    void onNewInfo(String callingMethod, Map<String, String> keyValuePairs);

}
