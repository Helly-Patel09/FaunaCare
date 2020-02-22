package com.vahapps.faunacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VetInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user;
    private TextView txtHname, vname, email, HospitalAddress, Contact;
    private FirebaseDatabase fdb, rateFdb, iFdb;
    private DatabaseReference myref, rateRef, iref,currentDb;
    private String userkey, name;
    private FirebaseAuth mAuth;

    private String vkey;
    //private Button btnRate;
    private RatingBar bar;
    private String r, VetKey, currentUserKey;
    private float rt, g, curRate;
    private int rate, count, avg, numberOfRatings = 0, totalSumOfRating;
    private NavigationView navigationView;
    private TextView navmail, navname;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_info);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        txtHname = (TextView) findViewById(R.id.txtHospitalName);
        vname = (TextView) findViewById(R.id.txtVetName);
        email = (TextView) findViewById(R.id.txtEmail);
        HospitalAddress = (TextView) findViewById(R.id.txtAdd);
        Contact = (TextView) findViewById(R.id.txtContact);
        //btnRate = (Button) findViewById(R.id.btnRate);
        bar = (RatingBar) findViewById(R.id.ratingBar2);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        vkey = getIntent().getStringExtra("Key");
        fdb = FirebaseDatabase.getInstance();
        final UserType objUser = (UserType) getApplicationContext();
        String userTyp = objUser.getUserType();
        if (userTyp.equals("finder") || (userTyp.equals("vet"))) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
            bar.setLayoutParams(param);
            bar.setVisibility(View.INVISIBLE);
            //btnRate.setLayoutParams(param);
            //btnRate.setVisibility(View.INVISIBLE);
        }

        myref = fdb.getReference("Vet");

        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot d, String s) {
                if (d.getKey().equals(vkey)) {
                    Log.d("Key in datasnapshot-->", vkey);
                    txtHname.setText(d.child("hospital").getValue().toString());
                    vname.setText(d.child("name").getValue().toString());
                    email.setText(d.child("email").getValue().toString());
                    Contact.setText(d.child("contact").getValue().toString());
                    HospitalAddress.setText(d.child("hospitaladd").getValue().toString());

                    bar.setRating(Float.parseFloat(d.child("serving").getValue().toString()));
                    flag=1;
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

        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(flag==1){
                    Intent intent = getIntent();
                    Bundle bd = intent.getExtras();
                    vkey = bd.get("Key").toString();
                    g = bar.getRating();
                    count = (int) g;
                    fdb = FirebaseDatabase.getInstance();
                    myref = fdb.getReference("Vet");
                    myref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot d, String s) {
                            if (d.getKey().equals(vkey)) {
                                Integer p = Integer.parseInt(d.child("count").getValue().toString());
                                if (p == 0) {
                                    p++;
                                    FirebaseDatabase.getInstance().getReference("Vet").child(vkey).child("count").setValue(p);
                                    avg = count;
                                } else {
                                    p++;
                                    FirebaseDatabase.getInstance().getReference("Vet").child(vkey).child("count").setValue(p);
                                    rate = Integer.parseInt(d.child("serving").getValue().toString());
                                    totalSumOfRating = (count * (p - 1)) + rate;
                                    avg = totalSumOfRating / p;
                                }
                                rateFdb = FirebaseDatabase.getInstance();
                                rateRef = rateFdb.getReference();
                                rateRef.child("Vet").child(vkey).child("serving").setValue(""+avg);
                                Toast.makeText(VetInfo.this, "Ratingf-->" + g + "int-->" + avg + " done", Toast.LENGTH_LONG).show();
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
                }

            }
        });
       /* btnRate.setOnClickListener(new View.OnClickListener() {
            Intent intent = getIntent();
            Bundle bd = intent.getExtras();
            @Override
            public void onClick(View v) {
                vkey = bd.get("Key").toString();
                g = bar.getRating();
                count = (int) g;
                fdb = FirebaseDatabase.getInstance();
                myref = fdb.getReference("Vet");
                myref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot d, String s) {
                        if (d.getKey().equals(vkey)) {
                            Integer p = Integer.parseInt(d.child("count").getValue().toString());
                            if (p == 0) {
                                p++;
                                FirebaseDatabase.getInstance().getReference("Vet").child(vkey).child("count").setValue(p);
                                avg = count;
                            } else {
                                p++;
                                FirebaseDatabase.getInstance().getReference("Vet").child(vkey).child("count").setValue(p);
                                rate = Integer.parseInt(d.child("serving").getValue().toString());
                                totalSumOfRating = (count * (p - 1)) + rate;
                                avg = totalSumOfRating / p;
                            }
                            rateFdb = FirebaseDatabase.getInstance();
                            rateRef = rateFdb.getReference();
                            rateRef.child("Vet").child(vkey).child("serving").setValue(""+avg);
                            Toast.makeText(VetInfo.this, "Ratingf-->" + g + "int-->" + avg + " done", Toast.LENGTH_LONG).show();
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
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_vol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_upload) {
            Intent i = new Intent(VetInfo.this, UploadFaunaDetails.class);
            startActivity(i);
        } else if (id == R.id.nav_fauna) {
            Intent i = new Intent(VetInfo.this, FaunasInNeed.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(VetInfo.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(VetInfo.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
            Intent i = new Intent(VetInfo.this, AllVet.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_myProfile) {
            final UserType objUser= (UserType) getApplicationContext();
            String userTyp=objUser.getUserType();
            Log.d("userTyp-->",userTyp);
            if(userTyp.equals("vet")){
                Intent i =new Intent(VetInfo.this,VetProfile.class);
                startActivity(i);
            }
            else if(userTyp.equals("vol")){
                Intent i =new Intent(VetInfo.this,VolProfile.class);
                startActivity(i);
            }else if(userTyp.equals("finder")){
                Intent i =new Intent(VetInfo.this,FinderProfile.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_tips) {
            Intent i =new Intent(VetInfo.this,Tips.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
