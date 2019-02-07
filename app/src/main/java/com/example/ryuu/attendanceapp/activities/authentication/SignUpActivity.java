package com.example.ryuu.attendanceapp.activities.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.activities.MainActivity;
import com.example.ryuu.attendanceapp.objects.Lecturer;
import com.example.ryuu.attendanceapp.objects.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected Button btn_sign_up;
    protected TextInputEditText txt_sign_up_email;
    protected TextInputEditText txt_sign_up_username;
    protected TextInputEditText txt_sign_up_matrix_no;
    protected TextInputEditText txt_sign_up_password;
    protected Spinner spinner;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;
    private FirebaseUser user;

    String password, email,role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txt_sign_up_email = findViewById(R.id.txt_sign_up_email);
        txt_sign_up_username = findViewById(R.id.txt_sign_up_username);
        txt_sign_up_matrix_no = findViewById(R.id.txt_sign_up_matrix_no);
        txt_sign_up_password = findViewById(R.id.txt_sign_up_password);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        spinner = findViewById(R.id.sp_gender_sign_up);
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //getting the reference of users
        firebaseAuth = FirebaseAuth.getInstance();

        btn_sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                password = txt_sign_up_password.getText().toString();
                email = txt_sign_up_email.getText().toString();

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

                                //get user matric from firebase
                                firebaseAuth = firebaseAuth.getInstance();
                                //get current user logged in
                                user = firebaseAuth.getCurrentUser();
                                String uid = user.getUid();
                                CreateUsers(uid);

                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                user.sendEmailVerification();
                                Toast.makeText(SignUpActivity.this, "An email has been sent to you for verification. Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error. Could not Sign Up", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

    }

    private void CreateUsers(String uid) {
        //getting the values to save
        String name = txt_sign_up_username.getText().toString().trim();
        String matric = txt_sign_up_matrix_no.getText().toString().toLowerCase().trim();
        String gender = spinner.getSelectedItem().toString().trim().toLowerCase();
        role = getRole(email);
        databaseUsers = FirebaseDatabase.getInstance().getReference("/users/"+role+"/");
        //checking if the value is provided
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(matric)) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            if(role.equals("student")) {
                //Saving the student
                //creating an Student Object
                Student user = new Student(name,email.toLowerCase(),matric,gender,"null");
                //matric is used as unique id for reference
                dataMap.put(uid, user.toMap());
            }else if(role.equals("lecturer")) {
                //Saving the teacher
                //creating an Lecturer Object
                Lecturer user = new Lecturer(name,email.toLowerCase(),matric.toLowerCase(),gender);
                //matric is used as unique id for reference
                dataMap.put(uid, user.toMap());
            }
            databaseUsers.updateChildren(dataMap);
            //displaying a success message
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage("Sign Up successful.").setTitle("Sign Up Successful").setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //if the value is not given displaying a toast
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage("Please enter required field.").setTitle("Sign Up Unsuccessful").setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getRole(String email){
        // Propose to add a ROLE column in DB for user table to store whether student or teacher
        // Temporary workaround is to parse the username and see if the regex matches student id or not
        return email.contains("@siswa.ukm.edu.my") ? "student" : "lecturer";
    }
}
