package com.novoda.noplayer.internal.mediaplayer;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PlayerCheckerTest {

    private static final String PROP_USE_NU_PLAYER = "media.stagefright.use-nuplayer";
    private static final String PROP_USE_AWESOME_PLAYER_PERSIST = "persist.sys.media.use-awesome";
    private static final String PROP_USE_AWESOME_PLAYER_MEDIA = "media.stagefright.use-awesome";

    @Mock
    SystemProperties systemProperties;

    private MediaPlayerTypeReader checker;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenTheUserIsOnLollipopWhenTheAwesomePlayerPersistPropertyIsPresentThenAwesomePlayerIsDetected() throws Exception {
        givenTheUserIsOnOSVersion(Build.VERSION_CODES.LOLLIPOP);

        whenPropertyisPresent(PROP_USE_AWESOME_PLAYER_PERSIST);

        thenPlayerTypeIs(AndroidMediaPlayerType.AWESOME);
    }

    @Test
    public void givenTheUserIsOnLollipopWhenTheAwesomePlayerMediaPropertyIsPresentThenAwesomePlayerIsDetected() throws Exception {
        givenTheUserIsOnOSVersion(Build.VERSION_CODES.LOLLIPOP);

        whenPropertyisPresent(PROP_USE_AWESOME_PLAYER_MEDIA);

        thenPlayerTypeIs(AndroidMediaPlayerType.AWESOME);
    }

    @Test
    public void givenTheUserIsOnLollipopWhenNoAwesomePlayerMediaPropertyIsPresentThenNuPlayerIsDetected() {
        givenTheUserIsOnOSVersion(Build.VERSION_CODES.LOLLIPOP);

        whenNoPlayerPropertiesArePresent();

        thenPlayerTypeIs(AndroidMediaPlayerType.NU);
    }

    @Test
    public void givenTheUserIsOnKitkatWhenTheNuPlayerPropertyIsPresentThenNuPlayerIsDetected() throws Exception {
        givenTheUserIsOnOSVersion(Build.VERSION_CODES.KITKAT);

        whenPropertyisPresent(PROP_USE_NU_PLAYER);

        thenPlayerTypeIs(AndroidMediaPlayerType.NU);
    }

    @Test
    public void givenTheUserIsOnKitkatWhenNoNuPlayerMediaPropertyIsPresentThenAwesomePlayerIsDetected() {
        givenTheUserIsOnOSVersion(Build.VERSION_CODES.KITKAT);

        whenNoPlayerPropertiesArePresent();

        thenPlayerTypeIs(AndroidMediaPlayerType.AWESOME);
    }

    @Test
    public void givenTheUserIsNotAbleToReadSystemPropertiesWhenFetchingThePlayerTypeThenUnknownPlayerIsDetected() throws Exception {
        givenTheUserIsOnOSVersion(Build.VERSION_CODES.KITKAT);

        when(systemProperties.get(anyString())).thenThrow(new SystemProperties.MissingSystemPropertiesException(new Exception()));

        thenPlayerTypeIs(AndroidMediaPlayerType.UNKNOWN);
    }

    private void givenTheUserIsOnOSVersion(int deviceOSVersion) {
        checker = new MediaPlayerTypeReader(systemProperties, deviceOSVersion);
    }

    private void whenPropertyisPresent(String property) throws SystemProperties.MissingSystemPropertiesException {
        when(systemProperties.get(property)).thenReturn(Boolean.TRUE.toString());
    }

    private void whenNoPlayerPropertiesArePresent() {
        // no-op because there is no work to do
    }

    private void thenPlayerTypeIs(AndroidMediaPlayerType playerType) {
        assertThat(checker.getPlayerType()).isEqualTo(playerType);
    }
}
