package com.example.employeetrackerapp.AdminActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.AllEmployeeAdapter;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.databinding.AllEmployeeRecordBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllEmployeeSearchActivity extends AppCompatActivity
{
    AllEmployeeRecordBinding binding;
    ArrayList<EmployeeRecord> al;
    AllEmployeeAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
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



    }


}
