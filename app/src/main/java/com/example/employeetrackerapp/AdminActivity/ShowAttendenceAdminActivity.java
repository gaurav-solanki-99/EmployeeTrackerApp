package com.example.employeetrackerapp.AdminActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AttendenceActivity;
import com.example.employeetrackerapp.AttendenceAdapter;
import com.example.employeetrackerapp.BuildConfig;
import com.example.employeetrackerapp.Constants;
import com.example.employeetrackerapp.EmployeeAttendence;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.GCurrentDateTime;
import com.example.employeetrackerapp.GMonth;
import com.example.employeetrackerapp.Parag.MonthYearPickerDialog;
import com.example.employeetrackerapp.Parag.MonthYearPickerDialogFragment;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.databinding.ShowAttendenceAdminActivityBinding;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;

public class ShowAttendenceAdminActivity extends AppCompatActivity
{

    ShowAttendenceAdminActivityBinding binding;
    ArrayList<EmployeeAttendence> al;
    AttendenceAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String selectYear,selectMonth;
    EmployeeRecord singleEmp;
    jxl.write.WritableWorkbook workbook;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ShowAttendenceAdminActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        Intent in  = getIntent();
         singleEmp  = (EmployeeRecord)in.getSerializableExtra("SingleEmployeeRecord");
        al=new ArrayList<>();

       selectMonth=GCurrentDateTime.getCurrentMonth();
       selectYear=""+Calendar.getInstance().get(Calendar.YEAR);
       binding.etchosemonth.setText(selectYear+"-"+selectMonth);
       binding.tvadminname.setText(singleEmp.getEmpName());
       binding.tvdepartment.setText(singleEmp.getEmpDepartment());
        Glide.with(getApplicationContext()).load(singleEmp.getEmpProfile()).error(R.drawable.ic_baseline_person_24).into(binding.profileImage);
       filterRecord();
       





        int yearSelected;
        int monthSelected;

//Set default values
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);

        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected);

        binding.etchosemonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), null);
            }
        });

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                // do something
                binding.etchosemonth.setText(year+"-"+ GMonth.getStringMonth(monthOfYear+1));
                selectMonth= GMonth.getStringMonth(monthOfYear+1);
                selectYear=""+year;
                filterRecord();
            }
        });







    }

    private void filterRecord()
    {
        al.clear();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int attendencecount = 0;
                int leavescount =0;
                int halfdaycount=0;


                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp= dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(singleEmp.getEmpid()==emp.getEmpId()&&emp.getMounth().equalsIgnoreCase(selectMonth)&&emp.getDate().endsWith(selectYear))
                    {
                        if (emp.getDayStatus().equalsIgnoreCase("Present")) {
                            attendencecount++;
                        }
                        if (emp.getDayStatus().equalsIgnoreCase("Absent")) {
                            leavescount++;
                        }
                        if (emp.getDayStatus().equalsIgnoreCase("Halfday")) {
                            halfdaycount++;
                        }

                        Toast.makeText(ShowAttendenceAdminActivity.this, ""+emp.getEmpName(), Toast.LENGTH_SHORT).show();

                        al.add(new EmployeeAttendence(emp.getMounth(),emp.getDate(), Constants.formateDate(emp.getStartTime()),Constants.formateDate(emp.getEndTime()),emp.getDayStatus(),emp.getWorkHours()));

                        System.out.println("al Size "+al.size());
                        adapter.notifyDataSetChanged();
                        myRef.child("EmployeeWorkingDetails").removeEventListener(this);
                    }

                    binding.leavescount.setText(""+leavescount);
                    binding.halfdaycount.setText(""+halfdaycount);
                    binding.attendencecount.setText(""+attendencecount);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        adapter=new AttendenceAdapter(ShowAttendenceAdminActivity.this,al);
        binding.rvemplist.setAdapter(adapter);
        binding.rvemplist.setLayoutManager(new LinearLayoutManager(ShowAttendenceAdminActivity.this));


        binding.dataExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else
                {
                    //your code
                    createExcelSheet();
                }

            }
        });

    }

    private void createExcelSheet()
    {
        String csvFile = "EmployeeAttendence.xls";
        java.io.File futureStudioIconFile = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/" + csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            workbook = Workbook.createWorkbook(futureStudioIconFile, wbSettings);
            createFirstSheet();
//            createSecondSheet();
            //closing cursor
            workbook.write();
            workbook.close();
            Toast.makeText(this , "Created Susscess" , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            File file = new File(Environment.getExternalStorageDirectory() + "/FolderName/" + "yourFile.apk");
            Uri data = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",futureStudioIconFile);
            intent.setDataAndType(data,"application/vnd.ms-excel");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this , ""+e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    private void createFirstSheet()
    {
        try {
//            List<Bean> listdata = new ArrayList<>();
//
//            listdata.add(new Bean("Mr Parag Wadhwani","888888888","sdssdsad","ffdfdfdffdfdfdfdff"));
//            listdata.add(new Bean("mr","firstName1","middleName1","lastName1"));
//            listdata.add(new Bean("mr","firstName1","middleName1","lastName1"));
            //Excel sheet name. 0 (number)represents first sheet
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            // column and row title
            sheet.addCell(new Label(0, 0, "empid"));
            sheet.addCell(new Label(1, 0, "empName"));
            sheet.addCell(new Label(2, 0, "empDepartment"));
            sheet.addCell(new Label(3, 0, "workday"));
            sheet.addCell(new Label(4, 0, "workdate"));
            sheet.addCell(new Label(5, 0, "intime"));
            sheet.addCell(new Label(6, 0, "outtime"));
            sheet.addCell(new Label(7, 0, "daystatus"));
            sheet.addCell(new Label(8, 0, "workhours"));


            for (int i = 0; i < al.size(); i++) {
                sheet.addCell(new Label(0, i + 1, ""+singleEmp.getEmpid()));
                sheet.addCell(new Label(1, i + 1, singleEmp.getEmpName()));
                sheet.addCell(new Label(2, i + 1,  singleEmp.getEmpDepartment()));
                sheet.addCell(new Label(3, i + 1, al.get(i).getWorkday()));
                sheet.addCell(new Label(4, i + 1, al.get(i).getWorkdate()));
                sheet.addCell(new Label(5, i + 1, al.get(i).getIntime()));
                sheet.addCell(new Label(6, i + 1, al.get(i).getOuttime()));
                sheet.addCell(new Label(7, i + 1, al.get(i).getDaystatus()));
                sheet.addCell(new Label(8, i + 1, al.get(i).getWorkhours()));









            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
