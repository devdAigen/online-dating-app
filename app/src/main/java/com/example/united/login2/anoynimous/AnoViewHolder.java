package com.example.united.login2.anoynimous;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.united.login2.Chats.ChatActivity;
import com.example.united.login2.R;

public class AnoViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView unknownId;

    public AnoViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        unknownId=(TextView)itemView.findViewById(R.id.unknownid);

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(v.getContext(),ChatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("matchId",unknownId.getText().toString().substring(5));
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }
}
