package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerBuilder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.internal.utils.NoPlayerLog;

public class MainActivity extends Activity {

    private static final String URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD = "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd";
    private static final String EXAMPLE_MODULAR_LICENSE_SERVER_PROXY = "https://proxy.uat.widevine.com/proxy?provider=widevine_test";
    private static final int HALF_A_SECOND_IN_MILLIS = 500;
    private static final int TWO_MEGABITS = 2000000;

    private NoPlayer player;
    private DemoPresenter demoPresenter;
    private DialogCreator dialogCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoPlayerLog.setLoggingEnabled(true);
        setContentView(R.layout.activity_main);
        PlayerView playerView = findViewById(R.id.player_view);
        View videoSelectionButton = findViewById(R.id.button_video_selection);
        View audioSelectionButton = findViewById(R.id.button_audio_selection);
        View subtitleSelectionButton = findViewById(R.id.button_subtitle_selection);
        ControllerView controllerView = findViewById(R.id.controller_view);

        videoSelectionButton.setOnClickListener(showVideoSelectionDialog);
        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);
        subtitleSelectionButton.setOnClickListener(showSubtitleSelectionDialog);

        DataPostingModularDrm drmHandler = new DataPostingModularDrm(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY);

        player = new PlayerBuilder()
                .withWidevineModularStreamingDrm(drmHandler)
                .withDowngradedSecureDecoder()
                .build(this);

        demoPresenter = new DemoPresenter(controllerView, player, player.getListeners(), playerView);
        dialogCreator = new DialogCreator(this, player);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Uri uri = Uri.parse(URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD);
        Options options = new OptionsBuilder()
                .withContentType(ContentType.DASH)
                .withMinDurationBeforeQualityIncreaseInMillis(HALF_A_SECOND_IN_MILLIS)
                .withMaxInitialBitrate(TWO_MEGABITS)
                .build();
        demoPresenter.startPresenting(uri, options);
    }

    private final View.OnClickListener showVideoSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (player == null || !player.isPlaying() || player.getVideoTracks().isEmpty()) {
                Toast.makeText(MainActivity.this, "no additional video tracks available!", Toast.LENGTH_LONG).show();
            } else {
                dialogCreator.showVideoSelectionDialog();
            }
        }
    };

    private final View.OnClickListener showAudioSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (player == null || !player.isPlaying() || player.getAudioTracks().size() == 0) {
                Toast.makeText(MainActivity.this, "no audio tracks available!", Toast.LENGTH_LONG).show();
            } else {
                dialogCreator.showAudioSelectionDialog();
            }
        }
    };

    private final View.OnClickListener showSubtitleSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (player == null || !player.isPlaying() || player.getSubtitleTracks().isEmpty()) {
                Toast.makeText(MainActivity.this, "no subtitles available!", Toast.LENGTH_LONG).show();
            } else {
                dialogCreator.showSubtitleSelectionDialog();
            }
        }
    };

    @Override
    protected void onStop() {
        demoPresenter.stopPresenting();
        super.onStop();
    }
}
