package com.novoda.demo;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.source.dash.DashUtil;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class OfflineLicense {

    private final Context context;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final OfflineLicenseHelper offlineLicenseHelper;
    private final DefaultHttpDataSourceFactory httpDataSourceFactory;
    private final Uri mpdAddress;

    OfflineLicense(Context context,
                   OfflineLicenseHelper offlineLicenseHelper,
                   DefaultHttpDataSourceFactory httpDataSourceFactory,
                   Uri mpdAddress) {
        this.context = context;
        this.offlineLicenseHelper = offlineLicenseHelper;
        this.httpDataSourceFactory = httpDataSourceFactory;
        this.mpdAddress = mpdAddress;
    }

    void download(final OfflineLicenseCallback offlineLicenseCallback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    DataSource dataSource = httpDataSourceFactory.createDataSource();
                    DashManifest dashManifest = DashUtil.loadManifest(
                            dataSource,
                            mpdAddress
                    );
                    DrmInitData drmInitData = DashUtil.loadDrmInitData(dataSource, dashManifest.getPeriod(0));
                    final byte[] offlineKeySetId = offlineLicenseHelper.downloadLicense(drmInitData);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            offlineLicenseCallback.onLicenseDownloaded(offlineKeySetId);
                        }
                    });
                } catch (DrmSession.DrmSessionException e) {
                    Log.e(getClass().getSimpleName(), "DrmSession.DrmSessionException", e);
                    Toast.makeText(context, "DrmSession.DrmSessionException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "IOException", e);
                    Toast.makeText(context, "IOException:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    Log.e(getClass().getSimpleName(), "InterruptedException", e);
                    Toast.makeText(context, "InterruptedException:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    interface OfflineLicenseCallback {
        void onLicenseDownloaded(byte[] license);
    }

}
