package com.novoda.noplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.upstream.DataSource;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutorCreator;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorException;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.internal.utils.Optional;

/**
 * Builds instances of {@link NoPlayer} for given exoplayer configurations
 */
public class ExoPlayerBuilder {
    private DrmType drmType = DrmType.NONE;
    private DrmHandler drmHandler = DrmHandler.NO_DRM;
    private boolean downgradeSecureDecoder;
    private Optional<DataSource.Factory> dataSourceFactory = Optional.absent();

    /**
     * Sets {@link ExoPlayerBuilder} to build a {@link NoPlayer} which supports Widevine modular streaming DRM.
     *
     * @param streamingModularDrm Implementation of {@link StreamingModularDrm}.
     * @return {@link ExoPlayerBuilder}
     * @see NoPlayer
     */
    public ExoPlayerBuilder withWidevineModularStreamingDrm(StreamingModularDrm streamingModularDrm) {
        return withDrm(DrmType.WIDEVINE_MODULAR_STREAM, streamingModularDrm);
    }

    /**
     * Sets {@link ExoPlayerBuilder} to build a {@link NoPlayer} which supports Widevine modular download DRM.
     *
     * @param downloadedModularDrm Implementation of {@link DownloadedModularDrm}.
     * @return {@link ExoPlayerBuilder}
     * @see NoPlayer
     */
    public ExoPlayerBuilder withWidevineModularDownloadDrm(DownloadedModularDrm downloadedModularDrm) {
        return withDrm(DrmType.WIDEVINE_MODULAR_DOWNLOAD, downloadedModularDrm);
    }

    /**
     * Sets {@link ExoPlayerBuilder} to build a {@link NoPlayer} which supports the specified parameters.
     *
     * @param drmType    {@link DrmType}
     * @param drmHandler {@link DrmHandler}
     * @return {@link ExoPlayerBuilder}
     * @see NoPlayer
     */
    public ExoPlayerBuilder withDrm(DrmType drmType, DrmHandler drmHandler) {
        this.drmType = drmType;
        this.drmHandler = drmHandler;
        return this;
    }

    /**
     * Forces secure decoder selection to be ignored in favour of using an insecure decoder.
     * e.g. Forcing an L3 stream to play with an insecure decoder instead of a secure decoder by default.
     *
     * @return {@link ExoPlayerBuilder}
     */
    public ExoPlayerBuilder withDowngradedSecureDecoder() {
        downgradeSecureDecoder = true;
        return this;
    }

    /**
     * Allows to use a custom data source factory for Exoplayer.
     * e.g A custom data source can implement cachin mechanism
     * e.g The OkHttpDataSource extension can be used
     *
     * @param dataSourceFactory a custom {@link DataSource.Factory} to be used when creating {@link com.google.android.exoplayer2.source.MediaSource}
     * @return {@link PlayerBuilder}
     */
    public ExoPlayerBuilder withDataSourceFactory(DataSource.Factory dataSourceFactory) {
        this.dataSourceFactory = Optional.of(dataSourceFactory);
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
        Handler handler = new Handler(Looper.getMainLooper());
        ProvisionExecutorCreator provisionExecutorCreator = new ProvisionExecutorCreator();
        DrmSessionCreatorFactory drmSessionCreatorFactory = new DrmSessionCreatorFactory(
                AndroidDeviceVersion.newInstance(),
                provisionExecutorCreator,
                handler
        );
        NoPlayerExoPlayerCreator noPlayerExoPlayerCreator = NoPlayerExoPlayerCreator.newInstance(handler, dataSourceFactory);
        try {
            DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(drmType, drmHandler);
            return noPlayerExoPlayerCreator.createExoPlayer(context, drmSessionCreator, downgradeSecureDecoder);
        } catch (DrmSessionCreatorException exception) {
            throw new UnableToCreatePlayerException(exception);
        }
    }
}
