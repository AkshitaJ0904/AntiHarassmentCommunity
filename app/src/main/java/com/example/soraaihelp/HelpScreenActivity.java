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
    private Button btnCommunity;
    private CardView btnChatAI;
    private ImageView btnBack;
    
    // FAQ items
    private CardView faqItem1, faqItem2, faqItem3;
    private TextView faqContent1;
    private ImageView faqArrow1, faqArrow2, faqArrow3;
    private boolean isComplaintExpanded = true; // Initially expanded as per screenshot
    private boolean isCommunityExpanded = false;
    private boolean isSafetyExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        // Initialize buttons
        btnChat = findViewById(R.id.btnChat);
        btnGeneral = findViewById(R.id.btnGeneral);
        btnCommunity = findViewById(R.id.btnCommunity);
        btnChatAI = findViewById(R.id.btnChatAI);
        btnBack = findViewById(R.id.btnBack);

        // Find the FAQ items
        faqItem1 = findViewById(R.id.faqItem1);
        faqItem2 = findViewById(R.id.faqItem2);
        faqItem3 = findViewById(R.id.faqItem3);

        faqContent1 = findViewById(R.id.faqContent1);
        
        faqArrow1 = findViewById(R.id.faqArrow1);
        faqArrow2 = findViewById(R.id.faqArrow2);
        faqArrow3 = findViewById(R.id.faqArrow3);

        // Set initial state
        // Initial state already set in the XML (first FAQ is expanded)

        // Set back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        btnCommunity.setOnClickListener(new View.OnClickListener() {
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
        
        // Set up FAQ item click listeners
        setFaqClickListeners();
    }

    private void setFaqClickListeners() {
        // FAQ Item 1
        faqItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isComplaintExpanded = !isComplaintExpanded;
                faqContent1.setVisibility(isComplaintExpanded ? View.VISIBLE : View.GONE);
                faqArrow1.setRotation(isComplaintExpanded ? 180 : 0);
            }
        });

        // FAQ Item 2
        faqItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCommunityExpanded = !isCommunityExpanded;
                // Note: Content for this FAQ might need to be added to the XML
                faqArrow2.setRotation(isCommunityExpanded ? 180 : 0);
            }
        });

        // FAQ Item 3
        faqItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSafetyExpanded = !isSafetyExpanded;
                // Note: Content for this FAQ might need to be added to the XML
                faqArrow3.setRotation(isSafetyExpanded ? 180 : 0);
            }
        });
    }
}