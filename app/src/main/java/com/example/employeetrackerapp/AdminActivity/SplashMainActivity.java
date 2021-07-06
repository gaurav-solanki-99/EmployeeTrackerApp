package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.databinding.SlashscreenadminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashMainActivity  extends AppCompatActivity
{
    SlashscreenadminBinding binding;
    SharedPreferences sp;
    String isAllRecordFound="false";
    String isAllSet="";
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= SlashscreenadminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        new MainSplashNext().start();
//        checkEmployeeLogin();


    }


      class MainSplashNext extends Thread
      {
          @Override
          public void run() {
              super.run();

              try {
                  Thread.sleep(3000);


                  startActivity(new Intent(SplashMainActivity.this,LoginMainActivity.class));
                  finish();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }

    private void checkEmployeeLogin()
    {

        String empMember= sp.getString("empMember",null);

        String empName=sp.getString("empName",null);
        int empId=sp.getInt("empId",0);
        EmployeeRecord[] emp2=new EmployeeRecord[1];

        myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                    if(empId==emp.getEmpid()&&empName.equalsIgnoreCase(emp.getEmpName()))
                    {
                        isAllSet= emp.getIsAllFill();
                        break;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        if(empMember!=null)
        {
            if(empMember.equalsIgnoreCase("Employee"))
            {

                if(isAllSet.equalsIgnoreCase("true"))
                {

                    myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                                if(empId==emp.getEmpid()&&empName.equalsIgnoreCase(emp.getEmpName()))
                                {  SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("empId",emp.getEmpid());
                                    editor.putString("empName",emp.getEmpName());
                                    editor.putString("empPhone",emp.getEmpPhone());
                                    editor.putString("empEmail",emp.getEmpEmail());
                                    editor.putString("empAddress",emp.getEmpAdress());
                                    editor.putString("empDepartment",emp.getEmpDepartment());
                                    editor.putString("empDOB",emp.getEmpDOB());
                                    editor.putString("empMember",emp.getEmpMember());
                                    editor.putString("empProfile",emp.getEmpProfile());
                                    editor.putString("isAllset",emp.getIsAllFill());
                                    editor.commit();
                                    startActivity(new Intent(SplashMainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });



                }else if(isAllSet.equalsIgnoreCase("false"))
                {

                    myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {


                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {


                                EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                                if(empId==emp.getEmpid()&&empName.equalsIgnoreCase(emp.getEmpName()))
                                {
//                                    emp2[0] = emp;
                                    Intent in = new Intent(SplashMainActivity.this,AllRecordSetActivity.class);
                                    in.putExtra("employeeRecord",emp);
                                    startActivity(in);
                                    finish();
                                    break;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
//                    Intent in = new Intent(LoginMainActivity.this,AllRecordSetActivity.class);
//                    in.putExtra("employeeRecord",emp2[0]);
//                    startActivity(in);
//                    finish();

                }





            }
            if(empMember.equalsIgnoreCase("Admin"))
            {
                startActivity(new Intent(SplashMainActivity.this, AdminDashboardActivity.class));
                finish();

            }
        }
    }

    }


