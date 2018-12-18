package com.example.ryuu.attendanceapp;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ryuu.attendanceapp.adapter.ClassListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassList_Teacher_Activity extends AppCompatActivity implements Add_Class_Activity.addClassActivityListener{

    ClassListAdapter classListAdapter;
    FloatingActionButton floatingActionButton;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    String classTitle="", classDate="", classTime="";
    List<ClassList> allClassList;
    Add_Class_Activity addClassActivity;
    ClassList classData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list__teacher_);

        Toolbar toolbar = findViewById(R.id.toolbar_classlist);
        setSupportActionBar(toolbar);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(false);

        linearLayoutManager = new LinearLayoutManager(ClassList_Teacher_Activity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recycler_view_class_list);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(ClassList_Teacher_Activity.this, DividerItemDecoration.VERTICAL));

        //show list
        applyText(classTitle, classDate, classTime);
        allClassList = getAllClassListInfo();
        classListAdapter = new ClassListAdapter(ClassList_Teacher_Activity.this, allClassList);
        classListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(classListAdapter);


        floatingActionButton = findViewById(R.id.fabutton_add);
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

        if(allClassList==null){
            allClassList = new ArrayList<>();
        }

        classData = new ClassList(classTitle, classDate, classTime);
        allClassList.add(classData);

    }

    public List<ClassList> getAllClassListInfo(){ //getdatatweets
        return allClassList;
    }
}
