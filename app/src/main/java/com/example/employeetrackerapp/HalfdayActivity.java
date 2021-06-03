package com.example.employeetrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.employeetrackerapp.databinding.ActivityHalfdayBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HalfdayActivity extends AppCompatActivity {

    ActivityHalfdayBinding binding;
    SharedPreferences sp;
    int empId;
    String empName,empDepartment;
    ArrayList<Halfday> al;
    HalfdayAdapter adapter;
    String CurrentMonth;
    String CurrentYear;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHalfdayBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        getDetailsFromSharedPrefrence();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();


        CurrentMonth=getCurrentMonth();
        CurrentYear=getCurrentYear();

        getDatafromemployeeWorkingDetails();


        al=new ArrayList<>();

        myRef.child("EmployeeHalfApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeeHalfApplicationRecord emphalfday=dataSnapshot.getValue(EmployeeHalfApplicationRecord.class);
                    if(empId==emphalfday.getEmpId()&&empName==empName)
                    {
                        Toast.makeText(HalfdayActivity.this, "Record Found ", Toast.LENGTH_SHORT).show();
                        al.add(new Halfday("Date",emphalfday.getHalddayDate(),emphalfday.getHalfdaySubject(),emphalfday.getHalfdayStatus()));
                        adapter.notifyDataSetChanged();

                    }
                    else
                    {
                        Toast.makeText(HalfdayActivity.this, "Record not found ", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {
                Toast.makeText(HalfdayActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });






        adapter=new HalfdayAdapter(HalfdayActivity.this,al);
        binding.halfdayrv.setAdapter(adapter);
        binding.halfdayrv.setLayoutManager(new LinearLayoutManager(HalfdayActivity.this));














        binding.addhalfdaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHalfdayRequestActivity();
            }
        });


        binding.backhomebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                finish();
            }
        });
        Toast.makeText(this, ""+empName, Toast.LENGTH_SHORT).show();


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

                        if(emp.getDayStatus().equals("Halfday"))
                        {

                            totalleavescount++;
                            binding.tvtotalleaves.setText(""+totalleavescount);

                            if(emp.getDate().endsWith(CurrentYear))
                            {
                                annualleavescount++;
                                binding.tvannualhalfday.setText(""+annualleavescount);
                            }

                            if(emp.getMounth().equals(CurrentMonth))
                            {
                                mounthleavescount++;
                                binding.tvmonthhalday.setText(""+mounthleavescount);
                            }
                        }



                    }


                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
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

    private void sendUserToHalfdayRequestActivity()
    {
        startActivity(new Intent(getApplicationContext(),HalfdayRequestActivity.class));
    }

    private void getDetailsFromSharedPrefrence()
    {
        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);

    }
}