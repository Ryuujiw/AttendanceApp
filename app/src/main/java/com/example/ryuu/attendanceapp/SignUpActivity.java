package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    protected Button btn_sign_up;
    protected TextInputEditText txt_sign_up_email;
    protected TextInputEditText txt_sign_up_username;
    protected TextInputEditText txt_sign_up_matrix_no;
    protected TextInputEditText txt_sign_up_password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txt_sign_up_email = findViewById(R.id.txt_sign_up_email);
        txt_sign_up_username = findViewById(R.id.txt_sign_up_username);
        txt_sign_up_matrix_no = findViewById(R.id.txt_sign_up_matrix_no);
        txt_sign_up_password = findViewById(R.id.txt_sign_up_password);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String password = txt_sign_up_password.getText().toString();
                String email = txt_sign_up_email.getText().toString();

                password = password.trim();
                email = email.trim();

                if(password.isEmpty() || email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Please enter an email and password").setTitle("Sign Up Unsuccessful").setPositiveButton("OK", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error. Could not Sign Up", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
