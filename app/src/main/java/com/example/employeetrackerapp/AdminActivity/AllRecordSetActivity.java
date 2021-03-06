package com.example.employeetrackerapp.AdminActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.DashboardActivity;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.R;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllRecordSetActivity extends AppCompatActivity
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
    ArrayAdapter aa,dd;
    String[] bloodgroup = { "Select Blood Group","O+", "O-", "A+", "A-", "B+","B-","AB+","AB-" };
    String[] departments = { "Select Departments", "Buisness Development Executive", "Android Team", "Web Team", "Office Boy", "HR Team"};
    String selectbloodgroup="Select Blood Group",selectdepartment="Select Departments",adharFrontImage,adharBackImage;
    String clickImage="";
    Boolean isAllSet=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=EmployeeregistrationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.btnregsiter.setText("Update");
        binding.tvheading.setText("Fill Record");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        binding.llmembertype.setVisibility(View.GONE);




        Intent in = getIntent();
        EmployeeRecord emp = (EmployeeRecord)in.getSerializableExtra("employeeRecord");
        userImage=emp.getEmpProfile();
        selectdepartment=emp.getEmpDepartment();


        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bloodgroup);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBloodGrop.setAdapter(aa);

        dd = new ArrayAdapter(this,android.R.layout.simple_list_item_1,departments);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDepartment.setAdapter(dd);

        binding.backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp =getSharedPreferences("employeeDetails",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                Intent in = new Intent(AllRecordSetActivity.this, LoginMainActivity.class);
                startActivity(in);
                finish();


            }
        });


        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), ""+departments[position], Toast.LENGTH_SHORT).show();
                selectdepartment=departments[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        binding.spinnerBloodGrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), ""+bloodgroup[position], Toast.LENGTH_SHORT).show();
                selectbloodgroup=bloodgroup[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        setRecordOnForm(emp);

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

//        binding.etadharno.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.toString().length()==16)
//                {
//                    isAllSet=true;
//                    binding.etadharno.setError(null);
//
//                }
//                else
//                {
//                    isAllSet=false;
//                    binding.etadharno.setError("Adhar no should be 12");
//                    binding.etadharno.requestFocus();
//                    return;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        binding.etadharno.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 14; // size of pattern 0000-0000-0000
            private static final int TOTAL_DIGITS = 12; // max numbers of digits in pattern: 0000 x 3
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = ' ';
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().length()==14)
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
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }
            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }
            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }
                return formatted.toString();
            }
            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
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
















        binding.etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(AllRecordSetActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dp = new DatePickerDialog(AllRecordSetActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        binding.frontadharbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage="frontAdhar";
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AllRecordSetActivity.this);
                binding.frontadhar.setVisibility(View.VISIBLE);

            }
        });

        binding.backadharbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage="backAdhar";
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AllRecordSetActivity.this);
                binding.backadhar.setVisibility(View.VISIBLE);
            }
        });




    }

    private boolean isValidEmail(String target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());

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
                    binding.frontadharbtn.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(resultUri).into(binding.frontadhar);
                    uploadAdharImage(result.getUri());
                }
                else if(clickImage.equalsIgnoreCase("backAdhar"))
                {
                    binding.backadharbtn.setVisibility(View.GONE);
                    adharBackImage=""+result.getUri();
                    Glide.with(getApplicationContext()).load(resultUri).into(binding.backadhar);
                    uploadAdharImage(result.getUri());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }

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
                    Toast.makeText(AllRecordSetActivity.this, "Succesfully Upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AllRecordSetActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            });
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
                    Toast.makeText(AllRecordSetActivity.this, "Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AllRecordSetActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    private void setRecordOnForm(EmployeeRecord emp)
    {
        binding.etfullname.setText(emp.getEmpName());
//        binding.etDepartment.set(emp.getEmpDepartment());
        binding.etdob.setText(emp.getEmpDOB());
//        binding.etbloodgroup.setText(emp.getEmpBloodGroup());
        binding.etadharno.setText(emp.getEmpAdharNo());
        binding.etaddress.setText(emp.getAdressLine1());
        binding.etmobileno.setText(emp.getEmpPhone());
        binding.etemail.setText(emp.getEmpEmail());
        binding.etemailPersonal.setText(emp.getEmpEmailPersonal());
        binding.etdatejoin.setText(emp.getEmpDateOFjoining());
        binding.etpassword.setText(emp.getEmpPassword());
        binding.etPosition.setText(emp.getPosition());
        binding.etaddress2.setText(emp.getAdressLine2());
        binding.etaddresscity.setText(emp.getCity());
        binding.etaddressState.setText(emp.getState());
        selectbloodgroup=emp.getEmpBloodGroup();
        selectdepartment=emp.getEmpDepartment();
        binding.spinnerDepartment.setSelection(dd.getPosition(selectdepartment));
        binding.spinnerBloodGrop.setSelection(aa.getPosition(selectbloodgroup));
        adharFrontImage=emp.getAdharFrount();
        adharBackImage= emp.getAdharBack();







        if(emp.getEmpProfile()!=null)
        {
            Glide.with(this).load(emp.getEmpProfile()).into(binding.profileImage);
        }

        if(emp.getAdharBack()!=null)
        {
            Glide.with(this).load(emp.getAdharBack()).error(R.drawable.ic_baseline_credit_card_24).into(binding.backadhar);

        }
        if(emp.getAdharFrount()!=null)
        {
            Glide.with(this).load(emp.getAdharFrount()).error(R.drawable.ic_baseline_credit_card_24).into(binding.frontadhar);

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
        String address=binding.etaddress.getText().toString();
        String phone = binding.etmobileno.getText().toString();
        String email = binding.etemail.getText().toString();
        String emailPersonal = binding.etemailPersonal.getText().toString();
        String password = binding.etpassword.getText().toString();
        String adharno=binding.etadharno.getText().toString().replaceAll("\\s", "");
        String position=binding.etPosition.getText().toString();
        String address2=binding.etaddress2.getText().toString();
        String city=binding.etaddresscity.getText().toString();
        String state=binding.etaddressState.getText().toString();
        selectbloodgroup = ""+binding.spinnerBloodGrop.getSelectedItem();
        selectdepartment = ""+binding.spinnerDepartment.getSelectedItem();
        edate=binding.etdob.getText().toString();
        ejoindate=binding.etdatejoin.getText().toString();






        if (TextUtils.isEmpty(userImage)) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("Please select Profoile");
            ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            ad.show();

//            binding.profileImage.setError("Required");
            binding.profileImage.setFocusable(true);
            binding.profileImage.setFocusableInTouchMode(true);
            binding.profileImage.requestFocus();
        }
        else if (TextUtils.isEmpty(name)) {
            binding.etfullname.setError("Required");
            binding.etfullname.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(position))
        {
            binding.etPosition.setError("Required");
            binding.etPosition.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(edate))
        {
            binding.etdob.setError("Required");
            binding.etPosition.requestFocus();
        }else if(TextUtils.isEmpty(adharno))
        {
            binding.etadharno.setError("Required");
            binding.etadharno.requestFocus();
        }
        else if(TextUtils.isEmpty(adharFrontImage))
        {
            binding.frontadharbtn.setError("Required");
            binding.frontadharbtn.setFocusable(true);
            binding.frontadharbtn.setFocusableInTouchMode(true);
            binding.frontadharbtn.requestFocus();
        }
        else if(TextUtils.isEmpty(adharBackImage))
        {
            binding.backadharbtn.setError("Required");
            binding.backadharbtn.setFocusable(true);
            binding.backadharbtn.setFocusableInTouchMode(true);
            binding.backadharbtn.requestFocus();
        }
        else if(TextUtils.isEmpty(address))
        {
            binding.etaddress.setError("Required");
            binding.etaddress.requestFocus();
        } else if(TextUtils.isEmpty(address2))
        {
            binding.etaddress2.setError("Required");
            binding.etaddress2.requestFocus();
        }
        else if(TextUtils.isEmpty(city))
        {
            binding.etaddresscity.setError("Required");
            binding.etaddresscity.requestFocus();
        }
        else if(TextUtils.isEmpty(state))
        {
            binding.etaddressState.setError("Required");
            binding.etaddressState.requestFocus();
        }
        else if(TextUtils.isEmpty(phone))
        {
            binding.etmobileno.setError("Required");
            binding.etmobileno.requestFocus();
        }
        else if(TextUtils.isEmpty(phone))
        {
            binding.etmobileno.setError("Required");
            binding.etmobileno.requestFocus();
        }
        else if(TextUtils.isEmpty(email))
        {
            binding.etemail.setError("Required");
            binding.etemail.requestFocus();
        }
        else if(TextUtils.isEmpty(emailPersonal))
        {
            binding.etemailPersonal.setError("Required");
            binding.etemailPersonal.requestFocus();
        }
        else if(TextUtils.isEmpty(ejoindate))
        {
            binding.etdatejoin.setError("Required");
            binding.etdatejoin.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            binding.etpassword.setError("Required");
            binding.etpassword.requestFocus();
        }
        else
        {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Please Wait");
            pd.show();


            myRef.child("EmployeeRecord").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


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
                            userUpdates.put(rootKey + "/empBloodGroup",selectbloodgroup);
                            userUpdates.put(rootKey + "/empDOB",edate);
                            userUpdates.put(rootKey + "/empDateOFjoining",ejoindate);
                            userUpdates.put(rootKey + "/empDepartment",selectdepartment);
                            userUpdates.put(rootKey + "/empEmail",email);
                            userUpdates.put(rootKey + "/empMember",membertype);
                            userUpdates.put(rootKey + "/empName",name);
                            userUpdates.put(rootKey + "/empPassword",password);
                            userUpdates.put(rootKey + "/empPhone",phone);
                            userUpdates.put(rootKey + "/adressLine1",address);
                            userUpdates.put(rootKey + "/adressLine2",address2);
                            userUpdates.put(rootKey + "/city",city);
                            userUpdates.put(rootKey + "/state",state);
                            userUpdates.put(rootKey + "/position",position);
                            userUpdates.put(rootKey + "/isAllFill","true");


                            if(userImage!=null)
                            {
                                userUpdates.put(rootKey+"/empProfile",userImage);
                            }
                            if(adharFrontImage!=null)
                            {
                                userUpdates.put(rootKey+"/adharFrount",adharFrontImage);

                            }
                            if(adharFrontImage!=null)
                            {
                                userUpdates.put(rootKey+"/adharBack",adharBackImage);
                            }



                            hopperRef.updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {



//                                    Toast.makeText(AllRecordSetActivity.this, "Sucess fully update", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp =getSharedPreferences("employeeDetails",MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("empId",emp.getEmpid());
                                    editor.putString("empName",emp.getEmpName());
                                    editor.putString("empPhone",emp.getEmpPhone());
                                    editor.putString("empEmail",emp.getEmpEmail());
                                    editor.putString("empAddress",emp.getEmpAdress());
                                    editor.putString("empDepartment",emp.getEmpDepartment());
                                    editor.putString("empDOB",emp.getEmpDOB());
                                    editor.putString("empMember",emp.getEmpMember());
                                    editor.putString("empProfile",emp.getEmpProfile());
                                    editor.putString("isAllset",emp.getIsAllFill());
                                    editor.commit();
                                    startActivity(new Intent(AllRecordSetActivity.this, DashboardActivity.class));

                                    finish();


                                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>"+emp.getIsAllFill());

                                }
                            });
                            myRef.child("EmployeeRecord").removeEventListener(this);
                            break;

                            //myRef.child("EmployeeRecord").removeEventListener(this);


                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    pd.dismiss();

                }
            });

            pd.dismiss();



        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder ad = new AlertDialog.Builder(AllRecordSetActivity.this);
        ad.setMessage("Are you want to exit ?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                     return;
            }
        });
        ad.show();
    }
}
