package com.novoda.noplayer;

import java.io.Serializable;

public interface Clock extends Serializable {

    long getCurrentTime();

}
