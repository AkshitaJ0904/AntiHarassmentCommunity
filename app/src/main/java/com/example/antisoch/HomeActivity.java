package com.example.antisoch;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreeting = findViewById(R.id.tvGreeting);

        // You can set a custom greeting based on user info
        // String username = getIntent().getStringExtra("username");
        // if (username != null && !username.isEmpty()) {
        //     tvGreeting.setText("Hi " + username + "!");
        // }
    }
}