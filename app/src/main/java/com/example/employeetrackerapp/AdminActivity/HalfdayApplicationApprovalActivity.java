package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.databinding.RequestApprovalApplicationAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HalfdayApplicationApprovalActivity  extends AppCompatActivity
{
    RequestApprovalApplicationAdminBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EmployeeHalfApplicationRecord empl;
    String status="",remark="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RequestApprovalApplicationAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.tvLeaveApplication.setText("Halfday Application");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();




        Intent in=getIntent();
        empl=(EmployeeHalfApplicationRecord) in.getSerializableExtra("ApproveEmp");
        Toast.makeText(this, ""+ empl.getHalddayDate(), Toast.LENGTH_LONG).show();
        binding.etLeaveSubject.setText(empl.getHalfdaySubject());
        binding.tvStartdate.setText(empl.getHalddayDate());
        binding.tvEndDtaeTime.setText(empl.getHaldaytime());
        binding.etLeaveDescription.setText(empl.getHalfdayDescription());

        binding.btnApproveLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="Approve";
                remark=binding.etremark.getText().toString();
                if(TextUtils.isEmpty(remark))
                {
                    binding.etremark.setError("Remark Mandadtory");
                }
                else
                {
                    updateHalfdayStatus();
                }

            }
        });

        binding.btnConcelForLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="Reject";
                remark=binding.etremark.getText().toString();
                if(TextUtils.isEmpty(remark))
                {
                    binding.etremark.setError("Remark Mandadtory");
                }
                else
                {
                    updateHalfdayStatus();
                }


            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senUserALLHalfdayActivity();
            }
        });


    }

    private void senUserALLHalfdayActivity()
    {
        startActivity(new Intent(HalfdayApplicationApprovalActivity.this, AllHalfdayRequestListActivity.class));
        finish();
    }

    private void updateHalfdayStatus()
    {
        myRef.child("EmployeeHalfApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    EmployeeHalfApplicationRecord empl1=dataSnapshot.getValue(EmployeeHalfApplicationRecord.class);
                    if(empl.getHalddayDate().equals(empl1.getHalddayDate())&&empl.getHaldaytime().equals(empl1.getHaldaytime())&& empl.getEmpId()==empl1.getEmpId())
                    {
                        String rootkey =dataSnapshot.getKey();
                        DatabaseReference hopperRef = myRef.child("EmployeeHalfApplicationRecord");
                        Map<String,Object> userUpdate = new HashMap<>();
                        userUpdate.put(rootkey+"/halfdayStatus",status);
                        userUpdate.put(rootkey+"/halfdayRemark",remark);
                        hopperRef.updateChildren(userUpdate);



                        Toast.makeText(HalfdayApplicationApprovalActivity.this, "Approve", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else
                    {
                        Toast.makeText(HalfdayApplicationApprovalActivity.this, "Data not match", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(HalfdayApplicationApprovalActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
