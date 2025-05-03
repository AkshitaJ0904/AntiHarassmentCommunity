package com.example.soraaihelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soraaihelp.database.ChatConversation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryViewHolder> {
    
    private List<ChatConversation> conversations = new ArrayList<>();
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault());
    
    @NonNull
    @Override
    public ChatHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_history, parent, false);
        return new ChatHistoryViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ChatHistoryViewHolder holder, int position) {
        ChatConversation conversation = conversations.get(position);
        holder.titleTextView.setText(conversation.getTitle());
        
        // Format the date
        String formattedDate = dateFormat.format(conversation.getTimestamp());
        holder.timestampTextView.setText(formattedDate);
        
        // Optional: Show message count or preview
        int messageCount = conversation.getMessages().size();
        holder.messageCountTextView.setText(messageCount + " messages");
    }
    
    @Override
    public int getItemCount() {
        return conversations.size();
    }
    
    public void setConversations(List<ChatConversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }
    
    public ChatConversation getConversationAt(int position) {
        return conversations.get(position);
    }
    
    class ChatHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView timestampTextView;
        private TextView messageCountTextView;
        
        public ChatHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewHistoryTitle);
            timestampTextView = itemView.findViewById(R.id.textViewHistoryTimestamp);
            messageCountTextView = itemView.findViewById(R.id.textViewMessageCount);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(conversations.get(position));
                    }
                }
            });
        }
    }
    
    public interface OnItemClickListener {
        void onItemClick(ChatConversation conversation);
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
} 