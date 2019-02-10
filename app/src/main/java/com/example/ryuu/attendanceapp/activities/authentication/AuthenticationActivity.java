package com.example.ryuu.attendanceapp.activities.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ryuu.attendanceapp.R;

public class AuthenticationActivity extends AppCompatActivity {

    Button btn_main_login, btn_main_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_main_login = findViewById(R.id.btn_main_login);
        btn_main_sign_up = findViewById(R.id.btn_main_sign_up);

        btn_main_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthenticationActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        btn_main_sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthenticationActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }
}