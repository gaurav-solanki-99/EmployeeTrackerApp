package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.EmployeeloginBinding;


public class LoginMainActivity extends AppCompatActivity
{

    EmployeeloginBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=EmployeeloginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.btnemplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendUserToDashborad();

            }
        });

    }

    private void sendUserToDashborad()
    {
       startActivity(new Intent(LoginMainActivity.this,AdminDashboardActivity.class));
    }
}
