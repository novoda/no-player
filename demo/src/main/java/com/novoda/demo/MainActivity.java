package com.novoda.demo;

import android.app.Activity;
import android.os.Bundle;

import com.novoda.noplayer.NoPlayerView;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.noplayer.player.PrioritisedPlayers;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoPlayerView noPlayerView = (NoPlayerView) findViewById(R.id.player_view);

        Player player = new PlayerFactory(this, PrioritisedPlayers.prioritiseExoPlayer()).create(DrmHandler.NO_DRM);
        player.attach(noPlayerView);
    }

}
