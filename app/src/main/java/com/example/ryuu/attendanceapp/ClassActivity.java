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
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.object.Class;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabtn_add_class;
    ClassRecyclerViewAdapter classRecyclerViewAdapter;
    private String classCode = "";

    private FirebaseAuth firebaseAuth;

    //MODE
    String loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // GET LOGIN MODE
        loginMode = getIntent().getStringExtra("LOGIN_MODE");

        Toast.makeText(ClassActivity.this,loginMode,Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        fabtn_add_class = findViewById(R.id.fabtn_add_class);

        Toolbar toolbar = findViewById(R.id.tb_class);
        setSupportActionBar(toolbar);

        linearLayoutManager = new LinearLayoutManager(ClassActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Class> allClassInfor = getAllClassInfor();
        classRecyclerViewAdapter = new ClassRecyclerViewAdapter(ClassActivity.this, allClassInfor);
        recyclerView.setAdapter(classRecyclerViewAdapter);

        firebaseAuth = firebaseAuth.getInstance();

        fabtn_add_class.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(loginMode.equals("student")){
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
                            Toast.makeText(ClassActivity.this,"Class Added",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ClassActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else if (loginMode.equals("teacher")){
                    Intent intent = new Intent(ClassActivity.this,CreateCourseActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView)item.getActionView();

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
    private List<Class> getAllClassInfor(){

        List<Class> allClass = new ArrayList<Class>();

        allClass.add(new Class("Mobile Programming",R.drawable.mobile));
        allClass.add(new Class("Web Programming",R.drawable.web));
        allClass.add(new Class("Numerical Analysis",R.drawable.numerical));
        allClass.add(new Class("Network Programming",R.drawable.network));

        return allClass;
    }

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
        intent.putExtra("LOGIN_MODE", loginMode);
        startActivity(intent);
    }
}
