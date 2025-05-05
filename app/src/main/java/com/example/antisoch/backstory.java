package com.example.antisoch;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class backstory extends AppCompatActivity {

    private ImageView nishu, toto, map;
    private ImageButton nextButton;
    private TextView dialogBox;
    private int clickStage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backstory);

        // Initialize views
        nishu = findViewById(R.id.nishu);
        toto = findViewById(R.id.toto);
        map = findViewById(R.id.map);
        nextButton = findViewById(R.id.nextButton);
        dialogBox = findViewById(R.id.dialogBox);

        // Set initial dialog text
        dialogBox.setText("Nishu loves adventures! One day, while playing, he finds a mysterious map glowing under a big tree.");

        // NEXT BUTTON listener
        nextButton.setOnClickListener(v -> {
            if (clickStage == 0) {
                animateCharacters();
            }
        });

        // MAP CLICK listener
        map.setOnClickListener(v -> {
            if (clickStage == 1) {
                dialogBox.setText("The map leads to Safety Island, a magical place where kids learn how to stay safe, strong, and smart. To uncover the treasure, Nishu must travel through the island, solving fun challenges that teach him how to stay safe in all sorts of situations!");

                // Move to next activity after delay
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(backstory.this, AdventureMapActivity.class);
                    startActivity(intent);
                    finish();
                }, 5000); // Wait 5 seconds
            }
        });
    }

    private void animateCharacters() {
        // Set walk animations
        nishu.setBackgroundResource(R.drawable.nishu_walk);
        toto.setBackgroundResource(R.drawable.toto_walk);

        AnimationDrawable nishuAnim = (AnimationDrawable) nishu.getBackground();
        AnimationDrawable totoAnim = (AnimationDrawable) toto.getBackground();

        nishuAnim.start();
        totoAnim.start();

        // Animate movement toward map
        nishu.animate()
                .translationXBy(200f)
                .setDuration(2500)
                .withEndAction(() -> {
                    nishuAnim.stop();
                    nishu.setBackgroundResource(R.drawable.nishu_idle);
                })
                .start();

        toto.animate()
                .translationXBy(200f)
                .setDuration(2500)
                .withEndAction(() -> {
                    totoAnim.stop();
                    toto.setBackgroundResource(R.drawable.toto_idle);

                    // Update text after animation ends
                    dialogBox.setText("His trusty teddy bear, Toto, sits beside him, ready for anything.\n\n\"It's a treasure map!\" Nishu says. \"But it's not gold, it's something even more special!\"");

                    clickStage = 1; // Enable map click
                })
                .start();
    }
}
