package com.example.antisoch;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserPostsActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private UserPostsAdapter adapter;
    private List<UserPost> userPosts;

    private Chip chipGeneral, chipSupport, chipLegal, chipSafety;
    private FloatingActionButton fabCreatePost;

    private static final int EDIT_POST_REQUEST_CODE = 1001;
    private static final int CREATE_POST_REQUEST_CODE = 1002;
    private int currentEditingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);

        chipGeneral = findViewById(R.id.chipGeneral);
        chipSupport = findViewById(R.id.chipSupport);
        chipLegal = findViewById(R.id.chipLegal);
        chipSafety = findViewById(R.id.chipSafety);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton editProfileButton = findViewById(R.id.editProfile);
        ImageButton cameraButton = findViewById(R.id.cameraButton);

        backButton.setOnClickListener(v -> onBackPressed());

        editProfileButton.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show();
        });

        cameraButton.setOnClickListener(v -> {
            // Use camera button as create post button
            Intent intent = new Intent(UserPostsActivity.this, CreatePostActivity.class);
            startActivityForResult(intent, CREATE_POST_REQUEST_CODE);
        });

        setupCategoryChips();
        setupRecyclerView();
        loadPostsFromFirebase();
    }

    private void setupCategoryChips() {
        View.OnClickListener chipClickListener = v -> {
            resetChips();
            Chip clickedChip = (Chip) v;
            clickedChip.setChipBackgroundColorResource(R.color.dark_blue);
            clickedChip.setTextColor(getResources().getColor(android.R.color.white));
            filterPostsByCategory(clickedChip.getText().toString());
        };

        chipGeneral.setOnClickListener(chipClickListener);
        chipSupport.setOnClickListener(chipClickListener);
        chipLegal.setOnClickListener(chipClickListener);
        chipSafety.setOnClickListener(chipClickListener);
    }

    private void resetChips() {
        chipGeneral.setChipBackgroundColorResource(android.R.color.white);
        chipGeneral.setTextColor(getResources().getColor(android.R.color.black));

        chipSupport.setChipBackgroundColorResource(android.R.color.white);
        chipSupport.setTextColor(getResources().getColor(android.R.color.black));

        chipLegal.setChipBackgroundColorResource(android.R.color.white);
        chipLegal.setTextColor(getResources().getColor(android.R.color.black));

        chipSafety.setChipBackgroundColorResource(android.R.color.white);
        chipSafety.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void filterPostsByCategory(String category) {
        if (category.equals("General")) {
            // Reload all posts
            loadPostsFromFirebase();
            return;
        }
        
        // Load posts filtered by category
        FirebaseUtil.getPostsByTag(category, new FirebaseUtil.PostsLoadListener() {
            @Override
            public void onPostsLoaded(List<ForumPost> posts) {
                convertAndDisplayPosts(posts);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(UserPostsActivity.this, 
                        "Error loading posts: " + errorMessage, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPostsFromFirebase() {
        // Show loading indicator if needed
        FirebaseUtil.getAllPosts(new FirebaseUtil.PostsLoadListener() {
            @Override
            public void onPostsLoaded(List<ForumPost> posts) {
                // Convert forum posts to user posts and update UI
                convertAndDisplayPosts(posts);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(UserPostsActivity.this, 
                        "Error loading posts: " + errorMessage, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void convertAndDisplayPosts(List<ForumPost> forumPosts) {
        userPosts = new ArrayList<>();
        
        for (ForumPost forumPost : forumPosts) {
            // Create a safer way to convert the string ID to an integer
            int postId;
            try {
                if (forumPost.getId() != null) {
                    // Use absolute value to avoid negative IDs
                    postId = Math.abs(forumPost.getId().hashCode());
                } else {
                    postId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
                }
            } catch (Exception e) {
                postId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
            }
            
            UserPost userPost = new UserPost(
                    postId,
                    forumPost.getUsername(),
                    forumPost.getContent(),
                    forumPost.getTimePosted() != null ? 
                            forumPost.getTimePosted() : forumPost.getFormattedTimestamp(),
                    forumPost.getLikes(),
                    forumPost.getComments(),
                    forumPost.getShares(),
                    forumPost.getTag()
            );
            userPosts.add(userPost);
        }
        
        adapter.updatePosts(userPosts);
    }

    private void setupRecyclerView() {
        userPosts = new ArrayList<>(); // Initialize with empty list
        adapter = new UserPostsAdapter(userPosts,
                post -> showDeleteConfirmationDialog(post),
                post -> {
                    currentEditingPosition = userPosts.indexOf(post);
                    Intent intent = new Intent(UserPostsActivity.this, EditPostActivity.class);
                    intent.putExtra("post_id", post.getId());
                    startActivityForResult(intent, EDIT_POST_REQUEST_CODE);
                }
        );

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog(UserPost post) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_confirmation);
        dialog.setCancelable(true);

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button confirmDeleteButton = dialog.findViewById(R.id.confirmDeleteButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        confirmDeleteButton.setOnClickListener(v -> {
            deletePost(post);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void deletePost(UserPost post) {
        // Show loading indicator if needed
        
        // Find the actual post ID in Firebase by querying
        FirebaseUtil.getPostsCollection()
                .whereEqualTo("username", post.getUserName())
                .whereEqualTo("content", post.getContent())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Get the first matching document
                        String documentId = querySnapshot.getDocuments().get(0).getId();
                        
                        // Delete the document
                        FirebaseUtil.getPostsCollection().document(documentId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    userPosts.remove(post);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error deleting post: " + e.getMessage(), 
                                            Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Post not found in Firebase", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error finding post: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }

    private String getSelectedCategory() {
        if (chipGeneral.getTextColors().getDefaultColor() == getResources().getColor(android.R.color.white)) {
            return "General";
        } else if (chipSupport.getTextColors().getDefaultColor() == getResources().getColor(android.R.color.white)) {
            return "Support";
        } else if (chipLegal.getTextColors().getDefaultColor() == getResources().getColor(android.R.color.white)) {
            return "Legal";
        } else if (chipSafety.getTextColors().getDefaultColor() == getResources().getColor(android.R.color.white)) {
            return "Safety";
        }
        return "General";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int updatedId = data.getIntExtra("updated_post_id", -1);
            String updatedContent = data.getStringExtra("updated_post_content");
            String updatedCategory = data.getStringExtra("updated_post_category");

            for (UserPost post : userPosts) {
                if (post.getId() == updatedId) {
                    post.setContent(updatedContent);
                    post.setCategory(updatedCategory);
                    break;
                }
            }

            adapter.updatePosts(userPosts);
        } else if (requestCode == CREATE_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh posts after a new post is created
            loadPostsFromFirebase();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh posts when activity is resumed
        loadPostsFromFirebase();
    }
}
