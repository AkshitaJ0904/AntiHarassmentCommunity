package com.example.antisoch.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChatConversation.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "chat_database";
    private static ChatDatabase instance;
    
    public abstract ChatDao chatDao();
    
    public static synchronized ChatDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ChatDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
} 