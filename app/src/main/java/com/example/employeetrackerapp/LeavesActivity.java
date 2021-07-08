package com.example.employeetrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.databinding.LeavesShowBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeavesActivity extends AppCompatActivity
{
    SharedPreferences sp;
    LeavesShowBinding binding;
    ArrayList<Leaves> al;
    LeavesAdapter adapter;
    int empId;
    String empName,empDepartment;
    FirebaseDatabase database;
    DatabaseReference myRef;

    String CurrentMonth;
    String CurrentYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=LeavesShowBinding.inflate(LayoutInflater.from(this));

        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        CurrentMonth=getCurrentMonth();
        CurrentYear=getCurrentYear();

                binding.addleavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToApplictionForm();
            }
        });


        getDatafromSharedPrefrence();//getSharedPrefrence data

        //tempDataStoreinEmployeeLeavesStatus();//to Store Temporary data in EmployeLeavesStatus

        getDatafromemployeeWorkingDetails();




        al=new ArrayList<>();


        myRef.child("EmployeLeavesApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                al.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    EmployeLeavesApplicationRecord emp = dataSnapshot.getValue(EmployeLeavesApplicationRecord.class);
                    if(empId==emp.getEmpId()&&empName.equals(emp.getEmpName()))
                    {


                        try {
                            ArrayList<Date> dates=new ArrayList<>();
                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            Date startDate = (Date) formatter.parse(emp.getLeaveStartDate());
                            Date endDate = (Date) formatter.parse(emp.getLeaveEndDate());
                            long interval = 24*1000 * 60 * 60; // 1 hour in millis
                            long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
                            long curTime = startDate.getTime();
                            while (curTime <= endTime) {
                                dates.add(new Date(curTime));
                                curTime += interval;
                            }
                            for(int i=0;i<dates.size();i++){
                                Date lDate =(Date)dates.get(i);
                                String ds = formatter.format(lDate);
                                System.out.println(" Date is ..." + ds);

                                al.add(new Leaves("Date",ds,emp.getLeaveSuject(),emp.getLeaveStatus(),emp.getLeaveStartDate(),emp.getLeaveEndDate(),emp.getLeaveDescription()));
                                adapter.notifyDataSetChanged();

                            }

                        }catch (Exception e)
                        {
                            Toast.makeText(LeavesActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                        }



                    }
                    else
                    {
//                        Toast.makeText(LeavesActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LeavesActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });





        adapter=new LeavesAdapter(LeavesActivity.this,al);
        binding.leavesrv.setAdapter(adapter);
        binding.leavesrv.setLayoutManager(new LinearLayoutManager(LeavesActivity.this));




        binding.backhomebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });


    }

    private String getCurrentYear()
    {
        return  ""+Calendar.getInstance().get(Calendar.YEAR);
    }

    private String getCurrentMonth()
    {
        String month="";
        int m = Calendar.getInstance().get(Calendar.MONTH)+1;

        switch (m)
        {
            case 1:
                month="January";
                break;
            case 2:
                month="Feburary";
                break;
            case 3:
                month="March";
                break;
            case 4:
                month="April";
                break;
            case 5:
                month="May";
                break;
            case 6:
                month="June";
                break;
            case 7:
                month="July";
                break;
            case 8:
                month="August";
                break;
            case 9:
                month="September";
                break;
            case 10:
                month="Octomber";
                break;
            case 11:
                month="November";
                break;
            case 12:
                month="December";
                break;

        }


        return month;
    }

    private void getDatafromemployeeWorkingDetails()
    {


        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            int annualleavescount=0;
            int mounthleavescount=0;
            int totalleavescount=0;


            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(empId==emp.getEmpId()&&empName.equals(emp.getEmpName()))
                    {

                       if(emp.getDayStatus().equals("Absent"))
                       {

                           totalleavescount++;
                           binding.tvtotalleaves.setText(""+totalleavescount);

                           if(emp.getDate().endsWith(CurrentYear))
                           {
                               annualleavescount++;
                               binding.tvannualleaves.setText(""+annualleavescount);
                           }

                           if(emp.getMounth().equals(CurrentMonth))
                           {
                               mounthleavescount++;
                               binding.tvmonthleaves.setText(""+mounthleavescount);
                           }
                       }



                    }


                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(LeavesActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToApplictionForm()
    {
        Intent in = new Intent(getApplicationContext(),LeavesRequestActivity.class);
        startActivity(in);
    }



    private void tempDataStoreinEmployeeLeavesStatus()
    {
        try {


            EmployeLeavesStatus emp = new EmployeLeavesStatus(empId, empName, empDepartment, 30, 5, 2);
            myRef.child("EmployeLeavesStatus").setValue(emp);

        }catch (Exception e)
        {
            Toast.makeText(this, "Exception "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getDatafromSharedPrefrence()
    {
        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);
    }
}
