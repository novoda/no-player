package com.novoda.noplayer.exoplayer;

import android.annotation.TargetApi;
import android.os.Build;

import com.novoda.noplayer.AndroidVersion;

import java.util.Arrays;

public class X86Detector {

    private static final String X86 = "x86";

    private final String[] supportedAbis;

    public static X86Detector newInstance(AndroidVersion androidVersion) {
        if (androidVersion.deviceOsVersion() >= Build.VERSION_CODES.LOLLIPOP) {
            return new X86Detector(getSupportedAbis());
        } else {
            return new X86Detector(getLegacySupportedAbis());
        }
    }

    X86Detector(String... supportedAbis) {
        this.supportedAbis = Arrays.copyOf(supportedAbis, supportedAbis.length);
    }

    public boolean isX86() {
        for (String abi : supportedAbis) {
            if (abi.contains(X86)) {
                return true;
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String[] getSupportedAbis() {
        return Build.SUPPORTED_ABIS;
    }

    private static String[] getLegacySupportedAbis() {
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

}
