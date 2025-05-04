package com.example.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer bgMusicPlayer;
    private SoundPool soundPool;
    private int buttonSoundId;
    private boolean musicEnabled = true;
    private boolean soundEnabled = true;
    private boolean isMusicPrepared = false;

    private SoundManager(Context context) {
        soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        buttonSoundId = soundPool.load(context, R.raw.button_click, 1);

        bgMusicPlayer = MediaPlayer.create(context, R.raw.background_music);
        bgMusicPlayer.setLooping(true);
        isMusicPrepared = true;
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        musicEnabled = prefs.getBoolean("music_enabled", true);
        soundEnabled = prefs.getBoolean("sound_enabled", true);
    }
    public static SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context.getApplicationContext());
        }
        return instance;
    }

    public void playButtonClick() {
        if (soundEnabled) {
            soundPool.play(buttonSoundId, 1, 1, 0, 0, 1);
        }
    }

    public void startMusic() {
        if (musicEnabled && isMusicPrepared && !bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.start();
        }
    }

    public void pauseMusic() {
        if (bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.pause();
        }
    }

    public void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (enabled) startMusic();
        else pauseMusic();
    }

    public void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void release() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }
        soundPool.release();
    }
}
