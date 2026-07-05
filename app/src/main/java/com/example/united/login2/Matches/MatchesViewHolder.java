package com.example.united.login2.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.united.login2.Chats.ChatActivity;
import com.example.united.login2.R;

/**
 * Created by United on 9/26/2018.
 */

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId,Matchname,Matchage,Matchgender,Matchcrush,Matchbreak,Matchcontact;
    public ImageView Matchimage;

    public MatchesViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId=(TextView)itemView.findViewById(R.id.Matchid);
        Matchname=(TextView)itemView.findViewById(R.id.MatchName);
        Matchage=(TextView)itemView.findViewById(R.id.Matchage);
        Matchcrush=(TextView)itemView.findViewById(R.id.crush);
        Matchbreak=(TextView)itemView.findViewById(R.id.heartbreak);
        Matchcontact=(TextView)itemView.findViewById(R.id.contact);

        Matchimage=(ImageView)itemView.findViewById(R.id.matchprofile);



    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(v.getContext(),ChatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("matchId",mMatchId.getText().toString().substring(5));
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }
}
