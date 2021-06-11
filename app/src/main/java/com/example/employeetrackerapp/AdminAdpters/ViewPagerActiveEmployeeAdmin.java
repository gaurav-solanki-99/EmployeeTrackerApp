package com.example.employeetrackerapp.AdminAdpters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.employeetrackerapp.FragmentAdmin.ActiveFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.HalfdayFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.LeaveFragmentClass;

public class ViewPagerActiveEmployeeAdmin  extends FragmentPagerAdapter
{

    public ViewPagerActiveEmployeeAdmin(FragmentManager manager){
        super(manager);
    }

    @NonNull

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position == 0)
            fragment = new ActiveFragmentClass();
        else if(position == 1)
            fragment = new LeaveFragmentClass();
        else if(position == 2)
            fragment = new HalfdayFragmentClass();

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
