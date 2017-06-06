package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.PlayerAudioTrack;

import java.util.ArrayList;
import java.util.List;

class AndroidMediaPlayerAudioTrackSelector {

    private static final int NO_FORMAT = 0;

    List<PlayerAudioTrack> getAudioTracks(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            throw new NullPointerException("You can only call getAudioTracks() when video is prepared.");
        }

        List<PlayerAudioTrack> audioTracks = new ArrayList<>();
        TrackInfosWrapper trackInfos = TrackInfosWrapper.from(mediaPlayer.getTrackInfo());

        for (int i = 0; i < trackInfos.size(); i++) {
            TrackInfoWrapper trackInfo = trackInfos.get(i);
            if (trackInfo.type() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                audioTracks.add(new PlayerAudioTrack(i, NO_FORMAT, String.valueOf(trackInfo.hashCode()), trackInfo.language(), "", -1, -1));
            }
        }
        return audioTracks;
    }

    void selectAudioTrack(MediaPlayer mediaPlayer, PlayerAudioTrack playerAudioTrack) {
        if (mediaPlayer == null) {
            throw new NullPointerException("You can only call selectAudioTrack() when video is prepared.");
        }

        mediaPlayer.selectTrack(playerAudioTrack.groupIndex());
    }
}
