package com.novoda.noplayer.drm.provision;

import android.media.MediaDrm;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProvisionExecutorTest {

    private static final String PROVISION_URL = "http://provisionurl.com";
    private static final byte[] PROVISION_DATA = "provision-payload".getBytes();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ProvisionExecutor provisionExecutor;
    private ArgumentCaptor<String> provisionUrlCaptor;

    @Mock
    HttpPoster httpPoster;

    @Mock
    MediaDrm.ProvisionRequest provisionRequest;

    @Mock
    ProvisioningCapabilities capabilities;

    @Before
    public void setUp() {
        provisionUrlCaptor = ArgumentCaptor.forClass(String.class);
        when(provisionRequest.getData()).thenReturn(PROVISION_DATA);
        when(provisionRequest.getDefaultUrl()).thenReturn(PROVISION_URL);

        provisionExecutor = new ProvisionExecutor(httpPoster, capabilities);
    }

    @Test(expected = UnableToProvisionException.class)
    public void givenNonCapableProvisionCapabilities_whenProvisioning_thenAnUnableToProvisionExceptionIsThrown() throws IOException, UnableToProvisionException {
        when(capabilities.canProvision()).thenReturn(false);

        provisionExecutor.execute(provisionRequest);
    }

    @Test
    public void givenCapableProvisionCapabilities_whenProvisioning_thenAHttpPostIsMade() throws IOException, UnableToProvisionException {
        when(capabilities.canProvision()).thenReturn(true);

        provisionExecutor.execute(provisionRequest);

        verify(httpPoster).post(anyString());
    }

    @Test
    public void givenCapableProvisionCapabilities_whenProvisioning_thenTheRequestUrlIsExpected() throws IOException, UnableToProvisionException {
        when(capabilities.canProvision()).thenReturn(true);
        String expectedProvisionUrl = PROVISION_URL + "&signedRequest=" + new String(PROVISION_DATA);

        provisionExecutor.execute(provisionRequest);
        verify(httpPoster).post(provisionUrlCaptor.capture());

        assertThat(provisionUrlCaptor.getValue()).isEqualTo(expectedProvisionUrl);
    }
}
