package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.noplayer.player.PrioritisedPlayers;
import com.novoda.notils.logger.simple.Log;

public class MainActivity extends Activity {

    private static final String URI_VIDEO_MP4 = "http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-85.mp4";
    private static final String URI_VIDEO_MPD = "https://storage.googleapis.com/content-samples/multi-audio/multi-audio.mpd";

    private Player player;
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.setShowLogs(true);
        setContentView(R.layout.activity_main);
        playerView = (PlayerView) findViewById(R.id.player_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Add switch in UI to avoid redeploy.
        player = new PlayerFactory(this, PrioritisedPlayers.prioritiseExoPlayer()).create();
        player.addPreparedListener(new Player.PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                player.play();
            }
        });
        player.attach(playerView);

        Uri uri = Uri.parse(URI_VIDEO_MPD);
        player.loadVideo(uri, ContentType.DASH);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }
}
