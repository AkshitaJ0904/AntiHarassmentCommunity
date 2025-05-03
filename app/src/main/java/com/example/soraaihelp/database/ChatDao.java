package com.example.soraaihelp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertConversation(ChatConversation conversation);
    
    @Update
    void updateConversation(ChatConversation conversation);
    
    @Delete
    void deleteConversation(ChatConversation conversation);
    
    @Query("SELECT * FROM chat_conversations ORDER BY timestamp DESC")
    LiveData<List<ChatConversation>> getAllConversations();
    
    @Query("SELECT * FROM chat_conversations WHERE id = :id")
    ChatConversation getConversationById(long id);
} 