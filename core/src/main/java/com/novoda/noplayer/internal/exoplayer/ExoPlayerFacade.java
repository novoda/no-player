package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ads.SinglePeriodAdTimeline;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.novoda.noplayer.AdvertView;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerSurfaceHolder;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.List;

import androidx.annotation.Nullable;

// Not much we can do, wrapping ExoPlayer is a lot of work
@SuppressWarnings("PMD.GodClass")
class ExoPlayerFacade {

    private static final boolean DO_NOT_RESET_STATE = false;
    private static final long NO_RESUME_POSITION = 0L;

    private final BandwidthMeterCreator bandwidthMeterCreator;
    private final AndroidDeviceVersion androidDeviceVersion;
    private final MediaSourceFactory mediaSourceFactory;
    private final CompositeTrackSelectorCreator trackSelectorCreator;
    private final ExoPlayerCreator exoPlayerCreator;
    private final RendererTypeRequesterCreator rendererTypeRequesterCreator;
    private final Optional<NoPlayerAdsLoader> adsLoader;

    @Nullable
    private SimpleExoPlayer exoPlayer;
    @Nullable
    private CompositeTrackSelector compositeTrackSelector;
    @Nullable
    private RendererTypeRequester rendererTypeRequester;
    @Nullable
    private Options options;

    ExoPlayerFacade(BandwidthMeterCreator bandwidthMeterCreator,
                    AndroidDeviceVersion androidDeviceVersion,
                    MediaSourceFactory mediaSourceFactory,
                    CompositeTrackSelectorCreator trackSelectorCreator,
                    ExoPlayerCreator exoPlayerCreator,
                    RendererTypeRequesterCreator rendererTypeRequesterCreator,
                    Optional<NoPlayerAdsLoader> adsLoader) {
        this.bandwidthMeterCreator = bandwidthMeterCreator;
        this.androidDeviceVersion = androidDeviceVersion;
        this.mediaSourceFactory = mediaSourceFactory;
        this.trackSelectorCreator = trackSelectorCreator;
        this.exoPlayerCreator = exoPlayerCreator;
        this.rendererTypeRequesterCreator = rendererTypeRequesterCreator;
        this.adsLoader = adsLoader;
    }

    boolean isPlaying() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    PlayerState.VideoType videoType() {
        if (exoPlayer == null) {
            return PlayerState.VideoType.UNDEFINED;
        }
        if (exoPlayer.isPlayingAd()) {
            return PlayerState.VideoType.ADVERT;
        }
        return PlayerState.VideoType.CONTENT;
    }

    long mediaDurationInMillis() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getDuration();
    }

    long contentDurationInMillis() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getContentDuration();
    }

    long playheadPositionInMillis() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getCurrentPosition();
    }

    long contentPositionInMillis() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getContentPosition();
    }

    long advertBreakDurationInMillis() throws IllegalStateException {
        assertVideoLoaded();
        Timeline currentTimeline = exoPlayer.getCurrentTimeline();
        if (isSetToPlayAdvert() && adsLoader.isPresent() && currentTimeline instanceof SinglePeriodAdTimeline) {
            SinglePeriodAdTimeline adTimeline = (SinglePeriodAdTimeline) currentTimeline;
            Timeline.Period period = adTimeline.getPeriod(0, new Timeline.Period());

            int currentAdGroupIndex = exoPlayer.getCurrentAdGroupIndex();
            int advertCount = period.getAdCountInAdGroup(currentAdGroupIndex);

            long advertBreakDurationInMicros = combinedAdvertDurationInGroup(period, advertCount);
            return C.usToMs(advertBreakDurationInMicros);
        }

        return 0;
    }

    long positionInAdvertBreakInMillis() throws IllegalStateException {
        assertVideoLoaded();
        Timeline currentTimeline = exoPlayer.getCurrentTimeline();
        if (isSetToPlayAdvert() && adsLoader.isPresent() && currentTimeline instanceof SinglePeriodAdTimeline) {
            SinglePeriodAdTimeline adTimeline = (SinglePeriodAdTimeline) currentTimeline;
            Timeline.Period period = adTimeline.getPeriod(0, new Timeline.Period());

            int advertCount = exoPlayer.getCurrentAdIndexInAdGroup();

            long playedAdvertBreakDurationInMicros = combinedAdvertDurationInGroup(period, advertCount);
            return C.usToMs(playedAdvertBreakDurationInMicros) + playheadPositionInMillis();
        }

        return 0;
    }

    private boolean isSetToPlayAdvert() {
        return videoType() == PlayerState.VideoType.ADVERT;
    }

    private long combinedAdvertDurationInGroup(Timeline.Period period, int numberOfAdvertsToInclude) {
        int adGroupIndex = exoPlayer.getCurrentAdGroupIndex();
        long advertBreakDurationInMicros = 0;
        NoPlayerAdsLoader noPlayerAdsLoader = adsLoader.get();

        for (int i = 0; i < numberOfAdvertsToInclude; i++) {
            long advertDurationInMicros = period.getAdDurationUs(adGroupIndex, i);
            if (advertDurationInMicros == C.TIME_UNSET) {
                advertDurationInMicros = noPlayerAdsLoader.advertDurationBy(adGroupIndex, i);
            }
            advertBreakDurationInMicros += advertDurationInMicros;
        }

        return advertBreakDurationInMicros;
    }

    int bufferPercentage() throws IllegalStateException {
        assertVideoLoaded();
        return exoPlayer.getBufferedPercentage();
    }

    void play(long positionInMillis) throws IllegalStateException {
        seekTo(positionInMillis);
        play();
    }

    void play() throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.setPlayWhenReady(true);
    }

    void pause() throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.setPlayWhenReady(false);
    }

    void seekTo(long positionInMillis) throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.seekTo(positionInMillis);
    }

    void release() {
        if (adsLoader.isPresent()) {
            adsLoader.get().release();
        }

        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    void loadVideo(PlayerSurfaceHolder playerSurfaceHolder,
                   DrmSessionCreator drmSessionCreator,
                   Uri uri,
                   Options options,
                   ExoPlayerForwarder forwarder,
                   boolean allowFallbackDecoder) {
        this.options = options;

        DefaultBandwidthMeter bandwidthMeter = bandwidthMeterCreator.create(options.maxInitialBitrate());

        compositeTrackSelector = trackSelectorCreator.create(options, bandwidthMeter);
        exoPlayer = exoPlayerCreator.create(
                drmSessionCreator,
                forwarder.drmSessionEventListener(),
                allowFallbackDecoder,
                compositeTrackSelector.trackSelector()
        );
        rendererTypeRequester = rendererTypeRequesterCreator.createfrom(exoPlayer);
        exoPlayer.addListener(forwarder.exoPlayerEventListener());
        exoPlayer.addAnalyticsListener(forwarder.analyticsListener());
        exoPlayer.addVideoListener(forwarder.videoListener());

        setMovieAudioAttributes(exoPlayer);

        if (adsLoader.isPresent()) {
            long advertBreakInitialPositionMillis = options.getInitialAdvertBreakPositionInMillis().or(NO_RESUME_POSITION);
            long playbackResumePositionMillis = options.getInitialPositionInMillis().or(NO_RESUME_POSITION);
            adsLoader.get().bind(
                    forwarder.advertListener(),
                    advertBreakInitialPositionMillis,
                    playbackResumePositionMillis
            );
        }

        MediaSource mediaSource = mediaSourceFactory.create(
                options,
                uri,
                forwarder.mediaSourceEventListener(),
                bandwidthMeter,
                adsLoader
        );
        attachToSurface(playerSurfaceHolder);

        boolean hasInitialPosition = options.getInitialPositionInMillis().isPresent();
        if (hasInitialPosition) {
            Long initialPositionInMillis = options.getInitialPositionInMillis().get();
            exoPlayer.seekTo(initialPositionInMillis);
        }

        exoPlayer.prepare(mediaSource, !hasInitialPosition, DO_NOT_RESET_STATE);
        if (adsLoader.isPresent()) {
            adsLoader.get().setPlayer(exoPlayer);
        }
    }

    private void setMovieAudioAttributes(SimpleExoPlayer exoPlayer) {
        if (androidDeviceVersion.isLollipopTwentyOneOrAbove()) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build();
            exoPlayer.setAudioAttributes(audioAttributes);
        }
    }

    private void attachToSurface(PlayerSurfaceHolder playerSurfaceHolder) {
        playerSurfaceHolder.attach(exoPlayer);
    }

    AudioTracks getAudioTracks() throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.getAudioTracks(rendererTypeRequester);
    }

    boolean selectAudioTrack(PlayerAudioTrack audioTrack) throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester);
    }

    boolean clearAudioTrackSelection() {
        assertVideoLoaded();
        return compositeTrackSelector.clearAudioTrack(rendererTypeRequester);
    }

    boolean selectVideoTrack(PlayerVideoTrack playerVideoTrack) {
        assertVideoLoaded();
        return compositeTrackSelector.selectVideoTrack(playerVideoTrack, rendererTypeRequester);
    }

    Optional<PlayerVideoTrack> getSelectedVideoTrack() {
        assertVideoLoaded();
        return compositeTrackSelector.getSelectedVideoTrack(exoPlayer, rendererTypeRequester, options.contentType());
    }

    List<PlayerVideoTrack> getVideoTracks() {
        assertVideoLoaded();
        return compositeTrackSelector.getVideoTracks(rendererTypeRequester, options.contentType());
    }

    boolean clearVideoTrackSelection() {
        assertVideoLoaded();
        return compositeTrackSelector.clearVideoTrack(rendererTypeRequester);
    }

    void setSubtitleRendererOutput(TextRendererOutput textRendererOutput) throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.addTextOutput(textRendererOutput.output());
    }

    void removeSubtitleRendererOutput(TextRendererOutput textRendererOutput) throws IllegalStateException {
        assertVideoLoaded();
        exoPlayer.removeTextOutput(textRendererOutput.output());
    }

    boolean selectSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester);
    }

    List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.getSubtitleTracks(rendererTypeRequester);
    }

    boolean hasPlayedContent() {
        return exoPlayer != null;
    }

    boolean clearSubtitleTrackSelection() throws IllegalStateException {
        assertVideoLoaded();
        return compositeTrackSelector.clearSubtitleTrack(rendererTypeRequester);
    }

    void setRepeating(boolean repeating) {
        assertVideoLoaded();
        exoPlayer.setRepeatMode(repeating ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
    }

    void setVolume(float volume) {
        assertVideoLoaded();
        exoPlayer.setVolume(volume);
    }

    float getVolume() {
        assertVideoLoaded();
        return exoPlayer.getVolume();
    }

    void clearMaxVideoBitrate() {
        assertVideoLoaded();
        compositeTrackSelector.clearMaxVideoBitrate();
    }

    void setMaxVideoBitrate(int maxVideoBitrate) {
        assertVideoLoaded();
        compositeTrackSelector.setMaxVideoBitrate(maxVideoBitrate);
    }

    private void assertVideoLoaded() {
        if (exoPlayer == null) {
            throw new IllegalStateException("Video must be loaded before trying to interact with the player");
        }
    }

    void attach(AdvertView advertView) {
        if (adsLoader.isPresent()) {
            NoPlayerAdsLoader adsLoader = this.adsLoader.get();
            advertView.attach(adsLoader);
        }
    }

    void detach(AdvertView advertView) {
        if (adsLoader.isPresent()) {
            NoPlayerAdsLoader adsLoader = this.adsLoader.get();
            advertView.detach(adsLoader);
        }
    }

    void disableAdverts() {
        if (adsLoader.isPresent()) {
            NoPlayerAdsLoader adsLoader = this.adsLoader.get();
            adsLoader.disableAdverts();
        }
    }

    void skipAdvertBreak() {
        if (adsLoader.isPresent()) {
            NoPlayerAdsLoader adsLoader = this.adsLoader.get();
            adsLoader.skipAdvertBreak();
        }
    }

    void skipAdvert() {
        if (adsLoader.isPresent()) {
            NoPlayerAdsLoader adsLoader = this.adsLoader.get();
            adsLoader.skipAdvert();
        }
    }

    void enableAdverts() {
        if (adsLoader.isPresent()) {
            NoPlayerAdsLoader adsLoader = this.adsLoader.get();
            adsLoader.enableAdverts();
        }
    }
}
