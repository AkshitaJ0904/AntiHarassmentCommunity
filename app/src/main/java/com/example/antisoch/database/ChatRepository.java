package com.example.antisoch.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.antisoch.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRepository {
    private ChatDao chatDao;
    private LiveData<List<ChatConversation>> allConversations;
    private ExecutorService executorService;
    
    public ChatRepository(Application application) {
        ChatDatabase database = ChatDatabase.getInstance(application);
        chatDao = database.chatDao();
        allConversations = chatDao.getAllConversations();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public LiveData<List<ChatConversation>> getAllConversations() {
        return allConversations;
    }
    
    public void insert(ChatConversation conversation) {
        executorService.execute(() -> {
            chatDao.insertConversation(conversation);
        });
    }
    
    public void update(ChatConversation conversation) {
        executorService.execute(() -> {
            chatDao.updateConversation(conversation);
        });
    }
    
    public void delete(ChatConversation conversation) {
        executorService.execute(() -> {
            chatDao.deleteConversation(conversation);
        });
    }
    
    public interface ChatConversationCallback {
        void onConversationLoaded(ChatConversation conversation);
    }
    
    public void getConversationById(long id, ChatConversationCallback callback) {
        executorService.execute(() -> {
            ChatConversation conversation = chatDao.getConversationById(id);
            if (callback != null) {
                callback.onConversationLoaded(conversation);
            }
        });
    }
    
    // Utility method to convert Message objects to MessageEntity objects
    public static List<MessageEntity> convertToMessageEntities(List<Message> messages) {
        List<MessageEntity> entities = new ArrayList<>();
        for (Message message : messages) {
            entities.add(new MessageEntity(message.getText(), message.getType()));
        }
        return entities;
    }
    
    // Utility method to convert MessageEntity objects to Message objects
    public static List<Message> convertToMessages(List<MessageEntity> entities) {
        List<Message> messages = new ArrayList<>();
        for (MessageEntity entity : entities) {
            messages.add(new Message(entity.getText(), entity.getType()));
        }
        return messages;
    }
    
    // Create a new conversation from current messages
    public void saveCurrentConversation(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        // Generate a title from the first user message
        String title = "Conversation";
        for (Message message : messages) {
            if (message.getType() == Message.TYPE_USER) {
                String userMessage = message.getText();
                // Limit title length
                title = userMessage.length() > 30 ? 
                        userMessage.substring(0, 27) + "..." : 
                        userMessage;
                break;
            }
        }
        
        List<MessageEntity> messageEntities = convertToMessageEntities(messages);
        ChatConversation conversation = new ChatConversation(title, new Date(), messageEntities);
        
        insert(conversation);
    }
} 