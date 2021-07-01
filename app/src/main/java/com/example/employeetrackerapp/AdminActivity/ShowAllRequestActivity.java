package com.example.employeetrackerapp.AdminActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.AdminAdpters.ViewPagerAllRequestAdapter;
import com.example.employeetrackerapp.databinding.ShowAllRequestLayoutBinding;

public class ShowAllRequestActivity  extends AppCompatActivity
{
    ShowAllRequestLayoutBinding binding;
    ViewPagerAllRequestAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ShowAllRequestLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        adapter = new ViewPagerAllRequestAdapter(getSupportFragmentManager());
        binding.veiwPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.veiwPager);
    }
}
