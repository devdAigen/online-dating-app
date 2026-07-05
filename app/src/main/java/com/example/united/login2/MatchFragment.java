package com.example.united.login2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.united.login2.Matches.MatchesAdapter;
import com.example.united.login2.Matches.MatchesObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchedAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private FirebaseAuth myauth;
    ProgressDialog progressDialog;

    private String name,age,userId,profilepicture,crush,heartbreak,sex,contact;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.fragmentrecyler);
        progressDialog=new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mMatchesLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);

        mMatchedAdapter=new MatchesAdapter(getMatchesDataSet(),getActivity());
        mRecyclerView.setAdapter(mMatchedAdapter);
        myauth=FirebaseAuth.getInstance();
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getMatcherUserId();
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


    }


    private void getMatcherUserId() {
        DatabaseReference matchDb= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("connections").child("match");
        matchDb.keepSynced(true);
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot matches:dataSnapshot.getChildren()){
                        fatchMatcherInformation(matches.getKey());
                    }
                }else{
                    Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fatchMatcherInformation(String key) {
        DatabaseReference userDb= FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.keepSynced(true);
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

}
