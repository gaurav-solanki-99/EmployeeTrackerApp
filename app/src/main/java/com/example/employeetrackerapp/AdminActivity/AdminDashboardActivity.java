package com.example.employeetrackerapp.AdminActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.MainActivity;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.databinding.AdminDashboardBinding;
import com.example.employeetrackerapp.databinding.ImageDialogBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
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
    String dateSeleted="";


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
        Glide.with(getApplicationContext()).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(binding.profileImage);
        Log.e("Profile>>>>>>>>>>>>>","Url "+sp.getString("empProfile",null));


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogBinding imageBinding = ImageDialogBinding.inflate(LayoutInflater.from(AdminDashboardActivity.this));
                AlertDialog ad = new AlertDialog.Builder(AdminDashboardActivity.this).create();
                ad.setView(imageBinding.getRoot());
                imageBinding.dialogname.setText(sp.getString("empName",null));
                Glide.with(getApplicationContext()).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(imageBinding.dialogImage);

                ad.show();
                ad.getWindow().setBackgroundDrawable(null);
            }
        });
        dateSeleted=getCurrentDate();
        binding.calederimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(AdminDashboardActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateSeleted = dayOfMonth + "-" + (month + 1) + "-" + year;
                        DateFormat parser = new SimpleDateFormat("dd-M-yyyy");
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date convertedDate = null;
                        try {
                            convertedDate = parser.parse(dateSeleted);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputDate = formatter.format(convertedDate);

                        binding.tvtodaydate.setText(outputDate);
                        dateSeleted=outputDate;
                        getTodayCount();



                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });



        binding.btnleaveRequest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendUserToShowAllRequestActivity();
             }
         });

         binding.btnhalfRequest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendUserToShowAllHalfayRequestActivity();
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

                 //sendToAddEmployeeActivity();
                 sendUserToAminGenerateEmployeeActivity();
             }
         });

         binding.btnlogout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder ad = new AlertDialog.Builder(AdminDashboardActivity.this);
                 ad.setTitle("Log out");
                 ad.setMessage("Are you sure to logout ");
                 ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         logoutUser();
                     }
                 });
                 ad.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         return;
                     }
                 });
                 ad.show();


             }
         });

         binding.btnhalfemplist.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendToAllEmployeeSearchActivity();

             }
         });

//         binding.btnaddTempEmp.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 sendUserToAminGenerateEmployeeActivity();
//             }
//         });


    }

    private void sendUserToAminGenerateEmployeeActivity() {
        startActivity(new Intent(AdminDashboardActivity.this,AdminGenerateEmployeeActivity.class));

    }

    private void sendUserToShowAllHalfayRequestActivity()
    {
        startActivity(new Intent(AdminDashboardActivity.this,ShowAllHalfdayRequestActivity.class));

    }

    private void sendUserToShowAllRequestActivity()
    {
        startActivity(new Intent(AdminDashboardActivity.this,ShowAllRequestActivity.class));
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
                        if(emp.getDate().equals(dateSeleted))
                        {

                            if(emp.getDayStatus().equals("Present"))
                            {
                                pcount++;
                            }else if(emp.getStartTime()!=""&&emp.getDayStatus().equalsIgnoreCase(""))
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
