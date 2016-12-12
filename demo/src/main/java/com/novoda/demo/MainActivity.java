package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayerView;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.noplayer.player.PrioritisedPlayers;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoPlayerView noPlayerView = (NoPlayerView) findViewById(R.id.player_view);

        final Player player = new PlayerFactory(this, PrioritisedPlayers.prioritiseExoPlayer()).create(DrmHandler.NO_DRM);
        player.attach(noPlayerView);
        player.addPreparedListener(new Player.PreparedListener() {
            @Override
            public void onPrepared(PlayerState playerMonitor) {
                player.play();
            }
        });

        Uri uri = Uri.parse("http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-85.mp4");
        player.loadVideo(uri, ContentType.H264);
    }

}
