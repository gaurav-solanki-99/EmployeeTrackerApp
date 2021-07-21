package com.example.employeetrackerapp.AdminActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.MainActivity;
import com.example.employeetrackerapp.databinding.EmployeeloginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginMainActivity extends AppCompatActivity {

    EmployeeloginBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String phone, password;
    SharedPreferences sp;
    String isAllRecordFound = "false";
    String isAllSet = "";
    boolean checkAuthenticate = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EmployeeloginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        sp = getSharedPreferences("employeeDetails", MODE_PRIVATE);


        //isAllSetupdate();



        binding.btnemplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendUserToDashborad();





            }
        });

        binding.tvforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(LoginMainActivity.this);
                ad.setMessage("Please Contact To Administrator");
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

    private void isAllSetupdate() {
        String empName = sp.getString("empName", null);
        int empId = sp.getInt("empId", 0);
        myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                    if (empId == emp.getEmpid() && empName.equalsIgnoreCase(emp.getEmpName())) {
                        isAllSet = emp.getIsAllFill();
                        break;

                    }

                }
                myRef.child("EmployeeRecord").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void sendUserToDashborad()
    {
        phone = binding.etphone.getText().toString();
        password = binding.etpassword.getText().toString();



        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Filleds are empty", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog pd = new ProgressDialog(LoginMainActivity.this);
            pd.setMessage("Please wait");
            pd.show();


            myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    boolean status = false;




                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);

                        if (phone.equalsIgnoreCase(emp.getEmpPhone()) && password.equalsIgnoreCase(emp.getEmpPassword()))
                        {
                            checkAuthenticate=true;

                            authenticateEmployee(emp);
                            pd.dismiss();
                            break;

                        }


                    }

//                    if (!status) {
//
//                        AlertDialog.Builder ad = new AlertDialog.Builder(LoginMainActivity.this);
//                        ad.setMessage("Please Check User Phone & Password");
//                        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                return;
//                            }
//                        });
//                        ad.show();
//                    }

                    if (!checkAuthenticate) {
                       pd.dismiss();

                        AlertDialog.Builder ad = new AlertDialog.Builder(LoginMainActivity.this);
                        ad.setMessage("Please Check User Phone & Password");
                        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        ad.show();
                    }

                    myRef.child("EmployeeRecord").removeEventListener(this);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }




            });




        }










    }

    private void authenticateEmployee(EmployeeRecord emp)
    {

        if (emp.getEmpMember().equalsIgnoreCase("Employee")) {
            isAllRecordFound = emp.getIsAllFill();

//            status = true;
            checkAuthenticate=true;
            if (isAllRecordFound.equalsIgnoreCase("true")) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("empId", emp.getEmpid());
                editor.putString("empName", emp.getEmpName());
                editor.putString("empPhone", emp.getEmpPhone());
                editor.putString("empEmail", emp.getEmpEmailPersonal());
                editor.putString("empOfficialEmail", emp.getEmpEmail());
                editor.putString("empAddress", emp.getEmpAdress());
                editor.putString("empDepartment", emp.getEmpDepartment());
                editor.putString("empDOB", emp.getEmpDOB());
                editor.putString("empMember", emp.getEmpMember());
                editor.putString("empProfile", emp.getEmpProfile());
                editor.putString("isAllset", emp.getIsAllFill());
                editor.putString("empFid", emp.getFid());
                editor.putString("empDesignation", emp.getPosition());
                editor.putString("empBloodGrup", emp.getEmpBloodGroup());
                editor.putString("empDtaeOfJoin", emp.getEmpDateOFjoining());


                editor.commit();
                startActivity(new Intent(LoginMainActivity.this, DashboardActivity.class));
                finish();

            } else {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("empId", emp.getEmpid());
                editor.putString("empName", emp.getEmpName());
                editor.putString("empPhone", emp.getEmpPhone());
                editor.putString("empEmail", emp.getEmpEmail());
                editor.putString("empAddress", emp.getEmpAdress());
                editor.putString("empDepartment", emp.getEmpDepartment());
                editor.putString("empDOB", emp.getEmpDOB());
                editor.putString("empMember", emp.getEmpMember());
                editor.putString("empProfile", emp.getEmpProfile());
                editor.putString("isAllset", emp.getIsAllFill());
                editor.putString("empFid", emp.getFid());
                editor.commit();

                Intent in = new Intent(LoginMainActivity.this, AllRecordSetActivity.class);
                in.putExtra("employeeRecord", emp);
                startActivity(in);
                finish();


            }


        } else if (emp.getEmpMember().equalsIgnoreCase("Admin")||emp.getEmpMember().equalsIgnoreCase("SuperAdmin")) {

            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("empId", emp.getEmpid());
            editor.putString("empName", emp.getEmpName());
            editor.putString("empPhone", emp.getEmpPhone());
            editor.putString("empEmail", emp.getEmpEmail());
            editor.putString("empAddress", emp.getEmpAdress());
            editor.putString("empDepartment", emp.getEmpDepartment());
            editor.putString("empDOB", emp.getEmpDOB());
            editor.putString("empMember", emp.getEmpMember());
            editor.putString("empDesignation", emp.getPosition());
            editor.putString("empProfile", emp.getEmpProfile());
            editor.putString("empFid", emp.getFid());

            editor.commit();
//            status = true;
            startActivity(new Intent(LoginMainActivity.this, AdminDashboardActivity.class));
            finish();

        }
        else if (emp.getEmpMember().equalsIgnoreCase("SubAdmin"))
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("empId", emp.getEmpid());
            editor.putString("empName", emp.getEmpName());
            editor.putString("empPhone", emp.getEmpPhone());
            editor.putString("empEmail", emp.getEmpEmail());
            editor.putString("empAddress", emp.getEmpAdress());
            editor.putString("empDepartment", emp.getEmpDepartment());
            editor.putString("empDOB", emp.getEmpDOB());
            editor.putString("empMember", emp.getEmpMember());
            editor.putString("empProfile", emp.getEmpProfile());
            editor.putString("isAllset", emp.getIsAllFill());
            editor.putString("empFid", emp.getFid());
            editor.putString("empDesignation", emp.getPosition());
            editor.commit();
            startActivity(new Intent(LoginMainActivity.this, SubAdminDashboardActivity.class));
            finish();



        }
    }
}
