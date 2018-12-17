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
import android.widget.Toast;

import com.example.ryuu.attendanceapp.object.Lecturer;
import com.example.ryuu.attendanceapp.object.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_edit_profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabaseUser;
    EditText et_name,et_email,et_course;
    Spinner spinner;
    String matric,login_mode;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.tb_editprofile);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.sp_gender);
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

        et_name = findViewById(R.id.et_name_edit);
        et_email = findViewById(R.id.et_email);
        et_course = findViewById(R.id.et_course);

        matric = getIntent().getStringExtra("matric");
        login_mode = getIntent().getStringExtra("LOGIN_MODE");

        if(login_mode.equals("teacher")){
            et_course.setVisibility(View.GONE);
        }

        // [START initialize_database_ref]
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(matric);
        // [END initialize_database_ref]
        //retrieve users data based on matric
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(login_mode.equals("student")) {
                    Student profile = dataSnapshot.getValue(Student.class);
                    et_name.setText(profile.getName());
                    et_email.setText(profile.getEmail());
                    et_course.setText(profile.getMajor());
                    spinner.setSelection(getIndex(spinner, profile.getGender()));
                }else if(login_mode.equals("teacher")){
                    Lecturer profile = dataSnapshot.getValue(Lecturer.class);
                    et_name.setText(profile.getName());
                    et_email.setText(profile.getEmail());
                    spinner.setSelection(getIndex(spinner, profile.getGender()));
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
                if(et_name.getText()!=null && et_course.getText()!=null && et_email.getText()!=null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Save changes made?");
                    alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //save and update database
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("name",et_name.getText().toString().trim());
                            result.put("email",et_email.getText().toString().trim());
                            if(login_mode.equals("student")) {
                                result.put("major", et_course.getText().toString().trim());
                            }
                            result.put("gender",spinner.getSelectedItem().toString().toLowerCase());
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
        intent.putExtra("matric",matric );
        intent.putExtra("LOGIN_MODE", login_mode);
        startActivity(intent);
    }
}
