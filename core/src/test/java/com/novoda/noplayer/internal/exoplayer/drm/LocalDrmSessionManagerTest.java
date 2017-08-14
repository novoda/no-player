package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.model.KeySetId;

import java.util.Collections;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;

public class LocalDrmSessionManagerTest {

    private static final UUID DRM_SCHEME = UUID.randomUUID();

    private static final DrmInitData.SchemeData UNRECOGNISED_SCHEME_DATA = new DrmInitData.SchemeData(
            UUID.randomUUID(), "ANY_TYPE", "ANY_MIME_TYPE", new byte[]{}
    );

    private static final DrmInitData.SchemeData RECOGNISED_SCHEME_DATA = new DrmInitData.SchemeData(
            DRM_SCHEME, "ANY_TYPE", "ANY_MIME_TYPE", new byte[]{}
    );

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private KeySetId keySetIdToRestore;
    @Mock
    private ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;
    @Mock
    private Handler handler;
    @Mock
    private DefaultDrmSessionManager.EventListener eventListener;

    private LocalDrmSessionManager localDrmSessionManager;

    @Before
    public void setUp() {
        localDrmSessionManager = new LocalDrmSessionManager(
                keySetIdToRestore,
                mediaDrm,
                DRM_SCHEME,
                handler,
                eventListener
        );
    }

    @Test
    public void givenDrmDataContainsDrmScheme_whenCheckingCanAcquireSession_thenReturnsTrue() {
        DrmInitData drmInitData = new DrmInitData(Collections.singletonList(RECOGNISED_SCHEME_DATA));

        boolean canAcquireSession = localDrmSessionManager.canAcquireSession(drmInitData);

        assertThat(canAcquireSession).isTrue();
    }

    @Test
    public void givenDrmDataDoesNotContainDrmScheme_whenCheckingCanAcquireSession_thenReturnsFalse() {
        DrmInitData drmInitData = new DrmInitData(Collections.singletonList(UNRECOGNISED_SCHEME_DATA));

        boolean canAcquireSession = localDrmSessionManager.canAcquireSession(drmInitData);

        assertThat(canAcquireSession).isFalse();
    }
}
