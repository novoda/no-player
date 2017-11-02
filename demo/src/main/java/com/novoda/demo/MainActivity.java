package com.novoda.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.novoda.utils.NoPlayerLog;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoPlayerLog.setLoggingEnabled(true);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        startActivity(intent);
    }
}
