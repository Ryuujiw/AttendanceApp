package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ClassListDetails extends AppCompatActivity {

    Button btn_startClass;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list_details);

        toolbar = findViewById(R.id.toolbar_classlist_details);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Class Details");

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(false);

        btn_startClass = findViewById(R.id.btn_startclass);

        btn_startClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ClassListDetails.this, );
//                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.classlist_details_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()){
            case R.id.classList_menu_forum:
                intent = new Intent(ClassListDetails.this, ForumFragment.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
