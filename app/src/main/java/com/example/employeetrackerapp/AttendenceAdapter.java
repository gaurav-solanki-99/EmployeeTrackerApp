package com.example.employeetrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.employeetrackerapp.databinding.AttendenceSingleViewBinding;


import java.util.ArrayList;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.AttendenceViewHolder> {

    Context context;
    ArrayList<EmployeeAttendence> al;



    public AttendenceAdapter(Context context,ArrayList<EmployeeAttendence> al)
    {
        this.context=context;
        this.al=al;
    }

    @NonNull

    @Override
    public AttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttendenceSingleViewBinding binding =AttendenceSingleViewBinding.inflate(LayoutInflater.from(context),parent,false);


        return new AttendenceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendenceAdapter.AttendenceViewHolder holder, int position) {

        EmployeeAttendence employee = al.get(position);

        holder.binding.tvdayleaves.setText(employee.getWorkday());
        holder.binding.tvdateleaves.setText(employee.getWorkdate());
        holder.binding.tvintime.setText(employee.getIntime());
        holder.binding.tvouttime.setText(employee.getOuttime());
        holder.binding.tvworkhours.setText(employee.getWorkhours());

        holder.binding.tvouttime.setVisibility(View.VISIBLE);

        String status = employee.getDaystatus();

        if(status.equalsIgnoreCase("Present"))
        {
            holder.binding.leavescardbackground.setVisibility(View.GONE);
        }
        if(status.equalsIgnoreCase("Absent"))
        {
            holder.binding.cardbackground.setVisibility(View.GONE);
            holder.binding.leavescardbackground.setVisibility(View.VISIBLE);
            holder.binding.leavestvdateleaves.setText(employee.getWorkdate());
            holder.binding.leavestvmonth.setText(employee.getWorkday());



        }
        if(status.equalsIgnoreCase("Halfday"))
        {
            holder.binding.cardbackground.setBackgroundResource(R.color.halfdaycolor);

        }



    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class  AttendenceViewHolder extends RecyclerView.ViewHolder
    {
        AttendenceSingleViewBinding binding;

        public AttendenceViewHolder(AttendenceSingleViewBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
