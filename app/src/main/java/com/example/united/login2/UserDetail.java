package com.example.united.login2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserDetail extends AppCompatActivity {


    private TextView nameTextView;
    private EditText contactEditText,crushEditText,heartbreakEditText,ageEditText;
    private ImageView profileImage;
    private Button back,update;


    private DatabaseReference muserdatabase;
    private String name,age,contact,userId,profilepicture,crush,heartbreak;
    private String usersex;
    private Uri resultUri;

    private FirebaseAuth myauth;
    private  DatabaseReference userdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        userdb=FirebaseDatabase.getInstance().getReference().child("Users");


        myauth=FirebaseAuth.getInstance();
        userId=myauth.getCurrentUser().getUid();

       muserdatabase= userdb.child(userId);

        nameTextView=(TextView)findViewById(R.id.name);
        profileImage=(ImageView)findViewById(R.id.profileimg);

        contactEditText=(EditText)findViewById(R.id.contact);
        ageEditText=(EditText)findViewById(R.id.age);
        crushEditText=(EditText)findViewById(R.id.crush);
        heartbreakEditText=(EditText)findViewById(R.id.heartbreak);

        back=(Button)findViewById(R.id.back);
        update=(Button)findViewById(R.id.update);


        getuserinfo();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveuserinfo();
            }
        });

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
                        ageEditText.setText(age);
                    }
                    if(map.get("phone")!=null){
                        contact=map.get("phone").toString();
                        contactEditText.setText(contact);
                    }
                    if(map.get("crush")!=null){
                        crush=map.get("crush").toString();
                        crushEditText.setText(crush);
                    }
                    if(map.get("heartbreak")!=null){
                        heartbreak=map.get("heartbreak").toString();
                        heartbreakEditText.setText(heartbreak);
                    }
                //    Glide.clear(profileImage);
                    if(map.get("profilepicture")!=null){

                        profilepicture=map.get("profilepicture").toString();
                        switch (profilepicture){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.maledp).into(profileImage);
                                break;
                                default:
                                    Glide.with(getApplication()).load(profilepicture).into(profileImage);
                                    break;
                        }
                        //      profilepicture=profilepicture+".jpeg";



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserDetail.this, "Databese Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveuserinfo() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait... Updating Profile");

        contact=contactEditText.getText().toString();
        age=ageEditText.getText().toString();
        crush=crushEditText.getText().toString();
        heartbreak=heartbreakEditText.getText().toString();

        if(contact.isEmpty()){
            Toast.makeText(UserDetail.this, "Contact field cannot be Empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(age.isEmpty()){
            Toast.makeText(UserDetail.this, "You Dont Know your Age ?", Toast.LENGTH_LONG).show();
            return;
        }
        if(crush.isEmpty()){
            Toast.makeText(UserDetail.this, "Kalla, I Know You Have Crush", Toast.LENGTH_LONG).show();
            return;
        }
        if(heartbreak.isEmpty()){
            Toast.makeText(UserDetail.this, "No Heartbreak ?", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Map userinfo=new HashMap();
       // userinfo.put("name",name);
        userinfo.put("phone",contact);
        userinfo.put("age",age);
        userinfo.put("crush",crush);
        userinfo.put("heartbreak",heartbreak);
        muserdatabase.updateChildren(userinfo);


        if(resultUri!=null){
            final StorageReference filepath= FirebaseStorage.getInstance().getReference().child("profilepicture").child(userId);
            Bitmap bitmap=null;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);

            }catch (IOException e){
                e.printStackTrace();
            }
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
            byte[] data=baos.toByteArray();
            UploadTask uploadTask=filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserDetail.this, "Failed to convert", Toast.LENGTH_SHORT).show();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profilepicture", uri.toString());
                            muserdatabase.updateChildren(newImage);
                            Toast.makeText(UserDetail.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();

                            finish();
                            return;
                        }
                    });

//                   @SuppressWarnings("VisibleForTests") Uri downloadUri=taskSnapshot.getDownloadUri();
//                   Map userinfo=new HashMap();
//                   userinfo.put("profilepicture",downloadUri.toString());
//                   muserdatabase.updateChildren(userinfo);
//                   finish();
//                   return;
                }
            });

        }else{

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            final Uri imageUri=data.getData();
            resultUri=imageUri;
            profileImage.setImageURI(resultUri);
        }
    }



}
