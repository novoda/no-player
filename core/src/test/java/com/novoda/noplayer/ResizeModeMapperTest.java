package com.novoda.noplayer;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ResizeModeMapperTest {

    private final ResizeModeMapper mapper = new ResizeModeMapper();

    @Test
    public void mapsResizeModeFitToAspectRatioFrameLayoutResizeModeFit() {
        int value = mapper.toValue(PlayerView.ResizeMode.FIT);

        assertThat(value).isEqualTo(AspectRatioFrameLayout.RESIZE_MODE_FIT);
    }

    @Test
    public void mapsResizeModeFixedWidthToAspectRatioFrameLayoutResizeModeFixedWidth() {
        int value = mapper.toValue(PlayerView.ResizeMode.FIXED_WIDTH);

        assertThat(value).isEqualTo(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
    }

    @Test
    public void mapsResizeModeFixedHeightToAspectRatioFrameLayoutResizeModeFixedHeight() {
        int value = mapper.toValue(PlayerView.ResizeMode.FIXED_HEIGHT);

        assertThat(value).isEqualTo(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
    }

    @Test
    public void mapsResizeModeFillToAspectRatioFrameLayoutResizeModeFill() {
        int value = mapper.toValue(PlayerView.ResizeMode.FILL);

        assertThat(value).isEqualTo(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    @Test
    public void mapsResizeModeZoomToAspectRatioFrameLayoutResizeModeZoom() {
        int value = mapper.toValue(PlayerView.ResizeMode.ZOOM);

        assertThat(value).isEqualTo(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
    }

    @Test
    public void mapsAspectRatioFrameLayoutResizeModeFitToResizeModeFit() {
        PlayerView.ResizeMode resizeMode = mapper.fromValue(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        assertThat(resizeMode).isEqualTo(PlayerView.ResizeMode.FIT);
    }

    @Test
    public void mapsAspectRatioFrameLayoutResizeModeFixedWidthToResizeModeFixedWidth() {
        PlayerView.ResizeMode resizeMode = mapper.fromValue(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

        assertThat(resizeMode).isEqualTo(PlayerView.ResizeMode.FIXED_WIDTH);
    }

    @Test
    public void mapsAspectRatioFrameLayoutResizeModeFixedHeightToResizeModeFixedHeight() {
        PlayerView.ResizeMode resizeMode = mapper.fromValue(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        assertThat(resizeMode).isEqualTo(PlayerView.ResizeMode.FIXED_HEIGHT);
    }

    @Test
    public void mapsAspectRatioFrameLayoutResizeModeFillToResizeModeFill() {
        PlayerView.ResizeMode resizeMode = mapper.fromValue(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        assertThat(resizeMode).isEqualTo(PlayerView.ResizeMode.FILL);
    }

    @Test
    public void mapsAspectRatioFrameLayoutResizeModeZoomToResizeModeZoom() {
        PlayerView.ResizeMode resizeMode = mapper.fromValue(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        assertThat(resizeMode).isEqualTo(PlayerView.ResizeMode.ZOOM);
    }
}
