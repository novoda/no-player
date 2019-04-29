package com.novoda.noplayer;

public enum DrmSecurityLevel {

    L1,
    L2,
    L3,
    UNKNOWN;

    public String toRawValue() {
        return name();
    }

    public static DrmSecurityLevel fromString(String level) {
        for (DrmSecurityLevel drmSecurityLevel : values()) {
            if (drmSecurityLevel.name().equalsIgnoreCase(level)) {
                return drmSecurityLevel;
            }
        }
        return UNKNOWN;
    }

    public boolean equalsToRaw(String rawLevelValue) {
        return toRawValue().equalsIgnoreCase(rawLevelValue);
    }
}
