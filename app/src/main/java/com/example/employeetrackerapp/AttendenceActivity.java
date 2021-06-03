package com.example.employeetrackerapp;

import android.app.ProgressDialog;
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

import com.example.employeetrackerapp.databinding.AttendenceBinding;
import com.example.employeetrackerapp.databinding.AttendenceShowBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendenceActivity extends AppCompatActivity
{
    AttendenceShowBinding binding;
    ArrayList<EmployeeAttendence> al;
    AttendenceAdapter adapter;
    SharedPreferences sp;
    String empName,empDepartment;
    int empId;


    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding=AttendenceShowBinding.inflate(LayoutInflater.from(this));

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);

        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);


        Toast.makeText(this, ""+empId, Toast.LENGTH_SHORT).show();



        al=new ArrayList<>();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {




                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                   EmployeeWorkingDetails emp= dataSnapshot.getValue(EmployeeWorkingDetails.class);
                   if(empId==emp.getEmpId())
                   {
                       Toast.makeText(AttendenceActivity.this, ""+emp.getEmpName(), Toast.LENGTH_SHORT).show();
                      al.add(new EmployeeAttendence(emp.getMounth(),emp.getDate(),emp.getStartTime(),emp.getEndTime(),emp.getDayStatus()));
                      System.out.println(al.size());
                      adapter.notifyDataSetChanged();
                   }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast.makeText(this, "ArrayList  "+al.size(), Toast.LENGTH_SHORT).show();




        /*
        al.add(new EmployeeAttendence("Mon","1April2020","10:00Am","7:00Pm"));
        al.add(new EmployeeAttendence("Tue","2April2020","10:00Am","7:00Pm"));
        al.add(new EmployeeAttendence("Wed","3April2020","10:00Am","7:00Pm"));
        al.add(new EmployeeAttendence("Thu","4April2020","10:00Am","7:00Pm"));
        al.add(new EmployeeAttendence("Fri","5April2020","10:00Am","7:00Pm"));
        al.add(new EmployeeAttendence("Sat","6April2020","10:00Am","7:00Pm"));
        */




        adapter=new AttendenceAdapter(AttendenceActivity.this,al);
        binding.attendencerv.setAdapter(adapter);
        binding.attendencerv.setLayoutManager(new LinearLayoutManager(AttendenceActivity.this));


        binding.backhomebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AttendenceActivity.this,DashboardActivity.class);
                startActivity(in);
                finish();
            }
        });



    }


}
