package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.RendererCapabilities;
import com.novoda.noplayer.model.Support;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SupportMapperTest {

    @Test
    public void mapsToHandledSupport_whenRenderCapabilitiesIsFormatHandled() {
        Support support = SupportMapper.from(RendererCapabilities.FORMAT_HANDLED);

        assertThat(support).isEqualTo(Support.FORMAT_HANDLED);
    }

    @Test
    public void mapsToExceedsCapabilitiesSupport_whenRenderCapabilitiesIsFormatExceedsCapabilities() {
        Support support = SupportMapper.from(RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES);

        assertThat(support).isEqualTo(Support.FORMAT_EXCEEDS_CAPABILITIES);
    }

    @Test
    public void mapsToUnsupportedDrmSupport_whenRenderCapabilitiesIsFormatUnsupportedDrm() {
        Support support = SupportMapper.from(RendererCapabilities.FORMAT_UNSUPPORTED_DRM);

        assertThat(support).isEqualTo(Support.FORMAT_UNSUPPORTED_DRM);
    }

    @Test
    public void mapsToUnsupportedSubtypeSupport_whenRenderCapabilitiesIsFormatUnsupportedSubtype() {
        Support support = SupportMapper.from(RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE);

        assertThat(support).isEqualTo(Support.FORMAT_UNSUPPORTED_SUBTYPE);
    }

    @Test
    public void mapsToUnsupportedTypeSupport_whenRenderCapabilitiesIsFormatUnsupportedType() {
        Support support = SupportMapper.from(RendererCapabilities.FORMAT_UNSUPPORTED_TYPE);

        assertThat(support).isEqualTo(Support.FORMAT_UNSUPPORTED_TYPE);
    }

    @Test
    public void mapsToUnknownSupport_whenValueIsMinusOne() {
        Support support = SupportMapper.from(-1);

        assertThat(support).isEqualTo(Support.FORMAT_UNKNOWN);
    }
}
