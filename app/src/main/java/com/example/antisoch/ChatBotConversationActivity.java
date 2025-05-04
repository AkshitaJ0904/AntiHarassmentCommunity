package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatBotConversationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText messageInput;
    private CardView btnSend;
    private ImageView btnBackChat;
    private LinearLayout recommendationLayout;
    private CardView legalHelpCard;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);

        // Initialize views
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageInput = findViewById(R.id.messageInput);
        btnSend = findViewById(R.id.btnSend);
        btnBackChat = findViewById(R.id.btnBackChat);
        recommendationLayout = findViewById(R.id.recommendationLayout);
        legalHelpCard = findViewById(R.id.legalHelpCard);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, this);
        
        // Set up RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        // Set up click listeners
        btnBackChat.setOnClickListener(v -> finish());
        
        btnSend.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.setText("");
            }
        });
        
        legalHelpCard.setOnClickListener(v -> {
            Intent intent = new Intent(ChatBotConversationActivity.this, LegalHelpFormActivity.class);
            startActivity(intent);
        });
    }

    private void sendMessage(String messageText) {
        // Add user message to the list
        messageList.add(new Message(messageText, Message.TYPE_USER));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
        
        // Check if message is about harassment
        if (messageText.toLowerCase().contains("harass")) {
            simulateBotResponse();
        } else {
            // Default response for other messages
            showTypingIndicator();
            
            handler.postDelayed(() -> {
                removeTypingIndicator();
                messageList.add(new Message("I'm sorry, I don't have enough information about that topic. How else can I help you?", Message.TYPE_BOT));
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                scrollToBottom();
            }, 2000);
        }
    }

    private void simulateBotResponse() {
        showTypingIndicator();
        
        // Simulate typing delay before response
        handler.postDelayed(() -> {
            removeTypingIndicator();
            
            // Add the first part of the response
            messageList.add(new Message("I'm so sorry this happened to you. Here are the next steps you can take:", Message.TYPE_BOT));
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            scrollToBottom();
            
            // Simulate delay for the second part
            handler.postDelayed(() -> {
                messageList.add(new Message("Make sure you're safe – If you're still on the bus or in the area, try to get to a safe place. If you feel threatened, seek help from authorities or someone nearby.", Message.TYPE_BOT));
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                scrollToBottom();
                
                // Simulate delay for the third part
                handler.postDelayed(() -> {
                    messageList.add(new Message("Report the incident – If you're comfortable, report it to the bus service or local authorities. Many public transport services have harassment reporting systems. I can help you find the right contact if needed. It would be good if you take a legal help.", Message.TYPE_BOT));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    scrollToBottom();
                    
                    // Show recommendation after a delay
                    handler.postDelayed(() -> {
                        recommendationLayout.setVisibility(View.VISIBLE);
                    }, 1000);
                    
                }, 2000);
                
            }, 2000);
            
        }, 3000);
    }
    
    private void showTypingIndicator() {
        messageList.add(new Message("", Message.TYPE_TYPING));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }
    
    private void removeTypingIndicator() {
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getType() == Message.TYPE_TYPING) {
                messageList.remove(i);
                messageAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }
    
    private void scrollToBottom() {
        recyclerViewMessages.post(() -> recyclerViewMessages.smoothScrollToPosition(Math.max(messageAdapter.getItemCount() - 1, 0)));
    }
} 