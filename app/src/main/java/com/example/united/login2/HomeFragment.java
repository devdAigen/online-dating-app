package com.example.united.login2;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.united.login2.Cards.arrayAdapter;
import com.example.united.login2.Cards.cards;
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

public class HomeFragment extends Fragment {

    private cards cards_data[];
    private FirebaseAuth myauth;
    private String userId;
    private String currentuserId;
    private arrayAdapter arrayadapter;
    private int i;
    private DatabaseReference userdb;

    ListView listView;

    ProgressDialog progressDialog;

    List<cards> rowItem;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        userdb=FirebaseDatabase.getInstance().getReference().child("Users");

        myauth=FirebaseAuth.getInstance();
        currentuserId =myauth.getCurrentUser().getUid().toString();


        checkuserSex();
        getuserinfo();
        sendNotification();
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);


        rowItem = new ArrayList<cards>();

        arrayadapter = new arrayAdapter(getActivity(),R.layout.profiles,rowItem);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView)view.findViewById(R.id.frameone);

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
                Toast.makeText(getActivity(), "Rejected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                cards obj=(cards) dataObject;
                String userId=obj.getUserId();
                userdb.child(userId).child("connections").child("swipeyes").child(currentuserId).setValue(true);

                Toast.makeText(getActivity(), "Accepted", Toast.LENGTH_SHORT).show();
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
                Intent intent=new Intent(getActivity(),OppositeUser.class);
                intent.putExtra("userSex",oppositegender);
                intent.putExtra("userId",userId);
                startActivity(intent);
                return;

            }
        });
    }

    private void sendNotification() {
        DatabaseReference currentuserDatabse=userdb.child(currentuserId).child("connections").child("match");
        currentuserDatabse.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity())
                                .setSmallIcon(R.drawable.hearts)
                                .setContentTitle("Congratulation : Match Found !! ")
                                .setContentText("Now You can start Conversation ");
                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
               mNotificationManager.notify(1, mBuilder.build());


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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }
    private void isConnectionMatch(final String userId) {
        DatabaseReference currentuserDatabse=userdb.child(currentuserId).child("connections").child("swipeyes").child(userId);
        currentuserDatabse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getActivity(), "Congratulation Match Found!", Toast.LENGTH_SHORT).show();
                    String key=FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    userdb.child(dataSnapshot.getKey()).child("connections").child("match").child(currentuserId).child("ChatId").setValue(key);

                    userdb.child(currentuserId).child("connections").child("match").child(dataSnapshot.getKey()).child("ChatId").setValue(key);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getActivity())
                                    .setSmallIcon(R.drawable.hearts)
                                    .setContentTitle("Congratulation : Match Found !! ")
                                    .setContentText("Now You can start Conversation ");
                    //+userdb.child(dataSnapshot.getKey()).child("name").getKey().toString()

                    // Gets an instance of the NotificationManager service//

                    // When you issue multiple notifications about the same type of event,
                    // it’s best practice for your app to try to update an existing notification
                    // with this new information, rather than immediately creating a new notification.
                    // If you want to update this notification at a later date, you need to assign it an ID.
                    // You can then use this ID whenever you issue a subsequent notification.
                    // If the previous notification is still visible, the system will update this existing notification,
                    // rather than create a new one. In this example, the notification’s ID is 001//
                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                    mNotificationManager.notify(1, mBuilder.build());


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


        // userdb.keepSynced(true);
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
                        Intent intent = new Intent(getActivity(), UserDetail.class);
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

}
