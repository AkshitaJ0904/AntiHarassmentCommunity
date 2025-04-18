package com.example.antisoch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    private Button btnJoinIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        btnJoinIn = findViewById(R.id.btnJoinIn);
        btnJoinIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}