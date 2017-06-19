package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.PlayerAudioTrack;

import java.util.ArrayList;
import java.util.List;

class AndroidMediaPlayerAudioTrackSelector {

    private static final int NO_FORMAT = 0;
    private static final int NO_CHANNELS = -1;
    private static final int NO_FREQUENCY = -1;
    private static final String NO_MIME_TYPE = "";

    private final TrackInfosFactory trackInfosFactory;

    AndroidMediaPlayerAudioTrackSelector(TrackInfosFactory trackInfosFactory) {
        this.trackInfosFactory = trackInfosFactory;
    }

    List<PlayerAudioTrack> getAudioTracks(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            throw new NullPointerException("You can only call getAudioTracks() when video is prepared.");
        }

        List<PlayerAudioTrack> audioTracks = new ArrayList<>();
        NoPlayerTrackInfos trackInfos = trackInfosFactory.createFrom(mediaPlayer);

        for (int i = 0; i < trackInfos.size(); i++) {
            NoPlayerTrackInfo trackInfo = trackInfos.get(i);
            if (trackInfo.type() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                audioTracks.add(
                        new PlayerAudioTrack(
                                i,
                                NO_FORMAT,
                                String.valueOf(trackInfo.hashCode()),
                                trackInfo.language(),
                                NO_MIME_TYPE,
                                NO_CHANNELS,
                                NO_FREQUENCY)
                );
            }
        }
        return audioTracks;
    }

    boolean selectAudioTrack(MediaPlayer mediaPlayer, PlayerAudioTrack playerAudioTrack) {
        if (mediaPlayer == null) {
            throw new NullPointerException("You can only call selectAudioTrack() when video is prepared.");
        }

        mediaPlayer.selectTrack(playerAudioTrack.groupIndex());
        return true;
    }
}
