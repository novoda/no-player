package com.novoda.support;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DeviceAndroidVersionTest {

    private static final int ANDROID_VERSION_18 = 18;
    private static final int ANDROID_VERSION_17 = 17;
    private static final int ANDROID_VERSION_19 = 19;
    private static final int ANDROID_VERSION_21 = 21;
    private static final int ANDROID_VERSION_20 = 20;
    private static final int ANDROID_VERSION_22 = 22;

    @Test
    public void givenAndroidVersionIs18_whenCheckForJellyBeanOrOver_thenCheckIsCorrect() {
        DeviceAndroidVersion deviceAndroidVersion = new DeviceAndroidVersion(ANDROID_VERSION_18);

        boolean jellyBeanOrOver = deviceAndroidVersion.is18JellyBeanOrOver();

        assertThat(jellyBeanOrOver).isTrue();
    }

    @Test
    public void givenAndroidVersionIs17_whenCheckForJellyBeanOrOver_thenCheckIsCorrect() {
        DeviceAndroidVersion deviceAndroidVersion = new DeviceAndroidVersion(ANDROID_VERSION_17);

        boolean jellyBeanOrOver = deviceAndroidVersion.is18JellyBeanOrOver();

        assertThat(jellyBeanOrOver).isFalse();
    }

    @Test
    public void givenAndroidVersionIs19_whenCheckForJellyBeanOrOver_thenCheckIsCorrect() {
        DeviceAndroidVersion deviceAndroidVersion = new DeviceAndroidVersion(ANDROID_VERSION_19);

        boolean jellyBeanOrOver = deviceAndroidVersion.is18JellyBeanOrOver();

        assertThat(jellyBeanOrOver).isTrue();
    }

    @Test
    public void givenAndroidVersionIs21_whenCheckForLolliPopOrOver_thenCheckIsCorrect() {
        DeviceAndroidVersion deviceAndroidVersion = new DeviceAndroidVersion(ANDROID_VERSION_21);

        boolean jellyBeanOrOver = deviceAndroidVersion.is21LollipopOrOver();

        assertThat(jellyBeanOrOver).isTrue();
    }

    @Test
    public void givenAndroidVersionIs17_whenCheckForLolliPopOrOver_thenCheckIsCorrect() {
        DeviceAndroidVersion deviceAndroidVersion = new DeviceAndroidVersion(ANDROID_VERSION_20);

        boolean jellyBeanOrOver = deviceAndroidVersion.is21LollipopOrOver();

        assertThat(jellyBeanOrOver).isFalse();
    }

    @Test
    public void givenAndroidVersionIs19_whenCheckForLolliPopOrOver_thenCheckIsCorrect() {
        DeviceAndroidVersion deviceAndroidVersion = new DeviceAndroidVersion(ANDROID_VERSION_22);

        boolean jellyBeanOrOver = deviceAndroidVersion.is21LollipopOrOver();

        assertThat(jellyBeanOrOver).isTrue();
    }
}
