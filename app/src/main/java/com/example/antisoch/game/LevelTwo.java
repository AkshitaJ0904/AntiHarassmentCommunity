package com.example.antisoch.game;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.antisoch.R;

import java.util.ArrayList;
import java.util.List;

public class LevelTwo extends AppCompatActivity {

    private GridLayout imageGrid;
    private TextView dialogBox;
    private MediaPlayer mediaPlayer;
    private ImageButton nextButton;

    private List<List<ImageItem>> allSets = new ArrayList<>();
    private int currentSetIndex = 0;
    private List<ImageView> currentImageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title bar for full-screen experience
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set the window flags to fullscreen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Hide the navigation and status bars (immersive sticky mode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }

        // Set content view to your XML layout
        setContentView(R.layout.activity_level_two);

        // Initialize the views
        imageGrid = findViewById(R.id.imageGrid);
        dialogBox = findViewById(R.id.dialogBox);
        nextButton = findViewById(R.id.nextButton);

        // Set up the grid layout dimensions
        setupGridLayoutDimensions();

        // Initialize the dialog box and play the intro audio
        dialogBox.setText("Select the private parts of our body");
        playAudio(R.raw.select_images);

        // Set up the image sets
        setupImageSets();
        displayImageSet(currentSetIndex);

        // Set the next button functionality
        nextButton.setOnClickListener(v -> {
            if (checkAnswers()) {
                dialogBox.setText("Correct, No one must touch you there!");
                playAudio(R.raw.correct_answer);
                showFeedbackIcons();
                nextButton.postDelayed(() -> {
                    currentSetIndex++;
                    if (currentSetIndex < allSets.size()) {
                        displayImageSet(currentSetIndex);
                        dialogBox.setText("Select the private parts of our body");
                        playAudio(R.raw.select_images);
                    } else {
                        playAudio(R.raw.level_2_end_bg);
                        mediaPlayer.setOnCompletionListener(mp -> {

                            dialogBox.setText("You did it! You're safe and smart");
                            playAudio(R.raw.you_did_it_level_complete);
                        });
                    }
                }, 3000);
            } else {
                dialogBox.setText("Try again!");
                playAudio(R.raw.try_again);
            }
        });
    }

    // Set up the grid layout dimensions to be 400x350dp and centered
    private void setupGridLayoutDimensions() {
        int gridWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
        int gridHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(gridWidth, gridHeight);
        layoutParams.gravity = Gravity.CENTER;
        imageGrid.setLayoutParams(layoutParams);
        imageGrid.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        imageGrid.setColumnCount(2);
        imageGrid.setRowCount(2);
    }

    // Set up the different image sets for each level
    private void setupImageSets() {
        List<ImageItem> set1 = List.of(
                new ImageItem(R.drawable.head_good4, true),
                new ImageItem(R.drawable.chest_bad1, false),
                new ImageItem(R.drawable.hand_good2, true),
                new ImageItem(R.drawable.feet_good1, true));

        List<ImageItem> set2 = List.of(
                new ImageItem(R.drawable.back_good3, true),
                new ImageItem(R.drawable.private_bad2, false),
                new ImageItem(R.drawable.thigh_bad3, false),
                new ImageItem(R.drawable.head_good5, true));

        List<ImageItem> set3 = List.of(
                new ImageItem(R.drawable.butt_bad4, false),
                new ImageItem(R.drawable.thigh_bad3, false),
                new ImageItem(R.drawable.private_bad2, false),
                new ImageItem(R.drawable.chest_bad1, false));

        allSets.add(set1);
        allSets.add(set2);
        allSets.add(set3);
    }

    // Display the current set of images
    private void displayImageSet(int index) {
        imageGrid.removeAllViews();
        currentImageViews.clear();
        List<ImageItem> set = allSets.get(index);

        int imageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int marginSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());

        for (ImageItem item : set) {
            FrameLayout frame = new FrameLayout(this);
            GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
            gridParams.width = imageSize;
            gridParams.height = imageSize;
            gridParams.setMargins(marginSize, marginSize, marginSize, marginSize);
            frame.setLayoutParams(gridParams);

            ImageView imageView = new ImageView(this);
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            imageView.setLayoutParams(imageParams);
            imageView.setImageResource(item.imageResId);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(30, 30, 30, 30); // Add some padding to avoid border clipping

            imageView.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                v.setBackground(v.isSelected() ?
                        ContextCompat.getDrawable(this, R.drawable.selector_border) : null);
            });

            frame.addView(imageView);
            imageGrid.addView(frame);
            currentImageViews.add(imageView);
        }
    }

    // Check if all selected answers are correct
    private boolean checkAnswers() {
        List<ImageItem> set = allSets.get(currentSetIndex);
        boolean allCorrect = true;

        for (int i = 0; i < set.size(); i++) {
            boolean isSelected = currentImageViews.get(i).isSelected();
            boolean shouldBeSelected = !set.get(i).isCorrect;
            if (isSelected != shouldBeSelected) {
                allCorrect = false;
            }
        }
        return allCorrect;
    }

    // Show feedback icons after correct answers
    private void showFeedbackIcons() {
        if (!checkAnswers()) return; // Only show feedback if answers are fully correct

        List<ImageItem> set = allSets.get(currentSetIndex);
        for (int i = 0; i < set.size(); i++) {
            ImageView mainImg = currentImageViews.get(i);
            FrameLayout parent = (FrameLayout) mainImg.getParent();

            ImageView icon = new ImageView(this);

            // Convert dp to px for icon size
            float sizeInDp = 40;
            int iconSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeInDp, getResources().getDisplayMetrics()
            );

            FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(iconSize, iconSize);
            iconParams.gravity = Gravity.TOP | Gravity.END;
            icon.setLayoutParams(iconParams);

            boolean isSelected = mainImg.isSelected();
            boolean isPrivatePart = !set.get(i).isCorrect;

            if (isPrivatePart && isSelected) {
                icon.setImageResource(R.drawable.tick);
                icon.setColorFilter(Color.parseColor("#663333"), PorterDuff.Mode.SRC_IN);
            } else if (!isPrivatePart && isSelected) {
                icon.setImageResource(R.drawable.cross);
            } else {
                continue;
            }

            parent.addView(icon);
        }
    }

    // Play audio for feedback or instructions
    private void playAudio(int resId) {
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }

    // Image item class to hold image resource and correctness
    static class ImageItem {
        int imageResId;
        boolean isCorrect;

        ImageItem(int resId, boolean correct) {
            imageResId = resId;
            isCorrect = correct;
        }
    }
}
