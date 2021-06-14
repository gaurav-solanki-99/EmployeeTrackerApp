package com.example.employeetrackerapp.AdminActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.databinding.EmployeeregistrationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEmployeeAdminActivity  extends AppCompatActivity
{

    EmployeeregistrationBinding binding;
    public     int empid;
    String edate;
    String ejoindate;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> al;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=EmployeeregistrationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef= database.getReference();
        getLastId();


        binding.btnregsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });

        binding.etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(AddEmployeeAdminActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edate = dayOfMonth+"-"+(month+1)+"-"+year;
                        binding.etdob.setText(edate);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        binding.etdatejoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(AddEmployeeAdminActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ejoindate = dayOfMonth+"-"+(month+1)+"-"+year;
                        binding.etdatejoin.setText(ejoindate);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });


    }

    private void addMember()
    {
        String name = binding.etfullname.getText().toString();
        String department=binding.etDepartment.getText().toString();
        String memberType=binding.etmemberType.getText().toString();
        String address=binding.etaddress.getText().toString();
        String phone = binding.etmobileno.getText().toString();
        String email = binding.etemail.getText().toString();
        String password = binding.etpassword.getText().toString();


        if(TextUtils.isEmpty(name)&&TextUtils.isEmpty(department)&&TextUtils.isEmpty(memberType)&&TextUtils.isEmpty(address)&&TextUtils.isEmpty(phone)&&TextUtils.isEmpty(email)&&TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "All fields are Mandatory ", Toast.LENGTH_SHORT).show();

        }
        else
        {
            if(al.contains(phone))
            {
                Toast.makeText(this, "No already exist", Toast.LENGTH_SHORT).show();
            }
            else {


                EmployeeRecord emp = new EmployeeRecord();
                emp.setEmpid(empid + 1);
                emp.setEmpName(name);
                emp.setEmpDepartment(department);
                emp.setEmpMember(memberType);
                emp.setEmpDOB(edate);
                emp.setEmpAdress(address);
                emp.setEmpPhone(phone);
                emp.setEmpEmail(email);
                emp.setEmpDateOFjoining(ejoindate);
                emp.setEmpPassword(password);


                myRef.child("EmployeeRecord").push().setValue(emp);
                Toast.makeText(this, "Add User Successfully ", Toast.LENGTH_SHORT).show();
            }



        }




    }

    private void getLastId()
    {

        al=new ArrayList<>();
        myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {

                    EmployeeRecord empr = dataSnapshot.getValue(EmployeeRecord.class);

                    empid =empr.getEmpid();
                    al.add(empr.getEmpPhone());


                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }
}
