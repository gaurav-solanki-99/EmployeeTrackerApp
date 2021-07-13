package com.example.employeetrackerapp.AdminAdpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminActivity.EditEmployeeRecordActivity;
import com.example.employeetrackerapp.AdminActivity.ShowAttendenceAdminActivity;
import com.example.employeetrackerapp.AdminActivity.SingleChatActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.GCurrentDateTime;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.TotitleClass;
import com.example.employeetrackerapp.databinding.LayoutRecyclerAllEmployeeRecordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllEmployeeAdapter extends RecyclerView.Adapter<AllEmployeeAdapter.AllEmployeeViewHolder> {

    Context context;
    ArrayList<EmployeeRecord> al;


    public AllEmployeeAdapter(Context context, ArrayList<EmployeeRecord> al) {
        this.context = context;
        this.al = al;

    }

    @NonNull

    @Override
    public AllEmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRecyclerAllEmployeeRecordBinding binding = LayoutRecyclerAllEmployeeRecordBinding.inflate(LayoutInflater.from(context), parent, false);


        return new AllEmployeeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllEmployeeAdapter.AllEmployeeViewHolder holder, int position) {
        EmployeeRecord emp = al.get(position);
        checkEmployeeLogin(emp,holder.binding);
        holder.binding.tvempname.setText(TotitleClass.convertToTitleCaseIteratingChars(emp.getEmpName()));
        holder.binding.tvempdepartment.setText(TotitleClass.convertToTitleCaseIteratingChars(emp.getPosition()));
        //holder.binding.empType.setText(emp.getEmpMember());
        if (emp.getEmpProfile() != null) {
            Glide.with(context).load(emp.getEmpProfile()).error(R.drawable.ic_baseline_person_24).into(holder.binding.profileImage);
        }

        holder.binding.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, SingleChatActivity.class);
                in.putExtra("employeeRecord", emp);
                if (holder.binding.loginImg.getVisibility() == View.VISIBLE) {
                    // Its visible

                    emp.setStatus("true");
                } else {
                    // Either gone or invisible
                    emp.setStatus("false");
                }
                context.startActivity(in);
            }
        });

        holder.binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.binding.ivMenu);
                Menu menu = popupMenu.getMenu();
                menu.add("Edit");
                menu.add("Delete");
                menu.add("Attendence");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();

                        switch (title) {
                            case "Edit":
                                Intent in = new Intent(context, EditEmployeeRecordActivity.class);
                                in.putExtra("employeeRecord", emp);
                                context.startActivity(in);
                                break;
                            case "Delete":
                                DeleteEmployeeRecord(emp);
                                break;
                            case "Attendence":
                                showAttendence(emp);
                                break;
                        }


                        return false;

                    }
                });
                popupMenu.show();

            }
        });

    }

    private void checkEmployeeLogin(EmployeeRecord emp2, LayoutRecyclerAllEmployeeRecordBinding binding)
    {
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        myRef.child("EmployeeWorkingDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    EmployeeWorkingDetails empRecord = dataSnapshot.getValue(EmployeeWorkingDetails.class);
                    if(emp2.getEmpid()==empRecord.getEmpId()&&!empRecord.getStartTime().equals("")&&empRecord.getEndTime().equals("")&& empRecord.getDate().equalsIgnoreCase(GCurrentDateTime.getCurrentDate()))
                    {
                        binding.loginImg.setVisibility(View.VISIBLE);




                    }


                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAttendence(EmployeeRecord emp)
    {

        Intent in = new Intent(context, ShowAttendenceAdminActivity.class);
        in.putExtra("SingleEmployeeRecord",emp);
        context.startActivity(in);
    }

    private void DeleteEmployeeRecord(EmployeeRecord emp) {
        Toast.makeText(context, "" + emp.getEmpName() + "" + emp.getEmpid(), Toast.LENGTH_SHORT).show();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("EmployeeRecord").child(emp.getFid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    class AllEmployeeViewHolder extends RecyclerView.ViewHolder {
        LayoutRecyclerAllEmployeeRecordBinding binding;

        public AllEmployeeViewHolder(LayoutRecyclerAllEmployeeRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
