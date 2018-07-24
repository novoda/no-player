package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.offline.DownloadException;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashManifestStaleException;
import com.google.android.exoplayer2.source.hls.SampleQueueMappingException;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

import java.io.IOException;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ErrorFormatter.formatMessage;

final class ExoPlayerErrorMapper {

    private ExoPlayerErrorMapper() {
        // Static class.
    }

    static NoPlayer.PlayerError errorFor(ExoPlaybackException exception) {
        String message = formatMessage(exception.getCause());

        switch (exception.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                return mapSourceError(exception.getSourceException(), message);
            case ExoPlaybackException.TYPE_RENDERER:
                return mapRendererError(exception.getRendererException(), message);
            case ExoPlaybackException.TYPE_UNEXPECTED:
                return mapUnexpectedError(exception.getUnexpectedException(), message);
            default:
                return new NoPlayerError(PlayerErrorType.UNKNOWN, formatMessage(exception.getCause()));
        }
    }

    private static NoPlayer.PlayerError mapSourceError(IOException sourceException, String message) {
        if (sourceException instanceof SampleQueueMappingException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_SAMPLE_QUEUE_MAPPING_ERROR, message);
        }

        if (sourceException instanceof FileDataSource.FileDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READING_LOCAL_FILE_ERROR, message);
        }

        if (sourceException instanceof Loader.UnexpectedLoaderException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_UNEXPECTED_LOADING_ERROR, message);
        }

        if (sourceException instanceof DashManifestStaleException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR, message);
        }

        if (sourceException instanceof DownloadException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_DOWNLOAD_ERROR, message);
        }

        if (sourceException instanceof AdsMediaSource.AdLoadException) {
            AdsMediaSource.AdLoadException adLoadException = (AdsMediaSource.AdLoadException) sourceException;
            switch (adLoadException.type) {
                case AdsMediaSource.AdLoadException.TYPE_AD:
                    return new NoPlayerError(PlayerErrorType.SOURCE_AD_LOAD_ERROR_THEN_WILL_SKIP, message);
                case AdsMediaSource.AdLoadException.TYPE_AD_GROUP:
                    return new NoPlayerError(PlayerErrorType.SOURCE_AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP, message);
                case AdsMediaSource.AdLoadException.TYPE_ALL_ADS:
                    return new NoPlayerError(PlayerErrorType.SOURCE_ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP, message);
                case AdsMediaSource.AdLoadException.TYPE_UNEXPECTED:
                    return new NoPlayerError(PlayerErrorType.SOURCE_ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP, message);
            }
        }

        if (sourceException instanceof MergingMediaSource.IllegalMergeException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES, message);
        }

        if (sourceException instanceof ClippingMediaSource.IllegalClippingException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE, message);
        }

        if (sourceException instanceof PriorityTaskManager.PriorityTooLowException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW, message);
        }

        if (sourceException instanceof ParserException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_PARSING_MEDIA_DATA_OR_METADATA_ERROR, message);
        }

        if (sourceException instanceof Cache.CacheException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_CACHE_WRITING_DATA_ERROR, message);
        }

        if (sourceException instanceof HlsPlaylistTracker.PlaylistStuckException) {
            HlsPlaylistTracker.PlaylistStuckException playlistStuckException = (HlsPlaylistTracker.PlaylistStuckException) sourceException;
            return new NoPlayerError(PlayerErrorType.SOURCE_HLS_PLAYLIST_STUCK_SERVER_SIDE_ERROR, playlistStuckException.url + " - " + message);
        }

        if (sourceException instanceof HlsPlaylistTracker.PlaylistResetException) {
            HlsPlaylistTracker.PlaylistResetException playlistStuckException = (HlsPlaylistTracker.PlaylistResetException) sourceException;
            return new NoPlayerError(PlayerErrorType.SOURCE_HLS_PLAYLIST_SERVER_HAS_RESET, playlistStuckException.url + " - " + message);
        }

        if (sourceException instanceof HttpDataSource.HttpDataSourceException) {
            HttpDataSource.HttpDataSourceException httpDataSourceException = (HttpDataSource.HttpDataSourceException) sourceException;
            switch (httpDataSourceException.type) {
                case HttpDataSource.HttpDataSourceException.TYPE_OPEN:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_CANNOT_OPEN_ERROR, message);
                case HttpDataSource.HttpDataSourceException.TYPE_READ:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_CANNOT_READ_ERROR, message);
                case HttpDataSource.HttpDataSourceException.TYPE_CLOSE:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_CANNOT_CLOSE_ERROR, message);
            }
        }

        if (sourceException instanceof AssetDataSource.AssetDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READ_LOCAL_ASSET_ERROR, message);
        }

        if (sourceException instanceof ContentDataSource.ContentDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READ_CONTENT_URI_ERROR, message);
        }

        if (sourceException instanceof UdpDataSource.UdpDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READ_FROM_UDP_ERROR, message);
        }

        if (sourceException instanceof DataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_DATA_POSITION_OUT_OF_RANGE_ERROR, message);
        }

        return new NoPlayerError(PlayerErrorType.SOURCE_UNKNOWN_ERROR, message);

    }

    private static NoPlayer.PlayerError mapRendererError(Exception rendererException, String message) {
        return null;
    }


//    private static NoPlayer.PlayerError mapSourceError(IOException sourceException) {
//        if (sourceException instanceof HttpDataSource.InvalidResponseCodeException) {
//            return new NoPlayerError(PlayerErrorType.INVALID_RESPONSE_CODE, formatMessage(sourceException));
//        } else if (sourceException instanceof ParserException) {
//            return new NoPlayerError(PlayerErrorType.MALFORMED_CONTENT, formatMessage(sourceException));
//        } else {
//            return new NoPlayerError(PlayerErrorType.CONNECTIVITY_ERROR, formatMessage(sourceException));
//        }
//    }
//
//    private static NoPlayer.PlayerError mapRendererError(Throwable throwable, PlayerErrorType fallbackErrorType) {
//        if (throwable instanceof MediaCodec.CryptoException) {
//            return new NoPlayerError(PlayerErrorType.FAILED_DRM_DECRYPTION, formatMessage(throwable));
//        } else if (throwable instanceof StreamingModularDrm.DrmRequestException) {
//            return new NoPlayerError(PlayerErrorType.FAILED_DRM_REQUEST, formatMessage(throwable));
//        } else {
//            return new NoPlayerError(fallbackErrorType, formatMessage(throwable));
//        }
//    }

    private static NoPlayer.PlayerError mapUnexpectedError(RuntimeException unexpectedException, String message) {
        return null;
    }
}
