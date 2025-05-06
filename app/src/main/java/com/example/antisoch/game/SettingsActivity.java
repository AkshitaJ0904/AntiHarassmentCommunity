package com.example.antisoch.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.antisoch.R;

public class SettingsActivity extends BaseActivity {

    private static final String PREFS_NAME = "GamePrefs";
    private static final String MUSIC_ENABLED_KEY = "music_enabled";
    private static final String SOUND_ENABLED_KEY = "sound_enabled";

    private boolean isMusicEnabled;
    private boolean isSoundEnabled;

    private ImageButton musicButton;
    private ImageButton soundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full-screen immersive mode
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_settings);
        hideSystemUI();

        ImageButton backBtn = findViewById(R.id.btnBackSettings);
        backBtn.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            finish();
        });

        musicButton = findViewById(R.id.option2_button);
        soundButton = findViewById(R.id.option1_button);

        loadPreferences();
        applySettingsToUI();

        musicButton.setOnClickListener(v -> {
            isMusicEnabled = !isMusicEnabled;
            SoundManager.getInstance(this).setMusicEnabled(isMusicEnabled);
            savePreferences();
            applySettingsToUI();
            SoundManager.getInstance(this).playButtonClick();
        });

        soundButton.setOnClickListener(v -> {
            isSoundEnabled = !isSoundEnabled;
            SoundManager.getInstance(this).setSoundEnabled(isSoundEnabled);
            savePreferences();
            applySettingsToUI();
            SoundManager.getInstance(this).playButtonClick();
        });
    }

    private void applySettingsToUI() {
        musicButton.setImageResource(isMusicEnabled ? R.drawable.option2_button : R.drawable.option2_button_off);
        soundButton.setImageResource(isSoundEnabled ? R.drawable.option1_button : R.drawable.option1_button_off);
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isMusicEnabled = prefs.getBoolean(MUSIC_ENABLED_KEY, true);
        isSoundEnabled = prefs.getBoolean(SOUND_ENABLED_KEY, true);

        SoundManager.getInstance(this).setMusicEnabled(isMusicEnabled);
        SoundManager.getInstance(this).setSoundEnabled(isSoundEnabled);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(MUSIC_ENABLED_KEY, isMusicEnabled);
        editor.putBoolean(SOUND_ENABLED_KEY, isSoundEnabled);
        editor.apply();
    }

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
