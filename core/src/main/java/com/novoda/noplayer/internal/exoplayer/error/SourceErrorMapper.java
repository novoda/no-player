package com.novoda.noplayer.internal.exoplayer.error;

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
import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

import java.io.IOException;

final class SourceErrorMapper {

    private SourceErrorMapper() {
        // non-instantiable class
    }

    @SuppressWarnings({"PMD.StdCyclomaticComplexity", "PMD.CyclomaticComplexity", "PMD.ModifiedCyclomaticComplexity", "PMD.NPathComplexity"})
    static NoPlayer.PlayerError map(IOException sourceException, String message) {
        if (sourceException instanceof SampleQueueMappingException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.SAMPLE_QUEUE_MAPPING_ERROR, message);
        }

        if (sourceException instanceof FileDataSource.FileDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.READING_LOCAL_FILE_ERROR, message);
        }

        if (sourceException instanceof Loader.UnexpectedLoaderException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.UNEXPECTED_LOADING_ERROR, message);
        }

        if (sourceException instanceof DashManifestStaleException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR, message);
        }

        if (sourceException instanceof DownloadException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.DOWNLOAD_ERROR, message);
        }

        if (sourceException instanceof AdsMediaSource.AdLoadException) {
            return mapAdsError((AdsMediaSource.AdLoadException) sourceException, message);
        }

        if (sourceException instanceof MergingMediaSource.IllegalMergeException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES, message);
        }

        if (sourceException instanceof ClippingMediaSource.IllegalClippingException) {
            return mapClippingError((ClippingMediaSource.IllegalClippingException) sourceException, message);
        }

        if (sourceException instanceof PriorityTaskManager.PriorityTooLowException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW, message);
        }

        if (sourceException instanceof ParserException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.PARSING_MEDIA_DATA_OR_METADATA_ERROR, message);
        }

        if (sourceException instanceof Cache.CacheException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.CACHE_WRITING_DATA_ERROR, message);
        }

        if (sourceException instanceof HlsPlaylistTracker.PlaylistStuckException) {
            HlsPlaylistTracker.PlaylistStuckException playlistStuckException = (HlsPlaylistTracker.PlaylistStuckException) sourceException;
            return new NoPlayerError(
                    PlayerErrorType.CONNECTIVITY,
                    DetailErrorType.HLS_PLAYLIST_STUCK_SERVER_SIDE_ERROR,
                    playlistStuckException.url + " - " + message
            );
        }

        if (sourceException instanceof HlsPlaylistTracker.PlaylistResetException) {
            HlsPlaylistTracker.PlaylistResetException playlistStuckException = (HlsPlaylistTracker.PlaylistResetException) sourceException;
            return new NoPlayerError(
                    PlayerErrorType.CONNECTIVITY,
                    DetailErrorType.HLS_PLAYLIST_SERVER_HAS_RESET,
                    playlistStuckException.url + " - " + message
            );
        }

        if (sourceException instanceof HttpDataSource.HttpDataSourceException) {
            return mapHttpDataSourceException((HttpDataSource.HttpDataSourceException) sourceException, message);
        }

        if (sourceException instanceof AssetDataSource.AssetDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.READ_LOCAL_ASSET_ERROR, message);
        }

        if (sourceException instanceof ContentDataSource.ContentDataSourceException) {
            return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.READ_CONTENT_URI_ERROR, message);
        }

        if (sourceException instanceof UdpDataSource.UdpDataSourceException) {
            return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.READ_FROM_UDP_ERROR, message);
        }

        if (sourceException instanceof DataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.DATA_POSITION_OUT_OF_RANGE_ERROR, message);
        }

        return new NoPlayerError(PlayerErrorType.UNKNOWN, DetailErrorType.UNKNOWN, message);
    }

    private static NoPlayer.PlayerError mapAdsError(AdsMediaSource.AdLoadException adLoadException, String message) {
        switch (adLoadException.type) {
            case AdsMediaSource.AdLoadException.TYPE_AD:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.AD_LOAD_ERROR_THEN_WILL_SKIP, message);
            case AdsMediaSource.AdLoadException.TYPE_AD_GROUP:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP, message);
            case AdsMediaSource.AdLoadException.TYPE_ALL_ADS:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP, message);
            case AdsMediaSource.AdLoadException.TYPE_UNEXPECTED:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP, message);
            default:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.UNKNOWN, message);
        }
    }

    private static NoPlayer.PlayerError mapClippingError(ClippingMediaSource.IllegalClippingException illegalClippingException, String message) {
        switch (illegalClippingException.reason) {
            case ClippingMediaSource.IllegalClippingException.REASON_INVALID_PERIOD_COUNT:
                return new NoPlayerError(
                        PlayerErrorType.SOURCE,
                        DetailErrorType.CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_INVALID_PERIOD_COUNT,
                        message
                );
            case ClippingMediaSource.IllegalClippingException.REASON_NOT_SEEKABLE_TO_START:
                return new NoPlayerError(
                        PlayerErrorType.SOURCE,
                        DetailErrorType.CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_NOT_SEEKABLE_TO_START,
                        message
                );
            case ClippingMediaSource.IllegalClippingException.REASON_START_EXCEEDS_END:
                return new NoPlayerError(
                        PlayerErrorType.SOURCE,
                        DetailErrorType.CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE_START_EXCEEDS_END,
                        message
                );
            default:
                return new NoPlayerError(
                        PlayerErrorType.SOURCE,
                        DetailErrorType.UNKNOWN,
                        message
                );
        }
    }

    private static NoPlayer.PlayerError mapHttpDataSourceException(HttpDataSource.HttpDataSourceException httpDataSourceException, String message) {
        switch (httpDataSourceException.type) {
            case HttpDataSource.HttpDataSourceException.TYPE_OPEN:
                return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.HTTP_CANNOT_OPEN_ERROR, message);
            case HttpDataSource.HttpDataSourceException.TYPE_READ:
                return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.HTTP_CANNOT_READ_ERROR, message);
            case HttpDataSource.HttpDataSourceException.TYPE_CLOSE:
                return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.HTTP_CANNOT_CLOSE_ERROR, message);
            default:
                return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.UNKNOWN, message);
        }
    }
}
