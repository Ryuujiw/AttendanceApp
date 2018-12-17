package com.example.ryuu.attendanceapp;

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
    ForumFragment forumFragment;

    private String loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        //Intent intent = getIntent();

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

    }

    private void setupViewPager(){
        ClassFragmentPagerAdapter adapter = new ClassFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AttendanceFragment(),"Attendance");
        adapter.addFrag(forumFragment,"Forum");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
