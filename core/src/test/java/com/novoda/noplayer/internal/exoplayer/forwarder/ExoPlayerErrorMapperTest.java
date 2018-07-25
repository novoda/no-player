package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.AudioDecoderException;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.DecryptionException;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.KeysExpiredException;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.metadata.MetadataDecoderException;
import com.google.android.exoplayer2.offline.DownloadException;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashManifestStaleException;
import com.google.android.exoplayer2.source.hls.SampleQueueMappingException;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.novoda.noplayer.PlayerErrorType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.novoda.noplayer.PlayerErrorType.*;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ExoPlayerErrorMapperTest {

    @Parameterized.Parameter(0)
    public ExoPlaybackException exoPlaybackException;
    @Parameterized.Parameter(1)
    public PlayerErrorType playerErrorType;

    @Parameterized.Parameters(name = "Exception: {0} is mapped to {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{createSource(new SampleQueueMappingException("mimetype-sample")), SOURCE_SAMPLE_QUEUE_MAPPING_ERROR},
                new Object[]{createSource(new FileDataSource.FileDataSourceException(new IOException())), SOURCE_READING_LOCAL_FILE_ERROR},
                new Object[]{createSource(new Loader.UnexpectedLoaderException(new Throwable())), SOURCE_UNEXPECTED_LOADING_ERROR},
                new Object[]{createSource(new DashManifestStaleException()), PlayerErrorType.SOURCE_LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR},
                new Object[]{createSource(new DownloadException("download-exception")), PlayerErrorType.SOURCE_DOWNLOAD_ERROR},
                new Object[]{createSource(AdsMediaSource.AdLoadException.createForAd(new Exception())), PlayerErrorType.SOURCE_AD_LOAD_ERROR_THEN_WILL_SKIP},
                new Object[]{createSource(AdsMediaSource.AdLoadException.createForAdGroup(new Exception(), 0)), PlayerErrorType.SOURCE_AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP},
                new Object[]{createSource(AdsMediaSource.AdLoadException.createForAllAds(new Exception())), PlayerErrorType.SOURCE_ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP},
                new Object[]{createSource(AdsMediaSource.AdLoadException.createForUnexpected(new RuntimeException())), PlayerErrorType.SOURCE_ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP},
                new Object[]{createSource(new MergingMediaSource.IllegalMergeException(MergingMediaSource.IllegalMergeException.REASON_PERIOD_COUNT_MISMATCH)), PlayerErrorType.SOURCE_MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES},
                new Object[]{createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_INVALID_PERIOD_COUNT)), PlayerErrorType.SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT},
                new Object[]{createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_NOT_SEEKABLE_TO_START)), PlayerErrorType.SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_NOT_SEEKABLE_TO_START},
                new Object[]{createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_START_EXCEEDS_END)), PlayerErrorType.SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_START_EXCEEDS_END},
                new Object[]{createSource(new PriorityTaskManager.PriorityTooLowException(1, 2)), PlayerErrorType.SOURCE_TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW},
                new Object[]{createSource(new ParserException()), PlayerErrorType.SOURCE_PARSING_MEDIA_DATA_OR_METADATA_ERROR},
                new Object[]{createSource(new Cache.CacheException("cache-exception")), PlayerErrorType.SOURCE_CACHE_WRITING_DATA_ERROR},
                // PlaylistStuckException constructor is private, cannot create
                // PlaylistResetException constructor is private, cannot create
                new Object[]{createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_OPEN)), PlayerErrorType.SOURCE_HTTP_CANNOT_OPEN_ERROR},
                new Object[]{createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_READ)), PlayerErrorType.SOURCE_HTTP_CANNOT_READ_ERROR},
                new Object[]{createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_CLOSE)), PlayerErrorType.SOURCE_HTTP_CANNOT_CLOSE_ERROR},
                new Object[]{createSource(new AssetDataSource.AssetDataSourceException(new IOException())), PlayerErrorType.SOURCE_READ_LOCAL_ASSET_ERROR},
                new Object[]{createSource(new ContentDataSource.ContentDataSourceException(new IOException())), PlayerErrorType.SOURCE_READ_CONTENT_URI_ERROR},
                new Object[]{createSource(new UdpDataSource.UdpDataSourceException(new IOException())), PlayerErrorType.SOURCE_READ_FROM_UDP_ERROR},
                new Object[]{createSource(new DataSourceException(DataSourceException.POSITION_OUT_OF_RANGE)), PlayerErrorType.SOURCE_DATA_POSITION_OUT_OF_RANGE_ERROR},

                new Object[]{createRenderer(new AudioSink.ConfigurationException("configuration-exception")), PlayerErrorType.RENDERER_AUDIO_SINK_CONFIGURATION_ERROR},
                new Object[]{createRenderer(new AudioSink.InitializationException(0, 0, 0, 0)), PlayerErrorType.RENDERER_AUDIO_SINK_INITIALISATION_ERROR},
                new Object[]{createRenderer(new AudioSink.WriteException(0)), PlayerErrorType.RENDERER_AUDIO_SINK_WRITE_ERROR},
                new Object[]{createRenderer(new AudioProcessor.UnhandledFormatException(0, 0, 0)), PlayerErrorType.RENDERER_AUDIO_UNHANDLED_FORMAT_ERROR},
                new Object[]{createRenderer(new AudioDecoderException("audio-decoder-exception")), PlayerErrorType.RENDERER_AUDIO_DECODER_ERROR},
                new Object[]{createRenderer(new MediaCodecRenderer.DecoderInitializationException(Format.createSampleFormat("id", "sample-mimety[e", 0), new Throwable(), true, 0)), PlayerErrorType.RENDERER_DECODER_INITIALISATION_ERROR},
                // MediaCodecUtil.DecoderQueryException constructor is private, cannot create
                new Object[]{createRenderer(new MetadataDecoderException("metadata-decoder-exception")), PlayerErrorType.RENDERER_DECODING_METADATA_ERROR},
                new Object[]{createRenderer(new SubtitleDecoderException("metadata-decoder-exception")), PlayerErrorType.RENDERER_DECODING_SUBTITLE_ERROR},
                new Object[]{createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME)), PlayerErrorType.RENDERER_UNSUPPORTED_DRM_SCHEME_ERROR},
                new Object[]{createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_INSTANTIATION_ERROR)), PlayerErrorType.RENDERER_DRM_INSTANTIATION_ERROR},
                new Object[]{createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_INSTANTIATION_ERROR)), PlayerErrorType.RENDERER_DRM_INSTANTIATION_ERROR},
                // DefaultDrmSessionManager.MissingSchemeDataException constructor is private, cannot create
                new Object[]{createRenderer(new DrmSession.DrmSessionException(new Throwable())), PlayerErrorType.RENDERER_DRM_SESSION_ERROR},
                new Object[]{createRenderer(new KeysExpiredException()), PlayerErrorType.RENDERER_DRM_KEYS_EXPIRED_ERROR},
                new Object[]{createRenderer(new DecryptionException(0, "decryption-exception")), PlayerErrorType.RENDERER_FAIL_DECRYPT_DATA_DUE_NON_PLATFORM_COMPONENT_ERROR},
                new Object[]{createRenderer(new MediaCodec.CryptoException(MediaCodec.CryptoException.ERROR_NO_KEY, "no-key")), PlayerErrorType.RENDERER_CRYPTO_KEY_NOT_FOUND_WHEN_DECRYPTION_ERROR},
                new Object[]{createRenderer(new MediaCodec.CryptoException(MediaCodec.CryptoException.ERROR_KEY_EXPIRED, "key-expired")), PlayerErrorType.RENDERER_CRYPTO_KEY_EXPIRED_ERROR},
                new Object[]{createRenderer(new MediaCodec.CryptoException(MediaCodec.CryptoException.ERROR_RESOURCE_BUSY, "resource-busy")), PlayerErrorType.RENDERER_CRYPTO_RESOURCE_BUSY_ERROR_THEN_SHOULD_RETRY},
                new Object[]{createRenderer(new MediaCodec.CryptoException(MediaCodec.CryptoException.ERROR_INSUFFICIENT_OUTPUT_PROTECTION, "insufficient-output-protection")), PlayerErrorType.RENDERER_CRYPTO_INSUFFICIENT_OUTPUT_PROTECTION_ERROR},
                new Object[]{createRenderer(new MediaCodec.CryptoException(MediaCodec.CryptoException.ERROR_SESSION_NOT_OPENED, "session-not-opened")), PlayerErrorType.RENDERER_CRYPTO_DECRYPTION_ATTEMPTED_ON_CLOSED_SEDDION_ERROR},
                new Object[]{createRenderer(new MediaCodec.CryptoException(MediaCodec.CryptoException.ERROR_UNSUPPORTED_OPERATION, "unsupported-operation")), PlayerErrorType.RENDERER_CRYPTO_LICENSE_POLICY_REQUIRED_NOT_SUPPORTED_BY_DEVICE_ERROR},
                new Object[]{createRenderer(new IllegalStateException()), PlayerErrorType.RENDERER_MEDIA_REQUIRES_DRM_SESSION_MANAGER_ERROR}

                // ExoPlaybackException.createForUnexpected constructor is package-protected, cannot create
        );
    }

    private static ExoPlaybackException createSource(IOException exception) {
        return ExoPlaybackException.createForSource(exception);
    }

    private static ExoPlaybackException createRenderer(Exception exception) {
        return ExoPlaybackException.createForRenderer(exception, 0);
    }

    @Test
    public void mapErrors() {
        assertThat(ExoPlayerErrorMapper.errorFor(exoPlaybackException).type()).isEqualTo(playerErrorType);
    }
}
