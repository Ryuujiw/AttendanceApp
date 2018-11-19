package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class acitvity_myprofile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acitvity_myprofile);
        ImageButton edit = findViewById(R.id.img_btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(acitvity_myprofile.this, activity_edit_profile.class);
                startActivity(intent);
            }
        });
    }
}
