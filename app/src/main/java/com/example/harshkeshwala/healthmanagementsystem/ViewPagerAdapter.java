package com.example.harshkeshwala.healthmanagementsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"My Patient", "All Patient"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        MyPatient myPatient = new MyPatient();
        position=position+1;
        Bundle bundle= new Bundle();
        bundle.putString("message","Fragment :" +position);
        myPatient.setArguments(bundle);


        return myPatient;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       // position =position+1;

        return tabTitles[position];
    }
}
