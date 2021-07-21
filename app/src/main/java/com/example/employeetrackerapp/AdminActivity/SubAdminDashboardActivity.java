package com.example.employeetrackerapp.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AttendenceActivity;
import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeProfile;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.GCurrentDateTime;
import com.example.employeetrackerapp.HalfdayActivity;
import com.example.employeetrackerapp.LeavesActivity;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.databinding.ActivitySubAdminDashboardBinding;
import com.example.employeetrackerapp.databinding.ImageDialogBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.Map;

public class SubAdminDashboardActivity extends AppCompatActivity {


    ActivitySubAdminDashboardBinding binding;
    String tabStatus="";
    SharedPreferences sp;

    FirebaseDatabase database;
    DatabaseReference myRef;
    String dateSeleted="";

    int empId;
    String empName, empDepartment,empProfile;
    String isloggedIn = "false";//21 june
    String isbreakIn = "";
    String isTodayPendinRequest="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySubAdminDashboardBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        dateSeleted= GCurrentDateTime.getCurrentDate();
        getTodayCount();

        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        empId = sp.getInt("empId", 0);
        empName = sp.getString("empName", null);
        empDepartment = sp.getString("empDepartment", null);
        binding.tvadminname.setText(sp.getString("empName",null));
        binding.tvdepartment.setText(sp.getString("empDesignation",null));
        Glide.with(getApplicationContext()).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(binding.profileImage);
        Log.e("Profile>>>>>>>>>>>>>","Url "+sp.getString("empProfile",null));



        isEmployeeLogIn();
        isBreakEmployee();




        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogBinding imageBinding = ImageDialogBinding.inflate(LayoutInflater.from(SubAdminDashboardActivity.this));
                AlertDialog ad = new AlertDialog.Builder(SubAdminDashboardActivity.this).create();
                ad.setView(imageBinding.getRoot());
                imageBinding.dialogname.setText(sp.getString("empName",null));
                imageBinding.editprofileBtn.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(sp.getString("empProfile",null)).error(R.drawable.ic_baseline_person_24).into(imageBinding.dialogImage);

                ad.show();
                ad.getWindow().setBackgroundDrawable(null);
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

        binding.btnemplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAllEmployeeSearchActivity();
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
                AlertDialog.Builder ad = new AlertDialog.Builder(SubAdminDashboardActivity.this);
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


        binding.btnAdminLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SubAdminDashboardActivity.this, LeavesActivity.class);

                startActivity(in);
            }
        });

        binding.btnAdminHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubAdminDashboardActivity.this, HalfdayActivity.class));
            }
        });

        binding.btnAdminAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SubAdminDashboardActivity.this, AttendenceActivity.class);

                startActivity(in);
            }
        });

        binding.btnAdminProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubAdminDashboardActivity.this, EmployeeProfile.class);
                startActivity(intent);
            }
        });






    }

    private void sendUserToActiveEmployee()
    {
        Intent in = new Intent(SubAdminDashboardActivity.this,ActivieEmployeeActivity.class);
        in.putExtra("tabStatus",tabStatus);
        startActivity(in);

    }

    private void sendUserToAminGenerateEmployeeActivity() {
        startActivity(new Intent(SubAdminDashboardActivity.this,AdminGenerateEmployeeActivity.class));

    }

    private void logoutUser()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Intent in = new Intent(SubAdminDashboardActivity.this, LoginMainActivity.class);
        startActivity(in);
        finish();
    }

    private void sendUserToShowAllHalfayRequestActivity()
    {
        startActivity(new Intent(SubAdminDashboardActivity.this,ShowAllHalfdayRequestActivity.class));

    }

    private void sendUserToShowAllRequestActivity()
    {
        startActivity(new Intent(SubAdminDashboardActivity.this,ShowAllRequestActivity.class));
    }

    private void sendToAllEmployeeSearchActivity()
    {
        startActivity(new Intent(SubAdminDashboardActivity.this,AllEmployeeSearchActivity.class));
    }

    private void openCalender() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(SubAdminDashboardActivity.this, new DatePickerDialog.OnDateSetListener() {
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


                dateSeleted=outputDate;
                getTodayCount();



            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    private void getTodayCount()
    {
        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
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
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }

    private void isEmployeeLogIn() {

        ProgressDialog pd = new ProgressDialog(SubAdminDashboardActivity.this);
        pd.setTitle("Please wait");
        pd.setMessage("Update record");
        pd.show();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (empId == emp.getEmpId() && empName.equals(emp.getEmpName())) {
                        if (emp.getDate().equals(GCurrentDateTime.getCurrentDate()) && !emp.getStartTime().equals("") && emp.getEndTime().equals("")) {
                            binding.worklogin.setText("Logout");
                            isloggedIn = "true";
                            binding.workbreak.setVisibility(View.VISIBLE);
                            System.out.println("334");
                        } else if (emp.getDate().equals(GCurrentDateTime.getCurrentDate()) && !emp.getStartTime().equals("") && !emp.getEndTime().equals("")) {
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
                AlertDialog.Builder ad = new AlertDialog.Builder(SubAdminDashboardActivity.this);
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


        binding.llworklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (isloggedIn.equalsIgnoreCase("Completed")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(SubAdminDashboardActivity.this);
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
                    AlertDialog.Builder ad = new AlertDialog.Builder(SubAdminDashboardActivity.this);
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


        binding.llworkbreak.setOnClickListener(new View.OnClickListener() {
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
                                if (GCurrentDateTime.getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {
                                    String rootKey = dataSnapshot.getKey();
                                    DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                                    Map<String, Object> userUpdates = new HashMap<>();
                                    userUpdates.put(rootKey + "/breakStartTime", GCurrentDateTime.getCurrentTime());
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

                    AlertDialog.Builder ad = new AlertDialog.Builder(SubAdminDashboardActivity.this);
                    ad.setMessage("Are You sure Break out");
                    ad.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ProgressDialog pd = new ProgressDialog(SubAdminDashboardActivity.this);
                            pd.setMessage("Please Wait");
                            pd.show();

                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                            DatabaseReference myRef1 = database1.getReference();

                            ValueEventListener ab = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                                        if (GCurrentDateTime.getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {

                                            String rootKey = dataSnapshot.getKey();
                                            DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                                            Map<String, Object> userUpdates = new HashMap<>();
                                            userUpdates.put(rootKey + "/breakEndTme", GCurrentDateTime.getCurrentTime());
                                            userUpdates.put(rootKey + "/breakHours", getBreakHours(emp.getBreakStartTime(),GCurrentDateTime.getCurrentTime()));
                                            hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    pd.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull  Exception e) {
                                                    Toast.makeText(SubAdminDashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SubAdminDashboardActivity.this, "You have Already \nTake a Break ", Toast.LENGTH_SHORT).show();



//                    System.out.println("241");
                } else {
//                    Toast.makeText(DashboardActivity.this, "Error in Taking Break", Toast.LENGTH_SHORT).show();
                    System.out.println("244");
                }

            }
        });


    }

    private void isBreakEmployee() {





        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if ( empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(GCurrentDateTime.getCurrentDate()) && !emp.getBreakStartTime().equals("") && !emp.getBreakEndTme().equals("")) {
                        binding.workbreak.setText("Break Completed");
                        isbreakIn = "completed";
                        System.out.println("297");

                    } else if (empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(GCurrentDateTime.getCurrentDate()) && !emp.getBreakStartTime().equals("") && emp.getBreakEndTme().equals("")) {
                        binding.workbreak.setText("End Break");
                        isbreakIn = "true";
                        System.out.println("301");

                    } else if(empId == emp.getEmpId() && empName.equals(emp.getEmpName()) && emp.getDate().equals(GCurrentDateTime.getCurrentDate()) && !emp.getStartTime().equals("") && emp.getBreakStartTime().equals("")) {
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

    private void workStartFunction() {
        //To check yesterday status and update leaves if they are not present
        //  isEmployeePresentYesterDay();


        System.out.println(">>>>Work Start Funtion Called ");//21 june
        binding.llworkbreak.setVisibility(View.VISIBLE);

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
                    if (emp1.getEmpId() == empId && emp1.getDate().equals(GCurrentDateTime.getCurrentDate()) && !emp1.getDayStatus().equals("")) {
                        isPresent = emp1.getDayStatus();
                        rootKey = dataSnapshot.getKey();
                        break;
                    } else {
                        isPresent = "no";
                    }

                }

                if (isPresent.equalsIgnoreCase("Absent")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(SubAdminDashboardActivity.this);
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
                                if (GCurrentDateTime.getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {
                                    String rootKey = dataSnapshot.getKey();
                                    DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                                    Map<String, Object> userUpdates = new HashMap<>();
                                    userUpdates.put(rootKey + "/startTime", GCurrentDateTime.getCurrentTime());
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
                    employeeWorking.setMounth(GCurrentDateTime.getCurrentMonth());
                    employeeWorking.setDate(GCurrentDateTime.getCurrentDate());
                    employeeWorking.setStartTime(GCurrentDateTime.getCurrentTime());
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

    private void workEndFunction() {


        binding.llworkbreak.setVisibility(View.GONE);

        ProgressDialog pd = new ProgressDialog(SubAdminDashboardActivity.this);
        pd.setMessage("Please Wait");
        pd.show();
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference();
        ValueEventListener ab = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if (GCurrentDateTime.getCurrentDate().equals(emp.getDate()) && empId == emp.getEmpId()) {
                        String rootKey = dataSnapshot.getKey();
                        DatabaseReference hopperRef = myRef1.child("EmployeeWorkingDetails");
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put(rootKey + "/endTime", GCurrentDateTime.getCurrentTime());
                        if(!emp.getDayStatus().equalsIgnoreCase("Halfday"))
                        {
                            userUpdates.put(rootKey + "/dayStatus", "Present");
                        }

                        userUpdates.put(rootKey + "/workHours", getWorkHours(emp.getStartTime(), GCurrentDateTime.getCurrentTime()));
                        hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pd.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                pd.dismiss();
                                Toast.makeText(SubAdminDashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SubAdminDashboardActivity.this, "" + error, Toast.LENGTH_SHORT).show();


            }
        };
        myRef1.child("EmployeeWorkingDetails").addValueEventListener(ab);


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
}