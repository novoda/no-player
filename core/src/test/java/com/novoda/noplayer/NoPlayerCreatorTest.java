package com.novoda.noplayer;

import android.content.Context;

import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.mediaplayer.NoPlayerMediaPlayerCreator;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(Enclosed.class)
public class NoPlayerCreatorTest {

    public abstract static class Base {

        static final boolean USE_SECURE_CODEC = false;
        static final NoPlayer EXO_PLAYER = mock(NoPlayer.class);
        static final NoPlayer MEDIA_PLAYER = mock(NoPlayer.class);

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        Context context;

        @Mock
        NoPlayerExoPlayerCreator noPlayerExoPlayerCreator;
        @Mock
        NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator;

        NoPlayerCreator noPlayerCreator;

        @Before
        public void setUp() {
            given(noPlayerExoPlayerCreator.createExoPlayer(context, USE_SECURE_CODEC)).willReturn(EXO_PLAYER);
            given(noPlayerMediaPlayerCreator.createMediaPlayer(context)).willReturn(MEDIA_PLAYER);
            noPlayerCreator = new NoPlayerCreator(context, prioritizedPlayerTypes(), noPlayerExoPlayerCreator, noPlayerMediaPlayerCreator);
        }

        abstract List<PlayerType> prioritizedPlayerTypes();
    }

    public static class GivenMediaPlayerPrioritized extends Base {

        @Override
        List<PlayerType> prioritizedPlayerTypes() {
            return Arrays.asList(PlayerType.MEDIA_PLAYER, PlayerType.EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeNone_thenReturnsMediaPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.NONE, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_CLASSIC, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_STREAM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }

    public static class GivenExoPlayerPlayerPrioritized extends Base {

        @Override
        List<PlayerType> prioritizedPlayerTypes() {
            return Arrays.asList(PlayerType.EXO_PLAYER, PlayerType.MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeNone_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.NONE, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_CLASSIC, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_STREAM, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, USE_SECURE_CODEC);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }
}
