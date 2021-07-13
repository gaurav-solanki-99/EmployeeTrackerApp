package com.example.employeetrackerapp.AdminActivity;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetrackerapp.ChatMessage;
import com.example.employeetrackerapp.Constants;
import com.example.employeetrackerapp.databinding.SingleChatLayoutBinding;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    ArrayList<ChatMessage> al;
    Context context;
    String userId;

    public ChatAdapter(Context context,ArrayList<ChatMessage> al,String userId)
    {
        this.context=context;
        this.al=al;
        this.userId=userId;
    }

    @NonNull

    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       SingleChatLayoutBinding binding = SingleChatLayoutBinding.inflate(LayoutInflater.from(context),parent,false);

        return new ChatViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull  ChatAdapter.ChatViewHolder holder, int position) {

            ChatMessage chatMessage=al.get(position);
            holder.binding.rlMyMessage.setVisibility(View.GONE);
            holder.binding.rlOtherMessage.setVisibility(View.GONE);


            if(userId.equals(chatMessage.getReceiverId()))
            {
                holder.binding.rlMyMessage.setVisibility(View.VISIBLE);
                holder.binding.tvsenderMessage.setText(chatMessage.getMessage());
                holder.binding.tvsenderMessagetime.setText(Constants.formateDate(chatMessage.getMessagetime()));
            }
            else if(userId.equals(chatMessage.getSenderId()))
            {
                holder.binding.rlOtherMessage.setVisibility(View.VISIBLE);
                holder.binding.tvMessage.setText(chatMessage.getMessage());
                holder.binding.tvMessagetime.setText(Constants.formateDate(chatMessage.getMessagetime()));

            }



    }

    @Override
    public int getItemCount() {
        return al.size();
    }






    class ChatViewHolder extends RecyclerView.ViewHolder
    {
        SingleChatLayoutBinding binding;

        public ChatViewHolder(SingleChatLayoutBinding binding)
        {

            super(binding.getRoot());
            this.binding=binding;
        }
    }


}
