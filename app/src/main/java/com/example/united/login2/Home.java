package com.example.united.login2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.united.login2.Cards.arrayAdapter;
import com.example.united.login2.Cards.cards;
import com.example.united.login2.Matches.MatchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    private Button logout;
    private cards cards_data[];
    private FirebaseAuth myauth;
    private String userId;
    private String currentuserId;
    private arrayAdapter arrayadapter;
    private int i;
    private  DatabaseReference userdb;

    ListView listView;



    ProgressDialog progressDialog;

    List<cards> rowItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        userdb=FirebaseDatabase.getInstance().getReference().child("Users");

        myauth=FirebaseAuth.getInstance();
        currentuserId =myauth.getCurrentUser().getUid().toString();


        checkuserSex();
        getuserinfo();

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(Home.this,UserDetail.class);

                intent.putExtra("userSex",usersex);
                startActivity(intent);
                return;

            }
        });

        rowItem = new ArrayList<cards>();

        arrayadapter = new arrayAdapter(this,R.layout.profiles,rowItem);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);

        flingContainer.setAdapter(arrayadapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItem.remove(0);
                arrayadapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                cards obj=(cards) dataObject;
                String userId=obj.getUserId();
                userdb.child(userId).child("connections").child("swipeno").child(currentuserId).setValue(true);
                Toast.makeText(Home.this, "Rejected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                cards obj=(cards) dataObject;
                String userId=obj.getUserId();
                userdb.child(userId).child("connections").child("swipeyes").child(currentuserId).setValue(true);

                Toast.makeText(Home.this, "Accepted", Toast.LENGTH_SHORT).show();
                isConnectionMatch(userId);
            }

            @Override

            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
               // al.add("XML ".concat(String.valueOf(i)));
                //arrayAdapter.notifyDataSetChanged();
                //Log.d("LIST", "notified");
                //i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                cards obj=(cards) dataObject;
                String userId=obj.getUserId();
                Intent intent=new Intent(Home.this,OppositeUser.class);
                intent.putExtra("userSex",oppositegender);
                intent.putExtra("userId",userId);
                startActivity(intent);
                return;

            }
        });
    }

    private void isConnectionMatch(final String userId) {
        DatabaseReference currentuserDatabse=userdb.child(currentuserId).child("connections").child("swipeyes").child(userId);
        currentuserDatabse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(Home.this, "Congratulation Match Found!", Toast.LENGTH_SHORT).show();
                    String key=FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    userdb.child(dataSnapshot.getKey()).child("connections").child("match").child(currentuserId).child("ChatId").setValue(key);

                    userdb.child(currentuserId).child("connections").child("match").child(dataSnapshot.getKey()).child("ChatId").setValue(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private String usersex;
    private String oppositegender;
    public void checkuserSex(){
        final FirebaseUser users= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb=userdb.child(users.getUid());
        userdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(users.getUid())){
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.child("sex") != null){
                            usersex=dataSnapshot.child("sex").getValue().toString();
                            oppositegender=dataSnapshot.child("interested").getValue().toString();

                            getoppositesexuser();
                        }

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

    public void getoppositesexuser(){


      //  userdb.keepSynced(true);
        userdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.child("sex").getValue().toString().equals(oppositegender) && !dataSnapshot.child("connections").child("swipeyes").hasChild(currentuserId) ){
                    String profileimage="default";
                    if(!dataSnapshot.child("profilepicture").getValue().equals("default")){
                       profileimage= dataSnapshot.child("profilepicture").getValue().toString();
                    }
                    Map<String, Object> map=(Map<String,Object>)dataSnapshot.getValue();
                    String name="",age="",sex="";


                    if(map.get("name")!=null){
                        name=map.get("name").toString();

                    }
                    if(map.get("age")!=null){
                        age=map.get("age").toString();

                    }
                    if(map.get("sex")!=null){
                        sex=map.get("sex").toString();

                    }


//                    if(dataSnapshot.child("profilepicture").getValue()==null){
//                        profile="maledp";
//                    }else{
//                        profile=dataSnapshot.child("profilepicture").getValue().toString();
//                    }
                        cards Item=new cards(dataSnapshot.getKey(),name,profileimage,age,sex);
                    rowItem.add(Item);
                        arrayadapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
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


    private void getuserinfo() {
        userdb.child(currentuserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();


                    if (map.get("age") == null) {
                        Intent intent = new Intent(Home.this, UserDetail.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        return;

                    }else{
                        String age=map.get("age").toString();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void logoutusers(View view) {
        myauth.signOut();
        Toast.makeText(this, "By By...See You Again", Toast.LENGTH_SHORT).show();
        Intent intent1=new Intent(Home.this,MainActivity.class);
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
}
