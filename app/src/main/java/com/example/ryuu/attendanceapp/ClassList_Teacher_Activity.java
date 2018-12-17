package com.example.ryuu.attendanceapp;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ryuu.attendanceapp.adapter.ClassListAdapter;
import com.example.ryuu.attendanceapp.object.Class;
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
    CardView cardView;
    List<Class> allClassList;
    Add_Class_Activity addClassActivity;
    String classTitle, classDate, classTime;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list__teacher_);

        Toolbar toolbar = findViewById(R.id.toolbar_classlist);
        setSupportActionBar(toolbar);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(false);

        cardView = findViewById(R.id.cardview_classlist);

        linearLayoutManager = new LinearLayoutManager(ClassList_Teacher_Activity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recycler_view_class_list);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(ClassList_Teacher_Activity.this, DividerItemDecoration.VERTICAL));

        databaseReference = FirebaseDatabase.getInstance().getReference("courses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("network").child("classes").exists()){
                    cardView.setVisibility(View.VISIBLE);
                }
                else{
                    cardView.setVisibility(View.INVISIBLE);
                    allClassList = getAllClassListInfo();
                    classListAdapter = new ClassListAdapter(ClassList_Teacher_Activity.this, allClassList);
                    classListAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(classListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    public List<Class> getAllClassListInfo(){
        return allClassList;
    }

    @Override
    public void applyText(final String id) {
        allClassList = new ArrayList<Class>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Class databaseClass = dataSnapshot.child("network").child("classes").child(id).getValue(Class.class);

                allClassList.add(databaseClass);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
