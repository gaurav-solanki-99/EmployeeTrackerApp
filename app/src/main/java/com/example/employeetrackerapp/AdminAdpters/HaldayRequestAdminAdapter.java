package com.example.employeetrackerapp.AdminAdpters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminActivity.ApprovalApplicationActivity;
import com.example.employeetrackerapp.AdminActivity.HalfdayApplicationApprovalActivity;
import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.TotitleClass;
import com.example.employeetrackerapp.databinding.LayoutRecyclerLaeveRequestAdminBinding;

import java.util.ArrayList;

public class HaldayRequestAdminAdapter extends RecyclerView.Adapter<HaldayRequestAdminAdapter.HalfdatyRequestViewHolder> {
    Context context;
    ArrayList<EmployeeHalfApplicationRecord> al;
    String HalfdayStatus;

    public HaldayRequestAdminAdapter(Context context, ArrayList<EmployeeHalfApplicationRecord> al,String HalfdayStatus) {
        this.al = al;
        this.context = context;
        this.HalfdayStatus=HalfdayStatus;
    }

    @NonNull

    @Override
    public HalfdatyRequestViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType)
    {
        LayoutRecyclerLaeveRequestAdminBinding binding =LayoutRecyclerLaeveRequestAdminBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HalfdatyRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  HaldayRequestAdminAdapter.HalfdatyRequestViewHolder holder, int position) {

         EmployeeHalfApplicationRecord empl = al.get(position);
        holder.binding.tvempname.setText(TotitleClass.convertToTitleCaseIteratingChars(empl.getEmpName()));
        holder.binding.tvempdepartment.setText(TotitleClass.convertToTitleCaseIteratingChars(empl.getEmpDepartment()));
        holder.binding.requestdate.setText(empl.getHalddayDate());
        Glide.with(context).load(empl.getProfile()).error(R.drawable.ic_baseline_person_24).into(holder.binding.profileImage);

            holder.binding.btnshowrequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in =new Intent(context, HalfdayApplicationApprovalActivity.class);
                    in.putExtra("ApproveEmp",empl);
                    in.putExtra("HalfdayStatus",HalfdayStatus);
                    context.startActivity(in);
                    ((Activity)context).finish();
                }
            });


    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    public class HalfdatyRequestViewHolder extends RecyclerView.ViewHolder {
        LayoutRecyclerLaeveRequestAdminBinding binding;

        public HalfdatyRequestViewHolder(LayoutRecyclerLaeveRequestAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
