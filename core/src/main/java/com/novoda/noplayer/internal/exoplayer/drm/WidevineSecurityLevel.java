package com.novoda.noplayer.internal.exoplayer.drm;

public enum WidevineSecurityLevel {

    L1,
    L2,
    L3,
    UNKNOWN;

    public String toRawValue() {
        return name();
    }

    public static WidevineSecurityLevel fromString(String level) {
        for (WidevineSecurityLevel widevineSecurityLevel : values()) {
            if (widevineSecurityLevel.name().equalsIgnoreCase(level)) {
                return widevineSecurityLevel;
            }
        }
        return UNKNOWN;
    }

    public boolean equalsToRaw(String rawLevelValue) {
        return toRawValue().equalsIgnoreCase(rawLevelValue);
    }
}
