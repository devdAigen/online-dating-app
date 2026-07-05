package com.example.united.login2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity{
    Button loginbtn;
    EditText email,password;
    FirebaseAuth myauth;
    FirebaseAuth.AuthStateListener firebaselistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myauth= FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        firebaselistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent= new Intent(login.this,HandlerFragment.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        loginbtn=(Button)findViewById(R.id.button2);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email=email.getText().toString();
                final String Password=password.getText().toString();
                if(Email==null){
                    Toast.makeText(login.this, "Email field cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(Password==null){
                    Toast.makeText(login.this, "Password field cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                myauth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(login.this, "Could not able  Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
