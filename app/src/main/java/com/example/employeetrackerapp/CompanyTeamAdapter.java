package com.example.employeetrackerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminActivity.SingleChatActivity;
import com.example.employeetrackerapp.databinding.SingleTeamMemberBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyTeamAdapter extends RecyclerView.Adapter<CompanyTeamAdapter.CompanyTeamViewHolder>
{
    ArrayList<EmployeeRecord> al ;
    Context context;
    String isActive="";


    CompanyTeamAdapter(Context context,ArrayList<EmployeeRecord> al)
    {

        this.context=context;
        this.al=al;

    }

    @NonNull

    @Override
    public CompanyTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        SingleTeamMemberBinding binding = SingleTeamMemberBinding.inflate(LayoutInflater.from(context),parent,false);

        return new CompanyTeamViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  CompanyTeamAdapter.CompanyTeamViewHolder holder, int position)
    {
        EmployeeRecord emp=al.get(position);

        if(position==0)
        {
            holder.binding.tvName.setTextColor(Color.parseColor("#2691E6"));

            holder.binding.profileImage.getLayoutParams().width=200;

        }

        checkEmployeeLogin(emp,holder.binding);

        holder.binding.tvName.setText(emp.getEmpName());
        holder.binding.tvDesignation.setText(emp.getPosition());




        if(emp.getEmpProfile()!=null)
        {
            Glide.with(context).load(emp.getEmpProfile()).error(R.drawable.ic_baseline_person_24).into(holder.binding.profileImage);

        }

        holder.binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, SingleChatActivity.class);


                if (holder.binding.loginImg.getVisibility() == View.VISIBLE) {
                    // Its visible

                    emp.setStatus("true");
                } else {
                    // Either gone or invisible
                    emp.setStatus("false");
                }
                in.putExtra("employeeRecord", emp);

                context.startActivity(in);
            }
        });


        System.out.println("Name "+emp.getEmpName()+"\n"+"Department "+ emp.empDepartment+"\n"+"Profile "+emp.getEmpProfile());
    }

    private void checkEmployeeLogin(EmployeeRecord emp2, SingleTeamMemberBinding binding)
    {

          boolean isactive= false;

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails empRecord = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(emp2.getEmpid()==empRecord.getEmpId()&&!empRecord.getStartTime().equals("")&&empRecord.getEndTime().equals("")&& empRecord.getDate().equalsIgnoreCase(GCurrentDateTime.getCurrentDate()))
                    {
                        binding.loginImg.setVisibility(View.VISIBLE);




                    }


                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    class CompanyTeamViewHolder extends RecyclerView.ViewHolder
    {
        SingleTeamMemberBinding binding;
        CompanyTeamViewHolder(SingleTeamMemberBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
