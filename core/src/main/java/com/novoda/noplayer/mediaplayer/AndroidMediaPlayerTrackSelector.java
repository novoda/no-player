package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.PlayerAudioTrack;

import java.util.ArrayList;

class AndroidMediaPlayerTrackSelector {

    private static final int NO_FORMAT = 0;

    ArrayList<PlayerAudioTrack> getAudioTracks(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            throw new NullPointerException("You can only call getAudioTracks() when video is prepared.");
        }

        ArrayList<PlayerAudioTrack> audioTracks = new ArrayList<>();
        MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();

        for (int i = 0; i < trackInfos.length; i++) {
            MediaPlayer.TrackInfo trackInfo = trackInfos[i];
            if (trackInfo.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                audioTracks.add(new PlayerAudioTrack(i, NO_FORMAT, String.valueOf(trackInfo.hashCode()), trackInfo.getLanguage(), "", -1, -1));
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
