package com.example.employeetrackerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.databinding.Dashboard2Binding;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DashboardActivity extends AppCompatActivity {

    Dashboard2Binding binding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sp;
    int empId;
    String empName, empDepartment;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String isloggedIn = "";
    String isbreakIn = "";


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


        isEmployeeLogIn();
        isBreakEmployee();

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

                        Toast.makeText(DashboardActivity.this, "ShowAttendenceClicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "ShowLeaves":
                        Toast.makeText(DashboardActivity.this, "ShowLeavesClicked", Toast.LENGTH_SHORT).show();
                        Intent leavesin = new Intent(DashboardActivity.this, LeavesActivity.class);
                        startActivity(leavesin);

                        break;
                    case "ShowHalfday":
                        Toast.makeText(DashboardActivity.this, "ShowHalfdayClicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HalfdayActivity.class));

                        break;
                    case "ShowProfile":
                        Toast.makeText(DashboardActivity.this, "Show Profile Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashboardActivity.this, EmployeeProfile.class);
                        startActivity(intent);

                        break;
                    case "LogOut":

                        sendUserToLoginPage();
                        Toast.makeText(DashboardActivity.this, "You are logout", Toast.LENGTH_SHORT).show();
                        finish();

                        break;

                }


                return false;
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
                    isloggedIn = "true";
                    binding.worklogin.setText("Logout");
                    System.out.println("170");
                }

            }
        });


        // action on breakday button
        binding.workbreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isbreakIn.equalsIgnoreCase("false") && isloggedIn.equalsIgnoreCase("true")) {

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
                                    Toast.makeText(DashboardActivity.this, "Taking Break Now", Toast.LENGTH_SHORT).show();
                                    binding.workbreak.setText("End Break");
                                    isbreakIn = "true";
                                    System.out.println("199");
                                    myRef1.child("EmployeeWorkingDetails").removeEventListener(this);
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
                    Toast.makeText(DashboardActivity.this, "You Cant Start Break Before Login", Toast.LENGTH_SHORT).show();
                    System.out.println("212");
                } else if (isbreakIn.equalsIgnoreCase("true")) {

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
                                    hopperRef.updateChildren(userUpdates);

                                    binding.workbreak.setText("Break Completed");
                                    isbreakIn = "completed";
                                    Toast.makeText(DashboardActivity.this, "Break Finish", Toast.LENGTH_SHORT).show();
                                    System.out.println("230");
                                    myRef1.child("EmployeeWorkingDetails").removeEventListener(this);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    myRef1.child("EmployeeWorkingDetails").addValueEventListener(ab);
                } else if (isbreakIn.equalsIgnoreCase("completed")) {
                    Toast.makeText(DashboardActivity.this, "You have Already \nTake a Break ", Toast.LENGTH_SHORT).show();
                    System.out.println("241");
                } else {
                    Toast.makeText(DashboardActivity.this, "Error in Taking Break", Toast.LENGTH_SHORT).show();
                    System.out.println("244");
                }

            }
        });

    }

    private String getBreakHours(String startTime, String endTime) {
        String Return = "";
















        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:a");

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
            Toast.makeText(DashboardActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
        }


        return Return;

    }

    private void isBreakEmployee() {

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(getCurrentDate()) && !emp.getBreakStartTime().equals("") && !emp.getBreakEndTme().equals("")) {
                        binding.workbreak.setText("Break Completed");
                        isbreakIn = "completed";
                        System.out.println("297");

                    } else if (empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(getCurrentDate()) && !emp.getBreakStartTime().equals("") && emp.getBreakEndTme().equals("")) {
                        binding.workbreak.setText("End Break");
                        isbreakIn = "true";
                        System.out.println("301");

                    } else {
                        binding.workbreak.setText("Start Break");
                        isbreakIn = "false";
                        System.out.println("306");

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


        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (empId == emp.getEmpId() && empName.equals(emp.getEmpName())) {
                        if (emp.getDate().equals(getCurrentDate()) && !emp.getStartTime().equals("") && emp.getEndTime().equals("")) {
                            binding.worklogin.setText("Logout");
                            isloggedIn = "true";
                            System.out.println("334");
                        } else if ( emp.getDate().equals(getCurrentDate()) && !emp.getStartTime().equals("") && !emp.getEndTime().equals("")) {
                            binding.worklogin.setText("Day Completed");
                            isloggedIn = "Completed";
                            System.out.println("337");
                        } else {
                            binding.worklogin.setText("Login");
                            isloggedIn = "false";
                            System.out.println("342");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                isloggedIn = "";

            }
        });


    }

    //workEnd Function
    private void workEndFunction() {

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
                        userUpdates.put(rootKey + "/dayStatus", "Present");
                        hopperRef.updateChildren(userUpdates);
                        Toast.makeText(DashboardActivity.this, "Success fully Complete Day", Toast.LENGTH_SHORT).show();
                        isloggedIn = "Completed";
                        binding.worklogin.setText("Work Completed");
                        System.out.println("380");
                        myRef1.child("EmployeeWorkingDetails").removeEventListener(this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Not Start Working" + emp.getDate(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        };
        myRef1.child("EmployeeWorkingDetails").addValueEventListener(ab);


    }


    // for working start
    private void workStartFunction() {
        //To check yesterday status and update leaves if they are not present

        //  isEmployeePresentYesterDay();
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

        myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking);
        System.out.println("415");

        Toast.makeText(this, "Your Working start Now ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DashboardActivity.this, "Yesterday Absent", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "Error in getting yesterday " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }


    //Methode to getCurrent Time
    private String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:a");
        return timeFormat.format(currentTime);

    }


    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }


    private String getCurrentMonth() {
        String TodayMonth = "";
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
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
                Toast.makeText(DashboardActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Temporary Data Saved ", Toast.LENGTH_SHORT).show();
    }


    private void sendUserToLoginPage() {

        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Intent in = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(in);
        finish();
    }

    private void setEmployeeName() {

        binding.empName.setText(sp.getString("empName", null));
        empId = sp.getInt("empId", 0);
        empName = sp.getString("empName", null);
        empDepartment = sp.getString("empDepartment", null);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
