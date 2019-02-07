package com.example.ryuu.attendanceapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseDetails extends AppCompatActivity {

    Button btn_done;
    String uid,login_mode,course_code;
    TextView txt_course_name, txt_course_code, txt_course_lecturer, txt_course_date_created;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        txt_course_code = findViewById(R.id.txt_coursecode);
        txt_course_lecturer = findViewById(R.id.txt_creatorname);
        txt_course_date_created = findViewById(R.id.txt_date);
        txt_course_name = findViewById(R.id.txt_coursename);

        //get user matric from firebase
        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();
        uid=User.getUid();
        login_mode = getIntent().getStringExtra("LOGIN_MODE");
        course_code = getIntent().getStringExtra("course_code");

        // [START initialize_database_ref]
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("/courses/"+course_code+"/");
        // [END initialize_database_ref]
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                txt_course_name.setText(dataSnapshot.child("course_name").getValue(String.class));
                txt_course_code.setText(dataSnapshot.child("course_code").getValue(String.class));
                txt_course_date_created.setText(dataSnapshot.child("date_created").getValue(String.class));
                getLecturerName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_done = findViewById(R.id.btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseDetails.this , ClassActivity.class);
                intent.putExtra("LOGIN_MODE", login_mode);
                startActivity(intent);
            }
        });
    }

    public void getLecturerName(){
        // [START initialize_database_ref]
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("/users/lecturer/"+uid+"/");
        // [END initialize_database_ref]
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                txt_course_lecturer.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CourseDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
