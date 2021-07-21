package com.example.employeetrackerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.employeetrackerapp.AdminActivity.ActivieEmployeeActivity;
import com.example.employeetrackerapp.databinding.ActivityLeavesRequestBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LeavesRequestActivity extends AppCompatActivity {

    ActivityLeavesRequestBinding binding;
    SharedPreferences sp;
    int empId;
    String empName,empDepartment,empProfile,empType;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public String dateSeleted = "";


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
        binding.tvEnd.setText("EndDate");



        binding.tvStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(LeavesRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateSeleted = dayOfMonth + "-" + (month + 1) + "-" + year;
                        DateFormat parser = new SimpleDateFormat("dd-M-yyyy");
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date convertedDate = null;
                        try {
                            convertedDate = parser.parse(dateSeleted);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputDate = formatter.format(convertedDate);

                        binding.tvStartdate.setText(outputDate);



                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });



        binding.tvEndDtaeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(LeavesRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateSeleted = dayOfMonth + "-" + (month + 1) + "-" + year;
                        DateFormat parser = new SimpleDateFormat("dd-M-yyyy");
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date convertedDate = null;
                        try {
                            convertedDate = parser.parse(dateSeleted);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputDate = formatter.format(convertedDate);

                        binding.tvEndDtaeTime.setText(outputDate);



                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });




        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLeavesActivity();
            }
        });

        binding.btnApplyForLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(LeavesRequestActivity.this);
                ad.setTitle("Apply");
                ad.setMessage("Are you sure to send Applicatipn");
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        applyForLeave();
                        onBackPressed();
                    }
                });
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });
                ad.show();
            }
        });


    }

    private void applyForLeave()//Apply For Apply
    {


        String leaveSubjecct = binding.etLeaveSubject.getText().toString();
        String leaveDescription = binding.etLeaveDescription.getText().toString();
        String startDate = binding.tvStartdate.getText().toString();
        String  endDate = binding.tvEndDtaeTime.getText().toString();
        String status="Pending";

        if(TextUtils.isEmpty(leaveSubjecct)&&TextUtils.isEmpty(leaveDescription)&&TextUtils.isEmpty(startDate)&&TextUtils.isEmpty(endDate))
        {
            Toast.makeText(this, "All Fields are mandatory, please check it", Toast.LENGTH_SHORT).show();
        }
        else {
           EmployeLeavesApplicationRecord empLeave = new EmployeLeavesApplicationRecord(empId, empName, empDepartment, leaveSubjecct, leaveDescription, startDate, endDate, status,empProfile);
            empLeave.setLeaveRemark("");
            empLeave.setAdminName("");
            empLeave.setEmpType(empType);
           myRef.child("EmployeLeavesApplicationRecord").push().setValue(empLeave);


            Toast.makeText(this, "Application send SuucessFully", Toast.LENGTH_SHORT).show();
        }

    }

    private void getEmployeeDetails()// from shared preference
    {
        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);
        empProfile=sp.getString("empProfile",null);
        empType=sp.getString("empMember",null);


    }

    private void sendToLeavesActivity()
    {
       onBackPressed();
    }
}