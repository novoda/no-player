package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.net.Uri;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlaybackExceptionFactory;
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
import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerErrorType;
import com.novoda.noplayer.internal.exoplayer.error.ExoPlayerErrorMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.novoda.noplayer.DetailErrorType.ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP;
import static com.novoda.noplayer.DetailErrorType.AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP;
import static com.novoda.noplayer.DetailErrorType.AD_LOAD_ERROR_THEN_WILL_SKIP;
import static com.novoda.noplayer.DetailErrorType.ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP;
import static com.novoda.noplayer.DetailErrorType.AUDIO_DECODER_ERROR;
import static com.novoda.noplayer.DetailErrorType.AUDIO_SINK_CONFIGURATION_ERROR;
import static com.novoda.noplayer.DetailErrorType.AUDIO_SINK_INITIALISATION_ERROR;
import static com.novoda.noplayer.DetailErrorType.AUDIO_SINK_WRITE_ERROR;
import static com.novoda.noplayer.DetailErrorType.AUDIO_UNHANDLED_FORMAT_ERROR;
import static com.novoda.noplayer.DetailErrorType.CACHE_WRITING_DATA_ERROR;
import static com.novoda.noplayer.DetailErrorType.CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT;
import static com.novoda.noplayer.DetailErrorType.CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_NOT_SEEKABLE_TO_START;
import static com.novoda.noplayer.DetailErrorType.DATA_POSITION_OUT_OF_RANGE_ERROR;
import static com.novoda.noplayer.DetailErrorType.DECODING_SUBTITLE_ERROR;
import static com.novoda.noplayer.DetailErrorType.DOWNLOAD_ERROR;
import static com.novoda.noplayer.DetailErrorType.DRM_INSTANTIATION_ERROR;
import static com.novoda.noplayer.DetailErrorType.DRM_KEYS_EXPIRED_ERROR;
import static com.novoda.noplayer.DetailErrorType.DRM_SESSION_ERROR;
import static com.novoda.noplayer.DetailErrorType.FAIL_DECRYPT_DATA_DUE_NON_PLATFORM_COMPONENT_ERROR;
import static com.novoda.noplayer.DetailErrorType.HTTP_CANNOT_CLOSE_ERROR;
import static com.novoda.noplayer.DetailErrorType.HTTP_CANNOT_OPEN_ERROR;
import static com.novoda.noplayer.DetailErrorType.HTTP_CANNOT_READ_ERROR;
import static com.novoda.noplayer.DetailErrorType.INITIALISATION_ERROR;
import static com.novoda.noplayer.DetailErrorType.LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR;
import static com.novoda.noplayer.DetailErrorType.MEDIA_REQUIRES_DRM_SESSION_MANAGER_ERROR;
import static com.novoda.noplayer.DetailErrorType.MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES;
import static com.novoda.noplayer.DetailErrorType.MULTIPLE_RENDERER_MEDIA_CLOCK_ENABLED_ERROR;
import static com.novoda.noplayer.DetailErrorType.PARSING_MEDIA_DATA_OR_METADATA_ERROR;
import static com.novoda.noplayer.DetailErrorType.READING_LOCAL_FILE_ERROR;
import static com.novoda.noplayer.DetailErrorType.READ_CONTENT_URI_ERROR;
import static com.novoda.noplayer.DetailErrorType.READ_FROM_UDP_ERROR;
import static com.novoda.noplayer.DetailErrorType.READ_LOCAL_ASSET_ERROR;
import static com.novoda.noplayer.DetailErrorType.SAMPLE_QUEUE_MAPPING_ERROR;
import static com.novoda.noplayer.DetailErrorType.TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW;
import static com.novoda.noplayer.DetailErrorType.UNEXPECTED_LOADING_ERROR;
import static com.novoda.noplayer.DetailErrorType.UNSUPPORTED_DRM_SCHEME_ERROR;
import static com.novoda.noplayer.PlayerErrorType.CONNECTIVITY;
import static com.novoda.noplayer.PlayerErrorType.CONTENT_DECRYPTION;
import static com.novoda.noplayer.PlayerErrorType.DRM;
import static com.novoda.noplayer.PlayerErrorType.RENDERER_DECODER;
import static com.novoda.noplayer.PlayerErrorType.SOURCE;
import static com.novoda.noplayer.PlayerErrorType.UNEXPECTED;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ExoPlayerErrorMapperTest {

    @Parameterized.Parameter
    public PlayerErrorType playerErrorType;
    @Parameterized.Parameter(1)
    public DetailErrorType detailErrorType;
    @Parameterized.Parameter(2)
    public ExoPlaybackException exoPlaybackException;

    @Parameterized.Parameters(name = "{0} with detail {1} is mapped from {2}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{SOURCE, SAMPLE_QUEUE_MAPPING_ERROR, createSource(new SampleQueueMappingException("mimetype-sample"))},
                new Object[]{SOURCE, READING_LOCAL_FILE_ERROR, createSource(new FileDataSource.FileDataSourceException(new IOException()))},
                new Object[]{SOURCE, UNEXPECTED_LOADING_ERROR, createSource(new Loader.UnexpectedLoaderException(new Throwable()))},
                new Object[]{SOURCE, LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR, createSource(new DashManifestStaleException())},
                new Object[]{SOURCE, DOWNLOAD_ERROR, createSource(new DownloadException("download-exception"))},
                new Object[]{SOURCE, AD_LOAD_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForAd(new Exception()))},
                new Object[]{SOURCE, AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForAdGroup(new Exception(), 0))},
                new Object[]{SOURCE, ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForAllAds(new Exception()))},
                new Object[]{SOURCE, ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForUnexpected(new RuntimeException()))},
                new Object[]{SOURCE, MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES, createSource(new MergingMediaSource.IllegalMergeException(MergingMediaSource.IllegalMergeException.REASON_PERIOD_COUNT_MISMATCH))},
                new Object[]{SOURCE, CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT, createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_INVALID_PERIOD_COUNT))},
                new Object[]{SOURCE, CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_NOT_SEEKABLE_TO_START, createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_NOT_SEEKABLE_TO_START))},
                new Object[]{SOURCE, CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT, createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_INVALID_PERIOD_COUNT))},
                new Object[]{SOURCE, TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW, createSource(new PriorityTaskManager.PriorityTooLowException(1, 2))},
                new Object[]{SOURCE, PARSING_MEDIA_DATA_OR_METADATA_ERROR, createSource(new ParserException())},
                new Object[]{SOURCE, CACHE_WRITING_DATA_ERROR, createSource(new Cache.CacheException("cache-exception"))},
                new Object[]{SOURCE, READ_LOCAL_ASSET_ERROR, createSource(new AssetDataSource.AssetDataSourceException(new IOException()))},
                new Object[]{SOURCE, DATA_POSITION_OUT_OF_RANGE_ERROR, createSource(new DataSourceException(DataSourceException.POSITION_OUT_OF_RANGE))},

                new Object[]{CONNECTIVITY, HTTP_CANNOT_OPEN_ERROR, createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_OPEN))},
                new Object[]{CONNECTIVITY, HTTP_CANNOT_READ_ERROR, createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_READ))},
                new Object[]{CONNECTIVITY, HTTP_CANNOT_CLOSE_ERROR, createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_CLOSE))},
                new Object[]{CONNECTIVITY, READ_CONTENT_URI_ERROR, createSource(new ContentDataSource.ContentDataSourceException(new IOException()))},
                new Object[]{CONNECTIVITY, READ_FROM_UDP_ERROR, createSource(new UdpDataSource.UdpDataSourceException(new IOException()))},

                new Object[]{RENDERER_DECODER, AUDIO_SINK_CONFIGURATION_ERROR, createRenderer(new AudioSink.ConfigurationException("configuration-exception"))},
                new Object[]{RENDERER_DECODER, AUDIO_SINK_INITIALISATION_ERROR, createRenderer(new AudioSink.InitializationException(0, 0, 0, 0))},
                new Object[]{RENDERER_DECODER, AUDIO_SINK_WRITE_ERROR, createRenderer(new AudioSink.WriteException(0))},
                new Object[]{RENDERER_DECODER, AUDIO_UNHANDLED_FORMAT_ERROR, createRenderer(new AudioProcessor.UnhandledFormatException(0, 0, 0))},
                new Object[]{RENDERER_DECODER, AUDIO_DECODER_ERROR, createRenderer(new AudioDecoderException("audio-decoder-exception"))},
                new Object[]{RENDERER_DECODER, INITIALISATION_ERROR, createRenderer(new MediaCodecRenderer.DecoderInitializationException(Format.createSampleFormat("id", "sample-mimety[e", 0), new Throwable(), true, 0))},
                new Object[]{RENDERER_DECODER, DECODING_SUBTITLE_ERROR, createRenderer(new SubtitleDecoderException("metadata-decoder-exception"))},

                new Object[]{DRM, UNSUPPORTED_DRM_SCHEME_ERROR, createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME))},
                new Object[]{DRM, DRM_INSTANTIATION_ERROR, createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_INSTANTIATION_ERROR))},
                new Object[]{DRM, DRM_SESSION_ERROR, createRenderer(new DrmSession.DrmSessionException(new Throwable()))},
                new Object[]{DRM, DRM_KEYS_EXPIRED_ERROR, createRenderer(new KeysExpiredException())},
                new Object[]{DRM, MEDIA_REQUIRES_DRM_SESSION_MANAGER_ERROR, createRenderer(new IllegalStateException())},

                new Object[]{CONTENT_DECRYPTION, FAIL_DECRYPT_DATA_DUE_NON_PLATFORM_COMPONENT_ERROR, createRenderer(new DecryptionException(0, "decryption-exception"))},

                new Object[]{UNEXPECTED, MULTIPLE_RENDERER_MEDIA_CLOCK_ENABLED_ERROR, ExoPlaybackExceptionFactory.createForUnexpected(new IllegalStateException("Multiple renderer media clocks enabled."))},
                new Object[]{PlayerErrorType.UNKNOWN, DetailErrorType.UNKNOWN, ExoPlaybackExceptionFactory.createForUnexpected(new IllegalStateException("Any other exception"))}
                // DefaultAudioSink.InvalidAudioTrackTimestampException is private, cannot create
                // EGLSurfaceTexture.GlException is private, cannot create
                // PlaylistStuckException constructor is private, cannot create
                // PlaylistResetException constructor is private, cannot create
                // MediaCodecUtil.DecoderQueryException constructor is private, cannot create
                // DefaultDrmSessionManager.MissingSchemeDataException constructor is private, cannot create
                // Crypto Exceptions cannot be instantiated, it throws a RuntimeException("Stub!")
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
        NoPlayer.PlayerError playerError = ExoPlayerErrorMapper.errorFor(exoPlaybackException);
        assertThat(playerError.type()).isEqualTo(playerErrorType);
        assertThat(playerError.detailType()).isEqualTo(detailErrorType);
    }
}
