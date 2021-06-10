package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.AdminDashboardBinding;


public class AdminDashboardActivity extends AppCompatActivity
{

    AdminDashboardBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          binding=AdminDashboardBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

         binding.btnleaveRequest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendToLeaveAprrovePade();
             }
         });


    }

    private void sendToLeaveAprrovePade()
    {
         startActivity(new Intent(AdminDashboardActivity.this, AdminLeaveRequestShowActivity.class));

    }
}
