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

public class ApprovalApplicationActivity  extends AppCompatActivity
{

 RequestApprovalApplicationAdminBinding binding;
 FirebaseDatabase database;
 DatabaseReference myRef;
 EmployeLeavesApplicationRecord empl;
 String status="",remark="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RequestApprovalApplicationAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();




        Intent in=getIntent();
        empl=(EmployeLeavesApplicationRecord) in.getSerializableExtra("ApproveEmp");
        Toast.makeText(this, ""+ empl.getLeaveStartDate(), Toast.LENGTH_LONG).show();
        binding.etLeaveSubject.setText(empl.getLeaveSuject());
        binding.tvStartdate.setText(empl.getLeaveStartDate());
        binding.tvEndDtaeTime.setText(empl.getLeaveEndDate());
        binding.etLeaveDescription.setText(empl.getLeaveDescription());

        binding.btnApproveLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="Approve";
                remark=binding.etremark.getText().toString();
                if(TextUtils.isEmpty(remark))
                {
                    binding.etremark.setError("Remark Mandatory");
                }
                else
                {
                    updateLeaveStatus();

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
                    binding.etremark.setError("Remark Mandatory");
                }
                else
                {
                    updateLeaveStatus();

                }


            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senUserALLRequestActivity();
            }
        });



    }



    private void updateLeaveStatus()
    {
        myRef.child("EmployeLeavesApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    EmployeLeavesApplicationRecord empl1=dataSnapshot.getValue(EmployeLeavesApplicationRecord.class);
                    if(empl.getLeaveStartDate().equals(empl1.getLeaveStartDate())&&empl.getLeaveEndDate().equals(empl1.getLeaveEndDate())&& empl.getEmpId()==empl1.getEmpId())
                    {
                        String rootkey =dataSnapshot.getKey();
                        DatabaseReference hopperRef = myRef.child("EmployeLeavesApplicationRecord");
                        Map<String,Object> userUpdate = new HashMap<>();
                        userUpdate.put(rootkey+"/leaveStatus",status);
                        userUpdate.put(rootkey+"/leaveRemark",remark);
                        hopperRef.updateChildren(userUpdate);



                        Toast.makeText(ApprovalApplicationActivity.this, "Approve", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else
                    {
                        Toast.makeText(ApprovalApplicationActivity.this, "Data not match", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

                Toast.makeText(ApprovalApplicationActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void senUserALLRequestActivity()
    {
            startActivity(new Intent(ApprovalApplicationActivity.this, AllRequestListActivity.class));
            finish();
    }
}
