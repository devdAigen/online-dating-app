package com.example.united.login2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileFragment extends Fragment {

    private TextView ageTextview, nameTextView, contactTextView, curshTextView, heartbreakTextView;
    private ImageView profileImage;
    private Button edit;
    private ImageButton logout;

    private FirebaseAuth myauth;
    private DatabaseReference muserdatabase;

    private String name, age, contact, userId, profilepicture, crush, heartbreak, usersex;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        edit = view.findViewById(R.id.edit);
        logout=view.findViewById(R.id.logout);
        nameTextView = (TextView) view.findViewById(R.id.name);
        ageTextview = (TextView) view.findViewById(R.id.age);
        profileImage = (ImageView) view.findViewById(R.id.profileimg);
        contactTextView = (TextView) view.findViewById(R.id.contact);
        curshTextView = (TextView) view.findViewById(R.id.crush);
        heartbreakTextView = (TextView) view.findViewById(R.id.heartbreak);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


// Adds the Intent that starts the Activity to the top of the stack

        myauth = FirebaseAuth.getInstance();
        userId = myauth.getCurrentUser().getUid();

        muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getuserinfo();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity(),UserDetail.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myauth.signOut();
                Toast.makeText(getContext(), "By By...See You Again", Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getActivity(),MainActivity.class);
                startActivity(intent1);

                return;
            }
        });

    }

    private void getuserinfo() {
        muserdatabase.keepSynced(true);
        muserdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        nameTextView.setText(name);
                    }
                    if (map.get("age") != null) {
                        age = map.get("age").toString();
                        ageTextview.setText(age);
                    }
                    if (map.get("phone") != null) {
                        contact = map.get("phone").toString();
                        contactTextView.setText(contact);
                    }
                    if (map.get("crush") != null) {
                        crush = map.get("crush").toString();
                        curshTextView.setText(crush);
                    }
                    if (map.get("heartbreak") != null) {
                        heartbreak = map.get("heartbreak").toString();
                        heartbreakTextView.setText(heartbreak);
                    }

                    if (map.get("sex") != null) {
                        usersex = map.get("sex").toString();
                        //  heartbreakTextView.setText(heartbreak);
                    }

                    if (map.get("profilepicture") != null) {
                        profilepicture = map.get("profilepicture").toString();
                        switch (profilepicture) {
                            case "default":
                                Glide.with(getActivity().getApplication()).load(R.drawable.maledp).into(profileImage);
                                break;
                            default:
                                Glide.with(getActivity().getApplication()).load(profilepicture).into(profileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
