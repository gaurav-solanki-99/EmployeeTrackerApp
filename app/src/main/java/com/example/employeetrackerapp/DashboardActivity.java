package com.example.employeetrackerapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminActivity.AdminDashboardActivity;
import com.example.employeetrackerapp.AdminActivity.ApprovalApplicationActivity;
import com.example.employeetrackerapp.AdminActivity.LoginMainActivity;
import com.example.employeetrackerapp.databinding.Dashboard2Binding;
import com.example.employeetrackerapp.databinding.ImageDialogBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DashboardActivity extends AppCompatActivity {

    Dashboard2Binding binding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sp;
    int empId;
    String empName, empDepartment,empProfile;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String isloggedIn = "false";//21 june
    String isbreakIn = "";
    String isTodayPendinRequest="";
    String isEmpoyeeAbsentToday="";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Dashboard2Binding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        sp = getSharedPreferences("employeeDetails", MODE_PRIVATE);
        setEmployeeName();//to set details from shared prefrences
       



        // intialize firebasedatabase object
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        //insert temporarary record to test Application
        //tempInsertIntoEmployeeSalaryStatus();

        // fetc record from EmployeeSalaryStatus and set on Ui
        setEmployeeRecordOnUi();

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogBinding imageBinding = ImageDialogBinding.inflate(LayoutInflater.from(DashboardActivity.this));
                AlertDialog ad = new AlertDialog.Builder(DashboardActivity.this).create();
                ad.setView(imageBinding.getRoot());
                imageBinding.dialogname.setText(sp.getString("empName",null));
                imageBinding.editprofileBtn.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(imageBinding.dialogImage);

                ad.show();
                ad.getWindow().setBackgroundDrawable(null);
            }
        });




        isEmployeeLogIn();
        isBreakEmployee();
//        CheckTodayPendingRequest();

        //check Employee is Logged in or Not

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close);

        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        binding.navmenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                String title = item.getTitle().toString();


                switch (title) {
                    case "ShowAttendence":
                        Intent in = new Intent(DashboardActivity.this, AttendenceActivity.class);

                        startActivity(in);

                        break;
                    case "ShowLeaves":
                        Intent leavesin = new Intent(DashboardActivity.this, LeavesActivity.class);
                        startActivity(leavesin);
                        break;
                    case "ShowHalfday":
                        startActivity(new Intent(getApplicationContext(), HalfdayActivity.class));

                        break;
                    case "ShowProfile":
                        Intent intent = new Intent(DashboardActivity.this, EmployeeProfile.class);
                        startActivity(intent);


                        break;
                    case "Team":
                        Intent teamintent = new Intent(DashboardActivity.this, CompanyTeamMemberactivty.class);
                        startActivity(teamintent);


                        break;
                    case "LogOut":


                        AlertDialog.Builder ad = new AlertDialog.Builder(DashboardActivity.this);
                        ad.setTitle("Log out");
                        ad.setMessage("Are you sure to logout ");
                        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sendUserToLoginPage();
                            }
                        });
                        ad.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                return;
                            }
                        });
                        ad.show();

                        break;

                }

                binding.myDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });






        binding.worklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (isloggedIn.equalsIgnoreCase("Completed")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(DashboardActivity.this);
                    ad.setMessage("Can't Logged in After day Completed");
                    ad.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                    return;
                }

                if (isloggedIn.equalsIgnoreCase("true")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(DashboardActivity.this);
                    ad.setMessage("Are You sure Logged out");
                    ad.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            workEndFunction();
                        }
                    });
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                } else {

                    workStartFunction();

                    System.out.println("170");
                }

            }
        });


        // action on breakday button
        binding.workbreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isbreakIn.equalsIgnoreCase("false") && isloggedIn.equalsIgnoreCase("true"))
                {


                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                    DatabaseReference myRef1 = database1.getReference();

                    ValueEventListener ab = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                                if (getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {
                                    String rootKey = dataSnapshot.getKey();
                                    DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                                    Map<String, Object> userUpdates = new HashMap<>();
                                    userUpdates.put(rootKey + "/breakStartTime", getCurrentTime());
                                    hopperRef.updateChildren(userUpdates);
//                                    Toast.makeText(DashboardActivity.this, "Taking Break Now", Toast.LENGTH_SHORT).show();
//                                    binding.workbreak.setText("End Break");
//                                    isbreakIn = "true";
                                    System.out.println("199");
                                    myRef1.child("EmployeeWorkingDetails").removeEventListener(this);
                                    binding.workbreak.setText("End Break");
                                    isbreakIn = "true";
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    myRef1.child("EmployeeWorkingDetails").addValueEventListener(ab);

                } else if (isbreakIn.equalsIgnoreCase("false") && isloggedIn.equalsIgnoreCase("false")) {
//                    Toast.makeText(DashboardActivity.this, "You Cant Start Break Before Login", Toast.LENGTH_SHORT).show();
                    System.out.println("212");
                } else if (isbreakIn.equalsIgnoreCase("true")) {

                    AlertDialog.Builder ad = new AlertDialog.Builder(DashboardActivity.this);
                    ad.setMessage("Are You sure Break out");
                    ad.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ProgressDialog pd = new ProgressDialog(DashboardActivity.this);
                            pd.setMessage("Please Wait");
                            pd.show();

                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                    DatabaseReference myRef1 = database1.getReference();

                    ValueEventListener ab = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                                if (getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {

                                    String rootKey = dataSnapshot.getKey();
                                    DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                                    Map<String, Object> userUpdates = new HashMap<>();
                                    userUpdates.put(rootKey + "/breakEndTme", getCurrentTime());
                                    userUpdates.put(rootKey + "/breakHours", getBreakHours(emp.getBreakStartTime(), getCurrentTime()));
                                    hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            pd.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    binding.workbreak.setText("Break Completed");
                                    isbreakIn = "completed";
//                                    Toast.makeText(DashboardActivity.this, "Break Finish", Toast.LENGTH_SHORT).show();
                                    System.out.println("230");
                                    myRef1.child("EmployeeWorkingDetails").removeEventListener(this);

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                                pd.dismiss();
                        }
                    };
                    myRef1.child("EmployeeWorkingDetails").addValueEventListener(ab);

                        }
                    });
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                } else if (isbreakIn.equalsIgnoreCase("completed")) {
                    Toast.makeText(DashboardActivity.this, "You have Already \nTake a Break ", Toast.LENGTH_SHORT).show();



//                    System.out.println("241");
                } else {
//                    Toast.makeText(DashboardActivity.this, "Error in Taking Break", Toast.LENGTH_SHORT).show();
                    System.out.println("244");
                }

            }
        });

    }


    private void CheckTodayPendingRequest()
    {


        myRef.child("EmployeLeavesApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    EmployeLeavesApplicationRecord record = dataSnapshot.getValue(EmployeLeavesApplicationRecord.class);
                    if(empId==record.getEmpId()&&record.getLeaveStatus().equalsIgnoreCase("Pending"))
                    {


                        String startdate = record.getLeaveStartDate();
                        String endate =record.getLeaveEndDate();

                        try {
                            ArrayList<Date> dates=new ArrayList<>();
                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            Date startDate = (Date) formatter.parse(startdate);
                            Date endDate = (Date) formatter.parse(endate);
                            long interval = 24*1000 * 60 * 60; // 1 hour in millis
                            long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
                            long curTime = startDate.getTime();
                            while (curTime <= endTime) {
                                dates.add(new Date(curTime));
                                curTime += interval;
                            }
                            for(int i=0;i<dates.size();i++){
                                Date lDate =(Date)dates.get(i);
                                String ds = formatter.format(lDate);
                                if(getCurrentDate().equals(ds))
                                {
//                                    Toast.makeText(DashboardActivity.this, ""+ds, Toast.LENGTH_LONG).show();
                                    isTodayPendinRequest="true";


                                }


                            }

                        }catch (Exception e)
                        {
//                            Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                        }



                    }

                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }


    private String getWorkHours(String startTime,String endTime)
    {
        String Return="";

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

            Date date1 = simpleDateFormat.parse(startTime);
            Date date2 = simpleDateFormat.parse(endTime);


            //calculating difference in milisecond
            long diffInMilisec = Math.abs(date2.getTime() - date1.getTime());


            //calculating the difference in hours
            long diffInHour = (diffInMilisec / (60 * 60 * 1000)) % 24;


            //calculating difference in minute
            long diffInMinutes = (diffInMilisec / (60 * 1000)) % 60;


            //calculating difference in second
            long diffInSec = (diffInMilisec / 1000) % 60;


            // make hours to save in database

            Return = diffInHour + ":" + diffInMinutes + ":" + diffInSec;


        } catch (Exception ex) {
//            Toast.makeText(DashboardActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
        }



        return Return;


    }

    private String getBreakHours(String startTime, String endTime) {
        String Return = "";

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

            Date date1 = simpleDateFormat.parse(startTime);
            Date date2 = simpleDateFormat.parse(endTime);


            //calculating difference in milisecond
            long diffInMilisec = Math.abs(date2.getTime() - date1.getTime());


            //calculating the difference in hours
            long diffInHour = (diffInMilisec / (60 * 60 * 1000)) % 24;


            //calculating difference in minute
            long diffInMinutes = (diffInMilisec / (60 * 1000)) % 60;


            //calculating difference in second
            long diffInSec = (diffInMilisec / 1000) % 60;


            // make hours to save in database

            Return = diffInHour + ":" + diffInMinutes + ":" + diffInSec;


        } catch (Exception ex) {
//            Toast.makeText(DashboardActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
        }


        return Return;

    }

    private void isBreakEmployee() {





        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if ( empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(getCurrentDate()) && !emp.getBreakStartTime().equals("") && !emp.getBreakEndTme().equals("")) {
                        binding.workbreak.setText("Break Completed");
                        isbreakIn = "completed";
                        System.out.println("297");

                    } else if (empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(getCurrentDate()) && !emp.getBreakStartTime().equals("") && emp.getBreakEndTme().equals("")) {
                        binding.workbreak.setText("End Break");
                        isbreakIn = "true";
                        System.out.println("301");

                    } else if(empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(getCurrentDate()) && !emp.getStartTime().equals("") && emp.getBreakStartTime().equals("")) {
                        binding.workbreak.setText("Start Break");
                        isbreakIn = "false";
                        System.out.println("306");
                        System.out.println("Start Break >>>>");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isbreakIn = "";
            }
        });

    }

    private void isEmployeeLogIn() {

        ProgressDialog pd = new ProgressDialog(DashboardActivity.this);
        pd.setTitle("Please wait");
        pd.setMessage("Update record");
        pd.show();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (empId == emp.getEmpId() && empName.equals(emp.getEmpName())) {
                        if (emp.getDate().equals(getCurrentDate()) && !emp.getStartTime().equals("") && emp.getEndTime().equals("")) {
                            binding.worklogin.setText("Logout");
                            isloggedIn = "true";
                            binding.workbreak.setVisibility(View.VISIBLE);
                            System.out.println("334");
                        } else if (emp.getDate().equals(getCurrentDate()) && !emp.getStartTime().equals("") && !emp.getEndTime().equals("")) {
                            binding.worklogin.setText("Day Completed");
                            isloggedIn = "Completed";
                            System.out.println("337");
                        } else {
                            binding.worklogin.setText("Login");
                            isloggedIn = "false";
                            System.out.println("342");
                        }
                    }
                    pd.dismiss();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                isloggedIn = "";

                pd.dismiss();
                AlertDialog.Builder ad = new AlertDialog.Builder(DashboardActivity.this);
                ad.setMessage("Error "+error);
                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                ad.show();


            }
        });


    }

    //workEnd Function
    private void workEndFunction() {


        binding.workbreak.setVisibility(View.GONE);

        ProgressDialog pd = new ProgressDialog(DashboardActivity.this);
        pd.setMessage("Please Wait");
        pd.show();
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference();
        ValueEventListener ab = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {
                        String rootKey = dataSnapshot.getKey();
                        DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put(rootKey + "/endTime", getCurrentTime());
                        if(!emp.getDayStatus().equalsIgnoreCase("Halfday"))
                        {
                            userUpdates.put(rootKey + "/dayStatus", "Present");
                        }

                        userUpdates.put(rootKey + "/workHours", getWorkHours(emp.getStartTime(), getCurrentTime()));
                        hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pd.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                pd.dismiss();
                                Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            }
                        });
//                        Toast.makeText(DashboardActivity.this, "Success fully Complete Day", Toast.LENGTH_SHORT).show();
                        isloggedIn = "Completed";
                        binding.worklogin.setText("Work Completed");
                        System.out.println("380");
                        myRef1.child("EmployeeWorkingDetails").removeEventListener(this);
                    } else {
//                        Toast.makeText(getApplicationContext(), "Not Start Working" + emp.getDate(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                                Toast.makeText(DashboardActivity.this, "" + error, Toast.LENGTH_SHORT).show();


            }
        };
        myRef1.child("EmployeeWorkingDetails").addValueEventListener(ab);


    }


    // for working start
    private void workStartFunction() {
        //To check yesterday status and update leaves if they are not present
        //  isEmployeePresentYesterDay();


            System.out.println(">>>>Work Start Funtion Called ");//21 june
            binding.workbreak.setVisibility(View.VISIBLE);

            database.getReference().child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String isPresent = "no";//21 june
                    String x = "";
                    String rootKey = "";
                    System.out.println(">>>>>>> Snapshaop in work Start Function Emp Object " + snapshot.getChildren());//21 june

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        EmployeeWorkingDetails emp1 = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                        System.out.println(">>>>>>> Snapshaop in work Start Function Emp Object " + emp1);//21 june
                        if (emp1.getEmpId() == empId && emp1.getDate().equals(getCurrentDate()) && !emp1.getDayStatus().equals("")) {
                            isPresent = emp1.getDayStatus();
                            rootKey = dataSnapshot.getKey();
                            break;
                        } else {
                            isPresent = "no";
                        }

                    }

                    if (isPresent.equalsIgnoreCase("Absent")) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(DashboardActivity.this);
                        ad.setMessage("You are on" + isPresent);
                        ad.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        ad.show();


                    } else if (isPresent.equalsIgnoreCase("HalfDay")) {

//                        DatabaseReference hopperRef = myRef.child("EmployeeWorkingDetails");
//                        Map<String, Object> userUpdates = new HashMap<>();
//                        userUpdates.put(rootKey + "/startTime", getCurrentTime());
//                        hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(DashboardActivity.this, "Success fully login", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference myRef1 = database1.getReference();
                        ValueEventListener ab = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                                    if (getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {
                                        String rootKey = dataSnapshot.getKey();
                                        DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                                        Map<String, Object> userUpdates = new HashMap<>();
                                        userUpdates.put(rootKey + "/startTime", getCurrentTime());
                                        hopperRef.updateChildren(userUpdates);
                                        isloggedIn = "true";
                                        binding.worklogin.setText("Logout");

                                        System.out.println("380");
                                        myRef1.child("EmployeeWorkingDetails").removeEventListener(this);
                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Not Start Working" + emp.getDate(), Toast.LENGTH_LONG).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(DashboardActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                            }
                        };




                    } else if (isPresent.equalsIgnoreCase("no")) {


//                        Toast.makeText(DashboardActivity.this, "Welcome" + getCurrentDate(), Toast.LENGTH_SHORT).show();
                        EmployeeWorkingDetails employeeWorking = new EmployeeWorkingDetails();
                        employeeWorking.setEmpId(empId);
                        employeeWorking.setEmpName(empName);
                        employeeWorking.setEmpDepartment(empDepartment);
                        employeeWorking.setMounth(getCurrentMonth());
                        employeeWorking.setDate(getCurrentDate());
                        employeeWorking.setStartTime(getCurrentTime());
                        employeeWorking.setEndTime("");
                        employeeWorking.setDayStatus("");
                        employeeWorking.setBreakStartTime("");
                        employeeWorking.setBreakEndTme("");
                        employeeWorking.setBreakHours("");
                        employeeWorking.setWorkHours("");

                        myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking);
                        System.out.println("415");
                        isloggedIn = "true";
                        binding.worklogin.setText("Logout");

                    }


                    System.out.println("33333333333" + isPresent);
                    myRef.child("EmployeeWorkingDetails").removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }










    // methode to get status of YesterDay Record(user present or not)
    private void isEmployeePresentYesterDay() {

        //getting yesterday date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String yesterdayDate = dateFormat.format(calendar.getTime());
        // Toast.makeText(this, ""+yesterdayDate, Toast.LENGTH_SHORT).show();


        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean status = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);

                    if (yesterdayDate.equals(emp.getDate()) && empId == emp.getEmpId()) {
                        status = false;
                        break;
                        // Toast.makeText(DashboardActivity.this, "Yester day present", Toast.LENGTH_SHORT).show();
                    } else {
                        status = true;


                        //Toast.makeText(DashboardActivity.this, "Yester day absent", Toast.LENGTH_SHORT).show();

                    }

                }
                if (status) {
                    EmployeeWorkingDetails employeeWorking = new EmployeeWorkingDetails();
                    employeeWorking.setEmpId(empId);
                    employeeWorking.setEmpName(empName);
                    employeeWorking.setEmpDepartment(empDepartment);
                    employeeWorking.setMounth(getCurrentMonth());
                    employeeWorking.setDate(yesterdayDate);
                    employeeWorking.setStartTime("");
                    employeeWorking.setEndTime("");
                    employeeWorking.setDayStatus("Absent");


                    myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking);
//                    Toast.makeText(DashboardActivity.this, "Yesterday Absent", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DashboardActivity.this, "Error in getting yesterday " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }


    //Methode to getCurrent Time
    private String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(currentTime);

    }


    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }


    private String getCurrentMonth() {
        String TodayMonth = "";
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.format(currentTime);

        Calendar cal = Calendar.getInstance();

        int mounth = cal.get(Calendar.MONTH) + 1;

        switch (mounth) {
            case 1:
                TodayMonth = "January";
                break;
            case 2:
                TodayMonth = "Feburary";
                break;
            case 3:
                TodayMonth = "March";
                break;
            case 4:
                TodayMonth = "April";
                break;
            case 5:
                TodayMonth = "May";
                break;
            case 6:
                TodayMonth = "June";
                break;
            case 7:
                TodayMonth = "July";
                break;
            case 8:
                TodayMonth = "August";
                break;
            case 9:
                TodayMonth = "September";
                break;
            case 10:
                TodayMonth = "Octomber";
                break;
            case 11:
                TodayMonth = "November";
                break;
            case 12:
                TodayMonth = "December";
                break;

        }
        return TodayMonth;
    }


    private void setEmployeeRecordOnUi() {


        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int leavescount = 0;
                int attendencecount = 0;
                int halfdaycount = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (empId == emp.getEmpId()) {
                        String dayStatus = emp.getDayStatus();

                        if (emp.getDayStatus().equals("Present")) {
                            attendencecount++;
                        }
                        if (emp.getDayStatus().equals("Absent")) {
                            leavescount++;
                        }
                        if (emp.getDayStatus().equals("Halfday")) {
                            halfdaycount++;
                        }
                    }
                }

                binding.attendencecount.setText("" + attendencecount);
                binding.leavescount.setText("" + leavescount);
                binding.halfdaycount.setText("" + halfdaycount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DashboardActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void tempInsertIntoEmployeeSalaryStatus() {
        EmployeeSalaryStatus employeeSalaryStatus = new EmployeeSalaryStatus();
        employeeSalaryStatus.setEmpId(empId);
        employeeSalaryStatus.setEmpName(empName);
        employeeSalaryStatus.setEmpDepartment(empDepartment);
        employeeSalaryStatus.setMonth("May");
        employeeSalaryStatus.setTotalattendence(20);
        employeeSalaryStatus.setTotalhalfdays(4);
        employeeSalaryStatus.setMonthsalary(7000);

        myRef.child("EmployeeSalaryStatus").push().setValue(employeeSalaryStatus);
//        Toast.makeText(this, "Temporary Data Saved ", Toast.LENGTH_SHORT).show();
    }


    private void sendUserToLoginPage() {

        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Intent in = new Intent(DashboardActivity.this, LoginMainActivity.class);
        startActivity(in);
        finish();
    }

    private void setEmployeeName() {

        binding.empName.setText(sp.getString("empName", null));
        empId = sp.getInt("empId", 0);
        empName = sp.getString("empName", null);
        empDepartment = sp.getString("empDepartment", null);
        empProfile=sp.getString("empProfile",null);
        if(empProfile!=null)
        {
            Glide.with(this).load(empProfile).into(binding.profileImage);

        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.myDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
