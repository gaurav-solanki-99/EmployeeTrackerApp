package com.example.employeetrackerapp.AdminActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.databinding.AdminGenerateEmployeeBinding;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AdminGenerateEmployeeActivity extends AppCompatActivity
{
    AdminGenerateEmployeeBinding binding;
    public int empid;
    ArrayList<String> al;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    Boolean isAllSet=false;
    String clickImage="";
    private Uri filePath;
    String membertype;
    String userImage;
    String ejoindate;
    private final int PICK_IMAGE_REQUEST = 22;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=AdminGenerateEmployeeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database= FirebaseDatabase.getInstance();
        myRef= database.getReference();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        getLastId();


        binding.etmobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==10)
                {
                    isAllSet=true;
                    binding.etmobileno.setError(null);
                }
                else
                {
                    isAllSet=false;
                    binding.etmobileno.setError("Mobile no should be 10");
                    binding.etmobileno.requestFocus();
                    return;
                }
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        binding.etemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(isValidEmail(s.toString()))
                {
                    isAllSet=true;
                    binding.etemail.setError(null);

                }
                else
                {
                    isAllSet=false;
                    binding.etemail.setError("Not Valid email");
                    binding.etemail.requestFocus();
                    return;

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.btnregsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMember();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickImage="userProfile";
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AdminGenerateEmployeeActivity.this);

            }
        });

        binding.etdatejoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(AdminGenerateEmployeeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ejoindate = dayOfMonth+"-"+(month+1)+"-"+year;
                        binding.etdatejoin.setText(ejoindate);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

    }



    private boolean isValidEmail(String target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private void getLastId()
    {

        al=new ArrayList<>();
        myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {

                    EmployeeRecord empr = dataSnapshot.getValue(EmployeeRecord.class);

                    empid =empr.getEmpid();
                    al.add(empr.getEmpPhone());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {


            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();




                if(clickImage.equalsIgnoreCase("userProfile"))
                {
                    filePath=result.getUri();
                    Glide.with(getApplicationContext()).load(resultUri).into(binding.profileImage);
                    uploadImage();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }

  }

    private void uploadImage()
    {
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
                    Toast.makeText(AdminGenerateEmployeeActivity.this, "Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AdminGenerateEmployeeActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }



    private void addMember() {

        if (binding.rdadmin.isChecked()) {
            membertype = "Admin";
        } else {
            membertype = "Employee";
        }
        String name = binding.etfullname.getText().toString();
        String phone = binding.etmobileno.getText().toString();
        String email = binding.etemail.getText().toString();
        String password = binding.etpassword.getText().toString();
        String firebaseId = myRef.child("EmployeeRecord").push().getKey();



        if (TextUtils.isEmpty(name)) {
            binding.etfullname.setError("Required");
            binding.etfullname.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(phone))
        {
            binding.etmobileno.setError("Required");
            binding.etmobileno.requestFocus();
            return;
        }else if (TextUtils.isEmpty(email))
        {
            binding.etemail.setError("Required");
            binding.etemail.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(password))
        {
            binding.etpassword.setError("Required");
            binding.etpassword.requestFocus();
            return;
        }
        else if (!isAllSet)
        {
            Toast.makeText(this, "Please Check Error ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(al.contains(phone))
            {
                Toast.makeText(this, "No already exist", Toast.LENGTH_SHORT).show();
            }
            else {


                EmployeeRecord emp = new EmployeeRecord();
                emp.setEmpid(empid + 1);
                emp.setEmpName(name);
                emp.setEmpDepartment("Select Departments");
                emp.setEmpMember(membertype);
                emp.setEmpDOB("");
                emp.setEmpAdress("");
                emp.setEmpPhone(phone);
                emp.setEmpEmail(email);
                emp.setEmpEmailPersonal("");
                emp.setEmpDateOFjoining(ejoindate);
                emp.setEmpPassword(password);
                emp.setEmpBloodGroup("Select Blood Group");
                emp.setEmpAdharNo("");
                emp.setEmpProfile(userImage);
                emp.setAdharFrount("");
                emp.setAdharBack("");
                emp.setAdressLine1("");
                emp.setAdressLine2("");
                emp.setCity("");
                emp.setState("");
                emp.setPosition("");
                emp.setFid(firebaseId);
                emp.setIsAllFill("false");


                myRef.child("EmployeeRecord").child(firebaseId).setValue(emp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(AdminGenerateEmployeeActivity.this);
                        ad.setTitle("Update Record Successfully ");
                        ad.setMessage(" Press Ok");
                        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        ad.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(AdminGenerateEmployeeActivity.this);
                        ad.setTitle("Error "+e.getMessage());
                        ad.setMessage("Please Contact to Administratot ");
                        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        ad.show();
                    }
                });



            }


        }


    }
}
