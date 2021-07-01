package com.example.employeetrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.employeetrackerapp.AdminActivity.SplashMainActivity;
import com.example.employeetrackerapp.databinding.LoginpageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    LoginpageBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String empName,empMobile;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginpageBinding.inflate(LayoutInflater.from(this));

        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        checkEmployeeLogin();



       //insertTestingRecord();//temporary record for test application


        binding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EmployeeAuthentication();
            }
        });


        binding.btnadminapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SplashMainActivity.class));
                finish();
            }
        });



    }

    private void checkEmployeeLogin()
    {
        String empName= sp.getString("empName",null);

        if(empName!=null)
          sendEmployeeToDashboard();

    }


    private void insertTestingRecord() {


        EmployeeRecord employeeRecord = new EmployeeRecord(101, "GauravSolanki", "gaurav@gmail.com", "987654321", "shubham palace,indore", "25/06/1999", "Application Development", "21/05/2021", null, "12345","Employee");
       myRef.child("EmployeeRecord").push().setValue(employeeRecord);
        Toast.makeText(MainActivity.this,"Data is saved",Toast.LENGTH_LONG).show();
    }


    private void EmployeeAuthentication() {

         empName= binding.employeename.getText().toString();
         empMobile=binding.employeephone.getText().toString();
        if(empName.equals("")&&empMobile.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please insert the fields to login", Toast.LENGTH_SHORT).show();
        }
        else
        {
            myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot)
                {
                    String details="";
                    boolean status=false;
                    for(DataSnapshot cSnapshot :snapshot.getChildren())
                    {
                        EmployeeRecord employeeRecord=cSnapshot.getValue(EmployeeRecord.class);
                        details=details+"Username :"+employeeRecord.getEmpName()+" Mobile :"+employeeRecord.getEmpName()+"\n";
                        // binding.tvtext.setText("Username :"+user.getName()+" Address :"+user.getAddress()+"\n");
                      //  Toast.makeText(MainActivity.this, ""+employeeRecord.getEmpName()+"\n"+employeeRecord.empPhone, Toast.LENGTH_SHORT).show();
                        if(empMobile.equalsIgnoreCase(employeeRecord.getEmpPhone())&&empName.equalsIgnoreCase(employeeRecord.getEmpName()))
                        {
                            Toast.makeText(MainActivity.this, "Authenticate user", Toast.LENGTH_SHORT).show();
                            sendEmployeeToDashboard();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("empId",employeeRecord.getEmpid());
                            editor.putString("empName",employeeRecord.getEmpName());
                            editor.putString("empPhone",employeeRecord.getEmpPhone());
                            editor.putString("empEmail",employeeRecord.getEmpEmail());
                            editor.putString("empAddress",employeeRecord.getEmpAdress());
                            editor.putString("empDepartment",employeeRecord.getEmpDepartment());
                            editor.putString("empDOB",employeeRecord.getEmpDOB());

                          editor.commit();

                            status=true;
                            break;



                        }


                    }
                    if(!status)
                    {
                        Toast.makeText(MainActivity.this, "Not a authentic user", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, ""+empName+"\n"+empMobile, Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error)
                {
                    Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

    private void sendEmployeeToDashboard() {


        Intent in = new Intent(MainActivity.this,DashboardActivity.class);
        startActivity(in);
        finish();
    }

}