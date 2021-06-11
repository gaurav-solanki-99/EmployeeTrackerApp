package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.ActiveEmployeeAdapter;
import com.example.employeetrackerapp.AdminAdpters.ViewPagerActiveEmployeeAdmin;
import com.example.employeetrackerapp.AttendenceActivity;
import com.example.employeetrackerapp.Constants;
import com.example.employeetrackerapp.EmployeeAttendence;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.MainActivity;
import com.example.employeetrackerapp.databinding.ActiveEmployeeLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ActivieEmployeeActivity  extends AppCompatActivity
{



    ActiveEmployeeLayoutBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<EmployeeWorkingDetails> al;
    ActiveEmployeeAdapter adapter;
    ViewPagerActiveEmployeeAdmin viewpageradapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActiveEmployeeLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();





        viewpageradapter = new ViewPagerActiveEmployeeAdmin(getSupportFragmentManager());
        binding.veiwPager.setAdapter(viewpageradapter);
        binding.tabLayout.setupWithViewPager(binding.veiwPager);


        Intent in = getIntent();
          String tabStatus= in.getStringExtra("tabStatus");

          switch (tabStatus)
          {
              case "Present":
                  binding.veiwPager.setCurrentItem(0);
                break;
              case "Absent":
                  binding.veiwPager.setCurrentItem(1);
                  break;
              case "Halfday":
                  binding.veiwPager.setCurrentItem(2);
                  break;

          }



        Toast.makeText(this, ""+tabStatus, Toast.LENGTH_LONG).show();








    }











    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }
}
