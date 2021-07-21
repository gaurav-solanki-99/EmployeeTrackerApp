package com.example.employeetrackerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.databinding.LeavesRecordListBinding;

import java.util.ArrayList;

public class LeavesAdapter extends RecyclerView.Adapter<LeavesAdapter.LeavesViewHolder>
{
    Context context;
    ArrayList<Leaves> al;
    public LeavesAdapter(Context context,ArrayList<Leaves> al)
    {
        this.context=context;
        this.al=al;
    }

    @NonNull

    @Override
    public LeavesViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LeavesRecordListBinding binding=LeavesRecordListBinding.inflate(LayoutInflater.from(context),parent,false);

        return new LeavesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeavesAdapter.LeavesViewHolder holder, int position) {

        Leaves leaves=al.get(position);

        holder.binding.tvleavesdate.setText(leaves.getLeavesDate());
        holder.binding.tvrequestmonth.setText(leaves.getLeavesMonth());

        holder.binding.tvleavesreason.setText(leaves.getLeavesReason());

        if(leaves.getLeavesStatus().equalsIgnoreCase("Approve")||leaves.getLeavesStatus().equalsIgnoreCase("Reject"))
        {
            holder.binding.tvlevesstatus.setText(leaves.getLeavesStatus()+" by "+leaves.getAdminName());
        }
        else if(leaves.getLeavesStatus().equalsIgnoreCase("Pending"))
        {
            holder.binding.tvlevesstatus.setText(leaves.getLeavesStatus());
        }

        holder.binding.leaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,EmployeeShowSingleLeaves.class);
                in.putExtra("LeaveRecord",leaves);
                context.startActivity(in);
            }
        });



    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class LeavesViewHolder extends RecyclerView.ViewHolder
    {
        LeavesRecordListBinding binding;

        public LeavesViewHolder(LeavesRecordListBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }


    }

}