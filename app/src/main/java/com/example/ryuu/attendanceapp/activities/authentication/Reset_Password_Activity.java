package com.example.ryuu.attendanceapp.activities.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password_Activity extends AppCompatActivity {

    private Button btn_submit;
    private EditText et_retrieve_email;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password_);

        btn_submit = findViewById(R.id.btn_submit);
        et_retrieve_email = findViewById(R.id.et_retrieve_email);

        mAuth = FirebaseAuth.getInstance();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String retrieve_email = et_retrieve_email.getText().toString().trim();

                if (TextUtils.isEmpty(retrieve_email)) {
                    Toast.makeText(Reset_Password_Activity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();

                }
                else{
                    //progressBar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(retrieve_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Reset_Password_Activity.this, "Reset password email has been sent successfully.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Reset_Password_Activity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(Reset_Password_Activity.this, "Error occured: "+message+" Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


                }
            });
    }
}
