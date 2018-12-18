package com.example.ryuu.attendanceapp;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ryuu.attendanceapp.adapter.ClassListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassList_Teacher_Activity extends AppCompatActivity implements Add_Class_Activity.addClassActivityListener{
    ClassListAdapter classListAdapter;
    FloatingActionButton floatingActionButton;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    String classTitle="", classDate="", classTime="", classID;
    List<Class_list> allClassList;
    Add_Class_Activity addClassActivity;
    Class_list classData;
    DatabaseReference mDataRef;
    TextView noClassView;
    String loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list__teacher_);

        Toolbar toolbar = findViewById(R.id.toolbar_classlist);
        setSupportActionBar(toolbar);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(false);

        loginMode = ClassList_Teacher_Activity.this.getIntent().getStringExtra("LoginMode");

        linearLayoutManager = new LinearLayoutManager(ClassList_Teacher_Activity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recycler_view_class_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(ClassList_Teacher_Activity.this, DividerItemDecoration.VERTICAL));

        noClassView = findViewById(R.id.empty_view);

        //show list
        mDataRef = FirebaseDatabase.getInstance().getReference("/classes/networkw1");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot classSnapshot) {
                if(classSnapshot!=null){
                    recyclerView.setVisibility(View.VISIBLE);
                    noClassView.setVisibility(View.GONE);
                    if(allClassList==null){
                        allClassList = new ArrayList<>();
                    }
                    allClassList.clear();
                    for(DataSnapshot dataSnapshot: classSnapshot.getChildren()){
                        classTitle = dataSnapshot.child("className").getValue(String.class);
                        classDate = dataSnapshot.child("date").getValue(String.class);
                        classTime = dataSnapshot.child("startTime").getValue(String.class);
                        classID = dataSnapshot.child("classID").getValue(String.class);
                        applyText(classTitle, classDate, classTime);
                    }
                    allClassList = getAllClassListInfo();
                    classListAdapter = new ClassListAdapter(ClassList_Teacher_Activity.this, allClassList, loginMode);
                    classListAdapter.notifyDataSetChanged();

                    recyclerView.setAdapter(classListAdapter);


                }else{
                    recyclerView.setVisibility(View.GONE);
                    noClassView.setVisibility(View.VISIBLE);
                    allClassList = new ArrayList<>();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        floatingActionButton = findViewById(R.id.fabutton_add);
        if(loginMode.equals("student")){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }else{
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    public void openDialog(){
        addClassActivity = new Add_Class_Activity();
        addClassActivity.show(getSupportFragmentManager(), "add class dialog");
    }

    @Override
    public void applyText(String title, String date, String time) { //process
        //set data from dialog
        this.classTitle = title;
        this.classDate = date;
        this.classTime = time;
        classData = new Class_list(classTitle, classDate, classTime);
        classData.setClassID(classID);
        allClassList.add(classData);

    }

    public List<Class_list> getAllClassListInfo(){ //getdatatweets
        return allClassList;
    }
}