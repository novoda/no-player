package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.chunk.Format;
import com.novoda.noplayer.Player;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InfoForwarderTest {

    private static final int UNUSED_TRIGGER = 0;
    private static final int UNUSED_MEDIA_TIME_MS = 0;
    private static final int UNKNOWN_SOURCE_ID = -1;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    Player.BitrateChangedListener bitrateChangedListener;

    private InfoForwarder infoForwarder;

    @Before
    public void setUp() {
        infoForwarder = new InfoForwarder(bitrateChangedListener);
    }

    @Test
    public void givenUnknownSourceId_whenDownstreamFormatChanges_thenBitrateChangedListenerIsNotNotified() {
        Format format = formatWithBitrate(0);

        infoForwarder.onDownstreamFormatChanged(UNKNOWN_SOURCE_ID, format, UNUSED_TRIGGER, UNUSED_MEDIA_TIME_MS);

        verifyZeroInteractions(bitrateChangedListener);
    }

    @Test
    public void givenAnAudioSourceId_whenDownstreamFormatChanges_thenBitrateChangedListenerIsNotifiedWithAudioBitrate() {
        int audioBitrate = 1000;
        Format format = formatWithBitrate(audioBitrate);

        infoForwarder.onDownstreamFormatChanged(Renderers.AUDIO_RENDERER_ID, format, UNUSED_TRIGGER, UNUSED_MEDIA_TIME_MS);

        verify(bitrateChangedListener).onBitrateChanged(Bitrate.fromBitsPerSecond(audioBitrate), Bitrate.fromBitsPerSecond(0));
    }

    @Test
    public void givenAnVideoSourceId_whenDownstreamFormatChanges_thenBitrateChangedListenerIsNotifiedWithVideoBitrate() {
        int videoBitrate = 1000;
        Format format = formatWithBitrate(videoBitrate);

        infoForwarder.onDownstreamFormatChanged(Renderers.VIDEO_RENDERER_ID, format, UNUSED_TRIGGER, UNUSED_MEDIA_TIME_MS);

        verify(bitrateChangedListener).onBitrateChanged(Bitrate.fromBitsPerSecond(0), Bitrate.fromBitsPerSecond(videoBitrate));
    }

    @Test
    public void givenDownstreamFormatHasChangedForAudio_whenDownstreamFormatChangesForVideo_thenBitrateChangedListenerIsNotifiedWithBothBitrates() {
        int audioBitrate = 1000;
        givenDownstreamFormatHasChangedFor(Renderers.AUDIO_RENDERER_ID, audioBitrate);

        int videoBitrate = 50;
        Format format = formatWithBitrate(videoBitrate);

        infoForwarder.onDownstreamFormatChanged(Renderers.VIDEO_RENDERER_ID, format, UNUSED_TRIGGER, UNUSED_MEDIA_TIME_MS);

        verify(bitrateChangedListener).onBitrateChanged(Bitrate.fromBitsPerSecond(audioBitrate), Bitrate.fromBitsPerSecond(videoBitrate));
    }

    @Test
    public void givenDownstreamFormatHasChangedForVideo_whenDownstreamFormatChangesForAudio_thenBitrateChangedListenerIsNotifiedWithBothBitrates() {
        int videoBitrate = 50;
        givenDownstreamFormatHasChangedFor(Renderers.VIDEO_RENDERER_ID, videoBitrate);

        int audioBitrate = 1000;
        Format format = formatWithBitrate(audioBitrate);

        infoForwarder.onDownstreamFormatChanged(Renderers.AUDIO_RENDERER_ID, format, UNUSED_TRIGGER, UNUSED_MEDIA_TIME_MS);

        verify(bitrateChangedListener).onBitrateChanged(Bitrate.fromBitsPerSecond(audioBitrate), Bitrate.fromBitsPerSecond(videoBitrate));
    }

    private void givenDownstreamFormatHasChangedFor(int sourceId, int bitrate) {
        Format format = formatWithBitrate(bitrate);
        infoForwarder.onDownstreamFormatChanged(sourceId, format, UNUSED_TRIGGER, UNUSED_MEDIA_TIME_MS);
    }

    private static Format formatWithBitrate(int bitrate) {
        return new Format(
                "id",
                "mimetype",
                1080,
                720,
                0,
                0,
                0,
                bitrate
        );
    }
}
