package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.ClassFragmentPagerAdapter;

public class ClassDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    Bitmap bitmap;
    String qrURL, classID;
    ForumFragment forumFragment;

    //Check student or lecturer
    String loginMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        Intent intent = getIntent();
        loginMode = intent.getStringExtra("LoginMode");
        bitmap = (Bitmap) intent.getParcelableExtra("qrImage");
        qrURL = intent.getStringExtra("qrURL");
        classID = intent.getStringExtra("classID");

        forumFragment = new ForumFragment();

        // LOGIN MODE
        loginMode = getIntent().getStringExtra("LOGIN_MODE");
        Toast.makeText(ClassDetailsActivity.this, loginMode, Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putString("LOGIN_MODE", loginMode);
        forumFragment.setArguments(bundle);

        toolbar = findViewById(R.id.toolbar_class);
        tabLayout = findViewById(R.id.tabs_class);
        viewPager = findViewById(R.id.view_pager_class);

        setSupportActionBar(toolbar);
        setupViewPager();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        /**Toast.makeText(ClassDetailsActivity.this,"Attendance is selected", Toast.LENGTH_SHORT).show();**/
                        break;
                    case 1:
                        Toast.makeText(ClassDetailsActivity.this,"Forum is selected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }

        });
    }

    private void setupViewPager(){
        ClassFragmentPagerAdapter adapter = new ClassFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AttendanceFragment(),"Attendance");
        adapter.addFrag(forumFragment,"Forum");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
