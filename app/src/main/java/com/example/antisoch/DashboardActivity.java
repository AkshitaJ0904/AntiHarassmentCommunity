package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.antisoch.database.ContactRepository;

public class DashboardActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView dashboardTitle;
    private TextView editDetailsText;
    private TextView addContactsText;
    private TextView tvDashboardContactCount;
    private ContactRepository contactRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize repository
        contactRepository = ContactRepository.getInstance(this);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        dashboardTitle = findViewById(R.id.dashboardTitle);
        editDetailsText = findViewById(R.id.editDetailsText);
        addContactsText = findViewById(R.id.addContactsText);
        tvDashboardContactCount = findViewById(R.id.tvDashboardContactCount);

        // Set click listener for back button
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // Set click listener for edit details
        if (editDetailsText != null) {
            editDetailsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContactsPage();
                }
            });
        }

        // Set click listener for add contacts
        if (addContactsText != null) {
            addContactsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContactsPage();
                }
            });
        }

        // Update contact count
        updateContactCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update contact count when returning to this activity
        updateContactCount();
    }

    private void updateContactCount() {
        if (tvDashboardContactCount != null && contactRepository != null) {
            // Get current contact count from repository
            int count = contactRepository.getContactCount();
            
            // Format with leading zeros if needed
            String formattedCount = String.format("%02d", count);
            
            // Update the TextView
            tvDashboardContactCount.setText(formattedCount);
            
            // Log for debugging
            android.util.Log.d("DashboardActivity", "Updated dashboard contact count to: " + count);
        }
    }

    private void openContactsPage() {
        Intent intent = new Intent(DashboardActivity.this, MyContactsActivity.class);
        startActivity(intent);
    }
}