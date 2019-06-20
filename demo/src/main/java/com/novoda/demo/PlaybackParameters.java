package com.novoda.demo;

import android.content.Context;
import android.widget.Toast;

enum PlaybackParameters {
    INSTANCE;

    private String mpdAddress;
    private String licenseServerAddress;
    private boolean downloadLicense;

    public void setMpdAddress(String mpdAddress) {
        this.mpdAddress = mpdAddress;
    }

    public void setLicenseServerAddress(String licenseServerAddress) {
        this.licenseServerAddress = licenseServerAddress;
    }

    public void setDownloadLicense(boolean downloadLicense) {
        this.downloadLicense = downloadLicense;
    }

    public String mpdAddress() {
        return mpdAddress;
    }

    public String licenseServerAddress() {
        return licenseServerAddress;
    }

    public boolean shouldDownloadLicense() {
        return downloadLicense;
    }

    public void toastMissingParameters(Context context) {
        if (mpdAddress == null || mpdAddress.isEmpty()) {
            Toast.makeText(context, "MPD address not specified", Toast.LENGTH_SHORT).show();
        }

        if (licenseServerAddress == null || licenseServerAddress.isEmpty()) {
            Toast.makeText(context, "License server address not specified", Toast.LENGTH_SHORT).show();
        }
    }
}
