package com.novoda.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerBuilder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.internal.utils.NoPlayerLog;

public class MainActivity extends Activity {

    private static final String URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd";
    private static final String EXAMPLE_MODULAR_LICENSE_SERVER_PROXY = "https://proxy.uat.widevine.com/proxy?provider=widevine_test";
    private static final int HALF_A_SECOND_IN_MILLIS = 500;
    private static final int TWO_MEGABITS = 2000000;
    private static final int MAX_VIDEO_BITRATE = 800000;

    private NoPlayer player;
    private DemoPresenter demoPresenter;
    private DialogCreator dialogCreator;
    private CheckBox hdSelectionCheckBox;
    private Uri mpdAddress = Uri.parse(URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extractFromIntent();

        NoPlayerLog.setLoggingEnabled(true);
        setContentView(R.layout.activity_main);
        PlayerView playerView = findViewById(R.id.player_view);
        View videoSelectionButton = findViewById(R.id.button_video_selection);
        View audioSelectionButton = findViewById(R.id.button_audio_selection);
        View subtitleSelectionButton = findViewById(R.id.button_subtitle_selection);
        hdSelectionCheckBox = findViewById(R.id.button_hd_selection);
        ControllerView controllerView = findViewById(R.id.controller_view);

        videoSelectionButton.setOnClickListener(showVideoSelectionDialog);
        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);
        subtitleSelectionButton.setOnClickListener(showSubtitleSelectionDialog);
        hdSelectionCheckBox.setOnCheckedChangeListener(toggleHdSelection);

        DataPostingModularDrm drmHandler = new DataPostingModularDrm(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY);

        player = new PlayerBuilder()
                .withWidevineModularStreamingDrm(drmHandler)
                .withDowngradedSecureDecoder()
                .withUserAgent("Android/Linux")
                .allowCrossProtocolRedirects()
                .build(this);

        demoPresenter = new DemoPresenter(controllerView, player, player.getListeners(), playerView);
        dialogCreator = new DialogCreator(this, player);

        player.getListeners().addDroppedVideoFrames(new NoPlayer.DroppedVideoFramesListener() {
            @Override
            public void onDroppedVideoFrames(int droppedFrames, long elapsedMsSinceLastDroppedFrames) {
                Log.v(getClass().toString(), "dropped frames: " + droppedFrames + " since: " + elapsedMsSinceLastDroppedFrames + "ms");
            }
        });

    }

    private void extractFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String mpdAddress = intent.getStringExtra(LandingActivity.KEY_MPD_ADDRESS);
            if (mpdAddress != null && !mpdAddress.isEmpty()) {
                this.mpdAddress = Uri.parse(mpdAddress);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Options options = new OptionsBuilder()
                .withContentType(ContentType.DASH)
                .withMinDurationBeforeQualityIncreaseInMillis(HALF_A_SECOND_IN_MILLIS)
                .withMaxInitialBitrate(TWO_MEGABITS)
                .withMaxVideoBitrate(getMaxVideoBitrate())
                .build();
        demoPresenter.startPresenting(mpdAddress, options);
    }

    private int getMaxVideoBitrate() {
        if (hdSelectionCheckBox.isChecked()) {
            return Integer.MAX_VALUE;
        }
        return MAX_VIDEO_BITRATE;
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
            if (player.getSubtitleTracks().isEmpty()) {
                Toast.makeText(MainActivity.this, "no subtitles available!", Toast.LENGTH_LONG).show();
            } else {
                dialogCreator.showSubtitleSelectionDialog();
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener toggleHdSelection = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                player.clearMaxVideoBitrate();
            } else {
                player.setMaxVideoBitrate(MAX_VIDEO_BITRATE);
            }
        }
    };

    @Override
    protected void onStop() {
        demoPresenter.stopPresenting();
        super.onStop();
    }
}
