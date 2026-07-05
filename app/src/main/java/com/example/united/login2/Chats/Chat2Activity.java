package com.example.united.login2.Chats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.united.login2.Matches.MatchActivity;
import com.example.united.login2.Matches.MatchesAdapter;
import com.example.united.login2.Matches.MatchesObject;
import com.example.united.login2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private String currentUserId,matchId,chatId;

    private EditText mSendEditText;
    private Button mSendButton;

    DatabaseReference mDatabseUser,mDatabseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId= getIntent().getExtras().getString("matchId");
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabseUser=FirebaseDatabase.getInstance().getReference().child(currentUserId).child("connections").child("match").child(matchId).child("chatId");
        mDatabseChat=FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();

        mRecyclerView=(RecyclerView)findViewById(R.id.recyler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        mChatLayoutManager=new LinearLayoutManager(Chat2Activity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter=new ChatAdapter(getChatDataSet(),Chat2Activity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendButton=findViewById(R.id.send);
        mSendEditText=findViewById(R.id.msg);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {
        String sendMessageText=mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessagedb=mDatabseChat.push();
            Map newMessage=new HashMap();
            newMessage.put("createdBy",currentUserId);
            newMessage.put("text",sendMessageText);
            newMessagedb.setValue(newMessage);

        }else{
            Toast.makeText(this, "Cannot send Empty msg", Toast.LENGTH_SHORT).show();
        }
        mSendEditText.setText(null);
    }

    private void getChatId(){
        mDatabseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    chatId=dataSnapshot.getValue().toString();
                    mDatabseChat=mDatabseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        mDatabseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String msg=null;
                    String createdByUser=null;
                    if(dataSnapshot.child("text").getValue()!=null){
                        msg=dataSnapshot.child("text").getValue().toString();
                    }

                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser=dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(msg!=null && createdByUser!=null){
                        Boolean currentUserBoolean=false;
                        if(createdByUser.equals(currentUserId)){
                            currentUserBoolean=true;
                        }

                        ChatObject newMessage= new ChatObject(msg,currentUserBoolean);
                        resultChats.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<ChatObject> resultChats= new ArrayList<ChatObject>();
    private List<ChatObject> getChatDataSet() {

        return resultChats;
    }
}
