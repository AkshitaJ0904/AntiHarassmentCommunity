package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity2 extends AppCompatActivity {

    private static final String TAG = "SplashActivity2";
    private static final long SPLASH_DISPLAY_TIME = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Make sure this layout exists!

        Log.d(TAG, "SplashActivity2 started");

        // Wait for 2 seconds then start main activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Starting MainActivity2");
                    Intent mainIntent = new Intent(SplashActivity2.this, MainActivity2.class);
                    startActivity(mainIntent);
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, "Error starting MainActivity2: " + e.getMessage(), e);
                }
            }
        }, SPLASH_DISPLAY_TIME);
    }
}