package com.example.ryuu.attendanceapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
    AttendanceFragment attendanceFragment;

    private String loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        attendanceFragment = new AttendanceFragment();

        // LOGIN MODE
        loginMode = getIntent().getStringExtra("LOGIN_MODE");
        Toast.makeText(ClassDetailsActivity.this, loginMode, Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();
        bundle.putString("LOGIN_MODE", loginMode);
        attendanceFragment.setArguments(bundle);

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
                        Toast.makeText(ClassDetailsActivity.this,"Attendance is selected", Toast.LENGTH_SHORT).show();
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
        adapter.addFrag(attendanceFragment,"Attendance");
        adapter.addFrag(new ForumFragment(),"Forum");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
