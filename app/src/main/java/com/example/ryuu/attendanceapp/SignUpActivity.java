package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    Button btn_sign_up;
    EditText et_name, et_email,et_matricnum,et_pass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btn_sign_up = findViewById(R.id.btn_sign_up);
        et_name = findViewById(R.id.txt_sign_up_username);
        et_email = findViewById(R.id.txt_sign_up_email);
        et_matricnum = findViewById(R.id.txt_sign_up_matrix_no);
        et_pass = findViewById(R.id.txt_sign_up_password);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();
                String password = et_pass.getText().toString().trim();

                Toast.makeText(SignUpActivity.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
