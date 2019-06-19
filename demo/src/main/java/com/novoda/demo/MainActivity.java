package com.novoda.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.source.dash.DashUtil;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerBuilder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.drm.ModularDrmKeyRequest;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.utils.NoPlayerLog;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private static final String URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD = "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd";
    private static final String EXAMPLE_MODULAR_LICENSE_SERVER_PROXY = "https://proxy.uat.widevine.com/proxy?video_id=f9a34cab7b05881a&provider=widevine_test";
    private static final int HALF_A_SECOND_IN_MILLIS = 500;
    private static final int TWO_MEGABITS = 2000000;
    private static final int MAX_VIDEO_BITRATE = 800000;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private byte[] offlineKeySetId;

    private NoPlayer player;
    private DemoPresenter demoPresenter;
    private DialogCreator dialogCreator;
    private CheckBox hdSelectionCheckBox;
    private OfflineLicenseHelper offlineLicenseHelper;
    private DefaultHttpDataSourceFactory httpDataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoPlayerLog.setLoggingEnabled(true);
        setContentView(R.layout.activity_main);
        PlayerView playerView = findViewById(R.id.player_view);
        final View videoSelectionButton = findViewById(R.id.button_video_selection);
        final View audioSelectionButton = findViewById(R.id.button_audio_selection);
        final View subtitleSelectionButton = findViewById(R.id.button_subtitle_selection);
        final View revokeDrmButton = findViewById(R.id.button_revoke_drm);
        hdSelectionCheckBox = findViewById(R.id.button_hd_selection);
        ControllerView controllerView = findViewById(R.id.controller_view);

        videoSelectionButton.setOnClickListener(showVideoSelectionDialog);
        audioSelectionButton.setOnClickListener(showAudioSelectionDialog);
        subtitleSelectionButton.setOnClickListener(showSubtitleSelectionDialog);
        hdSelectionCheckBox.setOnCheckedChangeListener(toggleHdSelection);

        httpDataSourceFactory = new DefaultHttpDataSourceFactory("no-player");
        try {
            offlineLicenseHelper = OfflineLicenseHelper.newWidevineInstance(
                    EXAMPLE_MODULAR_LICENSE_SERVER_PROXY,
                    httpDataSourceFactory
            );
        } catch (UnsupportedDrmException e) {
            Log.e("TAG", "UnsupportedDrmException", e);
        }

        downloadLicense(onLicenseDownloaded);
        StreamingModularDrm drmHandler = new DataPostingModularDrm(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY);

        player = new PlayerBuilder()
                .allowFallbackDecoders()
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
        player.getListeners().addTracksChangedListener(new NoPlayer.TracksChangedListener() {
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
        });
        revokeDrmButton.setOnClickListener(revokeDrmRights);

    }

    interface DownloadLicenseCallback {
        void onLicenseDownloaded(byte[] license);
    }

    private final DownloadLicenseCallback onLicenseDownloaded = new DownloadLicenseCallback() {
        @Override
        public void onLicenseDownloaded(byte[] license) {
            Uri uri = Uri.parse(URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD);
            Options options = new OptionsBuilder()
                    .withContentType(ContentType.DASH)
                    .withMinDurationBeforeQualityIncreaseInMillis(HALF_A_SECOND_IN_MILLIS)
                    .withMaxInitialBitrate(TWO_MEGABITS)
                    .withMaxVideoBitrate(getMaxVideoBitrate())
                    .withKeySetExecutor(new KeyRequestExecutor() {
                        @Override
                        public byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException {
                            return HttpClient.post(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY, request.data());
                        }
                    })
                    .build();
            demoPresenter.startPresenting(uri, options);
        }
    };

    private int getMaxVideoBitrate() {
        if (hdSelectionCheckBox.isChecked()) {
            return Integer.MAX_VALUE;
        }
        return MAX_VIDEO_BITRATE;
    }

    private final View.OnClickListener showVideoSelectionDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogCreator.showVideoSelectionDialog();
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
            dialogCreator.showSubtitleSelectionDialog();
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

    private final View.OnClickListener revokeDrmRights = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                offlineKeySetId = offlineLicenseHelper.renewLicense(offlineKeySetId);
            } catch (DrmSession.DrmSessionException e) {
                Log.e("TAG", "DrmSession.DrmSessionException", e);
            }
        }
    };

    @Override
    protected void onStop() {
        demoPresenter.stopPresenting();
        super.onStop();
    }

    private void downloadLicense(final DownloadLicenseCallback onLicenseDownloaded) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    DataSource dataSource = httpDataSourceFactory.createDataSource();
                    DashManifest dashManifest = DashUtil.loadManifest(
                            dataSource,
                            Uri.parse(URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD)
                    );
                    DrmInitData drmInitData = DashUtil.loadDrmInitData(dataSource, dashManifest.getPeriod(0));
                    offlineKeySetId = offlineLicenseHelper.downloadLicense(drmInitData);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onLicenseDownloaded.onLicenseDownloaded(offlineKeySetId);
                        }
                    });
                } catch (DrmSession.DrmSessionException e) {
                    Log.e("TAG", "DrmSession.DrmSessionException", e);
                } catch (IOException e) {
                    Log.e("TAG", "IOException", e);
                } catch (InterruptedException e) {
                    Log.e("TAG", "InterruptedException", e);
                }
            }
        });
    }
}
