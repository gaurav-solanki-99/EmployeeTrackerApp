package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.databinding.LayoutRecyclerEmployeStatusBinding;

import java.util.ArrayList;

public class HalfdayEmployeeAdminAdapter extends RecyclerView.Adapter<HalfdayEmployeeAdminAdapter.HalfdayViewHolder>
{
    Context context;
    ArrayList<EmployeeWorkingDetails> al;

    public HalfdayEmployeeAdminAdapter(Context context,ArrayList<EmployeeWorkingDetails> al)
    {
        this.al=al;
        this.context=context;
    }

    @NonNull

    @Override
    public HalfdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutRecyclerEmployeStatusBinding binding=LayoutRecyclerEmployeStatusBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HalfdayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  HalfdayEmployeeAdminAdapter.HalfdayViewHolder holder, int position)
    {
        EmployeeWorkingDetails emp = al.get(position);
        holder.binding.tvempname.setText(emp.getEmpName());
        holder.binding.tvempdepartment.setText(emp.getEmpDepartment());
        holder.binding.tvintime.setText(emp.getStartTime());
        holder.binding.tvouttime.setText(emp.getEndTime());


    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class HalfdayViewHolder extends RecyclerView.ViewHolder
    {
        LayoutRecyclerEmployeStatusBinding binding;
        public HalfdayViewHolder(LayoutRecyclerEmployeStatusBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
