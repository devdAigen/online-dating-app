package com.example.united.login2.anoynimous;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.united.login2.R;

import java.util.List;

public class ChatlistAdapter extends RecyclerView.Adapter<AnoViewHolder> {

    private List<AnoObject> chatlist;
    private Context context;



    public ChatlistAdapter(List<AnoObject> chatlist, Context context) {
        this.chatlist = chatlist;
        this.context = context;
    }




    @Override
    public AnoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.anonimous_list, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
        AnoViewHolder rcv = new AnoViewHolder((layoutView));
            return rcv;

    }

    @Override
    public void onBindViewHolder(AnoViewHolder holder, int position) {
              holder.unknownId.setText("id : "+chatlist.get(position).getUserId());

    }


    @Override
    public int getItemCount() {
        return chatlist.size();
    }
}