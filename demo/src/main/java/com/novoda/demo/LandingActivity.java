package com.novoda.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LandingActivity extends AppCompatActivity {

    public static final String KEY_MPD_ADDRESS = "MPD_ADDRESS";
    public static final String KEY_LICENSE_SERVER_ADDRESS = "LICENSE_SERVER_ADDRESS";
    public static final String KEY_TOKEN = "TOKEN_ADDRESS";

    private EditText mpdAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mpdAddress = findViewById(R.id.mpd_address);
        Button playUsingProperties = findViewById(R.id.button_custom_uri);

        playUsingProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customUri = mpdAddress.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .putExtra(KEY_MPD_ADDRESS, mpdAddress.getText().toString())
                        .putExtra(KEY_LICENSE_SERVER_ADDRESS, "")
                        .putExtra(KEY_TOKEN, "token")
                        .setData(Uri.parse(customUri));

                startActivity(intent);
            }
        });
    }

    private final View.OnClickListener playUsingProperties = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                    .putExtra(KEY_MPD_ADDRESS, mpdAddress.getText().toString())
                    .putExtra(KEY_LICENSE_SERVER_ADDRESS, "")
                    .putExtra(KEY_TOKEN, "token");

            startActivity(intent);
        }
    };

}
