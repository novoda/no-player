package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImpl;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImplFactory;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImpl;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImplFactory;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class PlayerFactoryTest {

    public static abstract class Base {

        static final StreamingModularDrm STREAMING_MODULAR_DRM = mock(StreamingModularDrm.class);
        static final DownloadedModularDrm DOWNLOADED_MODULAR_DRM = mock(DownloadedModularDrm.class);
        static final Player EXO_PLAYER = mock(Player.class);
        static final Player MEDIA_PLAYER = mock(Player.class);

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        Context context;
        @Mock
        PlayerFactory.ExoPlayerCreator exoPlayerCreator;
        @Mock
        PlayerFactory.MediaPlayerCreator mediaPlayerCreator;
        @Mock
        DrmSessionCreatorFactory drmSessionCreatorFactory;

        PlayerFactory playerFactory;

        @Before
        public void setUp() {
            DrmSessionCreator drmSessionCreator = mock(DrmSessionCreator.class);
            given(drmSessionCreatorFactory.createFor(any(DrmType.class), any(DrmHandler.class))).willReturn(drmSessionCreator);
            given(exoPlayerCreator.createExoPlayer(any(Context.class), eq(drmSessionCreator))).willReturn(EXO_PLAYER);
            given(mediaPlayerCreator.createMediaPlayer(any(Context.class))).willReturn(MEDIA_PLAYER);
            playerFactory = new PlayerFactory(context, prioritizedPlayerTypes(), exoPlayerCreator, mediaPlayerCreator, drmSessionCreatorFactory);
        }

        abstract PrioritizedPlayerTypes prioritizedPlayerTypes();
    }

    public static class GivenMediaPlayerPrioritized extends Base {

        @Override
        PrioritizedPlayerTypes prioritizedPlayerTypes() {
            return prioritizeMediaPlayer();
        }

        @Test
        public void whenCreatingPlayer_thenReturnsMediaPlayer() {
            Player player = playerFactory.create();

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeNone_thenReturnsMediaPlayer() {
            Player player = playerFactory.create(DrmType.NONE, DrmHandler.NO_DRM);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            Player player = playerFactory.create(DrmType.WIDEVINE_CLASSIC, DrmHandler.NO_DRM);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            Player player = playerFactory.create(DrmType.WIDEVINE_MODULAR_STREAM, STREAMING_MODULAR_DRM);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            Player player = playerFactory.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, DOWNLOADED_MODULAR_DRM);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }

    public static class GivenExoPlayerPlayerPrioritized extends Base {

        @Override
        PrioritizedPlayerTypes prioritizedPlayerTypes() {
            return prioritizeExoPlayer();
        }

        @Test
        public void whenCreatingPlayer_thenReturnsExoPlayer() {
            Player player = playerFactory.create();

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeNone_thenReturnsExoPlayer() {
            Player player = playerFactory.create(DrmType.NONE, DrmHandler.NO_DRM);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            Player player = playerFactory.create(DrmType.WIDEVINE_CLASSIC, DrmHandler.NO_DRM);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            Player player = playerFactory.create(DrmType.WIDEVINE_MODULAR_STREAM, STREAMING_MODULAR_DRM);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            Player player = playerFactory.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, DOWNLOADED_MODULAR_DRM);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }

    public static class ExoPlayerTwoCreatorTest {

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        ExoPlayerTwoImplFactory factory;
        @Mock
        ExoPlayerTwoImpl player;
        @Mock
        Context context;
        @Mock
        DrmSessionCreator drmSessionCreator;

        private PlayerFactory.ExoPlayerCreator creator;

        @Before
        public void setUp() {
            creator = new PlayerFactory.ExoPlayerCreator(factory);
            given(factory.create(context, drmSessionCreator)).willReturn(player);
        }

        @Test
        public void whenCreatingExoPlayerTwo_thenInitialisesPlayer() {
            creator.createExoPlayer(context, drmSessionCreator);

            verify(player).initialise();
        }
    }

    public static class MediaPlayerCreatorTest {

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        AndroidMediaPlayerImplFactory factory;
        @Mock
        AndroidMediaPlayerImpl player;
        @Mock
        Context context;

        private PlayerFactory.MediaPlayerCreator creator;

        @Before
        public void setUp() {
            creator = new PlayerFactory.MediaPlayerCreator(factory);
            given(factory.create(any(Context.class))).willReturn(player);
        }

        @Test
        public void whenCreatingMediaPlayer_thenInitialisesPlayer() {
            creator.createMediaPlayer(context);

            verify(player).initialise();
        }
    }
}
