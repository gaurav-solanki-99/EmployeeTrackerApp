package com.example.employeetrackerapp.AdminActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeetrackerapp.AdminAdpters.ActiveEmployeeAdapter;
import com.example.employeetrackerapp.AdminAdpters.ViewPagerActiveEmployeeAdmin;
import com.example.employeetrackerapp.AttendenceActivity;
import com.example.employeetrackerapp.Constants;
import com.example.employeetrackerapp.EmployeeAttendence;
import com.example.employeetrackerapp.EmployeeWorkingDetails;
import com.example.employeetrackerapp.FragmentAdmin.ActiveFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.HalfdayFragmentClass;
import com.example.employeetrackerapp.FragmentAdmin.LeaveFragmentClass;
import com.example.employeetrackerapp.MainActivity;
import com.example.employeetrackerapp.databinding.ActiveEmployeeLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ActivieEmployeeActivity extends AppCompatActivity {

    public ActiveEmployeeLayoutBinding binding;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<EmployeeWorkingDetails> al;
    ActiveEmployeeAdapter adapter;
    ViewPagerActiveEmployeeAdmin viewpageradapter;
    public String dateSeleted = "";

    LeaveFragmentClass leaveFragmentClass; //Fragments
    HalfdayFragmentClass halfdayFragmentClass;
    ActiveFragmentClass activeFragmentClass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActiveEmployeeLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        binding.etdate.setText(getCurrentDate());


        viewpageradapter = new ViewPagerActiveEmployeeAdmin(getSupportFragmentManager());
        binding.veiwPager.setAdapter(viewpageradapter);
        binding.tabLayout.setupWithViewPager(binding.veiwPager);


        Intent in = getIntent();
        String tabStatus = in.getStringExtra("tabStatus");


        switch (tabStatus) {
            case "Present":
                binding.veiwPager.setCurrentItem(0);
                break;
            case "Absent":
                binding.veiwPager.setCurrentItem(1);
                break;
            case "Halfday":
                binding.veiwPager.setCurrentItem(2);
                break;

        }


        Toast.makeText(this, "" + tabStatus, Toast.LENGTH_LONG).show();

        binding.etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(ActivieEmployeeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateSeleted = dayOfMonth + "-" + (month + 1) + "-" + year;
                        DateFormat parser = new SimpleDateFormat("dd-M-yyyy");
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date convertedDate = null;
                        try {
                            convertedDate = parser.parse(dateSeleted);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputDate = formatter.format(convertedDate);

                        binding.etdate.setText(outputDate);

                        if (binding.veiwPager.getCurrentItem()==0){
                            activeFragmentClass.searchActiveEmployee(outputDate);
                        } else if (binding.veiwPager.getCurrentItem()==1){
                            leaveFragmentClass.searchLeaveEmployee(outputDate);
                        }else if (binding.veiwPager.getCurrentItem()==2){
                            halfdayFragmentClass.searchHalfdayEmployee(outputDate);
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });


    }


    class ViewPagerActiveEmployeeAdmin extends FragmentPagerAdapter {


        public ViewPagerActiveEmployeeAdmin(FragmentManager manager) {
            super(manager);
        }

        @NonNull

        @Override
        public Fragment getItem(int position) {

            if (position == 1) {
                leaveFragmentClass = new LeaveFragmentClass(ActivieEmployeeActivity.this);
                return leaveFragmentClass;
            } else if (position == 2) {
                halfdayFragmentClass = new HalfdayFragmentClass(ActivieEmployeeActivity.this);
                return halfdayFragmentClass;
            } else {
                activeFragmentClass = new ActiveFragmentClass(ActivieEmployeeActivity.this);
                return activeFragmentClass;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable

        @Override
        public CharSequence getPageTitle(int position) {
            String tabTitle = "";
            if (position == 0)
                tabTitle = "Active";
            else if (position == 1)
                tabTitle = "Leave";
            else if (position == 2)
                tabTitle = "Halfday";

            return tabTitle;
        }
    }


    private String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }
}
