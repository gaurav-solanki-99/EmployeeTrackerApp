package com.example.employeetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.ActivityLeavesRequestBinding;
import com.example.employeetrackerapp.databinding.LeavesShowBinding;

public class EmployeeShowSingleLeaves extends AppCompatActivity
{
    ActivityLeavesRequestBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLeavesRequestBinding.inflate(LayoutInflater.from(this));
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        binding.etLeaveSubject.setEnabled(false);
        binding.etLeaveDescription.setEnabled(false);
        binding.tvEndDtaeTime.setEnabled(false);
        binding.tvStartdate.setEnabled(false);
        Intent in = getIntent();
        Leaves leaves = (Leaves)in.getSerializableExtra("LeaveRecord");
        binding.etLeaveSubject.setText(leaves.getLeavesReason());
        binding.etLeaveDescription.setText(leaves.getLeaveDescription());
        binding.tvStartdate.setText(leaves.getLeaveStartDate());
        binding.tvEndDtaeTime.setText(leaves.getLeaveEndDtae());
        binding.btnApplyForLeave.setText(leaves.getLeavesStatus());

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }
}
