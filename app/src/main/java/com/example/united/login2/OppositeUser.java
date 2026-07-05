package com.example.united.login2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class OppositeUser extends AppCompatActivity {

    private TextView ageTextview,nameTextView,contactTextView,curshTextView,heartbreakTextView,interestedTextView;
    private ImageView profileImage;
    private Button back;

    private FirebaseAuth myauth;

    private DatabaseReference muserdatabase;
    private String name,age,contact,userId,profilepicture,crush,heartbreak,usersex,interested;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opposite_user);

        userId=getIntent().getExtras().getString("userId");

        nameTextView=(TextView)findViewById(R.id.name);
        ageTextview=(TextView)findViewById(R.id.age);
        profileImage=(ImageView)findViewById(R.id.profileimg);
        contactTextView=(TextView)findViewById(R.id.contact);
        curshTextView=(TextView)findViewById(R.id.crush);
        heartbreakTextView=(TextView)findViewById(R.id.heartbreak);
        interestedTextView=(TextView)findViewById(R.id.interested);

        back=(Button)findViewById(R.id.back);

        myauth=FirebaseAuth.getInstance();


        muserdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getuserinfo();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

    }

    private void getuserinfo() {
        muserdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map=(Map<String,Object>)dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name=map.get("name").toString();
                        nameTextView.setText(name);
                    }
                    if(map.get("age")!=null){
                        age=map.get("age").toString();
                        ageTextview.setText(age);
                    }

                    if(map.get("crush")!=null){
                        crush=map.get("crush").toString();
                        curshTextView.setText(crush);
                    }
                    if(map.get("heartbreak")!=null){
                        heartbreak=map.get("heartbreak").toString();
                        heartbreakTextView.setText(heartbreak);
                    }

                    if(map.get("sex")!=null){
                        usersex=map.get("sex").toString();
                       contactTextView.setText("Gender : "+usersex);
                    }

                    if(map.get("interested")!=null){
                        interested=map.get("interested").toString();
                        interestedTextView.setText("Intersted In : "+interested);
                    }

                    if(map.get("profilepicture")!=null){
                        profilepicture=map.get("profilepicture").toString();
                        //      profilepicture=profilepicture+".jpeg";
                        switch (profilepicture){
                            case "default":
                               if(usersex.equals("Male")){
                                    Glide.with(getApplication()).load(R.drawable.maledp).into(profileImage);
                                }else{
                                    Glide.with(getApplication()).load(R.drawable.femaled).into(profileImage);
                                }
                                break;
                            default:
                                Glide.with(getApplication()).load(profilepicture).into(profileImage);
                                break;
                        }
                    }
                }else{
                    Toast.makeText(OppositeUser.this, "No data found for perticular User", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
