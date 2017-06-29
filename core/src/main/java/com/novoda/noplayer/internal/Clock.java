package com.novoda.noplayer.internal;

import java.io.Serializable;

public interface Clock extends Serializable {

    long getCurrentTime();
}
