package com.example.antisoch;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LegalHelpFormActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText emailInput, phoneInput;
    private Button btnSubmit, btnThankYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_help_form);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnThankYou = findViewById(R.id.btnThankYou);

        // Set up click listeners
        btnBack.setOnClickListener(v -> finish());
        
        btnSubmit.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            
            if (validateInputs(email, phone)) {
                // In a real app, you would submit this information to a server
                Toast.makeText(this, "Thank you! We'll contact you soon.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        
        btnThankYou.setOnClickListener(v -> {
            Toast.makeText(this, "Thank you for using our service!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
    
    private boolean validateInputs(String email, String phone) {
        // Basic validation
        if (email.isEmpty() && phone.isEmpty()) {
            Toast.makeText(this, "Please provide either email or phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
} 