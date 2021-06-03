package com.example.employeetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.SplashscreenBinding;

public class SplashActriviy extends AppCompatActivity
{

    SplashscreenBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=SplashscreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        new MySplashScreen().start();


    }

    class MySplashScreen extends Thread
    {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Intent in = new Intent(SplashActriviy.this,MainActivity.class);
                startActivity(in);
                finish();
            }catch (Exception e)
            {
                Toast.makeText(SplashActriviy.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
