package com.vahapps.faunacare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText email,pass;
    TextView txtRegister;
    Button login;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref;
    private FirebaseUser user;
    //private static int timeout=4000;
    ProgressDialog progressDialog;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        final UserType objUser= (UserType) getApplicationContext();
        if (user != null && user.isEmailVerified()){
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
            finish();
        }else {
            email = (EditText) findViewById(R.id.txtloginmail);
            pass = (EditText) findViewById(R.id.txtloginpass);
            login = (Button) findViewById(R.id.btnlogin);
            txtRegister = (TextView) findViewById(R.id.txtRegister);


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!email.getText().toString().trim().matches(getString(R.string.email_regx)) || email.getText()==null){
                        Toast.makeText(MainActivity.this, "Enter valid Email Id!", Toast.LENGTH_LONG).show();
                    }else if (pass.getText().toString().length() < 6) {
                        if (pass.getText().toString().length()==0){
                            Toast.makeText(MainActivity.this, "Enter Password! Must be 6 letters long", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Password too small! Must be 6 letters long", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Logging in...");
                        progressDialog.show();
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = mAuth.getCurrentUser();
                                    //Toast.makeText(MainActivity.this, "Authentication Successful!", Toast.LENGTH_LONG).show();
                                    if (user.isEmailVerified()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        fdb = FirebaseDatabase.getInstance();
                                        myref = fdb.getReference();
                                        //assert user != null;
                                        final String userId = user.getUid();
                                        myref.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                String key = dataSnapshot.getKey();
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    if (child.getKey().equals(userId)) {
                                                        //String key = myParentNode;
                                                        //String value = child.getValue().toString();
                                                        Intent intent = null;
                                                        Log.d("main: ", "key: " + key);
                                                        if (key.equals("Admin")) {
                                                            intent = new Intent(MainActivity.this, Admin.class);
                                                            // Log.d("username::",dataSnapshot.child(userId).child("name").getValue().toString());
                                                            // objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserType("admin");
                                                            //intent.putExtra("userType","admin");
                                                        } else if (key.equals("Finder")) {
                                                            intent = new Intent(MainActivity.this, Home.class);
                                                            Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserType("finder");
                                                            //intent.putExtra("userType","finder");
                                                        } else if (key.equals("Volunteer")) {
                                                            intent = new Intent(MainActivity.this, Home.class);
                                                            Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserType("vol");
                                                            //intent.putExtra("userType","vol");
                                                        } else if (key.equals("Vet")) {
                                                            intent = new Intent(MainActivity.this, Home.class);
                                                            Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserType("vet");
                                                            //intent.putExtra("userType","vet");
                                                        }
                                                        progressDialog.dismiss();
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getBaseContext(),"Check mail",Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Authentication Failed! Try checking Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
            txtRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentFinder = new Intent(MainActivity.this, Register.class);
                    startActivity(intentFinder);
                }
            });
        } /*else {
            *//*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(MainActivity.this,SplashScreen.class);
                    startActivity(i);
                }
            },timeout);*//*
            *//*progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();*//*
            fdb = FirebaseDatabase.getInstance();
            myref = fdb.getReference();
            final String userId = user.getUid();
            if(user.getEmail().equals("admin@gmail.com"))
            {
                Intent intent = new Intent(MainActivity.this, Admin.class);
                //intent.putExtra("userType","admin");
                objUser.setUserType("admin");
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        }*/
    }
    /*@Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
        final String type = getSnapshotType(snapshot);}
    @Nullable
    private String getSnapshotType(DataSnapshot snapshot) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return null;
        }
        DatabaseReference parent = snapshot.getRef().getParent();
        if (parent == null ) {
            return null;
        }
        parent = parent.getParent();
        return parent != null ? parent.getKey() : null;
    }*/
}