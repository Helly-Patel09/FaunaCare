package com.vahapps.faunacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vaibhavi on 13-Dec-17.
 */

public class RegVol extends AppCompatActivity {
    private static final String TAG = "RegVol";
    EditText name,email,pass,cpass,contact,aadhaar;
    Button register;
    //Spinner spinner;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_vol);

        name = (EditText) findViewById(R.id.txtvolname);
        email = (EditText) findViewById(R.id.txtvolemail);
        pass = (EditText) findViewById(R.id.txtvolpass);
        cpass = (EditText) findViewById(R.id.txtvolcpass);
        contact = (EditText) findViewById(R.id.txtvolcontact);
        aadhaar = (EditText) findViewById(R.id.txtvoladhr);
        register = (Button) findViewById(R.id.btnvolregister);


        /*spinner = (Spinner) findViewById(R.id.spinnervol);
        String[] items = new String[] {"One", "Two", "Three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        cpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String pwd=pass.getText().toString();
                String cpwd=cpass.getText().toString();
                if(editable.length()==pwd.length())
                    if(cpwd.equals(pwd))
                        Toast.makeText(getBaseContext(),"equal",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(),"Not Equal",Toast.LENGTH_LONG).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnCompleteListener(RegVol.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.d(TAG,"Created " + user.getUid());
                                    Toast.makeText(getBaseContext(),"Created",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Log.w(TAG, "Not created", task.getException());
                                    Toast.makeText(getBaseContext(), "Not created", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(RegVol.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    fdb=FirebaseDatabase.getInstance();
                                    myref=fdb.getReference();
                                    String userId=user.getUid();
                                    Toast.makeText(getBaseContext(),"Userid: "+userId,Toast.LENGTH_LONG).show();
                                    myref.child("Volunteer").child(userId).child("name").setValue(name.getText().toString());
                                    myref.child("Volunteer").child(userId).child("email").setValue(email.getText().toString());
                                    myref.child("Volunteer").child(userId).child("pass").setValue(pass.getText().toString());
                                    myref.child("Volunteer").child(userId).child("contact").setValue(contact.getText().toString());
                                    myref.child("Volunteer").child(userId).child("aadhaar").setValue(aadhaar.getText().toString());
                                    myref.child("Volunteer").child(userId).child("serving").setValue("0");
                                    Toast.makeText(getBaseContext(),name.getText().toString() + " added",Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "signin:success " + user.getUid());
                                    Intent intent = new Intent(RegVol.this,Home.class);
                                    startActivity(intent);

                                } else {
                                    Log.w(TAG, "signIn:failure", task.getException());
                                    Toast.makeText(RegVol.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        //mAuth.signOut();
    }
}