package com.example.soraaihelp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "contacts_db";
    private static ContactDatabase instance;
    
    public abstract ContactDao contactDao();
    
    public static synchronized ContactDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ContactDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // For simplicity, in prod use background threads
                    .build();
        }
        return instance;
    }
} 