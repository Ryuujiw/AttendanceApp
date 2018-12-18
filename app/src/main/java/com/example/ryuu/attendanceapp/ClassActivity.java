package com.example.ryuu.attendanceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.objects.Class;
import com.example.ryuu.attendanceapp.objects.Lecturer;
import com.example.ryuu.attendanceapp.objects.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    private FirebaseUser User;
    private DatabaseReference mDatabaseUser;
    private Query mDatabase;
    private FirebaseAuth firebaseAuth;

    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabtn_add_class;
    ClassRecyclerViewAdapter classRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView txt_no_result;
    List<Class> allClass;
    private String classCode = "";

    String matric = "", loginMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // GET LOGIN MODE
        loginMode = getIntent().getStringExtra("LOGIN_MODE");
        txt_no_result = findViewById(R.id.textView3);

        Toast.makeText(ClassActivity.this, loginMode, Toast.LENGTH_SHORT).show();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference("/courses/");
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (allClass == null) {
                        allClass = new ArrayList<>();
                    }
                    allClass.clear();
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                        if(childsnapshot.child(loginMode).child(matric).getValue()){
                        Class courses = childsnapshot.getValue(Class.class);
                        allClass.add(courses);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txt_no_result.setText("No available courses");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        fabtn_add_class = findViewById(R.id.fabtn_add_class);

        Toolbar toolbar = findViewById(R.id.tb_class);
        setSupportActionBar(toolbar);

        linearLayoutManager = new LinearLayoutManager(ClassActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        List<Class> allClassInfor = getAllClassInfor();
        classRecyclerViewAdapter = new ClassRecyclerViewAdapter(ClassActivity.this, allClass);
        classRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(classRecyclerViewAdapter);


        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();
        // [START initialize_database_ref]
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(loginMode);
        mDatabase = mDatabaseUser.orderByChild("email").equalTo(User.getEmail().toLowerCase());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    //get user matric id
                    matric = childSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fabtn_add_class.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (loginMode.equals("student")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClassActivity.this);
                    builder.setTitle("Enter your class code : ");

                    // Set up the input
                    final EditText input = new EditText(ClassActivity.this);
                    // Specify the type of input expected;
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            classCode = input.getText().toString();
                            Toast.makeText(ClassActivity.this, "Class Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ClassActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else if (loginMode.equals("lecturer")) {
                    Intent intent = new Intent(ClassActivity.this, CreateCourseActivity.class);
                    intent.putExtra("LOGIN_MODE", loginMode);
                    intent.putExtra("matric", matric);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                classRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

//    public void addintoClassList(String classCode, String className, String description, String date_created) {
//        allClass.add(new Class(classCode, className, description, date_created));
//    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch(item.getItemId())
        {
            case R.id.menu_myprofile:
                Toast.makeText(ClassActivity.this, "My Profile", Toast.LENGTH_SHORT).show();
                GoToMyProfile(loginMode);
                break;

            case R.id.menu_logout:
                firebaseAuth.signOut();
                intent = new Intent(ClassActivity.this, LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void GoToMyProfile(String loginMode){
        Intent intent = new Intent(ClassActivity.this,activity_myprofile.class);
        intent.putExtra("matric",matric);
        intent.putExtra("LOGIN_MODE",loginMode);
        startActivity(intent);
    }
}
