package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.List;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        LinearLayout character1Layout = findViewById(R.id.character1Layout);
        LinearLayout character2Layout = findViewById(R.id.character2Layout);
        LinearLayout character3Layout = findViewById(R.id.character3Layout);
        LinearLayout addPlayerLayout = findViewById(R.id.addPlayerLayout);
        ImageButton addPlayer = findViewById(R.id.btnAddPlayer);

        // Hide all at the start
        character1Layout.setVisibility(View.GONE);
        character2Layout.setVisibility(View.GONE);
        character3Layout.setVisibility(View.GONE);
        addPlayerLayout.setVisibility(View.GONE);

        List<PlayerProfile> profiles = ProfileManager.getProfiles(this);
        LinearLayout[] layouts = {character1Layout, character2Layout, character3Layout};

        for (int i = 0; i < profiles.size(); i++) {
            PlayerProfile profile = profiles.get(i);
            LinearLayout layout = layouts[i];
            layout.setVisibility(View.VISIBLE);

            ImageView characterImage = (ImageView) layout.getChildAt(0);
            TextView nameText = (TextView) layout.getChildAt(1);

            int imageRes = profile.characterName.equals("Quack")
                    ? R.drawable.character_quack : R.drawable.character_aaradhya;
            characterImage.setImageResource(imageRes);
            nameText.setText(profile.username);

            final int index = i;
            layout.setOnClickListener(v -> {
                AppUtils.playClickSound(this);
                Intent intent = new Intent(this, LevelMapActivity.class);
                intent.putExtra("profileIndex", index);
                startActivity(intent);
            });

            layout.setOnLongClickListener(v -> {
                ProfileManager.deleteProfile(this, index);
                recreate(); // refresh UI
                return true;
            });
        }

        // Show Add Player only if profiles are less than 3
        if (profiles.size() < 3) {
            addPlayerLayout.setVisibility(View.VISIBLE);
            addPlayer.setOnClickListener(v -> {
                AppUtils.playClickSound(this);
                startActivity(new Intent(this, CreateProfileActivity.class));
            });
        }
        ImageButton back = findViewById(R.id.btnHome);
        back.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            startActivity(new Intent(this, MainActivity.class));
        });
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
