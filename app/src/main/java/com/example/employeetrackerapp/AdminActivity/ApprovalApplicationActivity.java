package com.example.employeetrackerapp.AdminActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.GMonth;
import com.example.employeetrackerapp.Leaves;
import com.example.employeetrackerapp.LeavesActivity;
import com.example.employeetrackerapp.databinding.RequestApprovalApplicationAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApprovalApplicationActivity  extends AppCompatActivity
{

 RequestApprovalApplicationAdminBinding binding;
 FirebaseDatabase database;
 DatabaseReference myRef;
 EmployeLeavesApplicationRecord empl;
 String status="",remark="";
    int monthcount=0;
    String month="";
    SharedPreferences sp=null;
    String adminName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RequestApprovalApplicationAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        sp=getSharedPreferences("employeeDetails",MODE_PRIVATE);
        adminName=sp.getString("empName",null);





        Intent in=getIntent();
        empl=(EmployeLeavesApplicationRecord) in.getSerializableExtra("ApproveEmp");
        Toast.makeText(this, ""+ empl.getLeaveStartDate(), Toast.LENGTH_LONG).show();
        binding.etLeaveSubject.setText(empl.getLeaveSuject());
        binding.tvStartdate.setText(empl.getLeaveStartDate());
        binding.tvEndDtaeTime.setText(empl.getLeaveEndDate());
        binding.etLeaveDescription.setText(empl.getLeaveDescription());

        int x=1;


        for(String w:empl.getLeaveStartDate().split("-",0)){
            System.out.println(w);
            Log.e(">>>>>>>>>>>>>>> ", " : - "+w);
            if(x==2)
            {
                monthcount=Integer.parseInt(w);
                Log.e(">>>>>>>>>>>>>>> ", "Month Index  : - "+monthcount);
            }
            x++;

        }
        month= GMonth.getStringMonth(monthcount);
        Log.e(">>>>>>>>>>>>>>> ", "Month   : - "+month);
        Log.e(">>>>>>>>>>>>>>> ", "AdminName    : - "+adminName);



        binding.btnApproveLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="Approve";
                remark=binding.etremark.getText().toString();
                if(TextUtils.isEmpty(remark))
                {
                    binding.etremark.setError("Remark Mandatory");
                }
                else
                {
                    updateLeaveStatus();
//                    onBackPressed();

                }

            }
        });

        binding.btnConcelForLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="Reject";
                remark=binding.etremark.getText().toString();
                if(TextUtils.isEmpty(remark))
                {
                    binding.etremark.setError("Remark Mandatory");
                }
                else
                {
                    updateLeaveStatus();
                    onBackPressed();

                }


            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senUserALLRequestActivity();
            }
        });



    }



    private void updateLeaveStatus()
    {
        myRef.child("EmployeLeavesApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    EmployeLeavesApplicationRecord empl1=dataSnapshot.getValue(EmployeLeavesApplicationRecord.class);
                    if(empl.getLeaveStartDate().equals(empl1.getLeaveStartDate())&&empl.getLeaveEndDate().equals(empl1.getLeaveEndDate())&& empl.getEmpId()==empl1.getEmpId())
                    {
                        String rootkey =dataSnapshot.getKey();
                        DatabaseReference hopperRef = myRef.child("EmployeLeavesApplicationRecord");
                        Map<String,Object> userUpdate = new HashMap<>();
                        userUpdate.put(rootkey+"/leaveStatus",status);
                        userUpdate.put(rootkey+"/leaveRemark",remark);
                        userUpdate.put(rootkey+"/adminName",adminName);
                        hopperRef.updateChildren(userUpdate);
                        if(status.equalsIgnoreCase("Approve"))
                        {
                            updateInWorking(empl1.getLeaveStartDate(),empl1.getLeaveEndDate(),empl1);
                            status="";
                        }



//                        Toast.makeText(ApprovalApplicationActivity.this, "Approve", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else
                    {
//                        Toast.makeText(ApprovalApplicationActivity.this, "Data not match", Toast.LENGTH_SHORT).show();
                    }
                   myRef.child("EmployeLeavesApplicationRecord").removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

                Toast.makeText(ApprovalApplicationActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateInWorking(String leaveStartDate, String leaveEndDate, EmployeLeavesApplicationRecord empl2)
    {

        int id = empl2.getEmpId();
        String empName=empl2.getEmpName();
        String empDepartment=empl2.getEmpDepartment();
        Toast.makeText(this, ""+leaveStartDate+"\n"+leaveEndDate, Toast.LENGTH_SHORT).show();

        try {
            ArrayList<Date> dates=new ArrayList<>();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = (Date) formatter.parse(leaveStartDate);
            Date endDate = (Date) formatter.parse(leaveEndDate);
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
                System.out.println(" Date is ..." + ds);


                EmployeeWorkingDetails employeeWorking = new EmployeeWorkingDetails();
                employeeWorking.setEmpId(id);
                employeeWorking.setEmpName(empName);
                employeeWorking.setEmpDepartment(empDepartment);
                employeeWorking.setMounth(month);
                employeeWorking.setDate(ds);
                employeeWorking.setStartTime("");
                employeeWorking.setEndTime("");
                employeeWorking.setDayStatus("Absent");
                employeeWorking.setBreakStartTime("");
                employeeWorking.setBreakEndTme("");
                employeeWorking.setBreakHours("");
                employeeWorking.setWorkHours("");

                myRef.child("EmployeeWorkingDetails").push().setValue(employeeWorking);

                Toast.makeText(this, "Successfully Updated ", Toast.LENGTH_LONG).show();

            }

        }catch (Exception e)
        {
            Toast.makeText(ApprovalApplicationActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }


    }

    private void senUserALLRequestActivity()
    {
            startActivity(new Intent(ApprovalApplicationActivity.this, AllRequestListActivity.class));
            finish();
    }


}
