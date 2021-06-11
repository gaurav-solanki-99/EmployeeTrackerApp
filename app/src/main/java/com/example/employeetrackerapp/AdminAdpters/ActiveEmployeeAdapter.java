package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.databinding.LayoutRecyclerEmployeStatusBinding;

import java.util.ArrayList;

public class ActiveEmployeeAdapter extends RecyclerView.Adapter<ActiveEmployeeAdapter.EmployeeViewHolder>
{
    Context context;
    ArrayList<EmployeeWorkingDetails> al;

    public ActiveEmployeeAdapter(Context context,ArrayList<EmployeeWorkingDetails> al)
    {
        this.context=context;
        this.al=al;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutRecyclerEmployeStatusBinding binding=LayoutRecyclerEmployeStatusBinding.inflate(LayoutInflater.from(context),parent,false);

        return new EmployeeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  ActiveEmployeeAdapter.EmployeeViewHolder holder, int position) {

        EmployeeWorkingDetails emp=al.get(position);

        holder.binding.tvempname.setText(emp.getEmpName());
        holder.binding.tvempdepartment.setText(emp.getEmpDepartment());
        holder.binding.tvintime.setText(emp.getStartTime());
        holder.binding.tvouttime.setText(emp.getEndTime());
    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    public class EmployeeViewHolder extends RecyclerView.ViewHolder
    {
        LayoutRecyclerEmployeStatusBinding binding;

        public EmployeeViewHolder(LayoutRecyclerEmployeStatusBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
