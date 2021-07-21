package com.example.employeetrackerapp.AdminActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddEmployeeAdminActivity  extends AppCompatActivity {

    EmployeeregistrationBinding binding;
    public int empid;
    String edate;
    String ejoindate;
    String membertype;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> al;
    String userImage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PICK_ADHARFRONT_REQUEST = 23;
    private final int PICK_ADHARBACK_REQUEST = 24;
    FirebaseStorage storage;
    StorageReference storageReference;
    String[] bloodgroup = { "Select Blood Group","O+", "O-", "A+", "A-", "B+","B-","AB+","AB-" };
    String[] departments = { "Select Department","Admin", "Manager", "Buisness Development Executive", "Android Team", "Web Team", "Office Boy", "HR Team"};
    String selectbloodgroup,selectdepartment,adharFrontImage,adharBackImage;
    Boolean isAllSet=false;
    String clickImage="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=EmployeeregistrationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef= database.getReference();
        storage=FirebaseStorage.getInstance();
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


        binding.etadharno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==12)
                {
                    isAllSet=true;
                    binding.etadharno.setError(null);

                }
                else
                {
                    isAllSet=false;
                    binding.etadharno.setError("Adhar no should be 12");
                    binding.etadharno.requestFocus();
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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


        binding.etemailPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(isValidEmail(s.toString()))
                {
                    isAllSet=true;
                    binding.etemailPersonal.setError(null);

                }
                else
                {
                    isAllSet=false;
                    binding.etemailPersonal.setError("Not Valid email");
                    binding.etemailPersonal.requestFocus();
                    return;

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bloodgroup);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBloodGrop.setAdapter(aa);

        ArrayAdapter dd = new ArrayAdapter(this,android.R.layout.simple_list_item_1,departments);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDepartment.setAdapter(dd);





        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AddEmployeeAdminActivity.this, ""+departments[position], Toast.LENGTH_SHORT).show();
                selectdepartment=departments[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        binding.spinnerBloodGrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AddEmployeeAdminActivity.this, ""+bloodgroup[position], Toast.LENGTH_SHORT).show();
                selectbloodgroup=bloodgroup[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnregsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMember();
            }
        });

        binding.etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(AddEmployeeAdminActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dp = new DatePickerDialog(AddEmployeeAdminActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ejoindate = dayOfMonth+"-"+(month+1)+"-"+year;
                        binding.etdatejoin.setText(ejoindate);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AddEmployeeAdminActivity.this, "Select Image Function Called ", Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(
//                        Intent.createChooser(
//                                intent,
//                                "Select Image from here..."),
//                        PICK_IMAGE_REQUEST);
                clickImage="userProfile";
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddEmployeeAdminActivity.this);

            }
        });

        binding.frontadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage="frontAdhar";
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddEmployeeAdminActivity.this);
            }
        });

        binding.backadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage="backAdhar";
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddEmployeeAdminActivity.this);
            }
        });




    }

    private void uploadImage() {
//            Toast.makeText(this, "Upload Clicked ", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(AddEmployeeAdminActivity.this, "Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddEmployeeAdminActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
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


                if(clickImage.equalsIgnoreCase("frontAdhar"))
                {

                    Glide.with(getApplicationContext()).load(resultUri).into(binding.frontadhar);
                    uploadAdharImage(result.getUri());
                }
                else if(clickImage.equalsIgnoreCase("backAdhar"))
                {
                    adharBackImage=""+result.getUri();
                    Glide.with(getApplicationContext()).load(resultUri).into(binding.backadhar);
                    uploadAdharImage(result.getUri());
                }
                else if(clickImage.equalsIgnoreCase("userProfile"))
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





//        if (requestCode == PICK_IMAGE_REQUEST
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//
//            // Get the Uri of data
//            filePath = data.getData();
//            try {
//
//                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                getContentResolver(),
//                                filePath);
//                binding.profileImage.setImageBitmap(bitmap);
//                uploadImage();
//            }
//
//            catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
    }

    private void uploadAdharImage(Uri adharImage)
    {
        if(adharImage!=null)
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

            ref.putFile(adharImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(clickImage.equalsIgnoreCase("frontAdhar"))
                            {
                                adharFrontImage=""+uri;
                            }
                            else if(clickImage.equalsIgnoreCase("backAdhar"))
                            {
                                adharBackImage=""+uri;
                            }


                        }
                    });
                    pd.dismiss();
                    Toast.makeText(AddEmployeeAdminActivity.this, "Succesfully Upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddEmployeeAdminActivity.this, "Failure", Toast.LENGTH_SHORT).show();
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
        String position = binding.etPosition.getText().toString();

        String address = binding.etaddress.getText().toString();
        String phone = binding.etmobileno.getText().toString();
        String email = binding.etemail.getText().toString();
        String emailPersonal=binding.etemailPersonal.getText().toString();
        String password = binding.etpassword.getText().toString();
        String adharno = binding.etadharno.getText().toString();
        String address2 = binding.etaddress2.getText().toString();
        String city=binding.etaddresscity.getText().toString();
        String state=binding.etaddressState.getText().toString();
        selectbloodgroup = ""+binding.spinnerBloodGrop.getSelectedItem();
        selectdepartment = ""+binding.spinnerDepartment.getSelectedItem();
        String firebaseId = myRef.child("EmployeeRecord").push().getKey();



        if (TextUtils.isEmpty(name)) {
            binding.etfullname.setError("Required");
            binding.etfullname.requestFocus();
            return;
        } else if (TextUtils.isEmpty(position))
        {
            binding.etPosition.setError("Required");
            binding.etPosition.requestFocus();
            return;
        }else if (TextUtils.isEmpty(address))
        {
            binding.etaddress.setError("Required");
            binding.etaddress.requestFocus();
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
        else if (TextUtils.isEmpty(emailPersonal))
        {
            binding.etemailPersonal.setError("Required");
            binding.etemailPersonal.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            binding.etpassword.setError("Required");
            binding.etpassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(adharno))
        {
            binding.etadharno.setError("Required");
            binding.etadharno.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(address2))
        {
            binding.etaddress2.setError("Required");
            binding.etaddress2.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(city))
        {
            binding.etaddresscity.setError("Required");
            binding.etaddresscity.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(state))
        {
            binding.etaddressState.setError("Required");
            binding.etaddressState.requestFocus();
            return;
        }else if(binding.spinnerBloodGrop.getSelectedItem().toString().equalsIgnoreCase("Select Blood Group"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(AddEmployeeAdminActivity.this);
            ad.setMessage("Please Select Blood Group"+selectbloodgroup );
            ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    binding.spinnerBloodGrop.requestFocus();
                    return;

                }

            });
            ad.show();
        }
        else if(selectdepartment.equalsIgnoreCase("Select Department"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(AddEmployeeAdminActivity.this);
            ad.setMessage("Please Select Employee Department");
            ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    binding.spinnerDepartment.requestFocus();
                    return;

                }
            });
            ad.show();
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
                emp.setEmpDepartment(selectdepartment);
                emp.setEmpMember(membertype);
                emp.setEmpDOB(edate);
                emp.setEmpAdress(address);
                emp.setEmpPhone(phone);
                emp.setEmpEmail(email);
                emp.setEmpEmailPersonal(emailPersonal);
                emp.setEmpDateOFjoining(ejoindate);
                emp.setEmpPassword(password);
                emp.setEmpBloodGroup(selectbloodgroup);
                emp.setEmpAdharNo(adharno);
                emp.setEmpProfile(userImage);
                emp.setAdharFrount(adharFrontImage);
                emp.setAdharBack(adharBackImage);
                emp.setAdressLine1(address);
                emp.setAdressLine2(address2);
                emp.setCity(city);
                emp.setState(state);
                emp.setPosition(position);
                emp.setFid(firebaseId);


                myRef.child("EmployeeRecord").child(firebaseId).setValue(emp);
//                myRef.child("EmployeeRecord").setValue(emp);

                Toast.makeText(this, "Add User Successfully ", Toast.LENGTH_SHORT).show();
            }



//            Toast.makeText(this, "Button Clicked ", Toast.LENGTH_SHORT).show();
        }





//        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(selectdepartment)||TextUtils.isEmpty(address)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(adharno)||TextUtils.isEmpty(selectbloodgroup)
//          ||TextUtils.isEmpty(userImage))
//        {
//            Toast.makeText(this, "All fields are Mandatory ", Toast.LENGTH_SHORT).show();
//
//        }
//        else
//        {
//            if(al.contains(phone))
//            {
//                Toast.makeText(this, "No already exist", Toast.LENGTH_SHORT).show();
//            }
//            else {
//
//
//                EmployeeRecord emp = new EmployeeRecord();
//                emp.setEmpid(empid + 1);
//                emp.setEmpName(name);
//                emp.setEmpDepartment(selectdepartment);
//                emp.setEmpMember(membertype);
//                emp.setEmpDOB(edate);
//                emp.setEmpAdress(address);
//                emp.setEmpPhone(phone);
//                emp.setEmpEmail(email);
//                emp.setEmpDateOFjoining(ejoindate);
//                emp.setEmpPassword(password);
//                emp.setEmpBloodGroup(selectbloodgroup);
//                emp.setEmpAdharNo(adharno);
//                emp.setEmpProfile(userImage);
//
//
//                myRef.child("EmployeeRecord").push().setValue(emp);
//                Toast.makeText(this, "Add User Successfully ", Toast.LENGTH_SHORT).show();
//            }
//
//
//
//        }




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
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}