package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.databinding.AllRequstLayoutAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllRequestListActivity  extends AppCompatActivity
{
    AllRequstLayoutAdminBinding binding;
    LeaveRequestAdminAdapter adapter;
    ArrayList<EmployeLeavesApplicationRecord> al;
    FirebaseDatabase database;
    DatabaseReference myRef;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

         binding=AllRequstLayoutAdminBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        getALlLeavesRequest();


    }

    private void getALlLeavesRequest()
    {
        al=new ArrayList<>();
        myRef.child("EmployeLeavesApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeLeavesApplicationRecord empl = dataSnapshot.getValue(EmployeLeavesApplicationRecord.class);

                    if(empl.getLeaveStatus().equalsIgnoreCase("Pending"))
                    {
                        Toast.makeText(AllRequestListActivity.this, ""+empl.getEmpName(), Toast.LENGTH_SHORT).show();
                        al.add(empl);
                        adapter.notifyDataSetChanged();
                    }
//

                }


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(AllRequestListActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        adapter=new LeaveRequestAdminAdapter(AllRequestListActivity.this,al,"Pending");
        binding.rvleave.setAdapter(adapter);
        binding.rvleave.setLayoutManager(new LinearLayoutManager(AllRequestListActivity.this));



    }
}
