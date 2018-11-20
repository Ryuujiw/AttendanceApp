package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;

import java.util.List;

public class ClassList_Teacher_Activity extends AppCompatActivity {

    ClassRecyclerViewAdapter classRecyclerViewAdapter;
//    FloatingActionButton floatingActionButton;
//    LinearLayoutManager linearLayoutManager;
//    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list__teacher_);

        Toolbar toolbar = findViewById(R.id.toolbar_classlist);
        setSupportActionBar(toolbar);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(false);

//        recyclerView = findViewById(R.id.recycler_view_class_list);
//        linearLayoutManager = new LinearLayoutManager(ClassList_Teacher_Activity.this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.classlist_menu,menu);

        MenuItem item = menu.findItem(R.id.menu_search_classlist);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()){
            case R.id.menu_forum:
                intent = new Intent(ClassList_Teacher_Activity.this, ForumFragment.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
