package com.example.ryuu.attendanceapp.activities.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.activities.ClassActivity;
import com.example.ryuu.attendanceapp.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    protected Button btn_login, btn_back;
    protected TextInputEditText txt_login_username;
    protected TextInputEditText txt_login_password;
    protected ProgressDialog progressDialog;
    private TextView txt_forgotPassword;

    private String email;
    private String password;
    private String loginMode;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // HELPER FUNCTIONS

    /** THIS FUNCTION WILL RETURN STUDENT / TEACHER
     *  TEACHER AND STUDENTS WILL SHARE SIMILAR LAYOUTS
     *  BUT DIFFERENT ACTIONS
     */

    public String getLoginMode(String email){
        // Propose to add a ROLE column in DB for user table to store whether student or teacher
        // Temporary workaround is to parse the username and see if the regex matches student id or not

        return email.contains("@siswa.ukm.edu.my") ? "student" : "lecturer";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_back = findViewById(R.id.btn_back);
        txt_login_username = findViewById(R.id.txt_login_username);
        txt_login_password = findViewById(R.id.txt_login_password);
        txt_forgotPassword = findViewById(R.id.txt_forgotPassword);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait for awhile");
        firebaseAuth = FirebaseAuth.getInstance();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                email = txt_login_username.getText().toString();
                loginMode = getLoginMode(email);
                password = txt_login_password.getText().toString();

                password = password.trim();
                email = email.trim();

                if(password.isEmpty() || email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Please enter an email and password").setTitle("Login Unsuccessful").setPositiveButton("OK", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            user = firebaseAuth.getCurrentUser();
                            progressDialog.dismiss();
                            if(task.isSuccessful() && user.isEmailVerified()){
                                GoToMainActivity(loginMode);
                            }
                            else if(task.isSuccessful() && !user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Login Unsuccessful. Please verify your email.", Toast.LENGTH_LONG).show();
                                user.sendEmailVerification();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Login Unsuccessful. Please check your credentials.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Reset_Password_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void GoToMainActivity(String loginMode){
        Intent intent = new Intent(LoginActivity.this,ClassActivity.class);
        intent.putExtra("LOGIN_MODE", loginMode);
        startActivity(intent);
        finish();
    }
}