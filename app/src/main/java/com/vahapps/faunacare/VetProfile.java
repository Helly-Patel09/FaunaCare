package com.vahapps.faunacare;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class VetProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {
    private Button btnUpdate,btnLocate;
    private TextView txtVetName,txtHname,txtHAddress,txtContact,navmail,navname;
    private EditText editTextVetName,editTextHname,editTextHAddress,editTextContact;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref,currentDb,fref;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private NavigationView navigationView;
    private GoogleApiClient mGoogleApiClient;
    private String latitude,longitude;
    private String userkey, name;
    private String uid;
    private List<Fauna> uploads;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_profile);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btnUpdate= (Button) findViewById(R.id.btnUpdate);
        txtVetName= (TextView) findViewById(R.id.NameD);
        editTextVetName= (EditText) findViewById(R.id.txtName);
        btnLocate= (Button) findViewById(R.id.btnLocate);
        editTextHname= (EditText) findViewById(R.id.txtHName);
        editTextHAddress= (EditText) findViewById(R.id.txtHAdddress);
        editTextContact= (EditText) findViewById(R.id.txtContactNumber);
        txtHname = (TextView) findViewById(R.id.HNameD);
        txtHAddress= (TextView) findViewById(R.id.HAdressD);
        txtContact= (TextView) findViewById(R.id.ContactNumberD);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploads = new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid= user.getUid();
        Log.d("uid-->",uid);

        getData();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(VetProfile.this), 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab.setVisibility(View.VISIBLE);
                myref=FirebaseDatabase.getInstance().getReference("Vet");
                myref.child(uid).child("contact").setValue(editTextContact.getText().toString());
                myref.child(uid).child("name").setValue(editTextVetName.getText().toString());
                myref.child(uid).child("hospital").setValue(editTextHname.getText().toString());
                myref.child(uid).child("hospitaladd").setValue(editTextHAddress.getText().toString());
                myref.child(uid).child("gpsLatitude").setValue(latitude);
                myref.child(uid).child("gpsLongitude").setValue(longitude);
                Toast.makeText(VetProfile.this, "Updated Profile", Toast.LENGTH_LONG).show();
                txtVetName.setVisibility(View.VISIBLE);
                editTextHname.setVisibility(View.GONE);
                txtHname.setVisibility(View.VISIBLE);
                editTextVetName.setVisibility(View.GONE);
                txtHAddress.setVisibility(View.VISIBLE);
                editTextHAddress.setVisibility(View.GONE);
                txtContact.setVisibility(View.VISIBLE);
                editTextContact.setVisibility(View.GONE);
                btnLocate.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);
                getData();

            }
        });
        currentDb = FirebaseDatabase.getInstance().getReference();
        currentDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(user.getUid())) {
                        name = child.child("name").getValue().toString();
                        userkey = dataSnapshot.getKey();
                        View hView = navigationView.getHeaderView(0);
                        navname = hView.findViewById(R.id.textViewname);
                        navname.setText(name + " (" + userkey + ")");
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                txtVetName.setVisibility(View.GONE);
                editTextHname.setVisibility(View.VISIBLE);
                txtHname.setVisibility(View.GONE);
                editTextVetName.setVisibility(View.VISIBLE);
                txtHAddress.setVisibility(View.GONE);
                editTextHAddress.setVisibility(View.VISIBLE);
                txtContact.setVisibility(View.GONE);
                editTextContact.setVisibility(View.VISIBLE);
                btnLocate.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                myref=FirebaseDatabase.getInstance().getReference("Vet");
                myref.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot d, String s) {
                        if(uid.equals(d.getKey())){
                            editTextContact.setText( d.child("contact").getValue().toString());
                            editTextVetName.setText(d.child("name").getValue().toString());
                            editTextHAddress.setText(d.child("hospitaladd").getValue().toString());
                            editTextHname.setText(d.child("hospital").getValue().toString());}
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        View hView = navigationView.getHeaderView(0);
        navmail = hView.findViewById(R.id.textViewmail);
        navname = hView.findViewById(R.id.textViewname);
        navmail.setText(user.getEmail());
    }

    public  void getData(){
        myref=FirebaseDatabase.getInstance().getReference("Vet");
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot d, String s) {
                if(uid.equals(d.getKey())){
                    txtVetName.setText(d.child("name").getValue().toString());
                    txtHAddress.setText(d.child("hospitaladd").getValue().toString());
                    txtHname.setText(d.child("hospital").getValue().toString());
                    txtContact.setText(d.child("contact").getValue().toString());}}
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        fref = FirebaseDatabase.getInstance().getReference("Fauna");
        fref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot d, String s) {
                Log.d("inchild",d.child("vetId").getValue().toString());
                Fauna upload = d.getValue(Fauna.class);
                if (uid.equals(d.child("vetId").getValue().toString())) {
                    uploads.add(upload);
                    adapter = new MyAdapter(getApplicationContext(), uploads);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(mLayoutManager);
                    //adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(address);
                editTextHAddress.setText(stBuilder.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_upload) {
            Intent i = new Intent(VetProfile.this, UploadFaunaDetails.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fauna) {
            Intent i = new Intent(VetProfile.this, FaunasInNeed.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(VetProfile.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(VetProfile.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
            Intent i = new Intent(VetProfile.this, AllVet.class);
            startActivity(i);
        } else if (id == R.id.nav_myProfile) {
        } else if (id == R.id.nav_tips) {
            Intent i =new Intent(VetProfile.this,Tips.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
