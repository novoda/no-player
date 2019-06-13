package com.novoda.noplayer.internal.exoplayer;

final class CodecSecurityRequirement {

    private boolean secureCodecsRequired;

    static CodecSecurityRequirement getInstance() {
        return LazySingleton.PROVIDER;
    }

    void enableSecureCodecs() {
        secureCodecsRequired = true;
    }

    void disableSecureCodecs() {
        secureCodecsRequired = false;
    }

    boolean secureCodecsRequired() {
        return secureCodecsRequired;
    }

    private static class LazySingleton {

        private static final CodecSecurityRequirement PROVIDER = new CodecSecurityRequirement();
    }
}
