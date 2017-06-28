package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.internal.mediaplayer.NoPlayerMediaPlayerCreator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.novoda.noplayer.player.PrioritizedPlayerTypes.prioritizeExoPlayer;
import static com.novoda.noplayer.player.PrioritizedPlayerTypes.prioritizeMediaPlayer;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(Enclosed.class)
public class NoPlayerCreatorTest {

    public static abstract class Base {

        static final boolean USE_SECURE_CODEC = false;
        static final StreamingModularDrm STREAMING_MODULAR_DRM = mock(StreamingModularDrm.class);
        static final DownloadedModularDrm DOWNLOADED_MODULAR_DRM = mock(DownloadedModularDrm.class);
        static final Player EXO_PLAYER = mock(Player.class);
        static final Player MEDIA_PLAYER = mock(Player.class);

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        Context context;

        @Mock
        NoPlayerExoPlayerCreator noPlayerExoPlayerCreator;
        @Mock
        NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator;
        @Mock
        DrmSessionCreator drmSessionCreator;
        @Mock
        DrmSessionCreatorFactory drmSessionCreatorFactory;

        NoPlayerCreator noPlayerCreator;

        @Before
        public void setUp() {
            given(drmSessionCreatorFactory.createFor(any(DrmType.class), any(DrmHandler.class))).willReturn(drmSessionCreator);
            given(noPlayerExoPlayerCreator.createExoPlayer(context, drmSessionCreator, USE_SECURE_CODEC)).willReturn(EXO_PLAYER);
            given(noPlayerMediaPlayerCreator.createMediaPlayer(context)).willReturn(MEDIA_PLAYER);
            noPlayerCreator = new NoPlayerCreator(context, prioritizedPlayerTypes(), noPlayerExoPlayerCreator, noPlayerMediaPlayerCreator, drmSessionCreatorFactory);
        }

        abstract PrioritizedPlayerTypes prioritizedPlayerTypes();
    }

    public static class GivenMediaPlayerPrioritized extends Base {

        @Override
        PrioritizedPlayerTypes prioritizedPlayerTypes() {
            return prioritizeMediaPlayer();
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeNone_thenReturnsMediaPlayer() {
            Player player = noPlayerCreator.create(DrmType.NONE, DrmHandler.NO_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            Player player = noPlayerCreator.create(DrmType.WIDEVINE_CLASSIC, DrmHandler.NO_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            Player player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_STREAM, STREAMING_MODULAR_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            Player player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, DOWNLOADED_MODULAR_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }

    public static class GivenExoPlayerPlayerPrioritized extends Base {

        @Override
        PrioritizedPlayerTypes prioritizedPlayerTypes() {
            return prioritizeExoPlayer();
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeNone_thenReturnsExoPlayer() {
            Player player = noPlayerCreator.create(DrmType.NONE, DrmHandler.NO_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            Player player = noPlayerCreator.create(DrmType.WIDEVINE_CLASSIC, DrmHandler.NO_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            Player player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_STREAM, STREAMING_MODULAR_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            Player player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, DOWNLOADED_MODULAR_DRM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }
}
