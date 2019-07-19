package com.novoda.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class DialogCreator {

    private static final String VIDEO_TRACK_MESSAGE_FORMAT = "ID: %s Quality: %s, Codec: %s, Decoder: %s, Bitrate: %s";
    private static final String AUDIO_TRACK_MESSAGE_FORMAT = "ID: %s Type: %s";
    private static final int AUTO_TRACK_POSITION = 0;

    private final NoPlayer noPlayer;

    DialogCreator(NoPlayer noPlayer) {
        this.noPlayer = noPlayer;
    }

    void showVideoSelectionDialog(Activity activity) {
        final List<PlayerVideoTrack> videoTracks = noPlayer.getVideoTracks();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.list_item);
        adapter.addAll(mapVideoTrackToLabel(videoTracks));
        new AlertDialog.Builder(activity)
                .setTitle("Select Video track")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == AUTO_TRACK_POSITION) {
                            noPlayer.clearVideoTrackSelection();
                        } else {
                            PlayerVideoTrack videoTrack = videoTracks.get(position - 1);
                            noPlayer.selectVideoTrack(videoTrack);
                        }
                    }
                })
                .create()
                .show();
    }

    private List<String> mapVideoTrackToLabel(List<PlayerVideoTrack> videoTracks) {
        List<String> labels = new ArrayList<>();
        labels.add("Auto");
        for (PlayerVideoTrack videoTrack : videoTracks) {
            String message = String.format(
                    VIDEO_TRACK_MESSAGE_FORMAT,
                    videoTrack.id(),
                    videoTrack.height(),
                    videoTrack.codecName(),
                    videoTrack.associatedDecoderName(),
                    videoTrack.bitrate()
            );
            labels.add(message);
        }
        return labels;
    }

    void showAudioSelectionDialog(Activity activity) {
        final AudioTracks audioTracks = noPlayer.getAudioTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.list_item);
        adapter.addAll(mapAudioTrackToLabel(audioTracks));
        new AlertDialog.Builder(activity)
                .setTitle("Select audio track")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == AUTO_TRACK_POSITION) {
                            noPlayer.clearAudioTrackSelection();
                        } else {
                            PlayerAudioTrack audioTrack = audioTracks.get(position - 1);
                            noPlayer.selectAudioTrack(audioTrack);
                        }
                    }
                })
                .create()
                .show();
    }

    private List<String> mapAudioTrackToLabel(AudioTracks audioTracks) {
        List<String> labels = new ArrayList<>();
        labels.add("Auto");
        for (PlayerAudioTrack audioTrack : audioTracks) {
            String label = String.format(
                    Locale.UK,
                    AUDIO_TRACK_MESSAGE_FORMAT,
                    audioTrack.trackId(),
                    audioTrack.audioTrackType()
            );
            labels.add(label);
        }
        return labels;
    }

    void showSubtitleSelectionDialog(Activity activity) {
        final List<PlayerSubtitleTrack> subtitleTracks = noPlayer.getSubtitleTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.list_item);
        adapter.addAll(mapSubtitleTrackToLabel(subtitleTracks));
        new AlertDialog.Builder(activity)
                .setTitle("Select subtitle track")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == AUTO_TRACK_POSITION) {
                            noPlayer.hideSubtitleTrack();
                        } else {
                            PlayerSubtitleTrack subtitleTrack = subtitleTracks.get(position - 1);
                            noPlayer.showSubtitleTrack(subtitleTrack);
                        }
                    }
                })
                .create()
                .show();
    }

    private List<String> mapSubtitleTrackToLabel(List<PlayerSubtitleTrack> subtitleTracks) {
        List<String> labels = new ArrayList<>();
        labels.add("Dismiss subtitles");
        for (PlayerSubtitleTrack subtitleTrack : subtitleTracks) {
            labels.add("Group: " + subtitleTrack.groupIndex() + " Format: " + subtitleTrack.formatIndex());
        }
        return labels;
    }
}
