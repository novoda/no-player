package com.novoda.noplayer.internal.drm.provision;

import com.novoda.noplayer.drm.ModularDrmProvisionRequest;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.novoda.noplayer.internal.drm.provision.ProvisioningCapabilitiesFixtures.aProvisioningCapabilities;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class HttpPostingProvisionExecutorTest {

    private static final String PROVISION_URL = "http://provisionurl.com";
    private static final byte[] PROVISION_DATA = "provision-payload".getBytes();
    private static final ModularDrmProvisionRequest A_PROVISION_REQUEST = new ModularDrmProvisionRequest(PROVISION_URL, PROVISION_DATA);

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private HttpPostingProvisionExecutor httpPostingProvisionExecutor;
    private ArgumentCaptor<String> provisionUrlCaptor;

    @Mock
    private HttpUrlConnectionPoster httpPoster;

    @Before
    public void setUp() {
        provisionUrlCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test(expected = UnableToProvisionException.class)
    public void givenNonCapableProvisionCapabilities_whenProvisioning_thenAnUnableToProvisionExceptionIsThrown() throws IOException, UnableToProvisionException {
        ProvisioningCapabilities capabilities = aProvisioningCapabilities().thatCannotProvision();
        httpPostingProvisionExecutor = new HttpPostingProvisionExecutor(httpPoster, capabilities);

        httpPostingProvisionExecutor.execute(A_PROVISION_REQUEST);
    }

    @Test
    public void givenCapableProvisionCapabilities_whenProvisioning_thenTheRequestUrlIsExpected() throws IOException, UnableToProvisionException {
        ProvisioningCapabilities capabilities = aProvisioningCapabilities().thatCanProvision();
        httpPostingProvisionExecutor = new HttpPostingProvisionExecutor(httpPoster, capabilities);

        String expectedProvisionUrl = PROVISION_URL + "&signedRequest=" + new String(PROVISION_DATA);

        httpPostingProvisionExecutor.execute(A_PROVISION_REQUEST);
        verify(httpPoster).post(provisionUrlCaptor.capture());

        assertThat(provisionUrlCaptor.getValue()).isEqualTo(expectedProvisionUrl);
    }
}
