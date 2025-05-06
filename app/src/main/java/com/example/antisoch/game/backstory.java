package com.example.antisoch.game;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.antisoch.R;

public class backstory extends AppCompatActivity {

    private ImageView nishu, toto, map;
    private ImageButton nextButton;
    private TextView dialogBox, dialogBox2;
    private CardView dialogCard, dialogCard2;
    private int clickStage = 0;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backstory);

        // UI setup
        nishu = findViewById(R.id.nishu);
        toto = findViewById(R.id.toto);
        map = findViewById(R.id.map);
        nextButton = findViewById(R.id.nextButton);
        dialogBox = findViewById(R.id.dialogBox);
        dialogBox2 = findViewById(R.id.dialogBox2);
        dialogCard = findViewById(R.id.dialogCard);
        dialogCard2 = findViewById(R.id.dialogCard2);

        dialogCard2.setVisibility(View.INVISIBLE);
        dialogBox.setText("Nishu loves adventures! One day, while playing, he finds a mysterious map glowing under a big tree.");
        playAudio(R.raw.audio1);
        map.setClickable(false);

        startMapSwingAnimation();

        nextButton.setOnClickListener(v -> {
            switch (clickStage) {
                case 0:
                    animateCharacters();
                    clickStage++;
                    break;

                case 1:
                    dialogBox.setText("His trusty teddy bear, Toto, sits beside him, ready for anything.");
                    playAudio(R.raw.audio2);
                    clickStage++;
                    break;

                case 2:
                    dialogBox.setText("\"It's a treasure map!\" Nishu says. \"But it's not gold—it's something even more special!\"");
                    playAudio(R.raw.audio3);
                    clickStage++;
                    break;

                case 3:
                    dialogBox.setText("Tap on the map to begin the journey to Safety Island!");
                    playAudio(R.raw.audio4);
                    map.setClickable(true);
                    clickStage++;
                    break;
            }
        });

        map.setOnClickListener(v -> {
            if (clickStage == 4) {
                map.setClickable(false);

                dialogCard.setVisibility(View.INVISIBLE);
                map.setVisibility(View.INVISIBLE);

                ImageView cloudleft = findViewById(R.id.cloudleft);
                ImageView cloudright = findViewById(R.id.cloudright);

                ObjectAnimator cloudLeftIn = ObjectAnimator.ofFloat(cloudleft, "translationX", -715f, -580f);
                ObjectAnimator cloudRightIn = ObjectAnimator.ofFloat(cloudright, "translationX", 725f, 580f);
                cloudLeftIn.setDuration(1200);
                cloudRightIn.setDuration(1200);

                AnimatorSet cloudInSet = new AnimatorSet();
                cloudInSet.playTogether(cloudLeftIn, cloudRightIn);
                cloudInSet.start();

                cloudInSet.addListener(new android.animation.AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        dialogCard2.setVisibility(View.VISIBLE);

                        dialogBox2.setText("The map leads to Safety Island, a magical place where kids learn how to stay safe, strong, and smart. To uncover the treasure, Nishu must travel through the island, solving fun challenges that teach him how to stay safe in all sorts of situations!");

                        playAudio(R.raw.audio5);
                        int audioDuration = mediaPlayer.getDuration();

                        new Handler().postDelayed(() -> {
                            dialogCard2.setVisibility(View.INVISIBLE);

                            new Handler().postDelayed(() -> {
                                ObjectAnimator cloudLeftOut = ObjectAnimator.ofFloat(cloudleft, "translationX", -580f, -2000f);
                                ObjectAnimator cloudRightOut = ObjectAnimator.ofFloat(cloudright, "translationX", 580f, 2000f);
                                cloudLeftOut.setDuration(2000);
                                cloudRightOut.setDuration(2000);

                                AnimatorSet cloudOutSet = new AnimatorSet();
                                cloudOutSet.playTogether(cloudLeftOut, cloudRightOut);
                                cloudOutSet.start();

                                cloudOutSet.addListener(new android.animation.AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(android.animation.Animator animation) {
                                        Intent intent = new Intent(backstory.this, LevelMapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }, 500);
                        }, audioDuration);
                    }
                });
            }
        });
    }

    private void playAudio(int audioResId) {
        releaseMediaPlayer();
        mediaPlayer = MediaPlayer.create(backstory.this, audioResId);
        mediaPlayer.setVolume(2f, 2f);
        SoundManager.getInstance(this).setMusicVolume(0.1f);
        mediaPlayer.start();
    }



    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        releaseMediaPlayer();
        super.onDestroy();
    }

    private void animateCharacters() {
        nishu.setImageResource(R.drawable.nishu_walk);
        toto.setImageResource(R.drawable.toto_walk);

        AnimationDrawable nishuAnim = (AnimationDrawable) nishu.getDrawable();
        AnimationDrawable totoAnim = (AnimationDrawable) toto.getDrawable();

        nishuAnim.start();
        totoAnim.start();

        float walkDistance = 900f;

        nishu.animate()
                .translationXBy(walkDistance)
                .setDuration(3200)
                .withEndAction(() -> {
                    nishuAnim.stop();
                    nishu.setImageResource(R.drawable.nishu_idle);
                })
                .start();

        toto.animate()
                .translationXBy(walkDistance)
                .setDuration(3200)
                .withEndAction(() -> {
                    totoAnim.stop();
                    toto.setImageResource(R.drawable.toto_idle);
                })
                .start();
    }

    private void startMapSwingAnimation() {
        ObjectAnimator swingAnimator = ObjectAnimator.ofFloat(map, "rotation", -8f, 8f);
        swingAnimator.setDuration(800);
        swingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        swingAnimator.setRepeatMode(ValueAnimator.RESTART);
        swingAnimator.start();

        ValueAnimator bounceAnimator = ValueAnimator.ofFloat(0f, -45f, 0f);
        bounceAnimator.setDuration(900);
        bounceAnimator.setRepeatCount(ValueAnimator.INFINITE);
        bounceAnimator.setRepeatMode(ValueAnimator.RESTART);
        bounceAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            map.setTranslationY(value);
        });
        bounceAnimator.start();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
