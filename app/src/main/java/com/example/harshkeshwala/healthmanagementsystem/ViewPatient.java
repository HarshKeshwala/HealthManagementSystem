package com.example.harshkeshwala.healthmanagementsystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class ViewPatient extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private  ViewPagerAdapter adapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        viewPager=findViewById(R.id.pager);
        adapter= new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}
