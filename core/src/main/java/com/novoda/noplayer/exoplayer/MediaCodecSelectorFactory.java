package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.MediaCodecSelector;

class MediaCodecSelectorFactory {

    private final X86Detector x86Detector;

    public static MediaCodecSelectorFactory newInstance(int deviceOsVersion) {
        X86Detector x86Detector = X86Detector.newInstance(deviceOsVersion);
        return new MediaCodecSelectorFactory(x86Detector);
    }

    MediaCodecSelectorFactory(X86Detector x86Detector) {
        this.x86Detector = x86Detector;
    }

    public MediaCodecSelector createSelector() {
        if (x86Detector.isX86()) {
            return new UnsecureMediaCodecSelector();
        } else {
            return MediaCodecSelector.DEFAULT;
        }
    }
}
