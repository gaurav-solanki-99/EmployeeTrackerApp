package com.example.employeetrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
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
        holder.binding.tvlevesstatus.setText(leaves.getLeavesStatus());
        holder.binding.tvleavesreason.setText(leaves.getLeavesReason());



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
