package com.example.employeetrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.databinding.LeavesRecordListBinding;

import java.util.ArrayList;

public class HalfdayAdapter extends RecyclerView.Adapter<HalfdayAdapter.HalfdayViewHolder>{
    Context context;
    ArrayList<Halfday> al;
    public HalfdayAdapter(Context context,ArrayList<Halfday> al)
    {
        this.context=context;
        this.al=al;
    }

    @NonNull

    @Override
    public HalfdayViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LeavesRecordListBinding binding=LeavesRecordListBinding.inflate(LayoutInflater.from(context),parent,false);


        return new HalfdayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HalfdayAdapter.HalfdayViewHolder holder, int position)
    {
        Halfday halfday =al.get(position);

        holder.binding.tvleavesdate.setText(halfday.getHalfdayDate());
        holder.binding.tvrequestmonth.setText(halfday.getHalfdayMonth());
        holder.binding.tvlevesstatus.setText(halfday.getHalfdayStatus());
        holder.binding.tvleavesreason.setText(halfday.getHalfdayStatus());

    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    class HalfdayViewHolder extends RecyclerView.ViewHolder
    {
        LeavesRecordListBinding binding;

        public HalfdayViewHolder(LeavesRecordListBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }


    }
}