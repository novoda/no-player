package com.novoda.noplayer.internal.exoplayer.forwarder;

class ForwarderInformation {

    static final class Parameters {

        private Parameters() {
            // non-instantiable
        }

        static final String EVENT_TIME = "eventTime";
        static final String PLAY_WHEN_READY = "playWhenReady";
        static final String PLAYBACK_STATE = "playbackState";
        static final String REASON = "reason";
        static final String PLAYBACK_PARAMETERS = "playbackParameters";
        static final String REPEAT_MODE = "repeatMode";
        static final String SHUFFLE_MODE_ENABLED = "shuffleModeEnabled";
        static final String IS_LOADING = "isLoading";
        static final String ERROR = "error";
        static final String TRACK_GROUPS = "trackGroups";
        static final String TRACK_SELECTIONS = "trackSelections";
        static final String LOAD_EVENT_INFO = "loadEventInfo";
        static final String MEDIA_LOAD_DATA = "mediaLoadData";
        static final String WAS_CANCELED = "wasCanceled";
        static final String TOTAL_LOAD_TIME_MS = "totalLoadTimeMs";
        static final String TOTAL_BYTES_LOADED = "totalBytesLoaded";
        static final String BITRATE_ESTIMATE = "bitrateEstimate";
        static final String WIDTH = "width";
        static final String HEIGHT = "height";
        static final String METADATA = "metadata";
        static final String TRACK_TYPE = "trackType";
        static final String DECODER_COUNTERS = "decoderCounters";
        static final String DECODER_NAME = "decoderName";
        static final String INITIALIZATION_DURATION_MS = "initializationDurationMs";
        static final String FORMAT = "format";
        static final String AUDIO_SESSION_ID = "audioSessionId";
        static final String BUFFER_SIZE = "bufferSize";
        static final String BUFFER_SIZE_MS = "bufferSizeMs";
        static final String ELAPSED_SINCE_LAST_FEED_MS = "elapsedSinceLastFeedMs";
        static final String DROPPED_FRAMES = "droppedFrames";
        static final String ELAPSED_MS = "elapsedMs";
        static final String PIXEL_WIDTH_HEIGHT_RATIO = "pixelWidthHeightRatio";
        static final String UNAPPLIED_ROTATION_DEGREES = "unappliedRotationDegrees";
        static final String SURFACE = "surface";
        static final String TIMELINE = "timeline";
        static final String MANIFEST = "manifest";
        static final String WINDOW_INDEX = "windowIndex";
        static final String MEDIA_PERIOD_ID = "mediaPeriodId";

    }

    static final class Methods {

        private Methods() {
            // non-instantiable
        }

        static final String ON_PLAYER_STATE_CHANGED = "onPlayerStateChanged";
        static final String ON_TIMELINE_CHANGED = "onTimelineChanged";
        static final String ON_POSITION_DISCONTINUITY = "onPositionDiscontinuity";
        static final String ON_SEEK_STARTED = "onSeekStarted";
        static final String ON_SEEK_PROCESSED = "onSeekProcessed";
        static final String ON_PLAYBACK_PARAMETERS_CHANGED = "onPlaybackParametersChanged";
        static final String ON_REPEAT_MODE_CHANGED = "onRepeatModeChanged";
        static final String ON_SHUFFLE_MODE_CHANGED = "onShuffleModeChanged";
        static final String ON_PLAYER_ERROR = "onPlayerError";
        static final String ON_TRACKS_CHANGED = "onTracksChanged";
        static final String ON_LOAD_STARTED = "onLoadStarted";
        static final String ON_LOAD_COMPLETED = "onLoadCompleted";
        static final String ON_LOAD_CANCELED = "onLoadCanceled";
        static final String ON_LOAD_ERROR = "onLoadError";
        static final String ON_DOWNSTREAM_FORMAT_CHANGED = "onDownstreamFormatChanged";
        static final String ON_UPSTREAM_DISCARDED = "onUpstreamDiscarded";
        static final String ON_MEDIA_PERIOD_CREATED = "onMediaPeriodCreated";
        static final String ON_MEDIA_PERIOD_RELEASED = "onMediaPeriodReleased";
        static final String ON_READING_STARTED = "onReadingStarted";
        static final String ON_BANDWIDTH_ESTIMATE = "onBandwidthEstimate";
        static final String ON_SURFACE_SIZE_CHANGED = "onSurfaceSizeChanged";
        static final String ON_METADATA = "onMetadata";
        static final String ON_DECODER_ENABLED = "onDecoderEnabled";
        static final String ON_DECODER_INITIALIZED = "onDecoderInitialized";
        static final String ON_DECODER_INPUT_FORMAT_CHANGED = "onDecoderInputFormatChanged";
        static final String ON_DECODER_DISABLED = "onDecoderDisabled";
        static final String ON_AUDIO_SESSION_ID = "onAudioSessionId";
        static final String ON_AUDIO_UNDERRUN = "onAudioUnderrun";
        static final String ON_DROPPED_VIDEO_FRAMES = "onDroppedVideoFrames";
        static final String ON_VIDEO_SIZE_CHANGED = "onVideoSizeChanged";
        static final String ON_RENDERED_FIRST_FRAME = "onRenderedFirstFrame";
        static final String ON_DRM_KEYS_LOADED = "onDrmKeysLoaded";
        static final String ON_DRM_SESSION_MANAGER_ERROR = "onDrmSessionManagerError";
        static final String ON_DRM_KEYS_RESTORED = "onDrmKeysRestored";
        static final String ON_DRM_KEYS_REMOVED = "onDrmKeysRemoved";
        static final String ON_LOADING_CHANGED = "onLoadingChanged";
        static final String ON_SHUFFLE_MODE_ENABLED_CHANGED = "onShuffleModeEnabledChanged";
    }
}
