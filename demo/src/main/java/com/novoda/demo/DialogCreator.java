package com.novoda.demo;

import android.app.AlertDialog;
import android.content.Context;
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

    private static final String VIDEO_TRACK_MESSAGE_FORMAT = "ID: %s Quality: %s";
    private static final String AUDIO_TRACK_MESSAGE_FORMAT = "ID: %s Type: %s";
    private static final int AUTO_TRACK_POSITION = 0;

    private final Context context;
    private final NoPlayer noPlayer;

    DialogCreator(Context context, NoPlayer noPlayer) {
        this.context = context;
        this.noPlayer = noPlayer;
    }

    void showVideoSelectionDialog() {
        final List<PlayerVideoTrack> videoTracks = noPlayer.getVideoTracks();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        adapter.addAll(mapVideoTrackToLabel(videoTracks));
        AlertDialog videoTrackSelectionDialog = new AlertDialog.Builder(context)
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
                }).create();
        videoTrackSelectionDialog.show();
    }

    private List<String> mapVideoTrackToLabel(List<PlayerVideoTrack> videoTracks) {
        List<String> labels = new ArrayList<>();
        labels.add("Auto");
        for (PlayerVideoTrack videoTrack : videoTracks) {
            String message = String.format(VIDEO_TRACK_MESSAGE_FORMAT, videoTrack.id(), videoTrack.height());
            labels.add(message);
        }
        return labels;
    }

    void showAudioSelectionDialog() {
        final AudioTracks audioTracks = noPlayer.getAudioTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        adapter.addAll(mapAudioTrackToLabel(audioTracks));
        AlertDialog audioSelectionDialog = new AlertDialog.Builder(context)
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
                }).create();
        audioSelectionDialog.show();
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

    void showSubtitleSelectionDialog() {
        final List<PlayerSubtitleTrack> subtitleTracks = noPlayer.getSubtitleTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        adapter.addAll(mapSubtitleTrackToLabel(subtitleTracks));
        AlertDialog subtitlesSelectionDialog = new AlertDialog.Builder(context)
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
                }).create();
        subtitlesSelectionDialog.show();
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
