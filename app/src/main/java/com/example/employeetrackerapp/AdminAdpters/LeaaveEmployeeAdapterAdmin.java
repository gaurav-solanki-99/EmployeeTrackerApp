package com.example.employeetrackerapp.AdminAdpters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.databinding.LayoutRecyclerEmployeStatusBinding;
import com.example.employeetrackerapp.databinding.LeaveEmployeeAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        TakeDetaisl(emp,holder.binding.profileImage);
        holder.binding.tvouttime.setVisibility(View.GONE);
        holder.binding.tvOut.setVisibility(View.GONE);
        holder.binding.tvintime.setText(emp.getDate());
        holder.binding.tvIn.setText(emp.getDayStatus());
        holder.binding.tvempname.setText(emp.getEmpName());
        holder.binding.tvempdepartment.setText(emp.getEmpDepartment());

    }

    private void TakeDetaisl(EmployeeWorkingDetails emp, CircleImageView profileImage)
    {
        ProgressDialog pd =new ProgressDialog(context);
        pd.setMessage("Loadin......");
        pd.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference();

        myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    EmployeeRecord employeeRecord = dataSnapshot.getValue(EmployeeRecord.class);
                    if(employeeRecord.getEmpid()==emp.getEmpId()&&employeeRecord.getEmpName().equalsIgnoreCase(emp.getEmpName()))
                    {

                        Glide.with(context.getApplicationContext()).load(employeeRecord.getEmpProfile()).error(R.drawable.ic_baseline_person_24).into(profileImage);

                    }


                }

                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
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
