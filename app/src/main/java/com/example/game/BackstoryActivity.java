package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
public class BackstoryActivity extends BaseActivity {

    private ImageView imgNishu, imgToto;
    private TextView tvDialogue;
    private int sceneIndex = 0;
    private String createdUsername, selectedCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_backstory);
        hideSystemUI();
        setContentView(R.layout.activity_backstory);

        imgNishu = findViewById(R.id.imgNishu);
        imgToto = findViewById(R.id.imgToto);
        tvDialogue = findViewById(R.id.dialogueBox).findViewById(R.id.tvDialogue);
        ImageButton btnNext = findViewById(R.id.btnNext);

        createdUsername = getIntent().getStringExtra("username");
        selectedCharacter = getIntent().getStringExtra("character");

        btnNext.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            if (sceneIndex == 0) {
                animateCharactersToRight();
                tvDialogue.setText("His trusty teddy bear, Toto, standing beside him, ready for anything.\n \"It's a treasure map!\" Nishu says.");
                sceneIndex++;
            } else {
                // Navigate to CharacterSelectActivity
                Intent intent = new Intent(BackstoryActivity.this, CharacterSelectActivity.class);
                intent.putExtra("username", createdUsername);
                intent.putExtra("character", selectedCharacter);
                startActivity(intent);
                finish();
            }

        });
    }

    private void animateCharactersToRight() {
        imgNishu.animate().translationX(1100).setDuration(1000).start();
        imgToto.animate().translationX(1100).setDuration(1000).start();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUI();
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
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}
