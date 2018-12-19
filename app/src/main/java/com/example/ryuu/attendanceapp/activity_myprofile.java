package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.objects.Lecturer;
import com.example.ryuu.attendanceapp.objects.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_myprofile extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseUser;
    String uid,login_mode;
    TextView tv_name,tv_email,tv_matric,tv_role,tv_gender,tv_course,course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acitvity_myprofile);

        //get user matric from firebase
        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();

        login_mode=getIntent().getStringExtra("LOGIN_MODE");
        uid = User.getUid();

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_matric = findViewById(R.id.tv_matric);
        tv_role = findViewById(R.id.role);
        tv_gender = findViewById(R.id.gender);
        tv_course = findViewById(R.id.course);
        course = findViewById(R.id.tv_major);

        if(login_mode.equals("lecturer")){
            tv_course.setVisibility(View.GONE);
            course.setVisibility(View.GONE);
        }

        //get current user logged in
        User=FirebaseAuth.getInstance().getCurrentUser();
        //get student
        // [START initialize_database_ref]
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("users").child(login_mode).child(uid);
        // [END initialize_database_ref]
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(login_mode.equals("student")) {

                    Student profile = dataSnapshot.getValue(Student.class);
                    tv_name.setText(profile.getName());
                    tv_email.setText(profile.getEmail());
                    tv_role.setText(login_mode);
                    tv_gender.setText(profile.getGender());
                    tv_course.setText(profile.getMajor());
                    tv_matric.setText(profile.getMatric());
                }else if(login_mode.equals("lecturer")){

                    Lecturer profile = dataSnapshot.getValue(Lecturer.class);
                    tv_name.setText(profile.getName());
                    tv_email.setText(profile.getEmail());
                    tv_role.setText(login_mode);
                    tv_gender.setText(profile.getGender());
                    tv_matric.setText(profile.getMatric());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity_myprofile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton edit = findViewById(R.id.img_btn_edit);
        //go to edit profile
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               gotoeditprofile(login_mode);
            }
        });

        ImageButton back = findViewById(R.id.img_btn_back);
        //back to class page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(login_mode);
            }
        });
    }

    private void gotoeditprofile(String loginMode){
        Intent intent = new Intent(activity_myprofile.this,activity_edit_profile.class);
        intent.putExtra("LOGIN_MODE", loginMode);
        startActivity(intent);
    }

    private void back(String loginMode){
        Intent intent = new Intent(activity_myprofile.this,ClassActivity.class);
        intent.putExtra("LOGIN_MODE", loginMode);
        startActivity(intent);
    }
}


