package com.example.soraaihelp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Entity(tableName = "chat_conversations")
@TypeConverters(MessageListConverter.class)
public class ChatConversation {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String title;
    private Date timestamp;
    private List<MessageEntity> messages;

    public ChatConversation(String title, Date timestamp, List<MessageEntity> messages) {
        this.title = title;
        this.timestamp = timestamp;
        this.messages = messages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
} 