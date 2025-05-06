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
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int CREATE_POST_REQUEST_CODE = 1001;

    private ChipGroup tabChipGroup;
    private RecyclerView recyclerForumPosts;
    private ForumPostAdapter forumPostAdapter;
    private List<ForumPost> forumPosts;
    private List<ForumPost> allForumPosts; // Cache of all posts for local filtering

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI components
        initializeViews();
        setupTabChips();
        setupBottomNavigation();
        setupRecyclerView();
        setupEditButton();
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
        allForumPosts = new ArrayList<>();
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
                
                // Keep a copy of all posts for local filtering
                allForumPosts.clear();
                allForumPosts.addAll(posts);
                
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
        allForumPosts.clear();

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
        allForumPosts.add(post1);

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
        allForumPosts.add(post2);

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
        allForumPosts.add(post3);

        forumPostAdapter.notifyDataSetChanged();
    }

    private void filterPostsByCategory(int tabId) {
        if (tabId == R.id.chip_general) {
            // Load all posts
            resetToAllPosts();
            return;
        }
        
        // Get the selected chip text
        Chip selectedChip = findViewById(tabId);
        if (selectedChip != null) {
            String category = selectedChip.getText().toString();
            
            // First try server-side filtering
            filterPostsServerSide(category);
        }
    }
    
    /**
     * Filter posts using Firebase query (server-side)
     */
    private void filterPostsServerSide(String category) {
        // Show loading indicator if needed
        
        FirebaseUtil.getPostsByTag(category, new FirebaseUtil.PostsLoadListener() {
            @Override
            public void onPostsLoaded(List<ForumPost> posts) {
                if (posts != null && !posts.isEmpty()) {
                    // Update posts if server returned results
                    forumPosts.clear();
                    forumPosts.addAll(posts);
                    forumPostAdapter.notifyDataSetChanged();
                } else {
                    // Fall back to client-side filtering if server returns no results
                    filterPostsClientSide(category);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Fall back to client-side filtering on error
                filterPostsClientSide(category);
            }
        });
    }
    
    /**
     * Filter posts locally (client-side)
     */
    private void filterPostsClientSide(String category) {
        // Normalize category for case-insensitive comparison
        String normalizedCategory = category.toLowerCase(Locale.ROOT);
        
        List<ForumPost> filteredPosts = new ArrayList<>();
        
        for (ForumPost post : allForumPosts) {
            // Check in single tag field
            if (post.getTag() != null && 
                post.getTag().toLowerCase(Locale.ROOT).equals(normalizedCategory)) {
                filteredPosts.add(post);
                continue;
            }
            
            // Check in categories list
            List<String> categories = post.getCategories();
            if (categories != null) {
                for (String postCategory : categories) {
                    if (postCategory != null && 
                        postCategory.toLowerCase(Locale.ROOT).equals(normalizedCategory)) {
                        filteredPosts.add(post);
                        break;
                    }
                }
            }
        }
        
        // Update the UI with filtered posts
        forumPosts.clear();
        forumPosts.addAll(filteredPosts);
        forumPostAdapter.notifyDataSetChanged();
        
        // Show message if no posts match the filter
        if (filteredPosts.isEmpty()) {
            Toast.makeText(this, "No posts found with category: " + category, 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Reset to showing all posts
     */
    private void resetToAllPosts() {
        forumPosts.clear();
        forumPosts.addAll(allForumPosts);
        forumPostAdapter.notifyDataSetChanged();
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
