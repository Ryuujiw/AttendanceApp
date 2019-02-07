package com.example.ryuu.attendanceapp.activities.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.activities.ClassActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseUser;
    String uid,login_mode;
    TextView tv_name,tv_email,tv_matric,tv_role,tv_gender,tv_course,course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

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

                    tv_name.setText(dataSnapshot.child("name").getValue(String.class));
                    tv_email.setText(dataSnapshot.child("email").getValue(String.class));
                    tv_gender.setText(dataSnapshot.child("gender").getValue(String.class));
                    tv_course.setText(dataSnapshot.child("major").getValue(String.class));
                    tv_matric.setText(dataSnapshot.child("matric").getValue(String.class));
                    tv_role.setText(login_mode);

                }else if(login_mode.equals("lecturer")){

                    tv_name.setText(dataSnapshot.child("name").getValue(String.class));
                    tv_email.setText(dataSnapshot.child("email").getValue(String.class));
                    tv_gender.setText(dataSnapshot.child("gender").getValue(String.class));
                    tv_matric.setText(dataSnapshot.child("matric").getValue(String.class));
                    tv_role.setText(login_mode);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(ViewProfileActivity.this,EditProfileActivity.class);
        intent.putExtra("LOGIN_MODE", loginMode);
        startActivity(intent);
        finish();
    }

    private void back(String loginMode){
        Intent intent = new Intent(ViewProfileActivity.this,ClassActivity.class);
        intent.putExtra("LOGIN_MODE", loginMode);
        startActivity(intent);
        finish();
    }
}


