package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.novoda.noplayer.drm.DrmRequestException;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.exoplayer.playererror.ConnectivityError;
import com.novoda.noplayer.exoplayer.playererror.DrmRequestError;
import com.novoda.noplayer.exoplayer.playererror.InvalidResponseCodeError;
import com.novoda.noplayer.exoplayer.playererror.MalformedContentError;
import com.novoda.noplayer.exoplayer.playererror.UnknownError;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ExoPlayerErrorFactoryTest {

    @Test
    public void givenInvalidResponseCodeException_thenInvalidResponseCodeErrorIsReturned() {
        Exception givenException = new HttpDataSource.InvalidResponseCodeException(404, null, null);

        Player.PlayerError result = ExoPlayerErrorFactory.errorFor(givenException);

        thenReturnedErrorIsCorrect(result, givenException, InvalidResponseCodeError.class);
    }

    @Test
    public void givenManifestParsingException_thenMalformedContentErrorIsReturned() {
        Exception givenException = new ParserException("ParserMessage");

        Player.PlayerError result = ExoPlayerErrorFactory.errorFor(givenException);

        thenReturnedErrorIsCorrect(result, givenException, MalformedContentError.class);
    }

    @Test
    public void givenLicenseFetchException_thenDrmErrorIsReturned() {
        Exception cause = DrmRequestException.from(new IOException(""));
        Exception givenException = new ExoPlaybackException(cause);

        Player.PlayerError result = ExoPlayerErrorFactory.errorFor(givenException);

        thenReturnedErrorIsCorrect(result, cause, DrmRequestError.class);
    }

    @Test
    public void givenLicenseParsingException_thenDrmErrorIsReturned() {
        Exception cause = DrmRequestException.from(new JSONException(""));
        Exception givenException = new ExoPlaybackException(cause);

        Player.PlayerError result = ExoPlayerErrorFactory.errorFor(givenException);

        thenReturnedErrorIsCorrect(result, cause, DrmRequestError.class);
    }

    @Test
    public void givenIOException_thenConnectivityErrorIsReturned() {
        Exception givenException = new IOException("IOException");

        Player.PlayerError result = ExoPlayerErrorFactory.errorFor(givenException);

        thenReturnedErrorIsCorrect(result, givenException, ConnectivityError.class);
    }

    @Test
    public void givenUnexpectedException_thenUnknownErrorIsReturned() {
        Exception givenException = new Exception("UnknownException");

        Player.PlayerError result = ExoPlayerErrorFactory.errorFor(givenException);

        thenReturnedErrorIsCorrect(result, givenException, UnknownError.class);
    }

    void thenReturnedErrorIsCorrect(Player.PlayerError error, Throwable expectedExtra, Class<? extends Player.PlayerError> type) {
        assertThat(error).isInstanceOf(type);
        assertThat(error.getCause()).isEqualTo(expectedExtra);
    }

}
