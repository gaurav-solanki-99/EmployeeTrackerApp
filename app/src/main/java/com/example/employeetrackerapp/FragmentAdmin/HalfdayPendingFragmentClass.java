package com.example.employeetrackerapp.FragmentAdmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.HaldayRequestAdminAdapter;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.databinding.ShowAllHalfdayrequestApproveBinding;
import com.example.employeetrackerapp.databinding.ShowAllHalfdayrequestPendingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HalfdayPendingFragmentClass extends Fragment
{
  ShowAllHalfdayrequestPendingBinding binding;
  ArrayList<EmployeeHalfApplicationRecord> al;
  HaldayRequestAdminAdapter adapter;
  FirebaseDatabase database;
  DatabaseReference myRef;
  String empType;
  SharedPreferences sp;
  @Nullable
  @Override
  public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
    binding=ShowAllHalfdayrequestPendingBinding.inflate(LayoutInflater.from(getActivity()));
    database=FirebaseDatabase.getInstance();
    myRef=database.getReference();
    getALlLHalfdayeavesRequest();
    sp= getActivity().getSharedPreferences("employeeDetails", Context.MODE_PRIVATE);
    empType=sp.getString("empMember","");
    return binding.getRoot();
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

            if(empType.equalsIgnoreCase("SubAdmin")&&empl.getEmpType().equalsIgnoreCase("SubAdmin"))
            {

            }
            else
            {
              al.add(empl);

            }
            adapter.notifyDataSetChanged();

          }
//

        }


      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
      }
    });

    adapter=new HaldayRequestAdminAdapter(getActivity(),al,"Pending");
    binding.rvPending.setAdapter(adapter);
    binding.rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
  }
}