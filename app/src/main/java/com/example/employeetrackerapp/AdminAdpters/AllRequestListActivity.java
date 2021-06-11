package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.AllRequstLayoutAdminBinding;

public class AllRequestListActivity  extends AppCompatActivity
{
    AllRequstLayoutAdminBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        AllRequstLayoutAdminBinding binding=AllRequstLayoutAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }
}
