package com.example.employeetrackerapp.AdminActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.AdminAdpters.ViewPagerAllHalfdayRequestAdapter;
import com.example.employeetrackerapp.AdminAdpters.ViewPagerAllRequestAdapter;
import com.example.employeetrackerapp.databinding.ShowAllHalfdayrequestLayotBinding;

public class ShowAllHalfdayRequestActivity extends AppCompatActivity
{

    ShowAllHalfdayrequestLayotBinding binding;
    ViewPagerAllHalfdayRequestAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ShowAllHalfdayrequestLayotBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        adapter = new ViewPagerAllHalfdayRequestAdapter(getSupportFragmentManager());
        binding.veiwPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.veiwPager);

    }
}
