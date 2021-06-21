package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminActivity.EditEmployeeRecordActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.databinding.LayoutRecyclerAllEmployeeRecordBinding;

import java.util.ArrayList;

public class AllEmployeeAdapter  extends  RecyclerView.Adapter<AllEmployeeAdapter.AllEmployeeViewHolder>
{

    Context context;
    ArrayList<EmployeeRecord> al;


     public AllEmployeeAdapter(Context context,ArrayList<EmployeeRecord> al)
     {
         this.context=context;
         this.al=al;

     }

    @NonNull

    @Override
    public AllEmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutRecyclerAllEmployeeRecordBinding binding = LayoutRecyclerAllEmployeeRecordBinding.inflate(LayoutInflater.from(context),parent,false);


        return new AllEmployeeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllEmployeeAdapter.AllEmployeeViewHolder holder, int position)
    {
        EmployeeRecord emp = al.get(position);
        holder.binding.tvempname.setText(emp.getEmpName());
        holder.binding.tvempdepartment.setText(emp.getEmpDepartment());
        //holder.binding.empType.setText(emp.getEmpMember());
        if(emp.getEmpProfile()!=null)
        {
            Glide.with(context).load(emp.getEmpProfile()).into(holder.binding.profileImage);
        }

        holder.binding.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent in = new Intent(context, EditEmployeeRecordActivity.class);
               in.putExtra("employeeRecord",emp);
               context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    class AllEmployeeViewHolder extends RecyclerView.ViewHolder
    {
        LayoutRecyclerAllEmployeeRecordBinding binding;

        public  AllEmployeeViewHolder(LayoutRecyclerAllEmployeeRecordBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
