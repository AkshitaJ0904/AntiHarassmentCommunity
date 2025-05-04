package com.example.safespace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class EditPostActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editContent;
    private TextView userName;
    private TextView postTime;
    private Button saveButton;
    private Button cancelButton;
    private ImageButton backButton;
    private ChipGroup categoryChipGroup;

    private Chip chipGeneral, chipSafety, chipResource, chipProblems, chipSupport, chipLegal;

    private UserPost currentPost;
    private int postId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // Initialize views
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        userName = findViewById(R.id.userName);
        postTime = findViewById(R.id.postTime);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        backButton = findViewById(R.id.backButton);
        categoryChipGroup = findViewById(R.id.categoryChipGroup);

        chipGeneral = findViewById(R.id.chipGeneral);
        chipSafety = findViewById(R.id.chipSafety);
        chipResource = findViewById(R.id.chipResource);
        chipProblems = findViewById(R.id.chipProblems);
        chipSupport = findViewById(R.id.chipSupport);
        chipLegal = findViewById(R.id.chipLegal);

        // Get post data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("post_id")) {
            postId = intent.getIntExtra("post_id", -1);
            loadPostData(postId);
        } else {
            Toast.makeText(this, "Error: No post data found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up click listeners
        setupClickListeners();
    }

    private void loadPostData(int postId) {
        // In a real app, you would load the post from your database or API
        // For demonstration purposes, we'll create dummy data similar to your sample posts

        // This is a placeholder implementation
        // In your actual app, you should retrieve the post from your data source
        if (postId == 1) {
            currentPost = new UserPost(
                    1,
                    "Golangirya",
                    "Is there any Girls Hostel available in Bandra? I need a PG in Bandra as I work till late night and it gets really dark and I feel unsafe?",
                    "3 mins ago",
                    125,
                    15,
                    155,
                    "Safety"
            );
        } else if (postId == 2 || postId == 3) {
            currentPost = new UserPost(
                    postId,
                    "Golangirya",
                    "My friend is facing some issues in her college and no actions have been taken. If anyone can help her legally, Please Connect",
                    postId == 2 ? "3 days ago" : "4 days ago",
                    125,
                    15,
                    155,
                    "Legal"
            );
        } else {
            // Create a default empty post if ID doesn't match
            currentPost = new UserPost(
                    postId,
                    "Golangirya",
                    "",
                    "Just now",
                    0,
                    0,
                    0,
                    "General"
            );
        }

        // Populate the UI with post data
        updateUIWithPostData();
    }

    private void updateUIWithPostData() {
        userName.setText(currentPost.getUserName());
        postTime.setText(currentPost.getPostTime());

        // Extract the title from the content if it exists (for this example, we'll consider first line as title)
        String content = currentPost.getContent();
        String title = "";

        if (content.contains("?")) {
            int endIndex = content.indexOf("?") + 1;
            title = content.substring(0, endIndex);
            content = content.substring(endIndex).trim();

            if (content.isEmpty()) {
                content = title; // Keep all content in title if there's nothing after question mark
                // title remains as is
            }
        }

        editTitle.setText(title);
        editContent.setText(content);

        // Select the appropriate category chip
        selectCategoryChip(currentPost.getCategory());
    }

    private void selectCategoryChip(String category) {
        // Reset all chips first
        chipGeneral.setChecked(false);
        chipSafety.setChecked(false);
        chipResource.setChecked(false);
        chipProblems.setChecked(false);
        chipSupport.setChecked(false);
        chipLegal.setChecked(false);

        // Select the appropriate chip based on category
        switch (category) {
            case "General":
                chipGeneral.setChecked(true);
                break;
            case "Safety":
                chipSafety.setChecked(true);
                break;
            case "Resource":
                chipResource.setChecked(true);
                break;
            case "Problems":
                chipProblems.setChecked(true);
                break;
            case "Support":
                chipSupport.setChecked(true);
                break;
            case "Legal":
                chipLegal.setChecked(true);
                break;
            default:
                chipGeneral.setChecked(true);
                break;
        }
    }

    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> savePostChanges());

        cancelButton.setOnClickListener(v -> {
            // Go back without saving changes
            finish();
        });

        backButton.setOnClickListener(v -> {
            // Same as cancel button in this context
            finish();
        });

        // Bottom navigation bar buttons (simplified implementation)
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            // Navigate to home/main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void savePostChanges() {
        // Validate input
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Please enter post content", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected category
        String category = getSelectedCategory();

        // Update the post
        // In a real app, you would save this to your database or API
        String fullContent = title;
        if (!content.isEmpty()) {
            if (!title.endsWith("?") && !title.endsWith(".") && !title.isEmpty()) {
                fullContent += ". ";
            } else if (!title.isEmpty()) {
                fullContent += " ";
            }
            fullContent += content;
        }

        // Update the current post
        currentPost.setContent(fullContent);
        currentPost.setCategory(category);

        // Create intent to return the updated post to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated_post_id", currentPost.getId());
        resultIntent.putExtra("updated_post_content", currentPost.getContent());
        resultIntent.putExtra("updated_post_category", currentPost.getCategory());

        // Set the result to be returned to the calling activity
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getSelectedCategory() {
        int checkedChipId = categoryChipGroup.getCheckedChipId();

        if (checkedChipId == R.id.chipGeneral) {
            return "General";
        } else if (checkedChipId == R.id.chipSafety) {
            return "Safety";
        } else if (checkedChipId == R.id.chipResource) {
            return "Resource";
        } else if (checkedChipId == R.id.chipProblems) {
            return "Problems";
        } else if (checkedChipId == R.id.chipSupport) {
            return "Support";
        } else if (checkedChipId == R.id.chipLegal) {
            return "Legal";
        } else {
            return "General"; // Default value
        }
    }
}