package com.example.united.login2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button b1,b2;

    TextView logintxt;
    EditText email,password,name;
    FirebaseAuth myauth;
    private RadioGroup mradiogroup,interesedradiogroup;

    private ProgressDialog progressDialog;

    FirebaseAuth.AuthStateListener firebaselistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myauth= FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.editText2);
        password=(EditText)findViewById(R.id.editText3);
        name =(EditText)findViewById(R.id.editText);

        progressDialog=new ProgressDialog(this);

        firebaselistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent= new Intent(MainActivity.this,HandlerFragment.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mradiogroup=(RadioGroup)findViewById(R.id.radioGroup3);
        interesedradiogroup=(RadioGroup)findViewById(R.id.radioGroup4);
        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             int selectid=mradiogroup.getCheckedRadioButtonId();
             int interstedid=interesedradiogroup.getCheckedRadioButtonId();
             final RadioButton radiobutton=(RadioButton)findViewById(selectid);
             final RadioButton intradiobutton=(RadioButton)findViewById(interstedid);

             if(radiobutton.getText()==null){
                 Toast.makeText(MainActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                 return;
             }
             if(intradiobutton.getText()==null){
                 Toast.makeText(MainActivity.this, "Please Select interested field", Toast.LENGTH_SHORT).show();
                 return;
             }

                final String Email=email.getText().toString();
                final String Password=password.getText().toString();
                final String Name=name.getText().toString();

                if(Email==null){
                    Toast.makeText(MainActivity.this, "Email field cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if(Name==null){
                    Toast.makeText(MainActivity.this, "You Dont Have Name?", Toast.LENGTH_LONG).show();
                    return;
                }

                if(Password==null){
                    Toast.makeText(MainActivity.this, "Password field cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.setMessage("Please Wait...Registering User");
                progressDialog.show();

                myauth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Could not able to register the customer", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Successfuly Registered", Toast.LENGTH_SHORT).show();

                            String userid=myauth.getCurrentUser().getUid();
                            DatabaseReference currentuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            Map userinfo=new HashMap();
                            userinfo.put("name",Name);
                            userinfo.put("profilepicture","default");
                            userinfo.put("sex",radiobutton.getText().toString());
                            userinfo.put("interested",intradiobutton.getText().toString());



                            currentuser.updateChildren(userinfo);

                        }
                    }
                });

            }
        });
        b2=findViewById(R.id.button3);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,login.class);
                startActivity(i1);
                finish();
                return;
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        myauth.addAuthStateListener(firebaselistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myauth.removeAuthStateListener(firebaselistener);
    }
}
