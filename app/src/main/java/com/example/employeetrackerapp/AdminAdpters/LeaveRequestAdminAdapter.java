package com.example.employeetrackerapp.AdminAdpters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminActivity.ApprovalApplicationActivity;
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
        LayoutRecyclerLaeveRequestAdminBinding  binding= LayoutRecyclerLaeveRequestAdminBinding.inflate(LayoutInflater.from(context),parent,false);


        return new LeaveRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestAdminAdapter.LeaveRequestViewHolder holder, int position)
    {
        EmployeLeavesApplicationRecord empl = al.get(position);


        holder.binding.tvempname.setText(empl.getEmpName());
        holder.binding.tvempdepartment.setText(empl.getEmpDepartment());
        holder.binding.requestdate.setText(empl.getLeaveStartDate());
        Glide.with(context).load(empl.getProfile()).into(holder.binding.profileImage);


        holder.binding.btnshowrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(context,ApprovalApplicationActivity.class);
                in.putExtra("ApproveEmp",empl);
                context.startActivity(in);
                ((Activity)context).finish();
            }
        });



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
