package com.example.ryuu.attendanceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_edit_profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseUser User;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseUser;
    TextView tv_major;
    EditText et_name,et_matric,et_email;
    Spinner sp_gender,sp_major;
    String uid,login_mode;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //get user matric from firebase
        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();

        login_mode = getIntent().getStringExtra("LOGIN_MODE");
        uid = User.getUid();

        Toolbar toolbar = findViewById(R.id.tb_editprofile);
        setSupportActionBar(toolbar);

        sp_gender = findViewById(R.id.sp_gender);
        sp_gender.setOnItemSelectedListener(this);
        sp_major = findViewById(R.id.sp_major);
        sp_major.setOnItemSelectedListener(this);

        // Gender Drop down elements
        List<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Major Drop down elements
        List<String> major = new ArrayList<String>();
        major.add("Computer Science");
        major.add("SEIS");
        major.add("SEMM");
        major.add("Information Technology");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, major);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_gender.setAdapter(dataAdapter1);
        sp_major.setAdapter(dataAdapter2);

        et_name = findViewById(R.id.et_name_edit);
        et_email = findViewById(R.id.et_email);
        et_matric = findViewById(R.id.et_matric);
        tv_major = findViewById(R.id.tv_major);


        if(login_mode.equals("lecturer")){
            sp_major.setVisibility(View.GONE);
            tv_major.setVisibility(View.GONE);
        }

        // [START initialize_database_ref]
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(login_mode
        ).child(uid);
        // [END initialize_database_ref]
        //retrieve users data based on matric
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(login_mode.equals("student")) {
                    Student profile = dataSnapshot.getValue(Student.class);
                    et_name.setText(profile.getName());
                    et_email.setText(profile.getEmail());
                    et_matric.setText(profile.getMatric());
                    sp_major.setSelection(getIndex(sp_major, profile.getMajor()));
                    sp_gender.setSelection(getIndex(sp_gender, profile.getGender()));
                }else if(login_mode.equals("lecturer")){
                    Lecturer profile = dataSnapshot.getValue(Lecturer.class);
                    et_name.setText(profile.getName());
                    et_email.setText(profile.getEmail());
                    et_matric.setText(profile.getMatric());
                    sp_gender.setSelection(getIndex(sp_gender, profile.getGender()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity_edit_profile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId())
        {
            case R.id.menu_item_save:
                if(et_name.getText()!=null && et_matric.getText()!=null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Save changes made?");
                    alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //save and update database
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("name",et_name.getText().toString().trim());
                            result.put("email",et_email.getText().toString().trim());
                            result.put("matric",et_matric.getText().toString().trim());
                            if(login_mode.equals("student")) {
                                result.put("major", sp_major.getSelectedItem().toString().toLowerCase());
                            }
                            result.put("gender",sp_gender.getSelectedItem().toString().toLowerCase());
                            mDatabaseUser.updateChildren(result);
                            Toast.makeText(activity_edit_profile.this,"Change saved",Toast.LENGTH_LONG).show();
                            back();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(activity_edit_profile.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please fill in required information.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            case R.id.menu_item_close:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Discard changes?");
                alertDialogBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(activity_edit_profile.this,"Change discarded",Toast.LENGTH_LONG).show();
                        back();
                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //stay on current page
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //get gender by index
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().toLowerCase().equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void  back(){
        intent = new Intent(activity_edit_profile.this, activity_myprofile.class);
        intent.putExtra("LOGIN_MODE", login_mode);
        startActivity(intent);
    }
}
