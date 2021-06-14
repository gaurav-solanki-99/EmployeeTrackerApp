package com.example.employeetrackerapp.AdminActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.AllRequestListActivity;
import com.example.employeetrackerapp.AdminAdpters.HaldayRequestAdminAdapter;
import com.example.employeetrackerapp.AdminAdpters.HalfdayEmployeeAdminAdapter;
import com.example.employeetrackerapp.AdminAdpters.LeaveRequestAdminAdapter;
import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.databinding.AllRequstLayoutAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllHalfdayRequestListActivity extends AppCompatActivity
{
    AllRequstLayoutAdminBinding binding;
    ArrayList<EmployeeHalfApplicationRecord> al;
    HaldayRequestAdminAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=AllRequstLayoutAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.leaveRequest.setText("HalfDay Request");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        getALlLHalfdayeavesRequest();

        Toast.makeText(this, "AllHaldayRequestListActivity", Toast.LENGTH_SHORT).show();

    }

    private void getALlLHalfdayeavesRequest()
    {
        al=new ArrayList<>();
        myRef.child("EmployeeHalfApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeeHalfApplicationRecord empl = dataSnapshot.getValue(EmployeeHalfApplicationRecord.class);

                    if(empl.getHalfdayStatus().equalsIgnoreCase("Pending"))
                    {
                        Toast.makeText(AllHalfdayRequestListActivity.this, ""+empl.getEmpName(), Toast.LENGTH_SHORT).show();
                        al.add(empl);
                        adapter.notifyDataSetChanged();
                    }
//

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllHalfdayRequestListActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        adapter=new HaldayRequestAdminAdapter(AllHalfdayRequestListActivity.this,al);
        binding.rvleave.setAdapter(adapter);
        binding.rvleave.setLayoutManager(new LinearLayoutManager(AllHalfdayRequestListActivity.this));
    }
}
