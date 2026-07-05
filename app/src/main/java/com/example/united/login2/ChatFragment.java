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

import com.example.united.login2.anoynimous.AnoObject;
import com.example.united.login2.anoynimous.ChatlistAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchedAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private FirebaseAuth myauth;
    ProgressDialog progressDialog;

    private String userId;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.anochatrecyler);
        progressDialog=new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mMatchesLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);

        mMatchedAdapter=new ChatlistAdapter(getChatDataSet(),getActivity());
        mRecyclerView.setAdapter(mMatchedAdapter);
        myauth=FirebaseAuth.getInstance();
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getMatcherUserId();
    }

    private void getMatcherUserId() {
        DatabaseReference matchDb= FirebaseDatabase.getInstance().getReference().child("Users");
        matchDb.keepSynced(true);
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot matches:dataSnapshot.getChildren()){
                        if(!matches.getKey().equals(userId)) {
                            AnoObject obj = new AnoObject(matches.getKey());
                            resultchatlist.add(obj);
                            mMatchedAdapter.notifyDataSetChanged();
                        }
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


    private ArrayList<AnoObject> resultchatlist= new ArrayList<AnoObject>();
    private List<AnoObject> getChatDataSet() {

        return resultchatlist;
    }
}
