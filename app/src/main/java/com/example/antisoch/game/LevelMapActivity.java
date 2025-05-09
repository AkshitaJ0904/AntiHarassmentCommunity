package com.example.antisoch.game;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.antisoch.R;
import com.otaliastudios.zoom.ZoomLayout;

public class LevelMapActivity extends BaseActivity {

    private FrameLayout mapContainer;
    private ZoomLayout zoomLayout;
    private int profileIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            if (getSupportActionBar() != null) getSupportActionBar().hide();

            setContentView(R.layout.activity_level_map);
            hideSystemUI();

            mapContainer = findViewById(R.id.mapContainer);
            zoomLayout = findViewById(R.id.zoomLayout);

            ImageButton back = findViewById(R.id.btnBackMap);
            back.setOnClickListener(v -> {
                AppUtils.playClickSound(this);
                Intent intent = new Intent(this, CharacterSelectActivity.class);
                startActivity(intent);
                finish();
            });

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mapContainer.getLayoutParams();
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;
            mapContainer.setLayoutParams(layoutParams);

            mapContainer.post(() -> {
                try {
                    float scaleX = (float) screenWidth / mapContainer.getWidth();
                    float scaleY = (float) screenHeight / mapContainer.getHeight();
                    float minZoom = Math.min(scaleX, scaleY);

                    zoomLayout.setMinZoom(minZoom);
                    zoomLayout.zoomTo(minZoom, false);

                    addAllLevelButtons();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error initializing map: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in onCreate: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addAllLevelButtons() {
        addLevelButton(0.470f, 0.690f, "1"); // Level 1
        addLevelButton(0.180f, 0.580f, "2"); // Level 2
        addLevelButton(0.221f, 0.330f, "3"); // Level 3
        addLevelButton(0.480f, 0.280f, "4"); // Level 4
        addLevelButton(0.752f, 0.200f, "5"); // Level 5
        addLevelButton(0.765f, 0.620f, "6"); // Level 6
    }

    private void addLevelButton(float xPercent, float yPercent, String levelNumber) {
        FrameLayout buttonLayout = new FrameLayout(this);
        int sizeInDp = 48;
        int sizeInPx = (int) (sizeInDp * getResources().getDisplayMetrics().density);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(sizeInPx, sizeInPx);
        layoutParams.leftMargin = (int) (xPercent * mapContainer.getWidth()) - sizeInPx / 2;
        layoutParams.topMargin = (int) (yPercent * mapContainer.getHeight()) - sizeInPx / 2;

        buttonLayout.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.level_icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        TextView textView = getTextView(levelNumber);

        buttonLayout.addView(imageView);
        buttonLayout.addView(textView);

        buttonLayout.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            if ("1".equals(levelNumber)) {
                Intent intent = new Intent(this, LevelOneActivity.class);
                intent.putExtra("profileIndex", profileIndex);
                startActivity(intent);
            } else if ("2".equals(levelNumber)) {
                Intent intent = new Intent(this, LevelTwo.class);
                intent.putExtra("profileIndex", profileIndex);
                startActivity(intent);
            } else {
                android.widget.Toast.makeText(this, "Level " + levelNumber + " clicked", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        mapContainer.addView(buttonLayout);
    }

    @NonNull
    private TextView getTextView(String levelNumber) {
        TextView textView = new TextView(this);
        textView.setText(levelNumber);
        textView.setTextSize(22);
        textView.setTextColor(Color.WHITE);

        // Load Lilita One and apply BOLD style
        Typeface lilita = ResourcesCompat.getFont(this, R.font.lilitaone_regular);
        textView.setTypeface(Typeface.create(lilita, Typeface.BOLD)); // Enforce bold

        textView.setGravity(Gravity.CENTER);
        textView.setShadowLayer(8, 0, 2, Color.BLACK);
        textView.setIncludeFontPadding(false);
        textView.setPadding(0, 0, 0, 20);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );
        textView.setLayoutParams(params);

        return textView;
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
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}