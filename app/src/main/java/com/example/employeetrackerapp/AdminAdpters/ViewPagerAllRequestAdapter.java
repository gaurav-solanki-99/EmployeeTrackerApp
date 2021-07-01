package com.example.employeetrackerapp.AdminAdpters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.employeetrackerapp.FragmentAdmin.ApproveFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.PendingFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.RejectFragmentClass;

public class ViewPagerAllRequestAdapter extends FragmentPagerAdapter
{


    public ViewPagerAllRequestAdapter( FragmentManager manager) {
        super(manager);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position == 0)
            fragment = new PendingFragmentClass();
        else if(position == 1)
            fragment = new ApproveFragmentClass();
        else if(position == 2)
            fragment = new RejectFragmentClass();
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
            tabTitle = "Pending";
        else if(position == 1)
            tabTitle = "Approve";
        else if(position == 2)
            tabTitle = "Reject";
        return tabTitle;
    }
}
