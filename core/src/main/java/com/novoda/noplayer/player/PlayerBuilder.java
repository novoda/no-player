package com.novoda.noplayer.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.mediaplayer.NoPlayerMediaPlayerCreator;
import com.novoda.utils.AndroidDeviceVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerBuilder {

    private DrmType drmType;
    private DrmHandler drmHandler;
    private PrioritizedPlayerTypes prioritizedPlayerTypes = PrioritizedPlayerTypes.prioritizeExoPlayer();
    private boolean downgradeSecureDecoder = false;

    public PlayerBuilder withWidevineClassicDrm() {
        drmType = DrmType.WIDEVINE_CLASSIC;
        drmHandler = DrmHandler.NO_DRM;
        return this;
    }

    public PlayerBuilder withWidevineModularStreamingDrm(StreamingModularDrm streamingModularDrm) {
        drmType = DrmType.WIDEVINE_MODULAR_STREAM;
        drmHandler = streamingModularDrm;
        return this;
    }

    public PlayerBuilder withWidevineModularDownloadDrm(DownloadedModularDrm downloadedModularDrm) {
        drmType = DrmType.WIDEVINE_MODULAR_DOWNLOAD;
        drmHandler = downloadedModularDrm;
        return this;
    }

    @Deprecated
    public PlayerBuilder withDrm(DrmType drmType, DrmHandler drmHandler) {
        this.drmType = drmType;
        this.drmHandler = drmHandler;
        return this;
    }

    public PlayerBuilder withPriority(PlayerType playerType, PlayerType... playerTypes) {
        List<PlayerType> types = new ArrayList<>();
        types.add(playerType);
        types.addAll(Arrays.asList(playerTypes));
        prioritizedPlayerTypes = new PrioritizedPlayerTypes(types);
        return this;
    }

    public PlayerBuilder withDowngradedSecureDecoder() {
        downgradeSecureDecoder = true;
        return this;
    }

    public Player build(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        DrmSessionCreatorFactory drmSessionCreatorFactory = new DrmSessionCreatorFactory(AndroidDeviceVersion.newInstance(), handler);
        PlayerFactory playerFactory = new PlayerFactory(
                context,
                prioritizedPlayerTypes,
                NoPlayerExoPlayerCreator.newInstance(handler),
                NoPlayerMediaPlayerCreator.newInstance(handler),
                drmSessionCreatorFactory
        );
        return playerFactory.create(drmType, drmHandler, downgradeSecureDecoder);
    }
}
