package com.example.game;

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
import androidx.core.view.ViewCompat;

public class backstory extends AppCompatActivity {

    private ImageView nishu, toto, map;
    private ImageButton nextButton;
    private TextView dialogBox, dialogBox2;
    private int clickStage = 0;

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backstory);

        nishu = findViewById(R.id.nishu);
        toto = findViewById(R.id.toto);
        map = findViewById(R.id.map);
        nextButton = findViewById(R.id.nextButton);
        dialogBox = findViewById(R.id.dialogBox);
        dialogBox2 = findViewById(R.id.dialogBox2);
        dialogBox2.setVisibility(View.INVISIBLE);
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

                        dialogBox.setVisibility(View.INVISIBLE);
                        dialogBox2.setVisibility(View.VISIBLE);


                        dialogBox2.setText("The map leads to Safety Island, a magical place where kids learn how to stay safe, strong, and smart. To uncover the treasure, Nishu must travel through the island, solving fun challenges that teach him how to stay safe in all sorts of situations!");

                        playAudio(R.raw.audio5);

                        int audioDuration = mediaPlayer.getDuration();

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
                        }, audioDuration);
                    }
                });
            }
        });
    }

    private void playAudio(int audioResId) {
        releaseMediaPlayer();
        mediaPlayer = MediaPlayer.create(backstory.this, audioResId);
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
}