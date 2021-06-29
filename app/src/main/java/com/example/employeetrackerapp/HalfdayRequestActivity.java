package com.example.employeetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.employeetrackerapp.databinding.ActivityHalfdayRequestBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HalfdayRequestActivity extends AppCompatActivity {



    ActivityHalfdayRequestBinding binding;
    SharedPreferences sp;
    int empId;
    String empName;
    String empDepartment;
    String empProfile;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHalfdayRequestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        getDataFromSharedPrefrence();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Toast.makeText(this, ""+empName, Toast.LENGTH_SHORT).show();

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        binding.btnApplyForHalfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForHalf();
            }
        });



    }

    private void applyForHalf()
    {
        String halfdaySubjecct = binding.etHalfdaySubject.getText().toString();
        String halfdayDescription = binding.etHalfdayDescription.getText().toString();
        String startDate = binding.tvStartdate.getText().toString();
        String endTime = binding.tvEndDtaeTime.getText().toString();
        String status="Pending";

        if(TextUtils.isEmpty(halfdaySubjecct)&&TextUtils.isEmpty(halfdayDescription)&&TextUtils.isEmpty(startDate)&&TextUtils.isEmpty(endTime))
        {
            Toast.makeText(this, "All Fields are mandatory, please check it", Toast.LENGTH_SHORT).show();
        }
        else {
            EmployeeHalfApplicationRecord empLeave = new EmployeeHalfApplicationRecord(empId, empName, empDepartment, halfdaySubjecct, halfdayDescription, startDate, endTime, status);
           empLeave.setHalfdayRemark("");
           empLeave.setProfile(empProfile);

            myRef.child("EmployeeHalfApplicationRecord").push().setValue(empLeave);


            Toast.makeText(this, "Application send SuucessFully", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataFromSharedPrefrence()
    {
        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);
        empProfile=sp.getString("empProfile",null);


    }
}