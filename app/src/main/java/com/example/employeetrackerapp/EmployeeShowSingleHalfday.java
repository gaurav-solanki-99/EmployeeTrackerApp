package com.example.employeetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.ActivityLeavesRequestBinding;

public class EmployeeShowSingleHalfday  extends AppCompatActivity
{

    ActivityLeavesRequestBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLeavesRequestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        binding.etLeaveSubject.setEnabled(false);
        binding.etLeaveDescription.setEnabled(false);
        binding.tvEndDtaeTime.setEnabled(false);
        binding.tvStartdate.setEnabled(false);
        Intent in = getIntent();
        Halfday halfday = (Halfday)in.getSerializableExtra("HalfdayRecord");
        binding.etLeaveSubject.setText(halfday.getHalfdayReason());
        binding.etLeaveDescription.setText(halfday.getHalfdayDescription());
        binding.tvStartdate.setText(halfday.getHalfdayDate());
        binding.tvEndDtaeTime.setText(halfday.getHalfdatTime());
        binding.btnApplyForLeave.setText(halfday.getHalfdayStatus());

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
