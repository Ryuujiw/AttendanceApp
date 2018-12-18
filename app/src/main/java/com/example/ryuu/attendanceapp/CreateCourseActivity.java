package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateCourseActivity extends AppCompatActivity {

    Button btn_create;
    EditText et_course_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        btn_create = findViewById(R.id.btn_create);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Intent intent = new Intent(CreateCourseActivity.this, CourseDetails.class);
                startActivity(intent);
            }
        });

    }
}
