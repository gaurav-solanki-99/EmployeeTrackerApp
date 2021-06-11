package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.databinding.LayoutRecyclerLaeveRequestAdminBinding;

import java.util.ArrayList;

public class LeaveRequestAdminAdapter extends RecyclerView.Adapter<LeaveRequestAdminAdapter.LeaveRequestViewHolder>
{
    Context context;
    ArrayList<EmployeLeavesApplicationRecord> al;

    public LeaveRequestAdminAdapter(Context context,ArrayList<EmployeLeavesApplicationRecord> al)
    {
        this.al=al;
        this.context=context;
    }

    @NonNull

    @Override
    public LeaveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestAdminAdapter.LeaveRequestViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class LeaveRequestViewHolder extends RecyclerView.ViewHolder
    {
        LayoutRecyclerLaeveRequestAdminBinding binding;

        public LeaveRequestViewHolder(LayoutRecyclerLaeveRequestAdminBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
