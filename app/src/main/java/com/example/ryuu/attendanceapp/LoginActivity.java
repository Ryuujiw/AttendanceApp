package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    TextInputEditText txt_login_username;
    TextInputEditText txt_login_password;

    private static String login_mode;

    private String username;
    private String password;

    // HELPER FUNCTIONS

    /** THIS FUNCTION WILL RETURN STUDENT / TEACHER
     *  TEACHER AND STUDENTS WILL SHARE SIMILAR LAYOUTS
     *  BUT DIFFERENT ACTIONS
     */

    public String getLoginMode(String username){
        // Propose to add a ROLE column in DB for user table to store whether student or teacher
        // Temporary workaround is to parse the username and see if the regex matches student id or not

        return username.contains("@siswa.ukm.edu.my") ? "student" : "teacher";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        txt_login_username = findViewById(R.id.txt_login_username);

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                username = getLoginMode(txt_login_username.getText().toString());

                String loginToastMessage = "Login Successful as " + username;
                Toast.makeText(LoginActivity.this,loginToastMessage,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,ClassActivity.class);
                intent.putExtra("LOGIN_MODE", username);
                startActivity(intent);

            }
        });
    }
}