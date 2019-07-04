package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerBuilder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.internal.utils.NoPlayerLog;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.KeySetId;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.List;

public class MainActivity extends Activity {

    private static final int HALF_A_SECOND_IN_MILLIS = 500;
    private static final int TWO_MEGABITS = 2000000;
    private static final int MAX_VIDEO_BITRATE = 800000;

    private NoPlayer player;
    private ControllerView controllerView;
    private PlayerView playerView;
    private DemoPresenter demoPresenter;
    private DialogCreator dialogCreator;
    private View videoSelectionButton;
    private View audioSelectionButton;
    private View subtitleSelectionButton;
    private CheckBox hdSelectionCheckBox;

    private OfflineLicense offlineLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaybackParameters playbackParameters = PlaybackParameters.INSTANCE;
        playbackParameters.toastMissingParameters(this);

        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory("no-player");
        try {
            OfflineLicenseHelper offlineLicenseHelper = OfflineLicenseHelper.newWidevineInstance(
                    playbackParameters.licenseServerAddress(),
                    httpDataSourceFactory
            );
            offlineLicense = new OfflineLicense(
                    getApplicationContext(),
                    offlineLicenseHelper,
                    httpDataSourceFactory,
                    Uri.parse(playbackParameters.mpdAddress())
            );
        } catch (UnsupportedDrmException e) {
            Log.e(getClass().getSimpleName(), "UnsupportedDrmException", e);
            Toast.makeText(this, "UnsupportedDrmException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        NoPlayerLog.setLoggingEnabled(true);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.player_view);
        videoSelectionButton = findViewById(R.id.button_video_selection);
        audioSelectionButton = findViewById(R.id.button_audio_selection);
        subtitleSelectionButton = findViewById(R.id.button_subtitle_selection);
        hdSelectionCheckBox = findViewById(R.id.button_hd_selection);
        controllerView = findViewById(R.id.controller_view);

        videoSelectionButton.setOnClickListener(showVideoSelectionDialog);
        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);
        subtitleSelectionButton.setOnClickListener(showSubtitleSelectionDialog);
        hdSelectionCheckBox.setOnCheckedChangeListener(toggleHdSelection);
    }

    private final NoPlayer.TracksChangedListener tracksChangedListener = new NoPlayer.TracksChangedListener() {
        @Override
        public void onTracksChanged() {
            AudioTracks audioTracks = player.getAudioTracks();
            if (audioTracks.size() > 1) {
                audioSelectionButton.setVisibility(View.VISIBLE);
            } else {
                audioSelectionButton.setVisibility(View.GONE);
            }

            List<PlayerVideoTrack> videoTracks = player.getVideoTracks();
            if (videoTracks.size() > 1) {
                videoSelectionButton.setVisibility(View.VISIBLE);
            } else {
                videoSelectionButton.setVisibility(View.GONE);
            }

            List<PlayerSubtitleTrack> subtitleTracks = player.getSubtitleTracks();
            if (subtitleTracks.size() > 1) {
                subtitleSelectionButton.setVisibility(View.VISIBLE);
            } else {
                subtitleSelectionButton.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        offlineLicense.download(new OfflineLicense.OfflineLicenseCallback() {
            @Override
            public void onLicenseDownloaded(byte[] license) {
                PlaybackParameters playbackParameters = PlaybackParameters.INSTANCE;

                PlayerBuilder playerBuilder = new PlayerBuilder()
                        .allowFallbackDecoders()
                        .withUserAgent("Android/Linux")
                        .allowCrossProtocolRedirects();

                if (playbackParameters.shouldDownloadLicense()) {
                    playerBuilder = playerBuilder.withWidevineModularDownloadDrm(KeySetId.of(license));
                } else {
                    DataPostingModularDrm keyRequestExecutor = new DataPostingModularDrm(playbackParameters.licenseServerAddress());
                    playerBuilder = playerBuilder.withWidevineModularStreamingDrm(keyRequestExecutor);
                }

                player = playerBuilder.build(getApplicationContext());

                demoPresenter = new DemoPresenter(controllerView, player, player.getListeners(), playerView);
                dialogCreator = new DialogCreator(player);

                player.getListeners().addDroppedVideoFrames(new NoPlayer.DroppedVideoFramesListener() {
                    @Override
                    public void onDroppedVideoFrames(int droppedFrames, long elapsedMsSinceLastDroppedFrames) {
                        Log.v(getClass().toString(), "dropped frames: " + droppedFrames + " since: " + elapsedMsSinceLastDroppedFrames + "ms");
                    }
                });
                player.getListeners().addTracksChangedListener(tracksChangedListener);

                Options options = new OptionsBuilder()
                        .withContentType(ContentType.DASH)
                        .withMinDurationBeforeQualityIncreaseInMillis(HALF_A_SECOND_IN_MILLIS)
                        .withMaxInitialBitrate(TWO_MEGABITS)
                        .withMaxVideoBitrate(getMaxVideoBitrate())
                        .build();
                demoPresenter.startPresenting(Uri.parse(playbackParameters.mpdAddress()), options);
            }
        });
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
            dialogCreator.showVideoSelectionDialog(MainActivity.this);
        }
    };

    private final View.OnClickListener showAudioSelectionDialog = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            dialogCreator.showAudioSelectionDialog(MainActivity.this);
        }
    };

    private final View.OnClickListener showSubtitleSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogCreator.showSubtitleSelectionDialog(MainActivity.this);
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
