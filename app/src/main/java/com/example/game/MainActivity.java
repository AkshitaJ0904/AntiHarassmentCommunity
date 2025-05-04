package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the window full screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Hide the ActionBar if present
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set the layout
        setContentView(R.layout.activity_main);

        // Enter immersive full-screen mode
        hideSystemUI();

        // Button logic
        ImageButton playButton = findViewById(R.id.btnPlay);
        ImageButton settingsButton = findViewById(R.id.btnSettings);
        ImageButton backButton = findViewById(R.id.btnBack); // Optional use

        playButton.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            startActivity(new Intent(this, CharacterSelectActivity.class));
        });

        settingsButton.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            startActivity(new Intent(this, SettingsActivity.class));
        });

        backButton.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            finishAffinity(); // Closes all activities
            System.exit(0); // Optional: forcefully stops the process
        });
    }

    // Re-apply immersive mode when window regains focus
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SoundManager.getInstance(this).startMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundManager.getInstance(this).pauseMusic();
    }


    // Immersive mode function
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}
