package com.example.employeetrackerapp.FragmentAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminActivity.ActivieEmployeeActivity;
import com.example.employeetrackerapp.AdminAdpters.ActiveEmployeeAdapter;
import com.example.employeetrackerapp.AdminAdpters.LeaaveEmployeeAdapterAdmin;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.databinding.LeaveEmployeeAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeaveFragmentClass extends Fragment
{

    LeaveEmployeeAdminBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<EmployeeWorkingDetails> al;
    LeaaveEmployeeAdapterAdmin adapter;

    ActivieEmployeeActivity activieEmployeeActivity;

    public LeaveFragmentClass(ActivieEmployeeActivity activity){
        this.activieEmployeeActivity=activity;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
    binding=LeaveEmployeeAdminBinding.inflate(LayoutInflater.from(getActivity()));
    database=FirebaseDatabase.getInstance();
    myRef=database.getReference();


        searchLeaveEmployee(getCurrentDate());
        return binding.getRoot();

    }


    public void searchLeaveEmployee(String date)
    {
        al=new ArrayList<>();
        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);

                    if(emp.getDate().equals(date)&&emp.getDayStatus().equals("Absent"))
                    {

                        al.add(emp);
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new LeaaveEmployeeAdapterAdmin(getActivity(), al);
        binding.rvactivemp.setAdapter(adapter);
        binding.rvactivemp.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }


}
