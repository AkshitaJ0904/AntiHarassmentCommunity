package com.example.safespace;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private EditText commentEditText;
    private Button suggestButton, cancelButton;
    private ImageButton backButton;
    private TextView postTitleText, postContentText, usernameText, timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Initialize UI components
        initializeViews();

        // Get data from intent
        String postId = getIntent().getStringExtra("POST_ID");
        String postTitle = getIntent().getStringExtra("POST_TITLE");
        String postContent = getIntent().getStringExtra("POST_CONTENT");
        String username = getIntent().getStringExtra("USERNAME");
        String timestamp = getIntent().getStringExtra("TIMESTAMP");

        // Set post details
        postTitleText.setText(postTitle);
        postContentText.setText(postContent);
        usernameText.setText(username);
        timeText.setText(timestamp);

        // Initialize comment list and adapter
        commentList = new ArrayList<>();
        loadDummyComments(); // For testing, load some dummy comments

        commentAdapter = new CommentAdapter(this, commentList);
        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set click listeners
        backButton.setOnClickListener(view -> finish());

        cancelButton.setOnClickListener(view -> {
            commentEditText.setText("");
        });

        suggestButton.setOnClickListener(view -> {
            String commentText = commentEditText.getText().toString().trim();
            if (!commentText.isEmpty()) {
                addNewComment(commentText);
                commentEditText.setText("");
            } else {
                Toast.makeText(CommentActivity.this, "Please write a comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        suggestButton = findViewById(R.id.suggestButton);
        cancelButton = findViewById(R.id.cancelButton);
        backButton = findViewById(R.id.backButton);
        postTitleText = findViewById(R.id.postTitleText);
        postContentText = findViewById(R.id.postContentText);
        usernameText = findViewById(R.id.usernameText);
        timeText = findViewById(R.id.timeText);
    }

    private void addNewComment(String commentText) {
        // Get current timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        // Create new comment
        Comment newComment = new Comment(
                "user_" + System.currentTimeMillis(), // Generate unique ID
                "Current User", // Replace with actual username
                commentText,
                timestamp,
                0, // likes
                0  // dislikes
        );

        // Add to list and notify adapter
        commentList.add(0, newComment); // Add at the top
        commentAdapter.notifyItemInserted(0);
        commentsRecyclerView.smoothScrollToPosition(0);

        // In a real app, you would save this comment to your database here
    }

    private void loadDummyComments() {
        // Add some dummy data for testing
        commentList.add(new Comment(
                "user_1",
                "@unkind",
                "Hello, My uncle runs a pg in Bandra. You can contact him on 767SXXXXXX, Rama Kumar. Hope this helps you out!",
                "2 November 2022 16:45",
                12,
                3
        ));

        commentList.add(new Comment(
                "user_2",
                "@unkind",
                "Yeah, I contacted him too, he is sweet but currently no beds are there",
                "2 November 2022 17:00",
                5,
                0
        ));
    }
}
