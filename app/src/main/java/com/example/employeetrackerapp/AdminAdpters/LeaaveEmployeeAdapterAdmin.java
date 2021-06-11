package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.databinding.LayoutRecyclerEmployeStatusBinding;
import com.example.employeetrackerapp.databinding.LeaveEmployeeAdminBinding;

import java.util.ArrayList;

public class LeaaveEmployeeAdapterAdmin extends RecyclerView.Adapter<LeaaveEmployeeAdapterAdmin.LeaveEmployeeViewHolder>
{
     Context context;
     ArrayList<EmployeeWorkingDetails> al;

     public LeaaveEmployeeAdapterAdmin(Context context,ArrayList<EmployeeWorkingDetails> al)
     {
         this.context=context;
         this.al=al;
     }

    @NonNull

    @Override
    public LeaveEmployeeViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
      LayoutRecyclerEmployeStatusBinding binding=LayoutRecyclerEmployeStatusBinding.inflate(LayoutInflater.from(context),parent,false);
        return new LeaveEmployeeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  LeaaveEmployeeAdapterAdmin.LeaveEmployeeViewHolder holder, int position)
    {
        EmployeeWorkingDetails emp = al.get(position);
        holder.binding.tvouttime.setVisibility(View.GONE);
        holder.binding.tvOut.setVisibility(View.GONE);
        holder.binding.tvintime.setText(emp.getDate());
        holder.binding.tvIn.setText(emp.getDayStatus());
        holder.binding.tvempname.setText(emp.getEmpName());
        holder.binding.tvempdepartment.setText(emp.getEmpDepartment());

    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    class LeaveEmployeeViewHolder extends RecyclerView.ViewHolder
     {
         LayoutRecyclerEmployeStatusBinding binding;

         public LeaveEmployeeViewHolder(LayoutRecyclerEmployeStatusBinding binding)
         {
             super(binding.getRoot());
            this.binding=binding;
         }

     }


}
