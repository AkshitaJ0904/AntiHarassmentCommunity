package com.example.antisoch.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class ContactRepository {
    
    private ContactDao contactDao;
    private static ContactRepository instance;
    private Context context;
    
    private ContactRepository(Context context) {
        ContactDatabase database = ContactDatabase.getInstance(context);
        this.contactDao = database.contactDao();
        this.context = context.getApplicationContext();
    }
    
    public static synchronized ContactRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ContactRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    public void insertContact(Contact contact) {
        contactDao.insertContact(contact);
        updateProfileCounts();
        android.util.Log.d("ContactRepository", "Inserted contact: " + contact.getName() + 
                ", Total count: " + getContactCount());
    }
    
    public void updateContact(Contact contact) {
        contactDao.updateContact(contact);
        updateProfileCounts();
        android.util.Log.d("ContactRepository", "Updated contact: " + contact.getName() + 
                ", Total count: " + getContactCount());
    }
    
    public void deleteContact(Contact contact) {
        contactDao.deleteContact(contact);
        updateProfileCounts();
        android.util.Log.d("ContactRepository", "Deleted contact: " + contact.getName() + 
                ", Total count: " + getContactCount());
    }
    
    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }
    
    public List<Contact> getFavoriteContacts() {
        return contactDao.getFavoriteContacts();
    }
    
    public int getContactCount() {
        int count = contactDao.getContactCount();
        android.util.Log.d("ContactRepository", "Getting contact count: " + count);
        return count;
    }
    
    public int getFavoriteContactCount() {
        int count = contactDao.getFavoriteContactCount();
        android.util.Log.d("ContactRepository", "Getting favorite count: " + count);
        return count;
    }
    
    public List<Contact> searchContacts(String query) {
        return contactDao.searchContacts(query);
    }
    
    public void deleteAllContacts() {
        contactDao.deleteAllContacts();
        updateProfileCounts();
    }
    
    // Update profile counts in SharedPreferences for quick access
    private void updateProfileCounts() {
        // Get fresh counts directly from DAO
        int totalContacts = contactDao.getContactCount();
        int favoriteContacts = contactDao.getFavoriteContactCount();
        
        // Log the updated counts
        android.util.Log.d("ContactRepository", "Updating counts - Total: " + totalContacts + 
                ", Favorites: " + favoriteContacts);
        
        // Save to SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("contact_count", totalContacts);
        editor.putInt("favorite_count", favoriteContacts);
        editor.apply();
    }
    
    // Populate initial dummy data if the database is empty
    public void populateInitialData() {
        if (getContactCount() == 0) {
            insertContact(new Contact("Jay Price", "(480) 555-0103", "Price Financial", "Financial Planner", false, null));
            insertContact(new Contact("Jen Olson", "(270) 555-0117", "Be you Fitness", "Personal Trainer", false, null));
            insertContact(new Contact("Jennifer Williams", "(684) 555-0102", "Jennifer Williams", "Makeup Artist", false, null));
            insertContact(new Contact("Joann Short", "(704) 555-0127", "Venture Capitalist", "", true, null));
            insertContact(new Contact("Kevin Jones", "(505) 555-0125", "Living Designs", "Head Architect", false, null));
            insertContact(new Contact("Lindsay Adams", "(555) 123-4567", "SpotAnalytics", "Director of Marketing", false, null));
            insertContact(new Contact("Marco Rodriguez", "(555) 987-6543", "Little Mexico", "Owner and General Manager", true, null));
            insertContact(new Contact("Margot Bardeau", "(555) 555-5555", "J&B Consulting", "Consultant", false, null));
        }
    }
} 