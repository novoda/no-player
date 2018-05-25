package com.novoda.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        final EditText customUriView = findViewById(R.id.text_custom_uri);
        Button useCustomUri = findViewById(R.id.button_custom_uri);

        useCustomUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customUri = customUriView.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .setData(Uri.parse(customUri));

                startActivity(intent);
            }
        });
    }

}
