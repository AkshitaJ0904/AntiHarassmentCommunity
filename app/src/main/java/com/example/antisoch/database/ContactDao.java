package com.example.antisoch.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact contact);
    
    @Update
    void updateContact(Contact contact);
    
    @Delete
    void deleteContact(Contact contact);
    
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    List<Contact> getAllContacts();
    
    @Query("SELECT * FROM contacts WHERE isFavorite = 1 ORDER BY name ASC")
    List<Contact> getFavoriteContacts();
    
    @Query("SELECT COUNT(*) FROM contacts")
    int getContactCount();
    
    @Query("SELECT COUNT(*) FROM contacts WHERE isFavorite = 1")
    int getFavoriteContactCount();
    
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%'")
    List<Contact> searchContacts(String query);
    
    @Query("DELETE FROM contacts")
    void deleteAllContacts();
} 