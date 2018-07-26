package com.novoda.noplayer.internal.exoplayer.forwarder;

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
import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerErrorType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.novoda.noplayer.DetailErrorType.*;
import static com.novoda.noplayer.PlayerErrorType.*;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ExoPlayerErrorMapperTest {

    @Parameterized.Parameter(0)
    public PlayerErrorType playerErrorType;
    @Parameterized.Parameter(1)
    public DetailErrorType detailErrorType;
    @Parameterized.Parameter(2)
    public ExoPlaybackException exoPlaybackException;

    @Parameterized.Parameters(name = "{0} with detail {1} is mapped from {2}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{SOURCE, SOURCE_SAMPLE_QUEUE_MAPPING_ERROR, createSource(new SampleQueueMappingException("mimetype-sample"))},
                new Object[]{SOURCE, SOURCE_READING_LOCAL_FILE_ERROR, createSource(new FileDataSource.FileDataSourceException(new IOException()))},
                new Object[]{SOURCE, SOURCE_UNEXPECTED_LOADING_ERROR, createSource(new Loader.UnexpectedLoaderException(new Throwable()))},
                new Object[]{SOURCE, SOURCE_LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR, createSource(new DashManifestStaleException())},
                new Object[]{SOURCE, SOURCE_DOWNLOAD_ERROR, createSource(new DownloadException("download-exception"))},
                new Object[]{SOURCE, SOURCE_AD_LOAD_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForAd(new Exception()))},
                new Object[]{SOURCE, SOURCE_AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForAdGroup(new Exception(), 0))},
                new Object[]{SOURCE, SOURCE_ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForAllAds(new Exception()))},
                new Object[]{SOURCE, SOURCE_ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP, createSource(AdsMediaSource.AdLoadException.createForUnexpected(new RuntimeException()))},
                new Object[]{SOURCE, SOURCE_MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES, createSource(new MergingMediaSource.IllegalMergeException(MergingMediaSource.IllegalMergeException.REASON_PERIOD_COUNT_MISMATCH))},
                new Object[]{SOURCE, SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT, createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_INVALID_PERIOD_COUNT))},
                new Object[]{SOURCE, SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_NOT_SEEKABLE_TO_START, createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_NOT_SEEKABLE_TO_START))},
                new Object[]{SOURCE, SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT, createSource(new ClippingMediaSource.IllegalClippingException(ClippingMediaSource.IllegalClippingException.REASON_INVALID_PERIOD_COUNT))},
                new Object[]{SOURCE, SOURCE_TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW, createSource(new PriorityTaskManager.PriorityTooLowException(1, 2))},
                new Object[]{SOURCE, SOURCE_PARSING_MEDIA_DATA_OR_METADATA_ERROR, createSource(new ParserException())},
                new Object[]{SOURCE, SOURCE_CACHE_WRITING_DATA_ERROR, createSource(new Cache.CacheException("cache-exception"))},
                new Object[]{SOURCE, SOURCE_READ_LOCAL_ASSET_ERROR, createSource(new AssetDataSource.AssetDataSourceException(new IOException()))},
                new Object[]{SOURCE, SOURCE_DATA_POSITION_OUT_OF_RANGE_ERROR, createSource(new DataSourceException(DataSourceException.POSITION_OUT_OF_RANGE))},

                new Object[]{CONNECTIVITY, SOURCE_HTTP_CANNOT_OPEN_ERROR, createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_OPEN))},
                new Object[]{CONNECTIVITY, SOURCE_HTTP_CANNOT_READ_ERROR, createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_READ))},
                new Object[]{CONNECTIVITY, SOURCE_HTTP_CANNOT_CLOSE_ERROR, createSource(new HttpDataSource.HttpDataSourceException(new DataSpec(Uri.EMPTY, 0), HttpDataSource.HttpDataSourceException.TYPE_CLOSE))},
                new Object[]{CONNECTIVITY, SOURCE_READ_CONTENT_URI_ERROR, createSource(new ContentDataSource.ContentDataSourceException(new IOException()))},
                new Object[]{CONNECTIVITY, SOURCE_READ_FROM_UDP_ERROR, createSource(new UdpDataSource.UdpDataSourceException(new IOException()))},

                new Object[]{RENDERER_DECODER, RENDERER_AUDIO_SINK_CONFIGURATION_ERROR, createRenderer(new AudioSink.ConfigurationException("configuration-exception"))},
                new Object[]{RENDERER_DECODER, RENDERER_AUDIO_SINK_INITIALISATION_ERROR, createRenderer(new AudioSink.InitializationException(0, 0, 0, 0))},
                new Object[]{RENDERER_DECODER, RENDERER_AUDIO_SINK_WRITE_ERROR, createRenderer(new AudioSink.WriteException(0))},
                new Object[]{RENDERER_DECODER, RENDERER_AUDIO_UNHANDLED_FORMAT_ERROR, createRenderer(new AudioProcessor.UnhandledFormatException(0, 0, 0))},
                new Object[]{RENDERER_DECODER, RENDERER_AUDIO_DECODER_ERROR, createRenderer(new AudioDecoderException("audio-decoder-exception"))},
                new Object[]{RENDERER_DECODER, RENDERER_DECODER_INITIALISATION_ERROR, createRenderer(new MediaCodecRenderer.DecoderInitializationException(Format.createSampleFormat("id", "sample-mimety[e", 0), new Throwable(), true, 0))},
                new Object[]{RENDERER_DECODER, RENDERER_DECODING_METADATA_ERROR, createRenderer(new MetadataDecoderException("metadata-decoder-exception"))},
                new Object[]{RENDERER_DECODER, RENDERER_DECODING_SUBTITLE_ERROR, createRenderer(new SubtitleDecoderException("metadata-decoder-exception"))},

                new Object[]{DRM, RENDERER_UNSUPPORTED_DRM_SCHEME_ERROR, createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME))},
                new Object[]{DRM, RENDERER_DRM_INSTANTIATION_ERROR, createRenderer(new UnsupportedDrmException(UnsupportedDrmException.REASON_INSTANTIATION_ERROR))},
                new Object[]{DRM, RENDERER_DRM_SESSION_ERROR, createRenderer(new DrmSession.DrmSessionException(new Throwable()))},
                new Object[]{DRM, RENDERER_DRM_KEYS_EXPIRED_ERROR, createRenderer(new KeysExpiredException())},
                new Object[]{DRM, RENDERER_MEDIA_REQUIRES_DRM_SESSION_MANAGER_ERROR, createRenderer(new IllegalStateException())},

                new Object[]{CONTENT_DECRYPTION, RENDERER_FAIL_DECRYPT_DATA_DUE_NON_PLATFORM_COMPONENT_ERROR, createRenderer(new DecryptionException(0, "decryption-exception"))}

                // PlaylistStuckException constructor is private, cannot create
                // PlaylistResetException constructor is private, cannot create
                // ExoPlaybackException.createForUnexpected constructor is package-protected, cannot create
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
