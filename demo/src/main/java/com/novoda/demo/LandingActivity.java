package com.novoda.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    public static final String KEY_MPD_ADDRESS = "MPD_ADDRESS";
    public static final String KEY_LICENSE_SERVER_ADDRESS = "LICENSE_SERVER_ADDRESS";
    public static final String KEY_DOWNLOAD_LICENSE = "DOWNLOAD_LICENSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        final EditText mpdAddress = findViewById(R.id.mpd_address);
        final EditText licenseAddress = findViewById(R.id.license_address);
        final CheckBox downloadLicense = findViewById(R.id.download_license);
        final CheckBox useContentProtection = findViewById(R.id.use_content_proteciton);

        Button playUsingProperties = findViewById(R.id.button_custom_uri);

        playUsingProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackParameters playbackParameters = PlaybackParameters.INSTANCE;
                playbackParameters.setMpdAddress(mpdAddress.getText().toString());
                playbackParameters.setLicenseServerAddress(licenseAddress.getText().toString());
                playbackParameters.setDownloadLicense(downloadLicense.isChecked());
                playbackParameters.setUseContentProtection(useContentProtection.isChecked());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
