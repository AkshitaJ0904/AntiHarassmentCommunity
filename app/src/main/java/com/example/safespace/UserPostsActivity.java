package com.example.safespace;

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

import java.util.ArrayList;
import java.util.List;

public class UserPostsActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private UserPostsAdapter adapter;
    private List<UserPost> userPosts;

    private Chip chipGeneral, chipSupport, chipLegal, chipSafety;

    private static final int EDIT_POST_REQUEST_CODE = 1001;
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
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
        });

        setupCategoryChips();
        initializeSamplePosts();
        setupRecyclerView();
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
        List<UserPost> filteredPosts = new ArrayList<>();
        if (category.equals("General")) {
            adapter.updatePosts(userPosts);
            return;
        }
        for (UserPost post : userPosts) {
            if (post.getCategory().equals(category)) {
                filteredPosts.add(post);
            }
        }
        adapter.updatePosts(filteredPosts);
    }

    private void initializeSamplePosts() {
        userPosts = new ArrayList<>();
        userPosts.add(new UserPost(1, "Golangirya", "Is there any Girls Hostel available in Bandra? I need a PG in Bandra as I work till late night and it gets really dark and I feel unsafe?", "3 mins ago", 125, 15, 155, "Safety"));
        userPosts.add(new UserPost(2, "Golangirya", "My friend is facing some issues in her college and no actions have been taken. If anyone can help her legally, Please Connect", "3 days ago", 125, 15, 155, "Legal"));
        userPosts.add(new UserPost(3, "Golangirya", "My friend is facing some issues in her college and no actions have been taken. If anyone can help her legally, Please Connect", "4 days ago", 125, 15, 155, "Legal"));
    }

    private void setupRecyclerView() {
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
        userPosts.remove(post);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();

        String selectedCategory = getSelectedCategory();
        if (!selectedCategory.equals("General")) {
            filterPostsByCategory(selectedCategory);
        }
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
        }
    }
}
