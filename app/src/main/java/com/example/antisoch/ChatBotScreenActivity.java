package com.example.antisoch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antisoch.api.GeminiApiModels;
import com.example.antisoch.api.GeminiApiService;
import com.example.antisoch.api.RetrofitClient;
import com.example.antisoch.database.ChatRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotScreenActivity extends AppCompatActivity {

    private static final int INTERNET_PERMISSION_CODE = 100;
    private static final int REQUEST_CHAT_HISTORY = 1001;
    
    private ImageView btnBackChat, btnChatHistory;
    private CardView btnSend, suggestion1, suggestion2, suggestion3, legalHelpCard;
    private EditText messageInput, emailInput, phoneInput;
    private Button btnSubmit, btnThankYou;
    private RecyclerView recyclerViewMessages;
    private TextView welcomeTitle, suggestionTitle;
    private LinearLayout suggestionsLayout, recommendationLayout;
    private ConstraintLayout legalHelpFormLayout;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    
    // Gemini API service
    private GeminiApiService geminiApiService;
    
    // Repository for chat history
    private ChatRepository chatRepository;
    
    // Current conversation ID (null if new conversation)
    private Long currentConversationId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_screen);

        // Check for internet permission
        checkInternetPermission();
        
        // Initialize Gemini API service
        geminiApiService = RetrofitClient.getGeminiApiService();
        
        // Initialize repository
        chatRepository = new ChatRepository(getApplication());

        // Initialize views
        initializeViews();
        setupClickListeners();

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, this);
        
        // Set up RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);
    }
    
    private void checkInternetPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.INTERNET}, 
                    INTERNET_PERMISSION_CODE);
        }
    }

    private void initializeViews() {
        btnBackChat = findViewById(R.id.btnBackChat);
        btnChatHistory = findViewById(R.id.btnChatHistory);
        btnSend = findViewById(R.id.btnSend);
        messageInput = findViewById(R.id.messageInput);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        welcomeTitle = findViewById(R.id.welcomeTitle);
        suggestionTitle = findViewById(R.id.suggestionTitle);
        suggestionsLayout = findViewById(R.id.suggestionsLayout);
        recommendationLayout = findViewById(R.id.recommendationLayout);
        legalHelpCard = findViewById(R.id.legalHelpCard);
        legalHelpFormLayout = findViewById(R.id.legalHelpFormLayout);
        
        suggestion1 = findViewById(R.id.suggestion1);
        suggestion2 = findViewById(R.id.suggestion2);
        suggestion3 = findViewById(R.id.suggestion3);
        
        // Legal help form views
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnThankYou = findViewById(R.id.btnThankYou);
    }

    private void setupClickListeners() {
        // Set click listener for back button to finish activity
        btnBackChat.setOnClickListener(v -> finish());
        
        // Set click listener for history button
        btnChatHistory.setOnClickListener(v -> openChatHistory());
        
        // Send button click
        btnSend.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.setText("");
            }
        });
        
        // Suggestion clicks
        suggestion1.setOnClickListener(v -> {
            TextView textView = (TextView) ((CardView) v).getChildAt(0);
            sendMessage(textView.getText().toString());
        });
        
        suggestion2.setOnClickListener(v -> {
            TextView textView = (TextView) ((CardView) v).getChildAt(0);
            sendMessage(textView.getText().toString());
        });
        
        suggestion3.setOnClickListener(v -> {
            TextView textView = (TextView) ((CardView) v).getChildAt(0);
            sendMessage(textView.getText().toString());
        });
        
        // Legal help card
        legalHelpCard.setOnClickListener(v -> {
            // Show legal help form
            showLegalHelpForm();
        });
        
        // Legal help form buttons
        btnSubmit.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            
            if (validateInputs(email, phone)) {
                // In a real app, you would submit this information to a server
                Toast.makeText(this, "Thank you! We'll contact you soon.", Toast.LENGTH_LONG).show();
                hideLegalHelpForm();
            }
        });
        
        btnThankYou.setOnClickListener(v -> {
            Toast.makeText(this, "Thank you for using our service!", Toast.LENGTH_LONG).show();
            hideLegalHelpForm();
        });
    }
    
    private void openChatHistory() {
        // Save current conversation before opening history
        saveCurrentConversation();
        
        // Start the chat history activity
        Intent intent = new Intent(this, ChatHistoryActivity.class);
        startActivityForResult(intent, REQUEST_CHAT_HISTORY);
    }
    
    private void saveCurrentConversation() {
        // Only save if there are messages
        if (messageList != null && !messageList.isEmpty()) {
            // If this is an existing conversation, update it
            if (currentConversationId != null) {
                chatRepository.getConversationById(currentConversationId, conversation -> {
                    if (conversation != null) {
                        conversation.setMessages(ChatRepository.convertToMessageEntities(messageList));
                        chatRepository.update(conversation);
                    }
                });
            } else {
                // Otherwise save as a new conversation
                chatRepository.saveCurrentConversation(messageList);
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CHAT_HISTORY && resultCode == RESULT_OK && data != null) {
            long conversationId = data.getLongExtra(ChatHistoryActivity.EXTRA_CONVERSATION_ID, -1);
            if (conversationId != -1) {
                loadConversation(conversationId);
            }
        }
    }
    
    private void loadConversation(long conversationId) {
        chatRepository.getConversationById(conversationId, conversation -> {
            if (conversation != null) {
                // Convert message entities to message objects
                List<Message> loadedMessages = ChatRepository.convertToMessages(conversation.getMessages());
                
                // Update UI on main thread
                runOnUiThread(() -> {
                    // Clear current messages
                    messageList.clear();
                    
                    // Add loaded messages
                    messageList.addAll(loadedMessages);
                    messageAdapter.notifyDataSetChanged();
                    
                    // Show chat view
                    welcomeTitle.setVisibility(View.GONE);
                    suggestionTitle.setVisibility(View.GONE);
                    suggestionsLayout.setVisibility(View.GONE);
                    recyclerViewMessages.setVisibility(View.VISIBLE);
                    
                    // Set current conversation ID
                    currentConversationId = conversation.getId();
                    
                    // Scroll to bottom
                    scrollToBottom();
                });
            }
        });
    }

    private void sendMessage(String messageText) {
        // Show chat view and hide welcome screen
        if (suggestionsLayout.getVisibility() == View.VISIBLE) {
            welcomeTitle.setVisibility(View.GONE);
            suggestionTitle.setVisibility(View.GONE);
            suggestionsLayout.setVisibility(View.GONE);
            recyclerViewMessages.setVisibility(View.VISIBLE);
        }
        
        // Add user message to the list
        messageList.add(new Message(messageText, Message.TYPE_USER));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
        
        // Special case handling for harassment messages
        if (messageText.toLowerCase().contains("harass")) {
            simulateBotResponse();
        } else {
            // Call the Gemini API for other messages
            callGeminiApi(messageText);
        }
    }
    
    private void callGeminiApi(String prompt) {
        // Show typing indicator while API call is in progress
        showTypingIndicator();
        
        // Create request for Gemini API
        GeminiApiModels.GeminiRequest request = new GeminiApiModels.GeminiRequest(prompt);
        
        // Make the API call
        geminiApiService.generateContent(BuildConfig.GEMINI_API_KEY, request)
            .enqueue(new Callback<GeminiApiModels.GeminiResponse>() {
                @Override
                public void onResponse(Call<GeminiApiModels.GeminiResponse> call,
                                       Response<GeminiApiModels.GeminiResponse> response) {
                    // Remove typing indicator
                    removeTypingIndicator();
                    
                    if (response.isSuccessful() && response.body() != null) {
                        // Get the response text
                        String responseText = response.body().getFirstResponseText();
                        
                        // Add the bot message to the chat
                        messageList.add(new Message(responseText, Message.TYPE_BOT));
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        scrollToBottom();
                    } else {
                        // Handle error
                        messageList.add(new Message("Sorry, I couldn't process that request. Please try again later.", Message.TYPE_BOT));
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        scrollToBottom();
                    }
                }

                @Override
                public void onFailure(Call<GeminiApiModels.GeminiResponse> call, Throwable t) {
                    // Remove typing indicator
                    removeTypingIndicator();
                    
                    // Show error message
                    messageList.add(new Message("Network error. Please check your internet connection and try again.", Message.TYPE_BOT));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    scrollToBottom();
                    
                    // Log the error
                    t.printStackTrace();
                }
            });
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
    
    private void showLegalHelpForm() {
        legalHelpFormLayout.setVisibility(View.VISIBLE);
        recyclerViewMessages.setVisibility(View.GONE);
        recommendationLayout.setVisibility(View.GONE);
        messageInput.setEnabled(false);
        btnSend.setEnabled(false);
    }
    
    private void hideLegalHelpForm() {
        legalHelpFormLayout.setVisibility(View.GONE);
        recyclerViewMessages.setVisibility(View.VISIBLE);
        messageInput.setEnabled(true);
        btnSend.setEnabled(true);
    }
    
    private boolean validateInputs(String email, String phone) {
        // Basic validation
        if (email.isEmpty() && phone.isEmpty()) {
            Toast.makeText(this, "Please provide either email or phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Save conversation when app is paused
        saveCurrentConversation();
    }
}