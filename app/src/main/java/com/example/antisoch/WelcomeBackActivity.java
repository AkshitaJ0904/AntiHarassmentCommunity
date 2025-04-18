package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeBackActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnContinue, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_back);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnContinue = findViewById(R.id.btnContinue);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeBackActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeBackActivity.this, ChooseAvatarActivity.class);
                startActivity(intent);
            }
        });
    }
}