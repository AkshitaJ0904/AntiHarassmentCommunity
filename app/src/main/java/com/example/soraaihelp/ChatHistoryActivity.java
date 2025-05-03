package com.example.soraaihelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soraaihelp.database.ChatConversation;
import com.example.soraaihelp.database.ChatRepository;

import java.util.List;

public class ChatHistoryActivity extends AppCompatActivity {

    public static final String EXTRA_CONVERSATION_ID = "com.example.soraaihelp.EXTRA_CONVERSATION_ID";

    private RecyclerView recyclerViewHistory;
    private ChatHistoryAdapter adapter;
    private ChatRepository repository;
    private TextView emptyView;
    private ImageView btnBackHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        // Initialize views
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        emptyView = findViewById(R.id.emptyView);
        btnBackHistory = findViewById(R.id.btnBackHistory);

        // Set up RecyclerView
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setHasFixedSize(true);

        // Set up adapter
        adapter = new ChatHistoryAdapter();
        recyclerViewHistory.setAdapter(adapter);

        // Initialize repository
        repository = new ChatRepository(getApplication());

        // Observe conversations
        repository.getAllConversations().observe(this, new Observer<List<ChatConversation>>() {
            @Override
            public void onChanged(List<ChatConversation> chatConversations) {
                // Update RecyclerView
                adapter.setConversations(chatConversations);
                
                // Show empty view if no conversations
                if (chatConversations.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerViewHistory.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    recyclerViewHistory.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set up item click listener
        adapter.setOnItemClickListener(new ChatHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatConversation conversation) {
                // Start chat activity with the selected conversation
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CONVERSATION_ID, conversation.getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Set up back button
        btnBackHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
} 