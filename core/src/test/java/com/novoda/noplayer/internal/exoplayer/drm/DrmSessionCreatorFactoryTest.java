package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.novoda.noplayer.UnableToCreatePlayerException;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutorCreator;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import utils.ExceptionMatcher;

import static org.fest.assertions.api.Assertions.assertThat;

public class DrmSessionCreatorFactoryTest {

    private static final AndroidDeviceVersion UNSUPPORTED_MEDIA_DRM_DEVICE_VERSION = new AndroidDeviceVersion(17);
    private static final DrmHandler IGNORED_DRM_HANDLER = DrmHandler.NO_DRM;
    private static final AndroidDeviceVersion SUPPORTED_MEDIA_DRM_DEVICE = new AndroidDeviceVersion(18);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private Handler handler;
    @Mock
    private DownloadedModularDrm downloadedModularDrm;
    @Mock
    private StreamingModularDrm streamingModularDrm;
    @Mock
    private ProvisionExecutorCreator provisionExecutorCreator;

    private DrmSessionCreatorFactory drmSessionCreatorFactory;

    @Before
    public void setUp() {
        drmSessionCreatorFactory = new DrmSessionCreatorFactory(SUPPORTED_MEDIA_DRM_DEVICE, provisionExecutorCreator, handler);
    }

    @Test
    public void givenDrmTypeNone_whenCreatingDrmSessionCreator_thenReturnsNoDrmSession() throws DrmSessionCreatorException {
        DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(DrmType.NONE, IGNORED_DRM_HANDLER);

        assertThat(drmSessionCreator).isInstanceOf(NoDrmSessionCreator.class);
    }

    @Test
    public void givenDrmTypeWidevineClassic_whenCreatingDrmSessionCreator_thenReturnsNoDrmSession() throws DrmSessionCreatorException {
        DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(DrmType.WIDEVINE_CLASSIC, IGNORED_DRM_HANDLER);

        assertThat(drmSessionCreator).isInstanceOf(NoDrmSessionCreator.class);
    }

    @Test
    public void givenDrmTypeWidevineModularStream_whenCreatingDrmSessionCreator_thenReturnsStreaming() throws DrmSessionCreatorException {
        DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(DrmType.WIDEVINE_MODULAR_STREAM, streamingModularDrm);

        assertThat(drmSessionCreator).isInstanceOf(StreamingDrmSessionCreator.class);
    }

    @Test
    public void givenDrmTypeWidevineModularStream_andAndroidVersionDoesNotSupportMediaDrmApis_whenCreatingDrmSessionCreator_thenThrowsUnableToCreatePlayerException() throws DrmSessionCreatorException {
        drmSessionCreatorFactory = new DrmSessionCreatorFactory(UNSUPPORTED_MEDIA_DRM_DEVICE_VERSION, provisionExecutorCreator, handler);

        String message = "Device must be target: 18 but was: 17 for DRM type: WIDEVINE_MODULAR_STREAM";
        thrown.expect(ExceptionMatcher.matches(message, UnableToCreatePlayerException.class));

        drmSessionCreatorFactory.createFor(DrmType.WIDEVINE_MODULAR_STREAM, IGNORED_DRM_HANDLER);
    }

    @Test
    public void givenDrmTypeWidevineModularDownload_whenCreatingDrmSessionCreator_thenReturnsDownload() throws DrmSessionCreatorException {
        DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(DrmType.WIDEVINE_MODULAR_DOWNLOAD, downloadedModularDrm);

        assertThat(drmSessionCreator).isInstanceOf(DownloadDrmSessionCreator.class);
    }

    @Test
    public void givenDrmTypeWidevineDownloadStream_andAndroidVersionDoesNotSupportMediaDrmApis_whenCreatingDrmSessionCreator_thenThrowsUnableToCreatePlayerException() throws DrmSessionCreatorException {
        drmSessionCreatorFactory = new DrmSessionCreatorFactory(UNSUPPORTED_MEDIA_DRM_DEVICE_VERSION, provisionExecutorCreator, handler);

        String message = "Device must be target: 18 but was: 17 for DRM type: WIDEVINE_MODULAR_DOWNLOAD";
        thrown.expect(ExceptionMatcher.matches(message, UnableToCreatePlayerException.class));

        drmSessionCreatorFactory.createFor(DrmType.WIDEVINE_MODULAR_DOWNLOAD, IGNORED_DRM_HANDLER);
    }
}
