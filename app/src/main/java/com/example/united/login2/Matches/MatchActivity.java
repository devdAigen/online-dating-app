package com.example.united.login2.Matches;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.united.login2.Home;
import com.example.united.login2.MainActivity;
import com.example.united.login2.R;
import com.example.united.login2.Setting;
import com.example.united.login2.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchedAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private FirebaseAuth myauth;

    private String name,age,userId,profilepicture,crush,heartbreak,sex,contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager=new LinearLayoutManager(MatchActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchedAdapter=new MatchesAdapter(getMatchesDataSet(),MatchActivity.this);
        mRecyclerView.setAdapter(mMatchedAdapter);
        myauth=FirebaseAuth.getInstance();
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getMatcherUserId();

    }

    private void getMatcherUserId() {
        DatabaseReference matchDb= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("connections").child("swipeyes");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot matches:dataSnapshot.getChildren()){
                        fatchMatcherInformation(matches.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fatchMatcherInformation(String key) {
        DatabaseReference userDb= FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String, Object> map=(Map<String,Object>)dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name=map.get("name").toString();
                    }
                    if(map.get("age")!=null){
                        age=map.get("age").toString();
                    }
                    if(map.get("sex")!=null){
                        sex=map.get("sex").toString();
                    }

                    if(map.get("crush")!=null){
                        crush=map.get("crush").toString();
                    }

                    if(map.get("phone")!=null){
                        contact=map.get("phone").toString();
                    }
                    if(map.get("heartbreak")!=null){
                        heartbreak=map.get("heartbreak").toString();
                    }
                    if(map.get("profilepicture")!=null){
                        profilepicture=map.get("profilepicture").toString();
                    }

                    MatchesObject obj=new MatchesObject(dataSnapshot.getKey(),contact,name,profilepicture,age,sex,crush,heartbreak);
                    resultmatches.add(obj);
                    mMatchedAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private ArrayList<MatchesObject> resultmatches= new ArrayList<MatchesObject>();
    private List<MatchesObject> getMatchesDataSet() {

        return resultmatches;
    }


    public void logoutusers(View view) {
        myauth.signOut();
        Toast.makeText(this, "By By...See You Again", Toast.LENGTH_SHORT).show();
        Intent intent1=new Intent(this,MainActivity.class);
        startActivity(intent1);
        finish();
        return;
    }

    public void setting(View view) {
        Intent intent=new Intent(this,UserDetail.class);
        startActivity(intent);
        return;

    }

    public void profiledisplay(View view) {
        Intent intent=new Intent(this,Setting.class);
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent=new Intent(this,MatchActivity.class);
        startActivity(intent);
        return;
    }

    public void goToHome(View view) {
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
        return;
    }
}
