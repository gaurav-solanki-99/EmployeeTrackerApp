package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.MainActivity;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.databinding.AdminDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AdminDashboardActivity extends AppCompatActivity
{

    AdminDashboardBinding binding;
    String tabStatus="";
    SharedPreferences sp;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          binding=AdminDashboardBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        binding.tvtodaydate.setText(getCurrentDate());
        getTodayCount();



        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        binding.tvadminname.setText(sp.getString("empName",null));
        binding.tvdepartment.setText(sp.getString("empDepartment",null));
        Glide.with(this).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(binding.profileImage);
        Log.e("Profile>>>>>>>>>>>>>","Url "+sp.getString("empProfile",null));
         binding.btnleaveRequest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendToLeaveAprrovePade();
             }
         });

         binding.btnhalfRequest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendToHalfdayApprovePage();
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


         binding.btnaddEmp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendToAddEmployeeActivity();
             }
         });

         binding.btnlogout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 logoutUser();
             }
         });

         binding.btnhalfemplist.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendToAllEmployeeSearchActivity();

             }
         });


    }

    private void sendToAllEmployeeSearchActivity()
    {
        startActivity(new Intent(AdminDashboardActivity.this,AllEmployeeSearchActivity.class));
    }

    private void getTodayCount()
    {

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                int pcount=0;
                int hcount=0;
                int acount=0;
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                        if(emp.getDate().equals(getCurrentDate()))
                        {

                            if(emp.getDayStatus().equals("Present"))
                            {
                                pcount++;
                            }

                            if(emp.getDayStatus().equals("Halfday"))
                            {
                                hcount++;
                            }
                            if(emp.getDayStatus().equals("Absent"))
                            {
                                acount++;
                            }
                        }
                    }

                    binding.tvpresentcount.setText(""+pcount);
                    binding.tvabsentcount.setText(""+acount);
                    binding.tvhalfdaycount.setText(""+hcount);



            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {

            }
        });

    }

    private void logoutUser()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Intent in = new Intent(AdminDashboardActivity.this, LoginMainActivity.class);
        startActivity(in);
        finish();
    }

    private void sendToAddEmployeeActivity()
    {
        startActivity(new Intent(AdminDashboardActivity.this, AddEmployeeAdminActivity.class));

    }

    private void sendToHalfdayApprovePage()
    {
        startActivity(new Intent(AdminDashboardActivity.this, AllHalfdayRequestListActivity.class));
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

    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }
}
