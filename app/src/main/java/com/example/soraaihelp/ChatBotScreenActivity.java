package com.example.soraaihelp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class ChatBotScreenActivity extends AppCompatActivity {

    private ImageView btnBackChat;
    private TextView chatBotTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_screen);

        // Initialize back button and title
        btnBackChat = findViewById(R.id.btnBackChat);
        chatBotTitle = findViewById(R.id.chatBotTitle);

        // Hide the title
        chatBotTitle.setVisibility(View.GONE);

        // Set click listener for back button to finish activity
        btnBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}