package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
import com.novoda.noplayer.model.Dimension;
import com.novoda.noplayer.model.KeySetId;
import com.novoda.noplayer.metadata.Metadata;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.List;

public class MainActivity extends Activity {

    private static final int HALF_A_SECOND_IN_MILLIS = 500;
    private static final int TWO_MEGABITS = 2000000;
    private static final int MAX_VIDEO_BITRATE = 800000;
    private static final Dimension MAX_VIDEO_SIZE = Dimension.from(1024, 576);

    private NoPlayer player;
    private ControllerView controllerView;
    private PlayerView playerView;
    private DemoPresenter demoPresenter;
    private DialogCreator dialogCreator;
    private View videoSelectionButton;
    private View audioSelectionButton;
    private View subtitleSelectionButton;
    private CheckBox bitrateSelectionCheckBox;
    private CheckBox maxVideoSizeSelectionCheckBox;

    private OfflineLicense offlineLicense;

    private final PlaybackParameters playbackParameters = PlaybackParameters.INSTANCE;

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
        bitrateSelectionCheckBox = findViewById(R.id.button_bitrate_selection);
        maxVideoSizeSelectionCheckBox = findViewById(R.id.button_maxsize_selection);
        controllerView = findViewById(R.id.controller_view);

        videoSelectionButton.setOnClickListener(showVideoSelectionDialog);
        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);
        subtitleSelectionButton.setOnClickListener(showSubtitleSelectionDialog);
        bitrateSelectionCheckBox.setOnCheckedChangeListener(toggleBitrateSelection);
        maxVideoSizeSelectionCheckBox.setOnCheckedChangeListener(toggleVideoSizeSelection);
    }

    private final NoPlayer.MetadataChangedListener onMetadataChanged = new NoPlayer.MetadataChangedListener(){
        @Override
        public void onMetadataChanged(Metadata metadata) {
            Log.v(getClass().getSimpleName(), "onMetadata: " + metadata.getEntries().size());
        }
    };

    private final NoPlayer.TracksChangedListener tracksChangedListener = new NoPlayer.TracksChangedListener() {
        @Override
        public void onTracksChanged() {
            AudioTracks audioTracks = player.getAudioTracks();
            if (audioTracks.size() >= 1) {
                audioSelectionButton.setVisibility(View.VISIBLE);
            } else {
                audioSelectionButton.setVisibility(View.GONE);
            }

            List<PlayerVideoTrack> videoTracks = player.getVideoTracks();
            if (videoTracks.isEmpty()) {
                videoSelectionButton.setVisibility(View.GONE);
            } else {
                videoSelectionButton.setVisibility(View.VISIBLE);
            }

            List<PlayerSubtitleTrack> subtitleTracks = player.getSubtitleTracks();
            if (subtitleTracks.isEmpty()) {
                subtitleSelectionButton.setVisibility(View.GONE);
            } else {
                subtitleSelectionButton.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playbackParameters.useContentProtection()) {
            offlineLicense.download(new OfflineLicense.OfflineLicenseCallback() {
                @Override
                public void onLicenseDownloaded(byte[] license) {
                    load(license);
                }
            });
        } else {
            load(null);
        }
    }

    private void load(@Nullable byte[] license) {

        PlayerBuilder playerBuilder = new PlayerBuilder()
                .allowFallbackDecoders()
                .withUserAgent("Android/Linux")
                .allowCrossProtocolRedirects();

        if (license != null) {
            if (playbackParameters.shouldDownloadLicense()) {
                playerBuilder = playerBuilder.withWidevineModularDownloadDrm(KeySetId.of(license));
            } else {
                DataPostingModularDrm keyRequestExecutor = new DataPostingModularDrm(playbackParameters.licenseServerAddress());
                playerBuilder = playerBuilder.withWidevineModularStreamingDrm(keyRequestExecutor);
            }
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
        player.getListeners().addMetadataChangedListener(onMetadataChanged);

        Options options = new OptionsBuilder()
                .withContentType(ContentType.DASH)
                .withMinDurationBeforeQualityIncreaseInMillis(HALF_A_SECOND_IN_MILLIS)
                .withMaxInitialBitrate(TWO_MEGABITS)
                .withMaxVideoBitrate(getMaxVideoBitrate())
                .withMaxVideoSize(getMaxVideoSize())
                .build();
        demoPresenter.startPresenting(Uri.parse(playbackParameters.mpdAddress()), options);
    }

    private int getMaxVideoBitrate() {
        return bitrateSelectionCheckBox.isChecked() ? MAX_VIDEO_BITRATE : Integer.MAX_VALUE;
    }

    private Dimension getMaxVideoSize() {
        return maxVideoSizeSelectionCheckBox.isChecked() ? MAX_VIDEO_SIZE : Dimension.from(Integer.MAX_VALUE, Integer.MAX_VALUE);
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

    private final CompoundButton.OnCheckedChangeListener toggleBitrateSelection = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                player.setMaxVideoBitrate(MAX_VIDEO_BITRATE);
            } else {
                player.clearMaxVideoBitrate();
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener toggleVideoSizeSelection = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                player.setMaxVideoSize(MAX_VIDEO_SIZE);
            } else {
                player.clearMaxVideoSize();
            }
        }
    };

    @Override
    protected void onStop() {
        demoPresenter.stopPresenting();
        super.onStop();
    }
}
