package com.example.soraaihelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView dashboardTitle;
    private TextView editDetailsText;
    private TextView addContactsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        dashboardTitle = findViewById(R.id.dashboardTitle);
        editDetailsText = findViewById(R.id.editDetailsText);
        addContactsText = findViewById(R.id.addContactsText);

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
    }

    private void openContactsPage() {
        Intent intent = new Intent(DashboardActivity.this, MyContactsActivity.class);
        startActivity(intent);
    }
}