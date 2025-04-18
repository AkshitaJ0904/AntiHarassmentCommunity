package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseAvatarActivity extends AppCompatActivity {

    private ImageView ivMaleAvatar, ivFemaleAvatar, ivChildAvatar;
    private Button btnNext;
    private String selectedAvatar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_avatar);

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
                    Intent intent = new Intent(ChooseAvatarActivity.this, HomeActivity.class);
                    intent.putExtra("avatar", selectedAvatar);
                    startActivity(intent);
                }
            }
        });
    }

    private void highlightSelectedAvatar() {
        ivMaleAvatar.setAlpha(selectedAvatar.equals("male") ? 1.0f : 0.5f);
        ivFemaleAvatar.setAlpha(selectedAvatar.equals("female") ? 1.0f : 0.5f);
        ivChildAvatar.setAlpha(selectedAvatar.equals("child") ? 1.0f : 0.5f);
    }
}
