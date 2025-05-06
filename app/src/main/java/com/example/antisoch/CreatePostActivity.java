package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity {

    private EditText etSubject, etContent;
    private TextView tagGeneral, tagSupport, tagLegal, tagSafety, tagProblems, tagFeatures;
    private Button btnCreatePost, btnCancel;
    private ImageButton btnBack, btnCamera;

    private String selectedTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initializeViews();
        setUpTagListeners();
        setUpButtonListeners();
    }

    private void initializeViews() {
        etSubject = findViewById(R.id.etSubject);
        etContent = findViewById(R.id.etContent);

        tagGeneral = findViewById(R.id.tagGeneral);
        tagSupport = findViewById(R.id.tagSupport);
        tagLegal = findViewById(R.id.tagLegal);
        tagSafety = findViewById(R.id.tagSafety);
        tagProblems = findViewById(R.id.tagProblems);
        tagFeatures = findViewById(R.id.tagFeatures);

        btnCreatePost = findViewById(R.id.btnCreatePost);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
        btnCamera = findViewById(R.id.btnCamera);
    }

    private void setUpTagListeners() {
        View.OnClickListener tagClickListener = view -> {
            resetTagsSelection();
            view.setAlpha(1.0f);
            selectedTag = ((TextView) view).getText().toString();
            Toast.makeText(this, "Selected tag: " + selectedTag, Toast.LENGTH_SHORT).show();
        };

        tagGeneral.setOnClickListener(tagClickListener);
        tagSupport.setOnClickListener(tagClickListener);
        tagLegal.setOnClickListener(tagClickListener);
        tagSafety.setOnClickListener(tagClickListener);
        tagProblems.setOnClickListener(tagClickListener);
        tagFeatures.setOnClickListener(tagClickListener);
    }

    private void resetTagsSelection() {
        float defaultAlpha = 0.7f;
        tagGeneral.setAlpha(defaultAlpha);
        tagSupport.setAlpha(defaultAlpha);
        tagLegal.setAlpha(defaultAlpha);
        tagSafety.setAlpha(defaultAlpha);
        tagProblems.setAlpha(defaultAlpha);
        tagFeatures.setAlpha(defaultAlpha);
    }

    private void setUpButtonListeners() {
        btnCreatePost.setOnClickListener(view -> {
            if (validateInput()) {
                createNewPost();
            }
        });

        View.OnClickListener cancelListener = view -> finish();
        btnCancel.setOnClickListener(cancelListener);
        btnBack.setOnClickListener(cancelListener);

        btnCamera.setOnClickListener(view -> {
            Intent intent = new Intent(CreatePostActivity.this, UserPostsActivity.class);
            startActivity(intent);
        });

    }

    private boolean validateInput() {
        boolean isValid = true;

        if (etSubject.getText().toString().trim().isEmpty()) {
            etSubject.setError("Subject cannot be empty");
            isValid = false;
        }

        if (etContent.getText().toString().trim().isEmpty()) {
            etContent.setError("Content cannot be empty");
            isValid = false;
        }

        if (selectedTag.isEmpty()) {
            Toast.makeText(this, "Please select a tag", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void createNewPost() {
        String subject = etSubject.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        // Show loading indicator
        btnCreatePost.setEnabled(false);
        btnCreatePost.setText("Creating Post...");

        // Create post object
        ForumPost post = new ForumPost();
        post.setSubject(subject);
        post.setContent(content);
        post.setTag(selectedTag);
        post.setUsername("Golangnya"); // TODO: Replace with logged-in user's username
        post.setTimestamp(System.currentTimeMillis());
        post.setTimePosted("Just now"); // Will be formatted when retrieved
        
        // Add categories for compatibility with ForumPostAdapter
        List<String> categories = new ArrayList<>();
        categories.add("General");
        categories.add(selectedTag);
        post.setCategories(categories);
        
        // Set default counts
        post.setLikes(0);
        post.setComments(0);
        post.setShares(0);

        // Save post to Firebase
        FirebaseUtil.savePost(post, new FirebaseUtil.PostUploadListener() {
            @Override
            public void onSuccess(ForumPost post) {
                runOnUiThread(() -> {
                    Toast.makeText(CreatePostActivity.this, "Post created successfully!", Toast.LENGTH_SHORT).show();
                    
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("post_created", true);
                    
                    // If started for result, return result
                    setResult(RESULT_OK, resultIntent);
                    
                    // Navigate to UserPostsActivity
                    Intent intent = new Intent(CreatePostActivity.this, UserPostsActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    btnCreatePost.setEnabled(true);
                    btnCreatePost.setText("Create Post");
                    Toast.makeText(CreatePostActivity.this, "Error creating post: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Bottom navigation methods
    public void onHomeClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onSearchClick(View view) {
        // TODO: Navigate to search screen
    }

    public void onTwitterClick(View view) {
        // TODO: Navigate to Twitter integration
    }

    public void onGamesClick(View view) {
        // TODO: Navigate to games screen
    }

    public void onProfileClick(View view) {
        // TODO: Navigate to profile screen
    }
}
