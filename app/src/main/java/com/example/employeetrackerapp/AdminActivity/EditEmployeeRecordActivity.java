package com.example.employeetrackerapp.AdminActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.EmployeeRecord;

import com.example.employeetrackerapp.databinding.EmployeeregistrationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EditEmployeeRecordActivity  extends AppCompatActivity
{
    EmployeeregistrationBinding binding;
    String edate;
    String ejoindate;
    String membertype;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String userImage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=EmployeeregistrationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.btnregsiter.setText("Update");
        binding.tvheading.setText("Edit Record");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();



        Intent in = getIntent();
        EmployeeRecord emp = (EmployeeRecord)in.getSerializableExtra("employeeRecord");
        setRecordOnForm(emp);

        binding.etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(EditEmployeeRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edate = dayOfMonth+"-"+(month+1)+"-"+year;
                        binding.etdob.setText(edate);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        binding.etdatejoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(EditEmployeeRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ejoindate = dayOfMonth+"-"+(month+1)+"-"+year;
                        binding.etdatejoin.setText(ejoindate);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        binding.btnregsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateRecord(emp);

            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                binding.profileImage.setImageBitmap(bitmap);
                uploadImage();
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {

        Toast.makeText(this, "Upload Clicked ", Toast.LENGTH_SHORT).show();

        if(filePath!=null)
        {

            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("uploading Image");
            pd.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            userImage = ""+uri;

                            Log.e("TAG", "Download Url>>>>>>>>>>>  "+userImage );
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+userImage);

                            //Do what you want with the url
                        }
                    });
                    pd.dismiss();
                    Toast.makeText(EditEmployeeRecordActivity.this, "Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(EditEmployeeRecordActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    private void setRecordOnForm(EmployeeRecord emp)
    {
        binding.etfullname.setText(emp.getEmpName());
        binding.etDepartment.setText(emp.getEmpDepartment());
        binding.etdob.setText(emp.getEmpDOB());
        binding.etbloodgroup.setText(emp.getEmpBloodGroup());
        binding.etadharno.setText(emp.getEmpAdharNo());
        binding.etaddress.setText(emp.getEmpAdress());
        binding.etmobileno.setText(emp.getEmpPhone());
        binding.etemail.setText(emp.getEmpEmail());
        binding.etdatejoin.setText(emp.getEmpDateOFjoining());
        binding.etpassword.setText(emp.getEmpPassword());

        if(emp.getEmpProfile()!=null)
        {
            Glide.with(this).load(emp.getEmpProfile()).into(binding.profileImage);
        }
        if(emp.getEmpMember().equalsIgnoreCase("Admin"))
        {
            binding.rdadmin.setChecked(true);
        }else if (emp.getEmpMember().equalsIgnoreCase("Employee"))
        {
            binding.rdemployee.setChecked(true);
        }

    }


    private void updateRecord(EmployeeRecord emp2)
    {


        if(binding.rdadmin.isChecked())
        {
            membertype="Admin";
        }
        else
        {
            membertype="Employee";
        }

        String name = binding.etfullname.getText().toString();
        String department=binding.etDepartment.getText().toString();

        String address=binding.etaddress.getText().toString();
        String phone = binding.etmobileno.getText().toString();
        String email = binding.etemail.getText().toString();
        String password = binding.etpassword.getText().toString();
        String adharno=binding.etadharno.getText().toString();
        String bloodgroup=binding.etbloodgroup.getText().toString();


        if(TextUtils.isEmpty(name)&&TextUtils.isEmpty(department)&&TextUtils.isEmpty(address)&&TextUtils.isEmpty(phone)&&TextUtils.isEmpty(email)&&TextUtils.isEmpty(password)&&TextUtils.isEmpty(adharno)&&TextUtils.isEmpty(bloodgroup))
        {
            Toast.makeText(this, "All fields are Mandatory ", Toast.LENGTH_SHORT).show();

        }
        else
       {

           myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull  DataSnapshot snapshot) {


                   for(DataSnapshot dataSnapshot:snapshot.getChildren())
                   {
                       EmployeeRecord emp = dataSnapshot.getValue(EmployeeRecord.class);
                       if(emp.getEmpid()==emp2.getEmpid())
                       {
                           String rootKey=dataSnapshot.getKey();
                           DatabaseReference hopperRef = myRef.child("EmployeeRecord");
                           Map<String, Object> userUpdates = new HashMap<>();
                           userUpdates.put(rootKey + "/empAdharNo",adharno);
                           userUpdates.put(rootKey + "/empAdress",address);
                           userUpdates.put(rootKey + "/empBloodGroup",bloodgroup);
                           userUpdates.put(rootKey + "/empDOB",edate);
                           userUpdates.put(rootKey + "/empDateOFjoining",ejoindate);
                           userUpdates.put(rootKey + "/empDepartment",department);
                           userUpdates.put(rootKey + "/empEmail",email);
                           userUpdates.put(rootKey + "/empMember",membertype);
                           userUpdates.put(rootKey + "/empName",name);
                           userUpdates.put(rootKey + "/empPassword",password);
                           userUpdates.put(rootKey + "/empPhone",phone);
                           if(userImage!=null)
                           {
                               userUpdates.put(rootKey+"/empProfile",userImage);
                           }



                           hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(EditEmployeeRecordActivity.this, "Sucess fully update", Toast.LENGTH_SHORT).show();
                               }
                           });
                           myRef.child("EmployeeRecord").removeEventListener(this);
                           break;

                          //myRef.child("EmployeeRecord").removeEventListener(this);


                       }
                   }

               }

               @Override
               public void onCancelled(@NonNull  DatabaseError error) {

               }
           });




        }

    }

}
