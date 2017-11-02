package com.novoda.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerBuilder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.utils.NoPlayerLog;

public class MainActivity extends Activity {

    private static final String EXAMPLE_MODULAR_LICENSE_SERVER_PROXY = "https://proxy.uat.widevine.com/proxy?provider=widevine_test";
    private static final DataPostingModularDrm DRM_HANDLER = new DataPostingModularDrm(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY);

    private static final Video STANDARD_MP4 = Video.from("http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-85.mp4", ContentType.HLS);
    private static final Video STANDARD_MPD = Video.from("https://storage.googleapis.com/content-samples/multi-audio/manifest.mpd", ContentType.DASH);
    private static final Video WIDEVINE_MODULAR_MPD = Video.from("https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd", ContentType.DASH, DrmType.WIDEVINE_MODULAR_STREAM, DRM_HANDLER);

    private NoPlayer player;
    private DemoPresenter demoPresenter;
    private DialogCreator dialogCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoPlayerLog.setLoggingEnabled(true);
        setContentView(R.layout.activity_main);
        PlayerView playerView = (PlayerView) findViewById(R.id.player_view);
        View videoSelectionButton = findViewById(R.id.button_video_selection);
        View audioSelectionButton = findViewById(R.id.button_audio_selection);
        View subtitleSelectionButton = findViewById(R.id.button_subtitle_selection);
        final ControllerView controllerView = (ControllerView) findViewById(R.id.controller_view);

        videoSelectionButton.setOnClickListener(showVideoSelectionDialog);
        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);
        subtitleSelectionButton.setOnClickListener(showSubtitleSelectionDialog);

        player = new PlayerBuilder()
                .withDrm(WIDEVINE_MODULAR_MPD.drmType(), WIDEVINE_MODULAR_MPD.drmHandler())
                .withDowngradedSecureDecoder()
                .build(this);

        demoPresenter = new DemoPresenter(controllerView, player, player.getListeners(), playerView);
        dialogCreator = new DialogCreator(this, player);
    }

    @Override
    protected void onStart() {
        super.onStart();
        demoPresenter.startPresenting(WIDEVINE_MODULAR_MPD.videoUri(), WIDEVINE_MODULAR_MPD.contentType());
    }

    private final View.OnClickListener showVideoSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (player.getVideoTracks().isEmpty()) {
                Toast.makeText(MainActivity.this, "no additional video tracks available!", Toast.LENGTH_LONG).show();
            } else {
                dialogCreator.showVideoSelectionDialog();
            }
        }
    };

    private final View.OnClickListener showAudioSelectionDialog = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            dialogCreator.showAudioSelectionDialog();
        }
    };

    private final View.OnClickListener showSubtitleSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!player.getSubtitleTracks().isEmpty()) {
                dialogCreator.showSubtitleSelectionDialog();
            } else {
                Toast.makeText(MainActivity.this, "no subtitles available!", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        demoPresenter.stopPresenting();
    }
}
