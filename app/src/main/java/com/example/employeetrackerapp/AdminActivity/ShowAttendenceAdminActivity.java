package com.example.employeetrackerapp.AdminActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AttendenceActivity;
import com.example.employeetrackerapp.AttendenceAdapter;
import com.example.employeetrackerapp.Constants;
import com.example.employeetrackerapp.EmployeeAttendence;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.GCurrentDateTime;
import com.example.employeetrackerapp.GMonth;
import com.example.employeetrackerapp.Parag.MonthYearPickerDialog;
import com.example.employeetrackerapp.Parag.MonthYearPickerDialogFragment;
import com.example.employeetrackerapp.databinding.ShowAttendenceAdminActivityBinding;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowAttendenceAdminActivity extends AppCompatActivity
{

    ShowAttendenceAdminActivityBinding binding;
    ArrayList<EmployeeAttendence> al;
    AttendenceAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String selectYear,selectMonth;
    EmployeeRecord singleEmp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ShowAttendenceAdminActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        Intent in  = getIntent();
         singleEmp  = (EmployeeRecord)in.getSerializableExtra("SingleEmployeeRecord");
        al=new ArrayList<>();

       selectMonth=GCurrentDateTime.getCurrentMonth();
       selectYear=""+Calendar.getInstance().get(Calendar.YEAR);
       binding.etchosemonth.setText(selectYear+"-"+selectMonth);
       filterRecord();
       





        int yearSelected;
        int monthSelected;

//Set default values
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);

        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected);

        binding.etchosemonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), null);
            }
        });

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                // do something
                binding.etchosemonth.setText(year+"-"+ GMonth.getStringMonth(monthOfYear+1));
                selectMonth= GMonth.getStringMonth(monthOfYear+1);
                selectYear=""+year;
                filterRecord();
            }
        });







    }

    private void filterRecord()
    {
        al.clear();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int attendencecount = 0;


                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp= dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(singleEmp.getEmpid()==emp.getEmpId()&&emp.getMounth().equalsIgnoreCase(selectMonth)&&emp.getDate().endsWith(selectYear))
                    {
                        if (emp.getDayStatus().equals("Present")) {
                            attendencecount++;
                        }
                        Toast.makeText(ShowAttendenceAdminActivity.this, ""+emp.getEmpName(), Toast.LENGTH_SHORT).show();

                        al.add(new EmployeeAttendence(emp.getMounth(),emp.getDate(), Constants.formateDate(emp.getStartTime()),Constants.formateDate(emp.getEndTime()),emp.getDayStatus(),emp.getWorkHours()));

                        System.out.println(al.size());
                        adapter.notifyDataSetChanged();
                        myRef.child("EmployeeWorkingDetails").removeEventListener(this);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        adapter=new AttendenceAdapter(ShowAttendenceAdminActivity.this,al);
        binding.rvemplist.setAdapter(adapter);
        binding.rvemplist.setLayoutManager(new LinearLayoutManager(ShowAttendenceAdminActivity.this));

    }


}
