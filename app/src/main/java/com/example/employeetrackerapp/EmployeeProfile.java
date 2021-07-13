package com.example.employeetrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.databinding.ImageDialogBinding;
import com.example.employeetrackerapp.databinding.ProfilepageBinding;

public class EmployeeProfile  extends AppCompatActivity
{
    ProfilepageBinding binding;
    SharedPreferences sp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ProfilepageBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);

        setDetailsofEmployee();

        binding.llbackbatn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogBinding imageBinding = ImageDialogBinding.inflate(LayoutInflater.from(EmployeeProfile.this));
                AlertDialog ad = new AlertDialog.Builder(EmployeeProfile.this).create();
                ad.setView(imageBinding.getRoot());
                imageBinding.dialogname.setText(sp.getString("empName",null));
                imageBinding.editprofileBtn.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(imageBinding.dialogImage);

                ad.show();
                ad.getWindow().setBackgroundDrawable(null);
            }
        });

    }

    private void setDetailsofEmployee()
    {
        binding.empname.setText(sp.getString("empName",null));
        binding.empaddress.setText(sp.getString("empAddress",null));
        binding.empdob.setText(sp.getString("empDOB",null));
        binding.empemail.setText(sp.getString("empEmail",null));
        binding.empphone.setText(sp.getString("empPhone",null));
        binding.profilename.setText(sp.getString("empName",null));
        binding.employeeDepartment.setText(sp.getString("empDesignation",null));
        String empProfile=sp.getString("empProfile",null);
        if(empProfile!=null)
        {
            Glide.with(this).load(empProfile).into(binding.profileImage);
        }




    }
}
