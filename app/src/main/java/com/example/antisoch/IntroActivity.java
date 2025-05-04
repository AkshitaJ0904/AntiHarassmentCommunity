package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends AppCompatActivity {

    private Button btnNext;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            // User is already signed in, skip intro and redirect to ChooseAvatarActivity
            Intent intent = new Intent(IntroActivity.this, ChooseAvatarActivity.class);
            startActivity(intent);
            finish(); // Close this activity so user can't navigate back to it
            return; // Exit onCreate early to avoid setting up the UI
        }

        // Continue with normal intro flow if user is not logged in
        setContentView(R.layout.activity_intro);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, IntroActivity2.class);
                startActivity(intent);
            }
        });
    }
}