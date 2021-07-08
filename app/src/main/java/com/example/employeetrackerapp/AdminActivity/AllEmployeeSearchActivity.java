package com.example.employeetrackerapp.AdminActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.AllEmployeeAdapter;
import com.example.employeetrackerapp.BuildConfig;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.databinding.AllEmployeeRecordBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;

public class AllEmployeeSearchActivity extends AppCompatActivity
{
    AllEmployeeRecordBinding binding;
    ArrayList<EmployeeRecord> al;
    AllEmployeeAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    jxl.write.WritableWorkbook workbook;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=AllEmployeeRecordBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        searchAllmember();
    }

    private void searchAllmember()
    {
        al=new ArrayList<>();
        myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                al.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                    al.add(emp);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        adapter=new AllEmployeeAdapter(AllEmployeeSearchActivity.this,al);
        binding.rvemplist.setAdapter(adapter);
        binding.rvemplist.setLayoutManager(new LinearLayoutManager(this));

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
                    downloadExcelSheet();

                }
            }
        });


    }

    private void downloadExcelSheet()
    {
        ProgressDialog pd = new ProgressDialog(AllEmployeeSearchActivity.this);
        pd.setMessage("Downloading...");
        pd.show();

        String csvFile = "EmployeeRecordLsit.xls";
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
            pd.setMessage("Success");
            Toast.makeText(this , "Created Susscess" , Toast.LENGTH_LONG).show();

//            Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", futureStudioIconFile );
//            Uri path = Uri.fromFile(futureStudioIconFile);
//            Log.i("Fragment2", String.valueOf(photoURI));
//            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//            pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pdfOpenintent.setDataAndType(photoURI, "application/xls");
//            try {
//                this.startActivity(pdfOpenintent);
//            } catch (ActivityNotFoundException e) {
//
//            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
//            File file = new File(Environment.getExternalStorageDirectory() + "/FolderName/" + "yourFile.apk");
            Uri data = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",futureStudioIconFile);
            intent.setDataAndType(data,"application/vnd.ms-excel");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
            pd.setMessage(e.getMessage());
            pd.dismiss();
        }
        pd.dismiss();


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
            sheet.addCell(new Label(2, 0, "empEmail"));
            sheet.addCell(new Label(3, 0, "empPhone"));
            sheet.addCell(new Label(4, 0, "empAdress"));
            sheet.addCell(new Label(5, 0, "empDOB"));
            sheet.addCell(new Label(6, 0, "empDepartment"));
            sheet.addCell(new Label(7, 0, "empDateOFjoining"));
            sheet.addCell(new Label(8, 0, "empDateOFresign"));
            sheet.addCell(new Label(9, 0, "empPassword"));
            sheet.addCell(new Label(10, 0, "empMember"));
            sheet.addCell(new Label(11, 0, "empBloodGroup"));
            sheet.addCell(new Label(12, 0, "empAdharNo"));
            sheet.addCell(new Label(13, 0, "empProfile"));
            sheet.addCell(new Label(14, 0, "adharFrount"));
            sheet.addCell(new Label(15, 0, "adharBack"));
            sheet.addCell(new Label(16, 0, "adressLine1"));
            sheet.addCell(new Label(17, 0, "adressLine2"));
            sheet.addCell(new Label(18, 0, "city"));
            sheet.addCell(new Label(19, 0, "state"));
            sheet.addCell(new Label(20, 0, "position"));
            sheet.addCell(new Label(21, 0, "fid"));
            sheet.addCell(new Label(22, 0, "empEmailPersonal"));




            for (int i = 0; i < al.size(); i++) {
                sheet.addCell(new Label(0, i + 1, ""+al.get(i).getEmpid()));
                sheet.addCell(new Label(1, i + 1, al.get(i).getEmpName()));
                sheet.addCell(new Label(2, i + 1,  al.get(i).getEmpEmail()));
                sheet.addCell(new Label(3, i + 1, al.get(i).getEmpPhone()));
                sheet.addCell(new Label(4, i + 1, al.get(i).getEmpAdress()));
                sheet.addCell(new Label(5, i + 1, al.get(i).getEmpDOB()));
                sheet.addCell(new Label(6, i + 1, al.get(i).getEmpDepartment()));
                sheet.addCell(new Label(7, i + 1, al.get(i).getEmpDateOFjoining()));
                sheet.addCell(new Label(8, i + 1, al.get(i).getEmpDateOFresign()));
                sheet.addCell(new Label(9, i + 1, al.get(i).getEmpPassword()));
                sheet.addCell(new Label(10, i + 1, al.get(i).getEmpMember()));
                sheet.addCell(new Label(11, i + 1, al.get(i).getEmpBloodGroup()));
                sheet.addCell(new Label(12, i + 1, al.get(i).getEmpAdharNo()));
                sheet.addCell(new Label(13, i + 1, al.get(i).getEmpProfile()));
                sheet.addCell(new Label(14, i + 1, al.get(i).getAdharFrount()));
                sheet.addCell(new Label(15, i + 1, al.get(i).getAdharBack()));
                sheet.addCell(new Label(16, i + 1, al.get(i).getAdressLine1()));
                sheet.addCell(new Label(17, i + 1, al.get(i).getAdressLine2()));
                sheet.addCell(new Label(18, i + 1, al.get(i).getCity()));
                sheet.addCell(new Label(19, i + 1, al.get(i).getState()));
                sheet.addCell(new Label(20, i + 1, al.get(i).getPosition()));
                sheet.addCell(new Label(21, i + 1, al.get(i).getFid()));
                sheet.addCell(new Label(22, i + 1, al.get(i).getEmpEmailPersonal()));








            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
