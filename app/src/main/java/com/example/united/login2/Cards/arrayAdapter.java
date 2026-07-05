package com.example.united.login2.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.united.login2.Cards.cards;
import com.example.united.login2.R;

import java.util.List;

/**
 * Created by United on 8/21/2018.
 */

public class arrayAdapter  extends ArrayAdapter<cards>{
    Context context;
    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context,resourceId,items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card_items=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profiles,parent, false) ;
        }
        TextView name=(TextView)convertView.findViewById(R.id.helloText);
        TextView age=(TextView)convertView.findViewById(R.id.textViewAge);
        ImageView image=(ImageView)convertView.findViewById(R.id.images);

        name.setText(card_items.getName());
        if(!card_items.getAge().equals(0)){
            age.setText(" ("+card_items.getAge()+")");
        }

        if(!card_items.getProfilurl().equals("default")){

           Glide.with(getContext()).load(card_items.getProfilurl()).into(image);
        }else {
            if(card_items.getSex().equals("Male")){
                Glide.with(getContext()).load(R.drawable.maledp).into(image);
            }else{
                Glide.with(getContext()).load(R.drawable.femaled).into(image);
            }

        }

        return convertView;
    }
}
