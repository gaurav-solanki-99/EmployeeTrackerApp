package com.example.employeetrackerapp.AdminAdpters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.employeetrackerapp.FragmentAdmin.ApproveFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.HalfdayApproveFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.HalfdayPendingFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.HalfdayRejectFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.PendingFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.RejectFragmentClass;

public class ViewPagerAllHalfdayRequestAdapter extends FragmentPagerAdapter
{


    public ViewPagerAllHalfdayRequestAdapter( FragmentManager manager) {
        super(manager);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position == 0)
            fragment = new HalfdayPendingFragmentClass();
        else if(position == 1)
            fragment = new HalfdayApproveFragmentClass();
        else if(position == 2)
            fragment = new HalfdayRejectFragmentClass();
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
