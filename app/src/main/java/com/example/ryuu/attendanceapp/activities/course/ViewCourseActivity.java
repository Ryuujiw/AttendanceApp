package com.example.ryuu.attendanceapp.activities.course;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.activities.authentication.LoginActivity;
import com.example.ryuu.attendanceapp.activities.profile.ViewProfileActivity;
import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.objects.Class;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCourseActivity extends AppCompatActivity {

    private FirebaseUser User;
    private DatabaseReference mDatabaseUser, mDatabaseUser1;
    private FirebaseAuth firebaseAuth;

    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabtn_add_class;
    ClassRecyclerViewAdapter classRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView txt_no_result;
    List<Class> classList = new ArrayList<>();
    private String classCode = "";
    ProgressDialog progress;
    String matric=" ",loginMode,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        // GET LOGIN MODE
        loginMode = getIntent().getStringExtra("LOGIN_MODE");
        txt_no_result = findViewById(R.id.textView3);

        Toast.makeText(ViewCourseActivity.this, loginMode, Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.recycler_view);
        fabtn_add_class = findViewById(R.id.fabtn_add_class);
        progress = new ProgressDialog(ViewCourseActivity.this);
        progress.setTitle("Loading..");
        progress.setMessage("Please wait for a moment");

        Toolbar toolbar = findViewById(R.id.tb_class);
        setSupportActionBar(toolbar);
        progress.show();
        //get user matric from firebase
        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();
        uid = User.getUid();

        linearLayoutManager = new LinearLayoutManager(ViewCourseActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // [START initialize_database_ref]
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("/users/"+loginMode+"/"+uid+"/");
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(loginMode.equals("student")) {
                    matric = dataSnapshot.child("matric").getValue(String.class);;
                }else if(loginMode.equals("lecturer")){
                    matric = dataSnapshot.child("matric").getValue(String.class);;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewCourseActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //so far only done lecturer create course, this part is retrieving data from firebase. Will remove the login mode if statement later after i done student add courses
            mDatabaseUser1 = FirebaseDatabase.getInstance().getReference("/courses/");
            mDatabaseUser1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progress.dismiss();
                    classList.clear();
                    for(DataSnapshot courseSnapshot : dataSnapshot.getChildren()){
                        Boolean isPresent = courseSnapshot.child(loginMode).hasChild(matric);
                        if(isPresent){
                            Class course = courseSnapshot.getValue(Class.class);
                            classList.add(course);
                        }
                    }
                    classRecyclerViewAdapter = new ClassRecyclerViewAdapter(ViewCourseActivity.this, classList, loginMode);
                    classRecyclerViewAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(classRecyclerViewAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        fabtn_add_class.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (loginMode.equals("student")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourseActivity.this);
                    builder.setTitle("Enter your class code : ");

                    // Set up the input
                    final EditText input = new EditText(ViewCourseActivity.this);
                    // Specify the type of input expected;
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            classCode = input.getText().toString().toLowerCase().trim();
                            if(!classCode.isEmpty()) {
                                //add user matric into course child
                                registerCourse(classCode);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourseActivity.this);
                                builder.setMessage("Please enter course code.").setTitle("Error").setPositiveButton("OK", null);
                                AlertDialog dialog2 = builder.create();
                                dialog2.show();
                            }
//                            Toast.makeText(ViewCourseActivity.this, "Class Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ViewCourseActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else if (loginMode.equals("lecturer")) {
                    Intent intent = new Intent(ViewCourseActivity.this, CreateCourseActivity.class);
                    intent.putExtra("LOGIN_MODE", loginMode);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

//        MenuItem item = menu.findItem(R.id.menu_search);
//        SearchView searchView = (SearchView) item.getActionView();
//
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                classRecyclerViewAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    public void registerCourse(final String coursecode){
        mDatabaseUser1 = FirebaseDatabase.getInstance().getReference("/courses/");
        mDatabaseUser1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(coursecode)){
                    //Course exists
                    //check if user registered in the course
                    boolean registered = dataSnapshot.child("/"+coursecode+"/"+loginMode+"/").hasChild(matric);
                    if(registered == true){

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourseActivity.this);
                        builder.setMessage("Courses exists").setTitle("You have registered under this course.").setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else {
                        String course_name = dataSnapshot.child("/"+coursecode+"/course_name/").getValue(String.class);
                        mDatabaseUser1.child(coursecode).child(loginMode).child(matric).setValue(true);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourseActivity.this);
                        addCourseintoUserProfile(coursecode,uid);
                        builder.setMessage( "Successfully register "+course_name+".").setTitle("Courses register successfully").setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }else{
                    //Course does not exist
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourseActivity.this);
                    builder.setMessage("Please enter valid course code.").setTitle("Invalid Course Code.").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewCourseActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCourseintoUserProfile(String coursecode, String uid){
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("/users/"+loginMode+"/"+uid+"/");
        mDatabaseUser.child("courses").child(coursecode).setValue(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.menu_myprofile:
                Toast.makeText(ViewCourseActivity.this, "My Profile", Toast.LENGTH_SHORT).show();
                GoToMyProfile(loginMode);
                break;

            case R.id.menu_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourseActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ViewCourseActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                
        }
        return super.onOptionsItemSelected(item);
    }

    private void GoToMyProfile(String loginMode){
        Intent intent = new Intent(ViewCourseActivity.this,ViewProfileActivity.class);
        intent.putExtra("LOGIN_MODE",loginMode);
        startActivity(intent);
    }

}
