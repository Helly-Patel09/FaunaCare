package com.vahapps.faunacare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
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

/**
 * Created by Vaibhavi on 13-Dec-17.
 */

public class RegVet extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "RegVet";

    EditText fname,lname,email,pass,cpass,contact,gvc,aadhaar,hname,harea;
    Button register,btnlocate;
    //Spinner scity;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref,gvcRef;
    private GoogleApiClient mGoogleApiClient;
    private String latitude,longitude;
    private int PLACE_PICKER_REQUEST = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_vet);

        fname= (EditText) findViewById(R.id.txtvetfname);
        lname= (EditText) findViewById(R.id.txtvetlname);
        email= (EditText) findViewById(R.id.txtvetemail);
        pass= (EditText) findViewById(R.id.txtvetpass);
        cpass= (EditText) findViewById(R.id.txtvetcpass);
        contact= (EditText) findViewById(R.id.txtvetcontact);
        gvc= (EditText) findViewById(R.id.txtvetgvc);
        aadhaar= (EditText) findViewById(R.id.txtvetadhr);
        hname= (EditText) findViewById(R.id.txtvethosptlname);
        harea= (EditText) findViewById(R.id.txtvethosptlarea);
        btnlocate= (Button) findViewById(R.id.btnvlocate);
        register= (Button) findViewById(R.id.btnvetregister);

        /*String[] items = new String[] {"Ahmedabad", "Gandhinagar", "Kalol","Surat","Rajkot","Bharuch"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scity.setAdapter(adapter);*/
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
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
    btnlocate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(RegVet.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  final String city = scity.getSelectedItem().toString();
                gvcRef=FirebaseDatabase.getInstance().getReference("Gvc");
                gvcRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("GvcNumber").getValue().toString().equals(gvc.getText().toString())){
                            if (dataSnapshot.child("VetName").getValue().toString().equals(fname.getText().toString().trim()+" "+lname.getText().toString().trim())){
                                mAuth = FirebaseAuth.getInstance();

                                mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                                        .addOnCompleteListener(RegVet.this, new OnCompleteListener<AuthResult>() {
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
                                        .addOnCompleteListener(RegVet.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    fdb=FirebaseDatabase.getInstance();
                                                    myref=fdb.getReference();
                                                    String userId=user.getUid();
                                                    Toast.makeText(getBaseContext(),"Userid: "+userId,Toast.LENGTH_LONG).show();
                                                    myref.child("Vet").child(userId).child("name").setValue(fname.getText().toString()+" "+lname.getText().toString());
                                                    myref.child("Vet").child(userId).child("email").setValue(email.getText().toString());
                                                    myref.child("Vet").child(userId).child("pass").setValue(pass.getText().toString());
                                                    myref.child("Vet").child(userId).child("contact").setValue(contact.getText().toString());
                                                    myref.child("Vet").child(userId).child("gvc").setValue(gvc.getText().toString());
                                                    myref.child("Vet").child(userId).child("aadhaar").setValue(aadhaar.getText().toString());
                                                    myref.child("Vet").child(userId).child("hospital").setValue(hname.getText().toString());
                                                    myref.child("Vet").child(userId).child("hospitaladd").setValue(harea.getText().toString());
                                                    myref.child("Vet").child(userId).child("gpsLatitude").setValue(latitude);
                                                    myref.child("Vet").child(userId).child("gpsLongitude").setValue(longitude);
                                                    myref.child("Vet").child(userId).child("serving").setValue("0");
                                                    Toast.makeText(getBaseContext(),fname.getText().toString() + " added",Toast.LENGTH_LONG).show();
                                                    Log.d(TAG, "signin:success " + user.getUid());
                                                    Intent intent = new Intent(RegVet.this,Home.class);
                                                    startActivity(intent);

                                                } else {
                                                    Log.w(TAG, "signIn:failure", task.getException());
                                                    Toast.makeText(RegVet.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(RegVet.this, "GVC and Name Doesn't match!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegVet.this, "GVC number doesn't exist!", Toast.LENGTH_SHORT).show();
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

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {

        mGoogleApiClient.disconnect();
        super.onStop();
        //mAuth.signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(address);
                harea.setText(stBuilder.toString());
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Snackbar.make(btnlocate, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }
}
