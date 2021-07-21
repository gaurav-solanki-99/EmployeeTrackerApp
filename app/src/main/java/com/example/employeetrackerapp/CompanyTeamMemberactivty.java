package com.example.employeetrackerapp;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.example.employeetrackerapp.databinding.CompanyTeamLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CompanyTeamMemberactivty extends AppCompatActivity
{

        CompanyTeamLayoutBinding binding;
        FirebaseDatabase database;
        DatabaseReference myRef;
        ArrayList<EmployeeRecord> al;
        CompanyTeamAdapter adapter;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                binding=CompanyTeamLayoutBinding.inflate(LayoutInflater.from(this));
                setContentView(binding.getRoot());

                getSupportActionBar().hide();
                database=FirebaseDatabase.getInstance();
                myRef=database.getReference();


                AnimatedRecyclerView recyclerView = new AnimatedRecyclerView.Builder(this)
                        .orientation(LinearLayoutManager.VERTICAL)
                        .layoutManagerType(AnimatedRecyclerView.LayoutManagerType.GRID)
                        .animation(R.anim.layout_animation_from_scale)
                        .animationDuration(600)
                        .reverse(false)
                        .build();

//                adapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();










        }

        private void SearchTeam() {
                al=new ArrayList<>();
                myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                al.clear();

                                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                        EmployeeRecord empRecord= dataSnapshot.getValue(EmployeeRecord.class);
                                        if(empRecord.getEmpMember().equalsIgnoreCase("SuperAdmin"))
                                        {

                                        }
                                        else
                                        {
                                                al.add(empRecord);
                                        }

                                        Collections.sort(al, new Comparator<EmployeeRecord>() {
                                                @Override
                                                public int compare(EmployeeRecord lhs, EmployeeRecord rhs) {
                                                        return lhs.getEmpName().compareTo(rhs.getEmpName());
                                                }
                                        });


                                        adapter.notifyDataSetChanged();



                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });


                adapter=new CompanyTeamAdapter(CompanyTeamMemberactivty.this,al);
                binding.rvcompanyTeam.setAdapter(adapter);
                binding.rvcompanyTeam.setLayoutManager(new GridLayoutManager(this,2));
                SnapHelper helper = new LinearSnapHelper();
                helper.attachToRecyclerView(binding.rvcompanyTeam);
        }

        @Override
        protected void onStart() {
                super.onStart();
                SearchTeam();
        }
}
