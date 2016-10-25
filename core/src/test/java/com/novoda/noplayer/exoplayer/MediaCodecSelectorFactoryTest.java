package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.MediaCodecSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MediaCodecSelectorFactoryTest {

    private static final boolean X86_DEVICE = true;
    private static final boolean NON_X86_DEVICE = false;
    private static final Class<UnsecureMediaCodecSelector> UNSECURE_MEDIA_CODEC_SELECTOR = UnsecureMediaCodecSelector.class;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    X86Detector x86Detector;

    private MediaCodecSelectorFactory mediaCodecSelectorFactory;

    @Before
    public void setUp() {
        mediaCodecSelectorFactory = new MediaCodecSelectorFactory(x86Detector);
    }

    @Test
    public void givenX86Device_whenCreatingMediaCodecSelector_thenUnsecureSelectorIsReturned() {
        when(x86Detector.isX86()).thenReturn(X86_DEVICE);

        MediaCodecSelector mediaCodecSelector = mediaCodecSelectorFactory.createSelector();

        assertThat(mediaCodecSelector).isExactlyInstanceOf(UNSECURE_MEDIA_CODEC_SELECTOR);
    }

    @Test
    public void givenNonX86Device_whenCreatingMediaCodecSelector_thenDefaultSelectorIsReturned() {
        when(x86Detector.isX86()).thenReturn(NON_X86_DEVICE);

        MediaCodecSelector mediaCodecSelector = mediaCodecSelectorFactory.createSelector();

        assertThat(mediaCodecSelector).isEqualTo(MediaCodecSelector.DEFAULT);
    }
}
