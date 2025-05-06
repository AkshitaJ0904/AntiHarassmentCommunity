package com.example.antisoch.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;

import com.example.antisoch.R;

public class CreateProfileActivity extends BaseActivity {

    EditText etUsername;
    RadioGroup characterGroup;
    Button btnConfirm;
    String selectedCharacter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        etUsername = findViewById(R.id.etUsername);
        characterGroup = findViewById(R.id.characterGroup);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Set default selection
        characterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioQuack) {
                selectedCharacter = "Quack";
            } else if (checkedId == R.id.radioAaradhya) {
                selectedCharacter = "Aaradhya";
            }
        });

        btnConfirm.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            btnConfirm.setEnabled(false); // prevent double-tap

            String username = etUsername.getText().toString().trim();

            if (username.isEmpty() || selectedCharacter.isEmpty()) {
                Toast.makeText(this, "Please enter name and select character", Toast.LENGTH_SHORT).show();
                btnConfirm.setEnabled(true); // re-enable on validation fail
            } else {
                if (ProfileManager.canAddProfile(this)) {
                    PlayerProfile newProfile = new PlayerProfile(username, selectedCharacter, 0);
                    ProfileManager.addProfile(this, newProfile);

                    Intent intent = new Intent(this, backstory.class);
                    intent.putExtra("username", username);
                    intent.putExtra("character", selectedCharacter);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Maximum 3 profiles allowed!", Toast.LENGTH_SHORT).show();
                    btnConfirm.setEnabled(true); // re-enable on profile limit
                }
            }
        });
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
}
