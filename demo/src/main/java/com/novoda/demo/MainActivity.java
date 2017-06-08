package com.novoda.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.noplayer.player.PrioritisedPlayers;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String URI_VIDEO_MP4 = "http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-85.mp4";
    private static final String URI_VIDEO_MPD = "https://storage.googleapis.com/content-samples/multi-audio/manifest.mpd";

    private Player player;
    private PlayerView playerView;
    private View audioSelectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.setShowLogs(true);
        setContentView(R.layout.activity_main);
        playerView = (PlayerView) findViewById(R.id.player_view);
        audioSelectionButton = Views.findById(this, R.id.button_audio_selection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Add switch in UI to avoid redeploy.
        player = new PlayerFactory(this, PrioritisedPlayers.prioritiseExoPlayer()).create();
        player.getListeners().addPreparedListener(new Player.PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                player.play();
            }
        });

        player.attach(playerView);

        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);

        Uri uri = Uri.parse(URI_VIDEO_MPD);
        player.loadVideo(uri, ContentType.DASH);
    }

    private final View.OnClickListener showAudioSelectionDialog = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            showAudioSelectionDialog();
        }

        private void showAudioSelectionDialog() {
            final List<PlayerAudioTrack> audioTracks = player.getAudioTracks();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item);
            adapter.addAll(mapAudioTrackToLabel(audioTracks));
            AlertDialog audioSelectionDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Select audio track")
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            PlayerAudioTrack audioTrack = audioTracks.get(position);
                            player.selectAudioTrack(audioTrack);
                        }
                    }).create();
            audioSelectionDialog.show();
        }

        private List<String> mapAudioTrackToLabel(List<PlayerAudioTrack> audioTracks) {
            List<String> labels = new ArrayList<>();
            for (PlayerAudioTrack audioTrack : audioTracks) {
                labels.add("Group: " + audioTrack.groupIndex() + " Format: " + audioTrack.formatIndex());
            }
            return labels;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
        audioSelectionButton.setOnClickListener(null);
    }
}
