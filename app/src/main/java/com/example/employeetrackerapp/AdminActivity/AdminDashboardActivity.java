package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.databinding.AdminDashboardBinding;


public class AdminDashboardActivity extends AppCompatActivity
{

    AdminDashboardBinding binding;
    String tabStatus="";
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


         binding.btnpreset.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 tabStatus="Presnet";
                 sendUserToActiveEmployee();
             }
         });

         binding.btnAbsent.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 tabStatus="Absent";
                 sendUserToActiveEmployee();
             }
         });

         binding.btnHalfday.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 tabStatus="Halfday";
                 sendUserToActiveEmployee();
             }
         });


    }

    private void sendUserToActiveEmployee()
    {
        Intent in = new Intent(AdminDashboardActivity.this,ActivieEmployeeActivity.class);
        in.putExtra("tabStatus",tabStatus);
        startActivity(in);

       // startActivity(new Intent(AdminDashboardActivity.this,ActivieEmployeeActivity.class));
    }

    private void sendToLeaveAprrovePade()
    {
         startActivity(new Intent(AdminDashboardActivity.this, AllRequestListActivity.class));

    }
}
