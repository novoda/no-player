package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerErrorType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.novoda.noplayer.DetailErrorType.*;
import static com.novoda.noplayer.PlayerErrorType.*;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ErrorFactoryTest {

    @Parameterized.Parameter(0)
    public PlayerErrorType playerErrorType;
    @Parameterized.Parameter(1)
    public DetailErrorType detailErrorType;
    @Parameterized.Parameter(2)
    public int type;

    @Parameterized.Parameters(name = "{0} with detail {1} is mapped from {2}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{SOURCE, MEDIA_PLAYER_MALFORMED, MediaPlayer.MEDIA_ERROR_MALFORMED},
                new Object[]{SOURCE, MEDIA_PLAYER_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK, MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK},
                new Object[]{SOURCE, MEDIA_PLAYER_INFO_NOT_SEEKABLE, MediaPlayer.MEDIA_INFO_NOT_SEEKABLE},
                new Object[]{SOURCE, MEDIA_PLAYER_SUBTITLE_TIMED_OUT, MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT},
                new Object[]{SOURCE, MEDIA_PLAYER_UNSUPPORTED_SUBTITLE, MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE},

                new Object[]{CONNECTIVITY, MEDIA_PLAYER_TIMED_OUT, MediaPlayer.MEDIA_ERROR_TIMED_OUT},

                new Object[]{DRM, MEDIA_PLAYER_SERVER_DIED, MediaPlayer.MEDIA_ERROR_SERVER_DIED},
                new Object[]{DRM, MEDIA_PLAYER_PREPARE_DRM_STATUS_PREPARATION_ERROR, MediaPlayer.PREPARE_DRM_STATUS_PREPARATION_ERROR},
                new Object[]{DRM, MEDIA_PLAYER_PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR, MediaPlayer.PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR},
                new Object[]{DRM, MEDIA_PLAYER_PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR, MediaPlayer.PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR},

                new Object[]{RENDERER_DECODER, MEDIA_PLAYER_INFO_AUDIO_NOT_PLAYING, MediaPlayer.MEDIA_INFO_AUDIO_NOT_PLAYING},
                new Object[]{RENDERER_DECODER, MEDIA_PLAYER_BAD_INTERLEAVING, MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING},
                new Object[]{RENDERER_DECODER, MEDIA_PLAYER_INFO_VIDEO_NOT_PLAYING, MediaPlayer.MEDIA_INFO_VIDEO_NOT_PLAYING},
                new Object[]{RENDERER_DECODER, MEDIA_PLAYER_INFO_VIDEO_TRACK_LAGGING, MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING}
        );
    }

    @Test
    public void mapErrors() {
        NoPlayer.PlayerError playerError = ErrorFactory.createErrorFrom(type, 0);
        assertThat(playerError.type()).isEqualTo(playerErrorType);
        assertThat(playerError.detailType()).isEqualTo(detailErrorType);
    }
}
