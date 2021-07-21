package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.R;
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

//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim);
//        binding.imageView.setAnimation(animation);


        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        new MainSplashNext().start();




    }


      class MainSplashNext extends Thread
      {
          @Override
          public void run() {
              super.run();

              try {
                  Thread.sleep(2000);

                  checkEmployeeLogin();

              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }


    private void checkEmployeeLogin() {


        String empMember = sp.getString("empMember", null);

        String empName = sp.getString("empName", null);
        int empId = sp.getInt("empId", 0);
        isAllSet = sp.getString("isAllset", null);

        EmployeeRecord[] emp2 = new EmployeeRecord[1];


        if (empMember != null) {


            System.out.println(">>>>>>>>>>>>>>First Check Member " + empMember);
            if (empMember.equalsIgnoreCase("Employee")) {
                System.out.println(">>>>>>>>>>>>>>Second Check isAllser " + isAllSet);


                if (isAllSet.equalsIgnoreCase("true")) {
                    System.out.println(">>>>>>>>>>>>>>Second Found isAllser " + isAllSet);

                    startActivity(new Intent(SplashMainActivity.this, DashboardActivity.class));
                    finish();

                } else if (isAllSet.equalsIgnoreCase("false")) {
                    System.out.println(">>>>>>>>>>>>>>Second Check isAllSet " + isAllSet);

                    myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                                EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                                if (empId == emp.getEmpid() && empName.equalsIgnoreCase(emp.getEmpName())) {
//                                    emp2[0] = emp;
                                    Intent in = new Intent(SplashMainActivity.this, AllRecordSetActivity.class);
                                    in.putExtra("employeeRecord", emp);
                                    startActivity(in);
                                    finish();
                                    break;

                                }
                            }

                            myRef.child("EmployeeRecord").removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    Intent in = new Intent(LoginMainActivity.this,AllRecordSetActivity.class);
//                    in.putExtra("employeeRecord",emp2[0]);
//                    startActivity(in);
//                    finish();

                }


            }
            if (empMember.equalsIgnoreCase("Admin")||empMember.equalsIgnoreCase("SuperAdmin")) {

                startActivity(new Intent(SplashMainActivity.this, AdminDashboardActivity.class));
                finish();

            }
            if (empMember.equalsIgnoreCase("SubAdmin")) {

                startActivity(new Intent(SplashMainActivity.this, SubAdminDashboardActivity.class));
                finish();

            }
        }
        else
        {

                  startActivity(new Intent(SplashMainActivity.this,LoginMainActivity.class));
                  finish();
        }
    }


    }


