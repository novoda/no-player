package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerErrorType;
import com.novoda.noplayer.drm.StreamingModularDrm;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ExoPlayerErrorMapperTest {

    public static class GivenSourceException {
        @Test
        public void InvalidResponseCodeException_mapsTo_InvalidResponseCode() {
            InvalidResponseCodeException cause = new InvalidResponseCodeException(404, Collections.<String, List<String>>emptyMap(), new DataSpec(Uri.EMPTY));
            ExoPlaybackException sourceException = ExoPlaybackException.createForSource(cause);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(sourceException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.INVALID_RESPONSE_CODE);
        }

        @Test
        public void ParserException_mapsTo_MalformedContent() {
            ParserException cause = new ParserException();
            ExoPlaybackException sourceException = ExoPlaybackException.createForSource(cause);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(sourceException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.MALFORMED_CONTENT);
        }

        @Test
        public void otherIOException_mapsTo_ConnectivityError() {
            IOException cause = new IOException();
            ExoPlaybackException sourceException = ExoPlaybackException.createForSource(cause);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(sourceException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.CONNECTIVITY_ERROR);
        }
    }

    public static class GivenRendererException {
        @Test
        public void DrmSessionExceptionWrappingCryptoException_mapsTo_FailedDrmDecryption() {
            MediaCodec.CryptoException cause = new MediaCodec.CryptoException(-1, null);
            DrmSession.DrmSessionException drmSessionException = new DrmSession.DrmSessionException(cause);
            ExoPlaybackException rendererException = ExoPlaybackException.createForRenderer(drmSessionException, 0);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(rendererException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.FAILED_DRM_DECRYPTION);
        }

        @Test
        public void DrmSessionExceptionWrappingDrmRequestException_mapsTo_FailedDrmRequest() {
            StreamingModularDrm.DrmRequestException cause = StreamingModularDrm.DrmRequestException.from(new Exception());
            DrmSession.DrmSessionException drmSessionException = new DrmSession.DrmSessionException(cause);
            ExoPlaybackException rendererException = ExoPlaybackException.createForRenderer(drmSessionException, 0);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(rendererException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.FAILED_DRM_REQUEST);
        }

        @Test
        public void DrmSessionExceptionWrappingOtherException_mapsTo_UnknownDrmError() {
            DrmSession.DrmSessionException drmSessionException = new DrmSession.DrmSessionException(new Exception());
            ExoPlaybackException rendererException = ExoPlaybackException.createForRenderer(drmSessionException, 0);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(rendererException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.UNKNOWN_DRM_ERROR);
        }

        @Test
        public void CryptoException_mapsTo_FailedDrmDecryption() {
            MediaCodec.CryptoException cause = new MediaCodec.CryptoException(-1, null);
            ExoPlaybackException rendererException = ExoPlaybackException.createForRenderer(cause, 0);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(rendererException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.FAILED_DRM_DECRYPTION);
        }

        @Test
        public void DrmRequestException_mapsTo_FailedDrmRequest() {
            StreamingModularDrm.DrmRequestException cause = StreamingModularDrm.DrmRequestException.from(new Exception());
            ExoPlaybackException rendererException = ExoPlaybackException.createForRenderer(cause, 0);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(rendererException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.FAILED_DRM_REQUEST);
        }

        @Test
        public void OtherException_mapsTo_UnknownRendererError() {
            ExoPlaybackException rendererException = ExoPlaybackException.createForRenderer(new Exception(), 0);

            NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(rendererException);

            assertThat(playerError.type()).isEqualTo(PlayerErrorType.UNKNOWN_RENDERER_ERROR);
        }
    }
}
