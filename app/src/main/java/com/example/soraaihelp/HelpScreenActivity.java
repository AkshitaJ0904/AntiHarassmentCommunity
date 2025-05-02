package com.example.soraaihelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HelpScreenActivity extends AppCompatActivity {

    private Button btnChat;
    private Button btnGeneral;
    private Button btnChatPlus;
    private Button btnChatAI;
    private TextView helpScreenTitle;
    private CardView complaintCardView, communityCardView, safetyCardView;
    private TextView complaintAnswer, communityAnswer, safetyAnswer;
    private ImageView complaintArrow, communityArrow, safetyArrow;
    private boolean isComplaintExpanded = true; // Initially expanded as per screenshot
    private boolean isCommunityExpanded = false;
    private boolean isSafetyExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        // Initialize buttons and title
        btnChat = findViewById(R.id.btnChat);
        btnGeneral = findViewById(R.id.btnGeneral);
        btnChatPlus = findViewById(R.id.btnChatPlus);
        btnChatAI = findViewById(R.id.btnChatAI);
        helpScreenTitle = findViewById(R.id.helpScreenTitle);

        // Hide the title
        helpScreenTitle.setVisibility(View.GONE);

        // Find the card views and their components
        complaintCardView = findViewById(R.id.complaintCardView);
        communityCardView = findViewById(R.id.communityCardView);
        safetyCardView = findViewById(R.id.safetyCardView);

        complaintAnswer = findViewById(R.id.complaintAnswer);
        communityAnswer = findViewById(R.id.communityAnswer);
        safetyAnswer = findViewById(R.id.safetyAnswer);

        complaintArrow = findViewById(R.id.complaintArrow);
        communityArrow = findViewById(R.id.communityArrow);
        safetyArrow = findViewById(R.id.safetyArrow);

        // Set initial state
        complaintAnswer.setVisibility(View.VISIBLE); // This one is initially visible
        communityAnswer.setVisibility(View.GONE);
        safetyAnswer.setVisibility(View.GONE);

        // Set initial rotation of arrows
        complaintArrow.setRotation(isComplaintExpanded ? 180 : 0);
        communityArrow.setRotation(isCommunityExpanded ? 180 : 0);
        safetyArrow.setRotation(isSafetyExpanded ? 180 : 0);

        // Set click listeners for the card views
        setCardViewClickListeners();

        // Set click listener for Chat button
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Chat button click
                // You can add the navigation to the Chat screen here
            }
        });

        // Set click listener for General button to navigate to Dashboard
        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpScreenActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for Community button
        btnChatPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Community button click
                // You can add the navigation to the Community screen here
            }
        });

        // Set click listener for Chat+ AI button to navigate to ChatBot screen
        btnChatAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpScreenActivity.this, ChatBotScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCardViewClickListeners() {
        // Complaint Card
        complaintCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isComplaintExpanded = !isComplaintExpanded;
                complaintAnswer.setVisibility(isComplaintExpanded ? View.VISIBLE : View.GONE);
                complaintArrow.setRotation(isComplaintExpanded ? 180 : 0);
            }
        });

        // Community Card
        communityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCommunityExpanded = !isCommunityExpanded;
                communityAnswer.setVisibility(isCommunityExpanded ? View.VISIBLE : View.GONE);
                communityArrow.setRotation(isCommunityExpanded ? 180 : 0);
            }
        });

        // Safety Device Card
        safetyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSafetyExpanded = !isSafetyExpanded;
                safetyAnswer.setVisibility(isSafetyExpanded ? View.VISIBLE : View.GONE);
                safetyArrow.setRotation(isSafetyExpanded ? 180 : 0);
            }
        });
    }
}