package com.example.ryuu.attendanceapp.activities.course;

import android.content.Intent;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.objects.Class;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.objects.Lecturer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateCourseActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser User;
    private DatabaseReference databaseUsers;
    Button btn_create;
    EditText et_name, et_description, et_coursecode;
    String uid,matric,course_code,course_name,course_description,course_date_created;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        //get user matric from firebase
        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();
        uid=User.getUid();

        databaseUsers = FirebaseDatabase.getInstance().getReference("/users/lecturer/"+uid+"/");
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Lecturer profile = dataSnapshot.getValue(Lecturer.class);
                    matric = profile.getMatric();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreateCourseActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_create = findViewById(R.id.btn_create);
        et_name = findViewById(R.id.edittxt_course_name);
        et_description = findViewById(R.id.edittxt_course_description);
        et_coursecode = findViewById(R.id.et_course_code);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course_code = et_coursecode.getText().toString().trim().toLowerCase();
                course_name = et_name.getText().toString().trim();
                course_description = et_description.getText().toString().trim();
                if(!course_code.isEmpty() && !course_name.isEmpty() && !course_description.isEmpty()){
                    course_date_created = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    //save course detail
                    addCourse(course_code,course_name,course_description,course_date_created);
                    addLecturer(course_code);
                    add_Lecturer_courses(course_code);
                    gotoCourseDetails();
                }else{
                    //failed
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateCourseActivity.this);
                    builder.setMessage("Please enter required field.").setTitle("Course Creation Unsuccessful").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

    }

    public void addLecturer(String course_code){
        //add lecturer matric into course using boolean
        databaseUsers = FirebaseDatabase.getInstance().getReference("/courses/"+course_code);
        databaseUsers.child("lecturer").child(matric).setValue(true);
    }

    public void addCourse(String course_code, String course_name, String course_description, String course_date_created){
        //save course detail
        databaseUsers = FirebaseDatabase.getInstance().getReference("/courses/");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Class course = new Class(course_code,course_name,course_description,course_date_created);
        dataMap.put(course_code, course.toMap());
        databaseUsers.updateChildren(dataMap);
    }

    public void add_Lecturer_courses(String course_code){
        databaseUsers = FirebaseDatabase.getInstance().getReference("/users/lecturer/"+uid+"/");
        databaseUsers.child("courses").child(course_code).setValue(true);
    }

    public void gotoCourseDetails(){
        Intent intent = new Intent(CreateCourseActivity.this, CreateCourseSuccessActivity.class);
        intent.putExtra("LOGIN_MODE", "lecturer");
        intent.putExtra("course_code",course_code);
        startActivity(intent);
    }


}
