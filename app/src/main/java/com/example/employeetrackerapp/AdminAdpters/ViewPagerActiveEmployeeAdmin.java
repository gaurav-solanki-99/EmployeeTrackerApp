package com.example.employeetrackerapp.AdminAdpters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.employeetrackerapp.AdminActivity.ActivieEmployeeActivity;
import com.example.employeetrackerapp.FragmentAdmin.ActiveFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.HalfdayFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.LeaveFragmentClass;

public class ViewPagerActiveEmployeeAdmin  extends FragmentPagerAdapter
{
    ActivieEmployeeActivity activieEmployeeActivity;

    public ViewPagerActiveEmployeeAdmin(FragmentManager manager , ActivieEmployeeActivity activity){
        super(manager);
        this.activieEmployeeActivity = activity;
    }

    @NonNull

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position == 0)
            fragment = new ActiveFragmentClass(activieEmployeeActivity);
        else if(position == 1)
            fragment = new LeaveFragmentClass(activieEmployeeActivity);
        else if(position == 2)
            fragment = new HalfdayFragmentClass(activieEmployeeActivity);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable

    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle="";
        if(position ==0)
            tabTitle = "Active";
        else if(position == 1)
            tabTitle = "Leave";
        else if(position == 2)
            tabTitle = "Halfday";

        return tabTitle;
    }
}
