package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.noplayer.player.PrioritizedPlayers;

public class MainActivity extends Activity {

    private static final String URI_VIDEO_MP4 = "http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-85.mp4";

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = new PlayerFactory(this, PrioritizedPlayers.prioritizeExoPlayer()).create();
        player.addPreparedListener(new Player.PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerState) {
                player.play();
            }
        });
        PlayerView playerView = (PlayerView) findViewById(R.id.player_view);
        player.attach(playerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Uri uri = Uri.parse(URI_VIDEO_MP4);
        player.loadVideo(uri, ContentType.H264);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }
}
