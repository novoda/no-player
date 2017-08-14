package com.novoda.noplayer.internal.exoplayer.drm;

import android.media.MediaCryptoException;
import android.media.NotProvisionedException;
import android.media.ResourceBusyException;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaCryptoFixture;
import com.novoda.noplayer.model.KeySetId;

import java.util.Collections;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class LocalDrmSessionManagerTest {

    private static final Looper IGNORED_LOOPER = null;
    private static final DrmInitData IGNORED_DRM_DATA = null;

    private static final KeySetId KEY_SET_ID_TO_RESTORE = KeySetId.of(new byte[12]);
    private static final SessionId SESSION_ID = SessionId.of(new byte[10]);
    private static final UUID DRM_SCHEME = UUID.randomUUID();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;
    @Mock
    private Handler handler;
    @Mock
    private DefaultDrmSessionManager.EventListener eventListener;
    @Mock
    private ExoMediaCrypto exoMediaCrypto;

    private LocalDrmSessionManager localDrmSessionManager;
    private FrameworkMediaCrypto frameworkMediaCrypto;

    @Before
    public void setUp() throws ResourceBusyException, NotProvisionedException, MediaCryptoException {
        frameworkMediaCrypto = FrameworkMediaCryptoFixture.aFrameworkMediaCrypto().build();
        given(mediaDrm.openSession()).willReturn(SESSION_ID.asBytes());

        localDrmSessionManager = new LocalDrmSessionManager(
                KEY_SET_ID_TO_RESTORE,
                mediaDrm,
                DRM_SCHEME,
                handler,
                eventListener
        );
    }

    @Test
    public void givenDrmDataContainsDrmScheme_whenCheckingCanAcquireSession_thenReturnsTrue() {
        DrmInitData.SchemeData recognisedSchemeData = new DrmInitData.SchemeData(
                DRM_SCHEME, "ANY_TYPE", "ANY_MIME_TYPE", new byte[]{}
        );
        DrmInitData drmInitData = new DrmInitData(Collections.singletonList(recognisedSchemeData));

        boolean canAcquireSession = localDrmSessionManager.canAcquireSession(drmInitData);

        assertThat(canAcquireSession).isTrue();
    }

    @Test
    public void givenDrmDataDoesNotContainDrmScheme_whenCheckingCanAcquireSession_thenReturnsFalse() {
        DrmInitData.SchemeData unrecognisedSchemeData = new DrmInitData.SchemeData(
                UUID.randomUUID(), "ANY_TYPE", "ANY_MIME_TYPE", new byte[]{}
        );
        DrmInitData drmInitData = new DrmInitData(Collections.singletonList(unrecognisedSchemeData));

        boolean canAcquireSession = localDrmSessionManager.canAcquireSession(drmInitData);

        assertThat(canAcquireSession).isFalse();
    }

    @Test
    public void givenValidMediaDrm_whenAcquiringSession_thenRestoresKeys() throws MediaCryptoException {
        given(mediaDrm.createMediaCrypto(DRM_SCHEME, SESSION_ID.asBytes())).willReturn(frameworkMediaCrypto);

        localDrmSessionManager.acquireSession(IGNORED_LOOPER, IGNORED_DRM_DATA);

        verify(mediaDrm).restoreKeys(SESSION_ID.asBytes(), KEY_SET_ID_TO_RESTORE.asBytes());
    }

    @Test
    public void givenValidMediaDrm_whenAcquiringSession_thenReturnsLocalDrmSession() throws MediaCryptoException {
        given(mediaDrm.createMediaCrypto(DRM_SCHEME, SESSION_ID.asBytes())).willReturn(frameworkMediaCrypto);

        DrmSession<FrameworkMediaCrypto> drmSession = localDrmSessionManager.acquireSession(IGNORED_LOOPER, IGNORED_DRM_DATA);

        LocalDrmSession localDrmSession = new LocalDrmSession(frameworkMediaCrypto, KEY_SET_ID_TO_RESTORE, SESSION_ID);
        assertThat(drmSession).isEqualTo(localDrmSession);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void givenOpeningSessionError_whenAcquiringSession_thenNotifiesErrorEventListenerOnHandler() throws ResourceBusyException, NotProvisionedException {
        given(mediaDrm.openSession()).willThrow(new ResourceBusyException("resource is busy"));

        localDrmSessionManager.acquireSession(IGNORED_LOOPER, IGNORED_DRM_DATA);

        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(handler).post(argumentCaptor.capture());
        argumentCaptor.getValue().run();
        verify(eventListener).onDrmSessionManagerError(any(DrmSession.DrmSessionException.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void givenOpeningSessionError_whenAcquiringSession_thenReturnsInvalidDrmSession() throws ResourceBusyException, NotProvisionedException {
        ResourceBusyException resourceBusyException = new ResourceBusyException("resource is busy");
        given(mediaDrm.openSession()).willThrow(resourceBusyException);

        DrmSession<FrameworkMediaCrypto> drmSession = localDrmSessionManager.acquireSession(IGNORED_LOOPER, IGNORED_DRM_DATA);

        assertThat(drmSession).isInstanceOf(InvalidDrmSession.class);
        assertThat(drmSession.getError().getCause()).isEqualTo(resourceBusyException);
    }

    @Test
    public void givenAcquiredSession_whenReleasingSession_thenClosesCurrentSession() {
        DrmSession<FrameworkMediaCrypto> drmSession = new LocalDrmSession(frameworkMediaCrypto, KEY_SET_ID_TO_RESTORE, SESSION_ID);

        localDrmSessionManager.releaseSession(drmSession);

        verify(mediaDrm).closeSession(SESSION_ID.asBytes());
    }
}
