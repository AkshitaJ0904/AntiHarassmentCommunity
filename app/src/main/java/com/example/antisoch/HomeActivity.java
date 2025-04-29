package com.example.antisoch;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting;
    private ImageView ivAvatar;
    private FirebaseAuth mAuth;
    private boolean isChildUser = false;
    private RelativeLayout communityCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        tvGreeting = findViewById(R.id.tvGreeting);
        ivAvatar = findViewById(R.id.ivAvatar);

        // Find the Community card
        communityCard = findViewById(R.id.communityCard);

        // Get username from intent (passed from ChooseAvatarActivity)
        String username = getIntent().getStringExtra("username");

        // Set greeting with username
        if (username != null && !username.isEmpty()) {
            tvGreeting.setText("Hi " + username + "!");
        } else {
            // Fallback to getting username from Firebase if not passed in intent
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String firebaseUsername = currentUser.getDisplayName();
                if (firebaseUsername != null && !firebaseUsername.isEmpty()) {
                    tvGreeting.setText("Hi " + firebaseUsername + "!");
                }
            }
        }

        // Get avatar selection from intent
        String selectedAvatar = getIntent().getStringExtra("avatar");

        // Set the appropriate avatar image based on the selection
        if (selectedAvatar != null) {
            switch (selectedAvatar) {
                case "male":
                    // Use the male avatar image with glasses
                    ivAvatar.setImageResource(R.drawable.male_avatar_glasses);
                    break;
                case "female":
                    // Use the female avatar image with glasses
                    ivAvatar.setImageResource(R.drawable.female_avatar_glasses);
                    break;
                case "child":
                    // Use the child avatar group image
                    ivAvatar.setImageResource(R.drawable.child_avatar_group);
                    isChildUser = true;
                    break;
                default:
                    // Default to female avatar if no selection or unknown selection
                    ivAvatar.setImageResource(R.drawable.female_avatar_glasses);
                    break;
            }
        } else {
            // Default to female avatar if no selection was made
            ivAvatar.setImageResource(R.drawable.female_avatar_glasses);
        }

        // Apply child restrictions if needed
        applyChildRestrictions();
    }

    /**
     * Apply restrictions for child users
     */
    private void applyChildRestrictions() {
        if (isChildUser) {
            // For child users, we need to disable the Community feature
            // and add a click listener to show a toast when they try to access it

            // Make the community card appear disabled visually
            communityCard.setAlpha(0.5f);

            // Add click listener to show toast when child tries to access community
            communityCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this,
                            "Sorry! Community features are restricted for younger users. " +
                                    "You can still play the Anti-Soch Game or seek help.",
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // For adult users, set the normal click listener for community card
            communityCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Implement community feature navigation
                    // For example: startActivity(new Intent(HomeActivity.this, CommunityActivity.class));
                }
            });
        }

        // Set up click listeners for other cards
        setupOtherFeatureCards();
    }

    /**
     * Set up click listeners for other feature cards
     */
    private void setupOtherFeatureCards() {
        // Anti-Soch Game Card
        RelativeLayout gameCard = findViewById(R.id.gameCard);
        gameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement game feature navigation
                // For example: startActivity(new Intent(HomeActivity.this, GameActivity.class));
            }
        });

        // Seeking Help Card
        RelativeLayout helpCard = findViewById(R.id.helpCard);
        helpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement help feature navigation
                // For example: startActivity(new Intent(HomeActivity.this, HelpActivity.class));
            }
        });

        // Dashboard/Profile Card
        RelativeLayout dashboardCard = findViewById(R.id.dashboardCard);
        dashboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement dashboard feature navigation
                // For example: startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
            }
        });
    }
}