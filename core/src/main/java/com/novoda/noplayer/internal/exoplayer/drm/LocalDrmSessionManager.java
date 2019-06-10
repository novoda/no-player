package com.novoda.noplayer.internal.exoplayer.drm;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.model.KeySetId;

import java.util.HashMap;
import java.util.UUID;

import static com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator.WIDEVINE_MODULAR_UUID;

class LocalDrmSessionManager implements DrmSessionManager<FrameworkMediaCrypto> {

    private final DownloadedModularDrm downloadedModularDrm;
    private final ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;
    private final DefaultDrmSessionEventListener eventListener;
    private final UUID drmScheme;
    private final Handler handler;

    LocalDrmSessionManager(DownloadedModularDrm downloadedModularDrm,
                           ExoMediaDrm<FrameworkMediaCrypto> mediaDrm,
                           UUID drmScheme,
                           Handler handler,
                           DefaultDrmSessionEventListener eventListener) {
        this.downloadedModularDrm = downloadedModularDrm;
        this.mediaDrm = mediaDrm;
        this.eventListener = eventListener;
        this.drmScheme = drmScheme;
        this.handler = handler;
    }

    @Override
    public boolean canAcquireSession(DrmInitData drmInitData) {
        DrmInitData.SchemeData schemeData = drmInitData.get(drmScheme);
        return schemeData != null;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException") // We are forced to catch Exception as ResourceBusyException is minSdk 19
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public DrmSession<FrameworkMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        DrmSession<FrameworkMediaCrypto> drmSession;

        try {
            OfflineLicenseHelper<FrameworkMediaCrypto> offlineLicenseHelper = new OfflineLicenseHelper<>(
                    WIDEVINE_MODULAR_UUID,
                    FrameworkMediaDrm.newInstance(WIDEVINE_MODULAR_UUID),
                    new HttpMediaDrmCallback("", new DefaultHttpDataSourceFactory("android")),
                    new HashMap<String, String>()
            );

            SessionId sessionId = SessionId.of(mediaDrm.openSession());
            FrameworkMediaCrypto mediaCrypto = mediaDrm.createMediaCrypto(sessionId.asBytes());

            KeySetId keySetId = downloadedModularDrm.getKeySetId();
            byte[] licenseBytes = keySetId.asBytes();
            Pair<Long, Long> durationRemainingSec = offlineLicenseHelper.getLicenseDurationRemainingSec(keySetId.asBytes());
            if (durationRemainingSec.first == 0) {
                licenseBytes = offlineLicenseHelper.renewLicense(keySetId.asBytes());
            }

            mediaDrm.restoreKeys(sessionId.asBytes(), licenseBytes);

            drmSession = new LocalDrmSession(mediaCrypto, KeySetId.of(licenseBytes), sessionId);
        } catch (Exception exception) {
            drmSession = new InvalidDrmSession(new DrmSession.DrmSessionException(exception));
            notifyErrorListener(drmSession);
        }
        return drmSession;
    }

    private void notifyErrorListener(DrmSession<FrameworkMediaCrypto> drmSession) {
        final DrmSession.DrmSessionException error = drmSession.getError();
        handler.post(new Runnable() {
            @Override
            public void run() {
                eventListener.onDrmSessionManagerError(error);
            }
        });
    }

    @Override
    public void releaseSession(DrmSession<FrameworkMediaCrypto> drmSession) {
        FrameworkDrmSession frameworkDrmSession = (FrameworkDrmSession) drmSession;
        SessionId sessionId = frameworkDrmSession.getSessionId();
        mediaDrm.closeSession(sessionId.asBytes());
    }
}
