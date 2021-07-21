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

import com.example.employeetrackerapp.AdminAdpters.LeaveRequestAdminAdapter;
import com.example.employeetrackerapp.EmployeLeavesApplicationRecord;
import com.example.employeetrackerapp.databinding.ShowAllRequestApproveBinding;
import com.example.employeetrackerapp.databinding.ShowAllRequestPendingBinding;
import com.example.employeetrackerapp.databinding.ShowAllRequestRejectBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RejectFragmentClass extends Fragment
{

    LeaveRequestAdminAdapter adapter;
    ArrayList<EmployeLeavesApplicationRecord> al;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ShowAllRequestRejectBinding binding;
    SharedPreferences sp;
    String empType;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=ShowAllRequestRejectBinding.inflate(LayoutInflater.from(getActivity()));

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        sp= getActivity().getSharedPreferences("employeeDetails", Context.MODE_PRIVATE);
        empType=sp.getString("empMember","");
        getALlLeavesRequest();
        return binding.getRoot();
    }

    private void getALlLeavesRequest()
    {
        al=new ArrayList<>();
        myRef.child("EmployeLeavesApplicationRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EmployeLeavesApplicationRecord empl = dataSnapshot.getValue(EmployeLeavesApplicationRecord.class);

                    if(empl.getLeaveStatus().equalsIgnoreCase("Reject"))
                    {
//                        Toast.makeText(getActivity(), ""+empl.getEmpName(), Toast.LENGTH_SHORT).show();
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

        adapter=new LeaveRequestAdminAdapter(getActivity(),al,"Reject");
        binding.rvReject.setAdapter(adapter);
        binding.rvReject.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
