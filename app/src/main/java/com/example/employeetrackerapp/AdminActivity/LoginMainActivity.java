package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.MainActivity;
import com.example.employeetrackerapp.databinding.EmployeeloginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginMainActivity extends AppCompatActivity
{

    EmployeeloginBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String phone,password;
    SharedPreferences sp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=EmployeeloginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
       database=FirebaseDatabase.getInstance();
       myRef=database.getReference();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);

        checkEmployeeLogin();




        binding.btnemplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendUserToDashborad();

            }
        });

    }

    private void checkEmployeeLogin() {
        String empMember= sp.getString("empMember",null);

        if(empMember!=null)
        {
            if(empMember.equalsIgnoreCase("Employee"))
            {
                startActivity(new Intent(LoginMainActivity.this, DashboardActivity.class));
                finish();
            }
            if(empMember.equalsIgnoreCase("Admin"))
            {
                startActivity(new Intent(LoginMainActivity.this, AdminDashboardActivity.class));
                finish();

            }
        }
            //sendEmployeeToDashboard();
    }

    private void sendUserToDashborad()
    {
       phone=binding.etphone.getText().toString();
       password=binding.etpassword.getText().toString();



       if(TextUtils.isEmpty(phone)&&TextUtils.isEmpty(password))
       {
           Toast.makeText(this, "Filleds are empty", Toast.LENGTH_SHORT).show();
       }
       else {


           myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                       EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);

                       if (phone.equalsIgnoreCase(emp.getEmpPhone()) && password.equalsIgnoreCase(emp.getEmpPassword())) {
                           if (emp.getEmpMember().equalsIgnoreCase("Employee")) {
                               Toast.makeText(LoginMainActivity.this, "Employee", Toast.LENGTH_SHORT).show();
                               SharedPreferences.Editor editor = sp.edit();
                               editor.putInt("empId",emp.getEmpid());
                               editor.putString("empName",emp.getEmpName());
                               editor.putString("empPhone",emp.getEmpPhone());
                               editor.putString("empEmail",emp.getEmpEmail());
                               editor.putString("empAddress",emp.getEmpAdress());
                               editor.putString("empDepartment",emp.getEmpDepartment());
                               editor.putString("empDOB",emp.getEmpDOB());
                               editor.putString("empMember",emp.getEmpMember());
                               editor.putString("empProfile",emp.getEmpProfile());
                               editor.commit();
                               startActivity(new Intent(LoginMainActivity.this, DashboardActivity.class));
                               finish();



                           }else if (emp.getEmpMember().equalsIgnoreCase("Admin")) {
                               Toast.makeText(LoginMainActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                               SharedPreferences.Editor editor = sp.edit();
                               editor.putInt("empId",emp.getEmpid());
                               editor.putString("empName",emp.getEmpName());
                               editor.putString("empPhone",emp.getEmpPhone());
                               editor.putString("empEmail",emp.getEmpEmail());
                               editor.putString("empAddress",emp.getEmpAdress());
                               editor.putString("empDepartment",emp.getEmpDepartment());
                               editor.putString("empDOB",emp.getEmpDOB());
                               editor.putString("empMember",emp.getEmpMember());
                               editor.commit();
                               startActivity(new Intent(LoginMainActivity.this, AdminDashboardActivity.class));
                               finish();

                           }

                       }
                       else {
                           Toast.makeText(LoginMainActivity.this, "Not Authentic User ", Toast.LENGTH_SHORT).show();
                       }

                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });


       }



    }
}
