package com.novoda.noplayer;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AndroidVersionTest {

    private static final int ANDROID_VERSION_20 = 20;
    private static final int ANDROID_VERSION_21 = 21;
    private static final int ANDROID_VERSION_22 = 22;

    @Test
    public void givenAndroidVersionIs20_whenCheckForLollipopOrOver_thenReturnsFalse() {
        AndroidVersion deviceAndroidVersion = AndroidVersion.from(ANDROID_VERSION_20);

        boolean lollipopOrOver = deviceAndroidVersion.is21LollipopOrOver();

        assertThat(lollipopOrOver).isFalse();
    }

    @Test
    public void givenAndroidVersionIs21_whenCheckForLollipopOrOver_thenReturnsTrue() {
        AndroidVersion deviceAndroidVersion = AndroidVersion.from(ANDROID_VERSION_21);

        boolean lollipopOrOver = deviceAndroidVersion.is21LollipopOrOver();

        assertThat(lollipopOrOver).isTrue();
    }

    @Test
    public void givenAndroidVersionIs22_whenCheckForLollipopOrOver_thenReturnsTrue() {
        AndroidVersion deviceAndroidVersion = AndroidVersion.from(ANDROID_VERSION_22);

        boolean lollipopOrOver = deviceAndroidVersion.is21LollipopOrOver();

        assertThat(lollipopOrOver).isTrue();
    }

}
