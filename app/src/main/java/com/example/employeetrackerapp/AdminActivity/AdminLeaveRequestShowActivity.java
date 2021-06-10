package com.example.employeetrackerapp.AdminActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.RequestApprovalApplicationAdminBinding;


public class AdminLeaveRequestShowActivity extends AppCompatActivity
{
    RequestApprovalApplicationAdminBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RequestApprovalApplicationAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

    }
}
