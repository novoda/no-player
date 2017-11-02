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

    private final Context context;
    private final NoPlayer noPlayer;

    DialogCreator(Context context, NoPlayer noPlayer) {
        this.context = context;
        this.noPlayer = noPlayer;
    }

    void showVideoAssetSelectionDialog(final VideoSelectionAction videoSelectionAction) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        final Videos videos = Videos.getInstance();
        adapter.addAll(mapVideoToLabel(videos));
        AlertDialog videoTrackSelectionDialog = new AlertDialog.Builder(context)
                .setTitle("Select Video")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Video video = videos.get(position);
                        videoSelectionAction.perform(video);
                    }
                }).create();
        videoTrackSelectionDialog.show();
    }

    private List<String> mapVideoToLabel(Videos videos) {
        List<String> labels = new ArrayList<>();
        for (Video video : videos) {
            labels.add(video.humanReadableRepresentation());
        }
        return labels;
    }

    public interface VideoSelectionAction {

        void perform(Video video);
    }

    void showVideoTrackSelectionDialog() {
        final List<PlayerVideoTrack> videoTracks = noPlayer.getVideoTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        adapter.addAll(mapVideoTrackToLabel(videoTracks));
        AlertDialog videoTrackSelectionDialog = new AlertDialog.Builder(context)
                .setTitle("Select Video track")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        PlayerVideoTrack videoTrack = videoTracks.get(position);
                        noPlayer.selectVideoTrack(videoTrack);
                    }
                }).create();
        videoTrackSelectionDialog.show();
    }

    private List<String> mapVideoTrackToLabel(List<PlayerVideoTrack> videoTracks) {
        List<String> labels = new ArrayList<>();
        for (PlayerVideoTrack videoTrack : videoTracks) {
            labels.add("Quality " + videoTrack.height());
        }
        return labels;
    }

    void showAudioTrackSelectionDialog() {
        final AudioTracks audioTracks = noPlayer.getAudioTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        adapter.addAll(mapAudioTrackToLabel(audioTracks));
        AlertDialog audioSelectionDialog = new AlertDialog.Builder(context)
                .setTitle("Select audio track")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        PlayerAudioTrack audioTrack = audioTracks.get(position);
                        noPlayer.selectAudioTrack(audioTrack);
                    }
                }).create();
        audioSelectionDialog.show();
    }

    private List<String> mapAudioTrackToLabel(AudioTracks audioTracks) {
        List<String> labels = new ArrayList<>();
        for (PlayerAudioTrack audioTrack : audioTracks) {
            String label = String.format(
                    Locale.UK,
                    "Group: %s Format: %s Type: %s",
                    audioTrack.groupIndex(),
                    audioTrack.formatIndex(),
                    audioTrack.audioTrackType()
            );
            labels.add(label);
        }
        return labels;
    }

    void showSubtitleTrackSelectionDialog() {
        final List<PlayerSubtitleTrack> subtitleTracks = noPlayer.getSubtitleTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item);
        adapter.addAll(mapSubtitleTrackToLabel(subtitleTracks));
        AlertDialog subtitlesSelectionDialog = new AlertDialog.Builder(context)
                .setTitle("Select subtitle track")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                noPlayer.hideSubtitleTrack();
                                break;
                            default:
                                PlayerSubtitleTrack subtitleTrack = subtitleTracks.get(position - 1);
                                noPlayer.showSubtitleTrack(subtitleTrack);
                                break;
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
