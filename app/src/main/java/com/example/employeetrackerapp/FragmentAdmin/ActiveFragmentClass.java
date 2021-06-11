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
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.databinding.ActiveEmployeeAdminBinding;
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

public class ActiveFragmentClass extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<EmployeeWorkingDetails> al;
    ActiveEmployeeAdapter adapter;
    ActiveEmployeeAdminBinding binding;
    boolean isFirst = true;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActiveEmployeeAdminBinding.inflate(LayoutInflater.from(getActivity()));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        searchActiveEmployee();
        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        isFirst=true;
    }

    private void searchActiveEmployee() {
        al = new ArrayList<>();
        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    EmployeeWorkingDetails emp = dataSnapshot.getValue(EmployeeWorkingDetails.class);


                    if (emp.getDate().equalsIgnoreCase(getCurrentDate()) && !emp.getStartTime().equals("")) {


                        if (isFirst) {
                            System.out.print(">>>>>>>>>>>>>>> Al is Empty >>>>>>>>>>>>>>>>");
                            al.add(emp);
                        } else {
                            System.out.print(">>>>>>>>>>>>>>> Al is not Empty >>>>>>>>>>>>>>>>");

                            for (int i = 0; i < al.size(); i++) {
                                EmployeeWorkingDetails emp1 = al.get(i);
                                if (emp.getEmpId() == emp1.getEmpId()) {
                                    System.out.print(">>>>>>>>>>>>>>> Present >>>>>>>>>>>>>>>>");
                                    al.remove(i);
                                    al.add(i, emp);
                                    adapter.notifyItemChanged(i);
                                }

                            }
                        }


                        adapter.notifyDataSetChanged();

                        System.out.println(">>>>>>>>>>>>>>>>" + al.size());
                        Toast.makeText(getActivity(), "" + emp.getEmpName() + "" + al.size(), Toast.LENGTH_SHORT).show();


                    }
                }
                isFirst = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });


        adapter = new ActiveEmployeeAdapter(getActivity(), al);
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
