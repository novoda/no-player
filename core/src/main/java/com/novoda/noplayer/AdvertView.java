package com.novoda.noplayer;

import java.util.List;

public interface AdvertView {

    void setup(List<AdvertBreak> advertBreaks);

    void removeMarker(AdvertBreak advertBreak);

}
