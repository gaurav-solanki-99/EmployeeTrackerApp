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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DashboardActivity  extends AppCompatActivity {

    Dashboard2Binding binding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sp;
    int empId;
    String empName,empDepartment;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String TodayDate="";
    String TodayMonth="";
    String CurrentTime="";
    boolean isloggedIn=false;
    boolean isBreak=false;
    String Breakhours="";



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=Dashboard2Binding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        setEmployeeName();//to set details from shared prefrences

        // intialize firebasedatabase object
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();


        //insert temporarary record to test Application
        //tempInsertIntoEmployeeSalaryStatus();

        // fetc record from EmployeeSalaryStatus and set on Ui
        setEmployeeRecordOnUi();

        getCurrentTime();
        isEmployeeLogIn();
        isBreakEmployee();

        //check Employee is Logged in or Not





        actionBarDrawerToggle= new ActionBarDrawerToggle(this,binding.myDrawerLayout,R.string.nav_open,R.string.nav_close);

        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayShowTitleEnabled(false);


           binding.navmenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(MenuItem item) {

                String title=  item.getTitle().toString();


                switch (title)
                {
                        case "ShowAttendence":
                        Intent in = new Intent(DashboardActivity.this,AttendenceActivity.class);
                        startActivity(in);
                        finish();
                        Toast.makeText(DashboardActivity.this, "ShowAttendenceClicked", Toast.LENGTH_SHORT).show();
                        break;
                        case "ShowLeaves":
                        Toast.makeText(DashboardActivity.this, "ShowLeavesClicked", Toast.LENGTH_SHORT).show();
                        Intent leavesin =  new Intent(DashboardActivity.this,LeavesActivity.class);
                        startActivity(leavesin);
                        finish();
                        break;
                        case "ShowHalfday":
                        Toast.makeText(DashboardActivity.this, "ShowHalfdayClicked", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(getApplicationContext(),HalfdayActivity.class));

                        break;
                    case "ShowProfile":
                        Toast.makeText(DashboardActivity.this, "Show Profile Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashboardActivity.this,EmployeeProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "LogOut":

                        sendUserToLoginPage();
                        Toast.makeText(DashboardActivity.this, "You are logout", Toast.LENGTH_SHORT).show();

                        break;

                }


                   return false;
               }
           });


             binding.worklogin.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     if(binding.worklogin.getText().equals("Day Completed"))
                     {
                         AlertDialog.Builder ad= new AlertDialog.Builder(DashboardActivity.this);
                         ad.setMessage("Can't Logged in After day Completed");
                         ad.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         });
                         ad.show();
                      return;
                     }

                     if(isloggedIn)
                     {
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
                       isloggedIn=false;
                       binding.worklogin.setText("Login");
                         //Toast.makeText(DashboardActivity.this, "You have already Logged In", Toast.LENGTH_SHORT).show();
                     }
                     else
                     {
                         workStartFunction();
                         isloggedIn=true;
                         binding.worklogin.setText("DayEnd");
                     }

                 }
             });


             // action on breakday button
             binding.workbreak.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     getCurrentTime();
                    if(binding.workbreak.getText().toString().equals("StartBreak"))
                    {
                        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot)
                            {

                                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                {
                                    EmployeeWorkingDetails emp =dataSnapshot.getValue(EmployeeWorkingDetails.class);
                                    if(TodayDate.equals(emp.getDate())&&empId==emp.getEmpId()) {
                                        String rootKey = dataSnapshot.getKey();
                                        DatabaseReference hopperRef = myRef.child("EmployeeWorkingDetails");
                                        Map<String, Object> userUpdates = new HashMap<>();
                                        userUpdates.put(rootKey + "/breakStartTime",CurrentTime);
                                        hopperRef.updateChildren(userUpdates);
                                        Toast.makeText(DashboardActivity.this, "Taking Break Now", Toast.LENGTH_SHORT).show();
                                        binding.workbreak.setText("EndBreak");


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error)
                            {

                            }
                        });
                    }


                    if(binding.workbreak.getText().toString().equals("EndBreak"))
                    {
                        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot)
                            {
                                getCurrentTime();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                {
                                    EmployeeWorkingDetails emp =dataSnapshot.getValue(EmployeeWorkingDetails.class);
                                    if(TodayDate.equals(emp.getDate())&&empId==emp.getEmpId()) {

                                        updateBreakHours(emp.getBreakStartTime(),CurrentTime);
                                        String rootKey = dataSnapshot.getKey();
                                        DatabaseReference hopperRef = myRef.child("EmployeeWorkingDetails");
                                        Map<String, Object> userUpdates = new HashMap<>();
                                        userUpdates.put(rootKey + "/breakEndTme",CurrentTime);
                                        userUpdates.put(rootKey + "/breakHours",Breakhours);
                                        binding.workbreak.setText("BreakCompleted");

                                        hopperRef.updateChildren(userUpdates);
                                        Toast.makeText(DashboardActivity.this, "Break Finish", Toast.LENGTH_SHORT).show();






                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error)
                            {

                            }
                        });
                    }

                    if(binding.workbreak.getText().toString().equals("BreakCompleted"))
                    {
                        Toast.makeText(DashboardActivity.this, "You have Already \nTake a Break ", Toast.LENGTH_SHORT).show();
                    }






                 }
             });

    }

    private void updateBreakHours(String startTime,String endTime)
    {



        try {
                          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

                          Date date1 = simpleDateFormat.parse(startTime);
                          Date date2 = simpleDateFormat.parse(endTime);

                          //calculating difference in milisecond
                          long diffInMilisec = Math.abs(date2.getTime()-date1.getTime());

                          //calculating the difference in hours
                          long diffInHour = (diffInMilisec/(60*60*1000))%24;


                          //calculating difference in minute
                          long diffInMinutes=(diffInMilisec/(60*1000))%60;


                          //calculating difference in second
                          long diffInSec = (diffInMilisec/1000)%60;



                          // make hours to save in database

                           Breakhours = diffInHour+":"+diffInMinutes+":"+diffInSec;

                          /*
                          String rootKey1 = dataSnapshot.getKey();
                          DatabaseReference hopperRef1 = myRef.child("EmployeeWorkingDetails");
                          Map<String, Object> userUpdates1 = new HashMap<>();
                          userUpdates1.put(rootKey1 + "/breakHours",hours);
                          hopperRef1.updateChildren(userUpdates1);

                           */

                          //Toast.makeText(DashboardActivity.this, "Break Hours"+hours, Toast.LENGTH_SHORT).show();





                      }catch(Exception ex)
                      {
                          Toast.makeText(DashboardActivity.this, ""+ex, Toast.LENGTH_SHORT).show();
                      }

    }

    private void isBreakEmployee() {

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(empId==emp.getEmpId()&&empName.equals(emp.getEmpName())&&empDepartment.equals(emp.getEmpDepartment())&&emp.getDate().equals(TodayDate)&&emp.getBreakStartTime().equals(""))
                    {
                        isBreak=false;
                        binding.workbreak.setText("StartBreak");
                    }
                    else
                    {
                        isBreak=true;
                        binding.workbreak.setText("EndBreak");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

    private void isEmployeeLogIn()
    {


        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            boolean status=false;
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(empId==emp.getEmpId()&&empName.equals(emp.getEmpName())&&emp.getDate().equals(TodayDate)&&emp.getEndTime().equals(""))
                    {
                        binding.worklogin.setText("Logout");
                      isloggedIn=true;
                    }
                    else if(empId==emp.getEmpId()&&empName.equals(emp.getEmpName())&&emp.getDate().equals(TodayDate)&& !emp.getEndTime().equals(""))
                    {
                        binding.worklogin.setText("Day Completed");

                        isloggedIn=true;
                    }
                    else
                    {

                        binding.worklogin.setText("Login");
                        isloggedIn=false;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

                isloggedIn=false;

            }
        });


    }

    //workEnd Function
    private void workEndFunction()
    {
        getCurrentTime();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp =dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(TodayDate.equals(emp.getDate())&&empId==emp.getEmpId()) {
                        String rootKey = dataSnapshot.getKey();
                        DatabaseReference hopperRef = myRef.child("EmployeeWorkingDetails");
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put(rootKey + "/endTime",CurrentTime);

                            userUpdates.put(rootKey + "/dayStatus", "Present");

                        hopperRef.updateChildren(userUpdates);
                        Toast.makeText(DashboardActivity.this, "Success fully Complete Day", Toast.LENGTH_SHORT).show();


                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Not Start Working"+emp.getDate(),Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {
                Toast.makeText(DashboardActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });



    }


    // for working start
    private void workStartFunction()
    {
        //To check yesterday status and update leaves if they are not present

     //  isEmployeePresentYesterDay();


        getCurrentTime();




        EmployeeWorkingDetails employeeWorking = new EmployeeWorkingDetails();
        employeeWorking.setEmpId(empId);
        employeeWorking.setEmpName(empName);
        employeeWorking.setEmpDepartment(empDepartment);
        employeeWorking.setMounth(TodayMonth);
        employeeWorking.setDate(TodayDate);
        employeeWorking.setStartTime(CurrentTime);
        employeeWorking.setEndTime("");
        employeeWorking.setDayStatus("");
        employeeWorking.setBreakStartTime("");
        employeeWorking.setBreakEndTme("");
        employeeWorking.setBreakHours("");

        myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking);



        Toast.makeText(this, "Your Working start Now ", Toast.LENGTH_SHORT).show();





    }

    // methode to get status of YesterDay Record(user present or not)
   private void  isEmployeePresentYesterDay()
   {



       //getting yesterday date
       Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.DATE, -1);
       DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
       String yesterdayDate = dateFormat.format(calendar.getTime());
      // Toast.makeText(this, ""+yesterdayDate, Toast.LENGTH_SHORT).show();


       myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull  DataSnapshot snapshot)
           {
              boolean status=false;
               for(DataSnapshot dataSnapshot:snapshot.getChildren())
               {
                   EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);

                   if(yesterdayDate.equals(emp.getDate())&&empId==emp.getEmpId())
                   {
                       status=false;
                       break;
                      // Toast.makeText(DashboardActivity.this, "Yester day present", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       status=true;


                       //Toast.makeText(DashboardActivity.this, "Yester day absent", Toast.LENGTH_SHORT).show();

                   }

               }
               if(status)
               {
                   EmployeeWorkingDetails employeeWorking = new EmployeeWorkingDetails();
                   employeeWorking.setEmpId(empId);
                   employeeWorking.setEmpName(empName);
                   employeeWorking.setEmpDepartment(empDepartment);
                   employeeWorking.setMounth(TodayMonth);
                   employeeWorking.setDate(yesterdayDate);
                   employeeWorking.setStartTime("");
                   employeeWorking.setEndTime("");
                   employeeWorking.setDayStatus("Absent");


                   myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking);
                   Toast.makeText(DashboardActivity.this, "Yesterday Absent", Toast.LENGTH_SHORT).show();
               }




           }

           @Override
           public void onCancelled(@NonNull  DatabaseError error) {
               Toast.makeText(DashboardActivity.this, "Error in getting yesterday "+error, Toast.LENGTH_SHORT).show();
           }
       });





   }



    //Methode to getCurrent Time
    private void getCurrentTime()
    {

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
      CurrentTime =timeFormat.format(currentTime);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        TodayDate=dateFormat.format(currentTime);









        Calendar cal = Calendar.getInstance();

        int mounth = cal.get(Calendar.MONTH)+1;

        switch (mounth)
        {
            case 1:
                TodayMonth="January";
                break;
            case 2:
                TodayMonth="Feburary";
                break;
            case 3:
                TodayMonth="March";
                break;
            case 4:
                TodayMonth="April";
                break;
            case 5:
                TodayMonth="May";
                break;
            case 6:
                TodayMonth="June";
                break;
            case 7:
                TodayMonth="July";
                break;
            case 8:
                TodayMonth="August";
                break;
            case 9:
                TodayMonth="September";
                break;
            case 10:
                TodayMonth="Octomber";
                break;
            case 11:
                TodayMonth="November";
                break;
            case 12:
                TodayMonth="December";
                break;

        }
    }

    private void setEmployeeRecordOnUi()
    {




        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int leavescount=0;
                int attendencecount=0;
                int halfdaycount=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(empId==emp.getEmpId()) {
                        String dayStatus = emp.getDayStatus();

                        if(emp.getDayStatus().equals("Present"))
                        {
                            attendencecount++;
                        }
                        if(emp.getDayStatus().equals("Absent"))
                        {
                            leavescount++;
                        }
                        if(emp.getDayStatus().equals("Halfday"))
                        {
                            halfdaycount++;
                        }
                    }
                }

                binding.attendencecount.setText(""+attendencecount);
                binding.leavescount.setText(""+leavescount);
                binding.halfdaycount.setText(""+halfdaycount);

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {
                Toast.makeText(DashboardActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void tempInsertIntoEmployeeSalaryStatus()
    {
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
      Intent in = new Intent(DashboardActivity.this,MainActivity.class);
      startActivity(in);
      finish();
    }

    private void setEmployeeName() {

        binding.empName.setText(sp.getString("empName",null));
        empId=sp.getInt("empId",0);
        empName=sp.getString("empName",null);
        empDepartment=sp.getString("empDepartment",null);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {



            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
