package com.example.employeetrackerapp.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeetrackerapp.AdminAdpters.AllEmployeeAdapter;
import com.example.employeetrackerapp.ChatMessage;
import com.example.employeetrackerapp.EmployeeRecord;
import com.example.employeetrackerapp.GCurrentDateTime;
import com.example.employeetrackerapp.R;
import com.example.employeetrackerapp.databinding.ActivitySingleChatBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleChatActivity extends AppCompatActivity {

    ActivitySingleChatBinding binding;

    FirebaseDatabase database;
    DatabaseReference myRef;
    String senderName,senderId,senderProfile;
    String receiverName,receiverId,receiverProfile;
    SharedPreferences sp;
     ChatAdapter chatAdapter;
     ArrayList<ChatMessage> al;
     int messageCount=0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySingleChatBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        binding.sendbtn.setVisibility(View.GONE);


        Intent in =getIntent();
         EmployeeRecord emp = (EmployeeRecord) in.getSerializableExtra("employeeRecord");
         receiverName=emp.getEmpName();
         receiverId=emp.getFid();
         receiverProfile=emp.getEmpProfile();





             if(emp.getStatus().equalsIgnoreCase("true"))
             {
                 binding.tvStatus.setText("Online");
                 binding.tvStatus.setTextColor(getResources().getColor(R.color.green));

             }
        if(emp.getStatus().equalsIgnoreCase("false"))
             {
                 binding.tvStatus.setText("Offline");
                 binding.tvStatus.setTextColor(getResources().getColor(R.color.gray));
             }






         // Get Data From Shared Preferences
        sp = getSharedPreferences("employeeDetails", MODE_PRIVATE);
        senderId = sp.getString("empFid", null);
        senderName = sp.getString("empName", null);
        senderProfile=sp.getString("empProfile",null);




        searchAllMessage();

         if(emp.getEmpProfile()!=null)
         {
             Glide.with(getApplicationContext()).load(emp.getEmpProfile()).error(R.drawable.employeeprofile).into(binding.profileImage);

         }
        binding.tvname.setText(emp.getEmpName());
        binding.tvposition.setText(emp.getPosition());

        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0)
                    binding.sendbtn.setVisibility(View.VISIBLE);
                else {
                    binding.sendbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
                binding.rvMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.rvMessage.smoothScrollToPosition(binding.rvMessage.getAdapter().getItemCount() - 1);
                    }
                }, 1000);
            }
        });
    }

    private void searchAllMessage()
    {
        al=new ArrayList<>();

        myRef.child("ChatMessage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                al.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ChatMessage chatMessage=dataSnapshot.getValue(ChatMessage.class);
                    if(senderId.equals(chatMessage.getSenderId())&&receiverId.equals(chatMessage.getReceiverId()))
                    {

                        al.add(chatMessage);
                         messageCount++;
                        binding.rvMessage.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.rvMessage.smoothScrollToPosition(binding.rvMessage.getAdapter().getItemCount() - 1);
                            }
                        }, 1000);


                    }else if(receiverId.equals(chatMessage.getSenderId())&&senderId.equals(chatMessage.getReceiverId()))
                    {
                        al.add(chatMessage);
                        messageCount++;
                        binding.rvMessage.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.rvMessage.smoothScrollToPosition(binding.rvMessage.getAdapter().getItemCount() - 1);
                            }
                        }, 1000);
                    }

                    chatAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        chatAdapter =new ChatAdapter(SingleChatActivity.this,al,receiverId);
        binding.rvMessage.setAdapter(chatAdapter);
        binding.rvMessage.setLayoutManager(new LinearLayoutManager(SingleChatActivity.this));


        binding.rvMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up


                } else {
                    // Scrolling down
                    binding.tvmsgcount.setText(""+messageCount);

                }
            }

//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    // Do something
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    // Do something
//                } else {
//                    // Do something
//                }
//
//
//                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
////                    Toast.makeText(SingleChatActivity.this, "Last Position ", Toast.LENGTH_SHORT).show();
//
//                }
//            }
        });


         binding.llDropdown.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 binding.rvMessage.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         binding.rvMessage.smoothScrollToPosition(binding.rvMessage.getAdapter().getItemCount() - 1);
                     }
                 }, 500);

             }
         });


         binding.backbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });

    }

    private void SendMessage() {
        String message=binding.etMessage.getText().toString();
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        String messageId= myRef.child("ChatMessage").push().getKey();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(messageId);
        chatMessage.setMessage(message);
        chatMessage.setMessageStatus("send");
        chatMessage.setMessagetime(GCurrentDateTime.getCurrentTime());
        chatMessage.setReceiverId(receiverId);
        chatMessage.setReceiverName(receiverName);
        chatMessage.setReceiverProfile(receiverProfile);
        chatMessage.setSenderId(senderId);
        chatMessage.setSenderName(senderName);
        chatMessage.setReceiverProfile(receiverProfile);

        myRef.child("ChatMessage").child(messageId).setValue(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SingleChatActivity.this, "Message Send SucessFuly", Toast.LENGTH_SHORT).show();
                binding.etMessage.setText(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(SingleChatActivity.this, "Message not success"+e, Toast.LENGTH_SHORT).show();
            }
        });









    }


}