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

import com.example.employeetrackerapp.AdminAdpters.HaldayRequestAdminAdapter;
import com.example.employeetrackerapp.EmployeeHalfApplicationRecord;
import com.example.employeetrackerapp.databinding.ShowAllHalfdayrequestApproveBinding;
import com.example.employeetrackerapp.databinding.ShowAllHalfdayrequestPendingBinding;
import com.example.employeetrackerapp.databinding.ShowAllHalfdayrequestRejectBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HalfdayRejectFragmentClass extends Fragment
{


    ShowAllHalfdayrequestRejectBinding binding;
    ArrayList<EmployeeHalfApplicationRecord> al;
    HaldayRequestAdminAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        binding=ShowAllHalfdayrequestRejectBinding.inflate(LayoutInflater.from(getActivity()));
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        getALlLHalfdayeavesRequest();
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

                    if(empl.getHalfdayStatus().equalsIgnoreCase("Reject"))
                    {

                        al.add(empl);
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

        adapter=new HaldayRequestAdminAdapter(getActivity(),al);
        binding.rvReject.setAdapter(adapter);
        binding.rvReject.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
