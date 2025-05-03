package com.example.soraaihelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.soraaihelp.database.ContactRepository;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvTotalContacts;
    private TextView tvAddContacts;
    private ImageView btnBack;
    private CardView contactsCard;
    private TextView profileName;
    private TextView profileEmail;
    private ImageView btnSettings;
    private ContactRepository contactRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize repository
        contactRepository = ContactRepository.getInstance(this);

        // Initialize views
        tvTotalContacts = findViewById(R.id.tvTotalContacts);
        tvAddContacts = findViewById(R.id.tvAddContacts);
        btnBack = findViewById(R.id.btnBackProfile);
        contactsCard = findViewById(R.id.contactsCard);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        btnSettings = findViewById(R.id.btnSettings);

        // Set test data
        profileName.setText("John Doe");
        profileEmail.setText("johndoe@example.com");

        // Set up listeners
        btnBack.setOnClickListener(v -> finish());
        
        // Settings button listener
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show();
        });
        
        // Card click listener to open contacts activity
        contactsCard.setOnClickListener(v -> {
            // Open MyContactsActivity
            startActivity(new Intent(this, MyContactsActivity.class));
        });
        
        // Add contacts text view listener
        tvAddContacts.setOnClickListener(v -> {
            // Open MyContactsActivity
            startActivity(new Intent(this, MyContactsActivity.class));
        });
        
        // Update contact information - FORCE INITIAL OVERRIDE
        updateContactCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update contact information when returning to this activity - ALWAYS CHECK FOR UPDATES
        updateContactCount();
    }

    private void updateContactCount() {
        // Get the current count directly from the repository
        int count = contactRepository.getContactCount();
        
        // Format with leading zero if needed
        String formattedCount = String.format("%02d", count);
        
        // Direct, simple update of the TextView
        tvTotalContacts.setText(formattedCount);
        
        // Log for debugging
        android.util.Log.d("ProfileActivity", "Updated contact count to: " + count);
    }
} 