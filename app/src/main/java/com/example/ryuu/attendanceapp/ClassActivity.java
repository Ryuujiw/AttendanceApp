package com.example.ryuu.attendanceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabtn_add_class;
    private String classCode = "";

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

        linearLayoutManager = new LinearLayoutManager(ClassActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Class> allClassInfor = getAllClassInfor();
        ClassRecyclerViewAdapter classRecyclerViewAdapter = new ClassRecyclerViewAdapter(ClassActivity.this, allClassInfor);
        recyclerView.setAdapter(classRecyclerViewAdapter);

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

    private List<Class> getAllClassInfor(){

        List<Class> allClass = new ArrayList<Class>();

        allClass.add(new Class("Mobile Programming",R.drawable.mobile));
        allClass.add(new Class("Web Programming",R.drawable.web));
        allClass.add(new Class("Numerical Analysis",R.drawable.numerical));
        allClass.add(new Class("Network Programming",R.drawable.network));

        return allClass;
    }
}
