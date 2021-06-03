package com.example.employeetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.employeetrackerapp.databinding.ActivityLeavesRequestBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeavesRequestActivity extends AppCompatActivity {

    ActivityLeavesRequestBinding binding;
    SharedPreferences sp;
    int empId;
    String empName,empDepartment;
    FirebaseDatabase database;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLeavesRequestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        getEmployeeDetails();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();





        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLeavesActivity();
            }
        });

        binding.btnApplyForLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForLeave();
            }
        });

        Toast.makeText(this, ""+empName, Toast.LENGTH_SHORT).show();
    }

    private void applyForLeave()//Apply For Apply
    {
        String leaveSubjecct = binding.etLeaveSubject.getText().toString();
        String leaveDescription = binding.etLeaveDescription.getText().toString();
        String startDate = binding.tvStartdate.getText().toString();
        String endDate = binding.tvEndDtaeTime.getText().toString();
        String status="Pending";

        if(TextUtils.isEmpty(leaveSubjecct)&&TextUtils.isEmpty(leaveDescription)&&TextUtils.isEmpty(startDate)&&TextUtils.isEmpty(endDate))
        {
            Toast.makeText(this, "All Fields are mandatory, please check it", Toast.LENGTH_SHORT).show();
        }
        else {
            EmployeLeavesApplicationRecord empLeave = new EmployeLeavesApplicationRecord(empId, empName, empDepartment, leaveSubjecct, leaveDescription, startDate, endDate, status);
            myRef.child("EmployeLeavesApplicationRecord").push().setValue(empLeave);


            Toast.makeText(this, "Application send SuucessFully", Toast.LENGTH_SHORT).show();
        }

    }

    private void getEmployeeDetails()// from shared preference
    {
        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);

    }

    private void sendToLeavesActivity()
    {
        Intent intent = new Intent(getApplicationContext(),LeavesActivity.class);
        startActivity(intent);
        finish();
    }
}