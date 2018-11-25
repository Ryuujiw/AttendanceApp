package com.example.ryuu.attendanceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.ClassListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassList_Teacher_Activity extends AppCompatActivity {

    ClassListAdapter classListAdapter;
    FloatingActionButton floatingActionButton;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    String classTitle = "";
    String classDate = "";
    String classTime = "";
    List<ClassList> allClassList;



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

        List<ClassList> allClassListInfo = getAllClassListInfo();
        classListAdapter = new ClassListAdapter(ClassList_Teacher_Activity.this, allClassListInfo);
        recyclerView.setAdapter(classListAdapter);

        floatingActionButton = findViewById(R.id.fabutton_add);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ClassList_Teacher_Activity.this);

                LayoutInflater inflater = ClassList_Teacher_Activity.this.getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.activity_add__class_,null));

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //how to retrieve data from Add_Class_Activity.class?
                        
                        allClassList.add(new ClassList(classTitle, classDate, classTime));
                        Toast.makeText(ClassList_Teacher_Activity.this, "Class added", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
        });

    }

    public List<ClassList> getAllClassListInfo() {

        allClassList = new ArrayList<ClassList>();

        //retrieve class list from database
        allClassList.add(new ClassList("Lecture Week 3", "2018-07-28", "2.00 p.m."));
        allClassList.add(new ClassList("Lecture Week 4", "2018-07-30", "2.00 p.m."));
        allClassList.add(new ClassList("Lecture Week 5", "2018-08-02", "10.00 a.m."));
        allClassList.add(new ClassList("Lecture Week 6", "2018-08-10", "2.00 p.m."));
        allClassList.add(new ClassList("Lecture Week 7", "2018-08-15", "4.00 p.m."));
        allClassList.add(new ClassList("Lecture Week 8", "2018-08-22", "2.00 p.m."));
        allClassList.add(new ClassList("Lecture Week 9", "2018-08-27", "2.00 p.m."));
        allClassList.add(new ClassList("Lecture Week 10", "2018-09-03", "8.00 a.m."));

        return allClassList;

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.classlist_menu,menu);
//
//        MenuItem item = menu.findItem(R.id.menu_search_classlist);
//        SearchView searchView = (SearchView)item.getActionView();
//
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        Intent intent;
//
//        switch (item.getItemId()){
//            case R.id.menu_forum:
//                intent = new Intent(ClassList_Teacher_Activity.this, ForumFragment.class);
//                startActivity(intent);
//                break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
