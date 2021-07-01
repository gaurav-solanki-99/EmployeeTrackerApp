package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.GMonth;
import com.example.employeetrackerapp.databinding.RequestApprovalApplicationAdminBinding;
import com.google.android.gms.tasks.OnSuccessListener;
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
    int monthcount=0;
    String month="";
    SharedPreferences sp=null;
    String adminName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RequestApprovalApplicationAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.tvLeaveApplication.setText("Halfday Application");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        adminName=sp.getString("empName",null);




        Intent in=getIntent();
        empl=(EmployeeHalfApplicationRecord) in.getSerializableExtra("ApproveEmp");

        binding.etLeaveSubject.setText(empl.getHalfdaySubject());
        binding.tvStartdate.setText(empl.getHalddayDate());
        binding.tvEndDtaeTime.setText(empl.getHaldaytime());
        binding.etLeaveDescription.setText(empl.getHalfdayDescription());

        int x=1;


        for(String w:empl.getHalddayDate().split("-",0)){
            System.out.println(w);
            Log.e(">>>>>>>>>>>>>>> ", " : - "+w);
            if(x==2)
            {
                monthcount=Integer.parseInt(w);
                Log.e(">>>>>>>>>>>>>>> ", "Month Index  : - "+monthcount);
            }
            x++;

        }
        month= GMonth.getStringMonth(monthcount);
        Log.e(">>>>>>>>>>>>>>> ", "Month   : - "+month);
        Log.e(">>>>>>>>>>>>>>> ", "AdminName    : - "+adminName);






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
                    onBackPressed();
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
                    onBackPressed();
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
                        userUpdate.put(rootkey+"/adminName",adminName);
                        hopperRef.updateChildren(userUpdate);
                        updateInWorking(empl1);




                        Toast.makeText(HalfdayApplicationApprovalActivity.this, "Approve", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else
                    {
//                        Toast.makeText(HalfdayApplicationApprovalActivity.this, "Data not match", Toast.LENGTH_SHORT).show();
                    }
                    myRef.child("EmployeeHalfApplicationRecord").removeEventListener(this);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(HalfdayApplicationApprovalActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateInWorking(EmployeeHalfApplicationRecord empl2)
    {
        int id = empl2.getEmpId();
        String empName=empl2.getEmpName();
        String empDepartment=empl2.getEmpDepartment();
        String ds=empl2.getHalddayDate();

        EmployeeWorkingDetails employeeWorking = new EmployeeWorkingDetails();
        employeeWorking.setEmpId(id);
        employeeWorking.setEmpName(empName);
        employeeWorking.setEmpDepartment(empDepartment);
        employeeWorking.setMounth(month);
        employeeWorking.setDate(ds);
        employeeWorking.setStartTime("");
        employeeWorking.setEndTime("");
        employeeWorking.setDayStatus("Halfday");
        employeeWorking.setBreakStartTime("");
        employeeWorking.setBreakEndTme("");
        employeeWorking.setBreakHours("");
        employeeWorking.setWorkHours("");

        myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(HalfdayApplicationApprovalActivity.this, "Approve Half", Toast.LENGTH_SHORT).show();
            }
        });

       // Toast.makeText(this, "Successfully Updated ", Toast.LENGTH_LONG).show();
    }
}
