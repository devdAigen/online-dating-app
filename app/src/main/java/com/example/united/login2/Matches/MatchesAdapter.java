package com.example.united.login2.Matches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.united.login2.MatchFragment;
import com.example.united.login2.R;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by United on 9/26/2018.
 */

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder> {

    private List<MatchesObject> matcheslist;
    private Context context;

    public MatchesAdapter(List<MatchesObject> matcheslist, Context context ) {
            this.matcheslist=matcheslist;
            this.context=context;
    }



    @Override
    public MatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches,null,false);
        RecyclerView.LayoutParams lp= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolder rcv=new MatchesViewHolder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(MatchesViewHolder holder, int position) {
            holder.mMatchId.setText("id : "+matcheslist.get(position).getUserId());
            holder.Matchcontact.setText("Contact : "+matcheslist.get(position).getContact());
            holder.Matchname.setText(matcheslist.get(position).getName());
            holder.Matchage.setText(matcheslist.get(position).getAge());
           // holder.Matchgender.setText(matcheslist.get(position).getSex());
            holder.Matchcrush.setText(matcheslist.get(position).getCrush());
            holder.Matchbreak.setText(matcheslist.get(position).getHeartbreak());

        if(!matcheslist.get(position).getProfilurl().equals("default")){

            Glide.with(context).load(matcheslist.get(position).getProfilurl()).into(holder.Matchimage);
        }else {
            if(matcheslist.get(position).getSex().equals("Male")){
                Glide.with(context).load(R.drawable.maledp).into(holder.Matchimage);
            }else{
                Glide.with(context).load(R.drawable.femaled).into(holder.Matchimage);
            }


        }

    }

    @Override
    public int getItemCount() {
        return matcheslist.size();
    }
}
