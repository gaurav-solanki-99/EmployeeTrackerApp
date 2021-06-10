package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.SlashscreenadminBinding;


public class SplashMainActivity  extends AppCompatActivity
{
    SlashscreenadminBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= SlashscreenadminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        new MainSplashNext().start();

    }


      class MainSplashNext extends Thread
      {
          @Override
          public void run() {
              super.run();

              try {
                  Thread.sleep(3000);
                  startActivity(new Intent(SplashMainActivity.this,LoginMainActivity.class));
                  finish();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }

}
