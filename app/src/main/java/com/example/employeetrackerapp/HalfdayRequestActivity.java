package com.example.employeetrackerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.employeetrackerapp.databinding.ActivityHalfdayRequestBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HalfdayRequestActivity extends AppCompatActivity {



    ActivityHalfdayRequestBinding binding;
    SharedPreferences sp;
    int empId;
    String empName;
    String empDepartment;
    String empProfile;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String dateSeleted="";
    String am_pm="";
    String empType;


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
        binding.tvStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(HalfdayRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);


                if (c.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (c.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(HalfdayRequestActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                binding.tvEndDtaeTime.setText(hourOfDay + ":" + minute+":"+am_pm);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        binding.btnApplyForHalfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder ad = new AlertDialog.Builder(HalfdayRequestActivity.this);
                ad.setTitle("Apply");
                ad.setMessage("Are you sure to send Applicatipn");
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        applyForHalf();
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
           empLeave.setAdminName("");
           empLeave.setEmpType(empType);

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
        empType=sp.getString("empMember",null);


    }
}