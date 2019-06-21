package com.novoda.noplayer;

import android.content.Context;

import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorException;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.internal.mediaplayer.NoPlayerMediaPlayerCreator;
import com.novoda.noplayer.model.KeySetId;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(Enclosed.class)
public class NoPlayerCreatorTest {

    public abstract static class Base {

        static final KeySetId KEY_SET_ID = KeySetId.of(new byte[0]);
        static final boolean USE_SECURE_CODEC = false;
        static final boolean ALLOW_CROSS_PROTOCOL_REDIRECTS = false;
        static final KeyRequestExecutor KEY_REQUEST_EXECUTOR = mock(KeyRequestExecutor.class);
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
        @Mock
        DrmSessionCreator drmSessionCreator;
        @Mock
        DrmSessionCreatorFactory drmSessionCreatorFactory;

        NoPlayerCreator noPlayerCreator;

        @Before
        public void setUp() throws DrmSessionCreatorException {
            given(drmSessionCreatorFactory.createFor(any(DrmType.class), any(KeyRequestExecutor.class), any(KeySetId.class))).willReturn(drmSessionCreator);
            given(noPlayerExoPlayerCreator.createExoPlayer(context, drmSessionCreator, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS)).willReturn(EXO_PLAYER);
            given(noPlayerMediaPlayerCreator.createMediaPlayer(context)).willReturn(MEDIA_PLAYER);
            noPlayerCreator = new NoPlayerCreator(context, prioritizedPlayerTypes(), noPlayerExoPlayerCreator, noPlayerMediaPlayerCreator, drmSessionCreatorFactory);
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
            NoPlayer player = noPlayerCreator.create(DrmType.NONE, KeyRequestExecutor.NOT_REQUIRED, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_CLASSIC, KeyRequestExecutor.NOT_REQUIRED, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_STREAM, KEY_REQUEST_EXECUTOR, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, KEY_REQUEST_EXECUTOR, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

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
            NoPlayer player = noPlayerCreator.create(DrmType.NONE, KeyRequestExecutor.NOT_REQUIRED, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineClassic_thenReturnsMediaPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_CLASSIC, KeyRequestExecutor.NOT_REQUIRED, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(MEDIA_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularStream_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_STREAM, KEY_REQUEST_EXECUTOR, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }

        @Test
        public void whenCreatingPlayerWithDrmTypeWidevineModularDownload_thenReturnsExoPlayer() {
            NoPlayer player = noPlayerCreator.create(DrmType.WIDEVINE_MODULAR_DOWNLOAD, KEY_REQUEST_EXECUTOR, KEY_SET_ID, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

            assertThat(player).isEqualTo(EXO_PLAYER);
        }
    }
}
