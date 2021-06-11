package com.example.employeetrackerapp.FragmentAdmin;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.HalfdayEmployeeAdminAdapter;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.Halfday;
import com.example.employeetrackerapp.databinding.HalfdayEmployeeAdminBinding;
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

public class HalfdayFragmentClass extends Fragment
{
    HalfdayEmployeeAdminBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<EmployeeWorkingDetails> al;
    HalfdayEmployeeAdminAdapter adapter;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        binding=HalfdayEmployeeAdminBinding.inflate(LayoutInflater.from(getActivity()));
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        searchHalfdayEmployee();
        return binding.getRoot();
    }

    private void searchHalfdayEmployee()
    {
        al=new ArrayList<>();

        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(emp.getDayStatus().equalsIgnoreCase("Halfday")&&emp.getDate().equalsIgnoreCase(getCurrentDate()))
                    {
                        Toast.makeText(getActivity(), ""+emp.getEmpName(), Toast.LENGTH_SHORT).show();

                        al.add(emp);

                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {

            }
        });

        adapter=new HalfdayEmployeeAdminAdapter(getActivity(),al);
        binding.rvhalfdayemp.setAdapter(adapter);
        binding.rvhalfdayemp.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }
}
