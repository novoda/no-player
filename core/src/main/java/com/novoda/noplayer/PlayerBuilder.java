package com.novoda.noplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutorCreator;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.internal.mediaplayer.NoPlayerMediaPlayerCreator;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builds instances of {@link NoPlayer} for given configurations.
 */
public class PlayerBuilder {

    private DrmType drmType = DrmType.NONE;
    private DrmHandler drmHandler = DrmHandler.NO_DRM;
    private List<PlayerType> prioritizedPlayerTypes = Arrays.asList(PlayerType.EXO_PLAYER, PlayerType.MEDIA_PLAYER);
    private boolean downgradeSecureDecoder; /* initialised to false by default */
    private boolean allowCrossProtocolRedirects; /* initialised to false by default */
    private String userAgent = "user-agent";

    /**
     * Sets {@link PlayerBuilder} to build a {@link NoPlayer} which supports Widevine classic DRM.
     *
     * @return {@link PlayerBuilder}
     * @see NoPlayer
     */
    public PlayerBuilder withWidevineClassicDrm() {
        return withDrm(DrmType.WIDEVINE_CLASSIC, DrmHandler.NO_DRM);
    }

    /**
     * Sets {@link PlayerBuilder} to build a {@link NoPlayer} which supports Widevine modular streaming DRM.
     *
     * @param streamingModularDrm Implementation of {@link StreamingModularDrm}.
     * @return {@link PlayerBuilder}
     * @see NoPlayer
     */
    public PlayerBuilder withWidevineModularStreamingDrm(StreamingModularDrm streamingModularDrm) {
        return withDrm(DrmType.WIDEVINE_MODULAR_STREAM, streamingModularDrm);
    }

    /**
     * Sets {@link PlayerBuilder} to build a {@link NoPlayer} which supports Widevine modular download DRM.
     *
     * @param downloadedModularDrm Implementation of {@link DownloadedModularDrm}.
     * @return {@link PlayerBuilder}
     * @see NoPlayer
     */
    public PlayerBuilder withWidevineModularDownloadDrm(DownloadedModularDrm downloadedModularDrm) {
        return withDrm(DrmType.WIDEVINE_MODULAR_DOWNLOAD, downloadedModularDrm);
    }

    /**
     * Sets {@link PlayerBuilder} to build a {@link NoPlayer} which supports the specified parameters.
     *
     * @param drmType    {@link DrmType}
     * @param drmHandler {@link DrmHandler}
     * @return {@link PlayerBuilder}
     * @see NoPlayer
     */
    public PlayerBuilder withDrm(DrmType drmType, DrmHandler drmHandler) {
        this.drmType = drmType;
        this.drmHandler = drmHandler;
        return this;
    }

    /**
     * Sets {@link PlayerBuilder} to build a {@link NoPlayer} which will prioritise the underlying player when
     * multiple underlying players share the same features.
     *
     * @param playerType First {@link PlayerType} with the highest priority.
     * @param playerTypes Remaining {@link PlayerType} in order of priority.
     * @return {@link PlayerBuilder}
     * @see NoPlayer
     */
    public PlayerBuilder withPriority(PlayerType playerType, PlayerType... playerTypes) {
        List<PlayerType> types = new ArrayList<>();
        types.add(playerType);
        types.addAll(Arrays.asList(playerTypes));
        prioritizedPlayerTypes = types;
        return this;
    }

    /**
     * Forces secure decoder selection to be ignored in favour of using an insecure decoder.
     * e.g. Forcing an L3 stream to play with an insecure decoder instead of a secure decoder by default.
     *
     * @return {@link PlayerBuilder}
     */
    public PlayerBuilder withDowngradedSecureDecoder() {
        downgradeSecureDecoder = true;
        return this;
    }

    /**
     * @param userAgent The application's user-agent value
     * @return {@link PlayerBuilder}
     */
    public PlayerBuilder withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Network connections will be allowed to perform redirects between HTTP and HTTPS protocols
     * @return {@link PlayerBuilder}
     */
    public PlayerBuilder allowCrossProtocolRedirects() {
        allowCrossProtocolRedirects = true;
        return this;
    }

    /**
     * Builds a new {@link NoPlayer} instance.
     *
     * @param context The {@link Context} associated with the player.
     * @return a {@link NoPlayer} instance.
     * @throws UnableToCreatePlayerException thrown when the configuration is not supported and there is no way to recover.
     * @see NoPlayer
     */
    public NoPlayer build(Context context) throws UnableToCreatePlayerException {
        Context applicationContext = context.getApplicationContext();
        Handler handler = new Handler(Looper.getMainLooper());
        ProvisionExecutorCreator provisionExecutorCreator = new ProvisionExecutorCreator();
        DrmSessionCreatorFactory drmSessionCreatorFactory = new DrmSessionCreatorFactory(
                AndroidDeviceVersion.newInstance(),
                provisionExecutorCreator,
                handler
        );
        NoPlayerCreator noPlayerCreator = new NoPlayerCreator(
                applicationContext,
                prioritizedPlayerTypes,
                NoPlayerExoPlayerCreator.newInstance(userAgent, handler),
                NoPlayerMediaPlayerCreator.newInstance(handler),
                drmSessionCreatorFactory
        );
        return noPlayerCreator.create(drmType, drmHandler, downgradeSecureDecoder, allowCrossProtocolRedirects);
    }

}
