package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int CREATE_POST_REQUEST_CODE = 1001;

    private ChipGroup tabChipGroup;
    private RecyclerView recyclerForumPosts;
    private ForumPostAdapter forumPostAdapter;
    private List<ForumPost> forumPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI components
        initializeViews();
        setupTabChips();
        setupBottomNavigation();
        setupRecyclerView();
        setupEditButton(); // Changed from FAB to Edit Button
        loadForumPostsFromFirebase();
    }

    private void initializeViews() {
        tabChipGroup = findViewById(R.id.tab_chip_group);
        recyclerForumPosts = findViewById(R.id.recycler_forum_posts);
    }

    private void setupTabChips() {
        Chip generalChip = findViewById(R.id.chip_general);
        generalChip.setChecked(true);

        tabChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            filterPostsByCategory(checkedId);
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        recyclerForumPosts.setLayoutManager(new LinearLayoutManager(this));
        forumPosts = new ArrayList<>();
        forumPostAdapter = new ForumPostAdapter(this, forumPosts, getSupportFragmentManager());
        recyclerForumPosts.setAdapter(forumPostAdapter);
    }

    // 🔄 Replaces FAB with Edit Button functionality
    private void setupEditButton() {
        ImageButton btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(view -> openCreatePostScreenForResult());
    }

    private void openCreatePostScreen() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }

    private void openCreatePostScreenForResult() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivityForResult(intent, CREATE_POST_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            loadForumPostsFromFirebase();
            Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadForumPostsFromFirebase() {
        // Show loading indicator or progress bar if needed
        
        FirebaseUtil.getAllPosts(new FirebaseUtil.PostsLoadListener() {
            @Override
            public void onPostsLoaded(List<ForumPost> posts) {
                // Update the posts list and notify adapter
                forumPosts.clear();
                forumPosts.addAll(posts);
                forumPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity2.this, 
                        "Error loading posts: " + errorMessage, 
                        Toast.LENGTH_SHORT).show();
                
                // Load sample data as fallback if Firebase fails
                loadSamplePosts();
            }
        });
    }

    private void loadSamplePosts() {
        forumPosts.clear();

        final int likes = 125;
        final int comments = 15;
        final int shares = 155;

        List<String> tags1 = new ArrayList<>(Arrays.asList("General", "Safety"));
        ForumPost post1 = new ForumPost(
                "Golangirya",
                "8 min ago",
                "Is there any Girls Hostel available in Bandra?",
                "I need a PG in Bandra as I work till late night and it gets really dark and I feel unsafe?",
                null,
                tags1,
                likes, comments, shares);
        forumPosts.add(post1);

        List<String> tags2 = new ArrayList<>(Arrays.asList("General", "Support"));
        ForumPost post2 = new ForumPost(
                "Sunaina",
                "2 min ago",
                "Product Query",
                "What is the use of this white button?",
                R.drawable.pink_blue_product,
                tags2,
                likes, comments, shares);
        forumPosts.add(post2);

        List<String> tags3 = new ArrayList<>(Arrays.asList("General", "Support"));
        ForumPost post3 = new ForumPost(
                "Sunaina",
                "3 min ago",
                "Product Query",
                "What is the use of this white button?",
                R.drawable.pink_blue_product,
                tags3,
                likes, comments, shares);
        forumPosts.add(post3);

        forumPostAdapter.notifyDataSetChanged();
    }

    private void filterPostsByCategory(int tabId) {
        if (tabId == R.id.chip_general) {
            // Load all posts
            loadForumPostsFromFirebase();
            return;
        }
        
        // Get the selected chip text
        Chip selectedChip = findViewById(tabId);
        if (selectedChip != null) {
            String category = selectedChip.getText().toString();
            
            // Load posts filtered by category
            FirebaseUtil.getPostsByTag(category, new FirebaseUtil.PostsLoadListener() {
                @Override
                public void onPostsLoaded(List<ForumPost> posts) {
                    forumPosts.clear();
                    forumPosts.addAll(posts);
                    forumPostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MainActivity2.this, 
                            "Error loading filtered posts: " + errorMessage, 
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            return true;
        } else if (id == R.id.nav_search) {
            return true;
        } else if (id == R.id.nav_twitter) {
            return true;
        } else if (id == R.id.nav_games) {
            return true;
        } else if (id == R.id.nav_profile) {
            return true;
        }
        return false;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh posts when coming back to this activity
        loadForumPostsFromFirebase();
    }
}
