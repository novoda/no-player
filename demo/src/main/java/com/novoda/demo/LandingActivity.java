package com.novoda.demo;

import android.content.Intent;
import android.net.Uri;
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

        Button playUsingProperties = findViewById(R.id.button_custom_uri);

        playUsingProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customUri = mpdAddress.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .putExtra(KEY_MPD_ADDRESS, mpdAddress.getText().toString())
                        .putExtra(KEY_LICENSE_SERVER_ADDRESS, licenseAddress.getText().toString())
                        .putExtra(KEY_DOWNLOAD_LICENSE, downloadLicense.isChecked())
                        .setData(Uri.parse(customUri));

                startActivity(intent);
            }
        });
    }

}
