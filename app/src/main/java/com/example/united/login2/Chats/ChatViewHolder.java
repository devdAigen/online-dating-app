package com.example.united.login2.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.united.login2.R;

/**
 * Created by United on 9/26/2018.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;
    public ImageView Matchimage;

    public ChatViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMessage=itemView.findViewById(R.id.messages);
        mContainer=itemView.findViewById(R.id.container);



    }

    @Override
    public void onClick(View v) {

    }
}
