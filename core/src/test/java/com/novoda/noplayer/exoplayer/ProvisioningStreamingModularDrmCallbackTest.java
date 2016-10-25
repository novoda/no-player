package com.novoda.noplayer.exoplayer;

import android.media.MediaDrm;

import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;

import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class ProvisioningStreamingModularDrmCallbackTest {

    private static final UUID IGNORED_UUID = null;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    ProvisioningModularDrmCallback provisioningModularDrmCallback;

    @Mock
    MediaDrm.ProvisionRequest anyProvisionRequest;

    @Mock
    MediaDrm.KeyRequest anyKeyRequest;

    @Mock
    StreamingModularDrm streamingModularDrm;

    @Mock
    ProvisionExecutor provisionExecutor;

    @Test
    public void whenAProvisionRequestCallbackIsInvoked_thenDelegateToTheProvisionRequester() throws Exception {
        provisioningModularDrmCallback.executeProvisionRequest(IGNORED_UUID, anyProvisionRequest);

        verify(provisionExecutor).execute(anyProvisionRequest);
    }

    @Test
    public void whenAKeyRequestCallbackIsInvoked_thenDelegateToTheModularDrm() throws Exception {
        provisioningModularDrmCallback.executeKeyRequest(IGNORED_UUID, anyKeyRequest);

        verify(streamingModularDrm).executeKeyRequest(anyKeyRequest);
    }
}
