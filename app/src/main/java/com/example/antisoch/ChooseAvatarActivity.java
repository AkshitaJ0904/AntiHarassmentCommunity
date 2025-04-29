package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseAvatarActivity extends AppCompatActivity {
    private ImageView ivMaleAvatar, ivFemaleAvatar, ivChildAvatar;
    private Button btnNext;
    private String selectedAvatar = "";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_avatar);

        // Get the username from the intent (passed from WelcomeBackActivity)
        username = getIntent().getStringExtra("username");

        ivMaleAvatar = findViewById(R.id.ivMaleAvatar);
        ivFemaleAvatar = findViewById(R.id.ivFemaleAvatar);
        ivChildAvatar = findViewById(R.id.ivChildAvatar);
        btnNext = findViewById(R.id.btnNext);

        ivMaleAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAvatar = "male";
                highlightSelectedAvatar();
            }
        });

        ivFemaleAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAvatar = "female";
                highlightSelectedAvatar();
            }
        });

        ivChildAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAvatar = "child";
                highlightSelectedAvatar();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedAvatar.isEmpty()) {
                    // Navigate to Home page with both avatar selection and username
                    Intent intent = new Intent(ChooseAvatarActivity.this, HomeActivity.class);
                    intent.putExtra("avatar", selectedAvatar);
                    // Forward the username to HomeActivity
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish(); // Close this activity so user can't go back with back button
                } else {
                    // Show a message if no avatar is selected
                    Toast.makeText(ChooseAvatarActivity.this, "Please select an avatar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void highlightSelectedAvatar() {
        ivMaleAvatar.setAlpha(selectedAvatar.equals("male") ? 1.0f : 0.5f);
        ivFemaleAvatar.setAlpha(selectedAvatar.equals("female") ? 1.0f : 0.5f);
        ivChildAvatar.setAlpha(selectedAvatar.equals("child") ? 1.0f : 0.5f);

        // Enable the Next button when an avatar is selected
        btnNext.setEnabled(true);
    }
}