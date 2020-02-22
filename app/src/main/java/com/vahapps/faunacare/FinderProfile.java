package com.vahapps.faunacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FinderProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnUpdate,btnLocate;
    private TextView txtVolName,txtEmail,txtContact,navmail,navname;
    private EditText editTextVolName,editTextContact;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref,currentDb,fref ;
    private FirebaseUser user;
    private NavigationView navigationView;
    private String userkey, name;
    private String uid;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Fauna> uploads;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_profile);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btnUpdate= (Button) findViewById(R.id.btnUpdate);
        txtVolName= (TextView) findViewById(R.id.NameD);
        txtEmail= (TextView) findViewById(R.id.EmailD);
        editTextVolName= (EditText) findViewById(R.id.txtName);
        editTextContact= (EditText) findViewById(R.id.txtContactNumber);
        txtContact= (TextView) findViewById(R.id.ContactNumberD);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploads = new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid= user.getUid();
        Log.d("uid-->",uid);
        getData();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                txtVolName.setVisibility(View.GONE);
                txtEmail.setVisibility(View.GONE);
                editTextVolName.setVisibility(View.VISIBLE);

                txtContact.setVisibility(View.GONE);
                editTextContact.setVisibility(View.VISIBLE);

                btnUpdate.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                myref=FirebaseDatabase.getInstance().getReference("Finder");
                myref.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot d, String s) {
                        if(uid.equals(d.getKey())){
                            editTextContact.setText( d.child("contact").getValue().toString());
                            editTextVolName.setText(d.child("name").getValue().toString());}
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
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab.setVisibility(View.VISIBLE);
                myref=FirebaseDatabase.getInstance().getReference("Finder");
                myref.child(uid).child("contact").setValue(editTextContact.getText().toString());
                myref.child(uid).child("name").setValue(editTextVolName.getText().toString());

                Toast.makeText(FinderProfile.this, "Updated Profile", Toast.LENGTH_LONG).show();
                txtVolName.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                editTextVolName.setVisibility(View.GONE);
                txtContact.setVisibility(View.VISIBLE);
                editTextContact.setVisibility(View.GONE);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        View hView = navigationView.getHeaderView(0);
        navmail = hView.findViewById(R.id.textViewmail);
        navname = hView.findViewById(R.id.textViewname);
        navmail.setText(user.getEmail());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void getData() {
        myref=FirebaseDatabase.getInstance().getReference("Finder");
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot d, String s) {
                if(uid.equals(d.getKey())){
                    txtVolName.setText(d.child("name").getValue().toString());
                    txtEmail.setText(d.child("email").getValue().toString());
                    txtContact.setText(d.child("contact").getValue().toString());}
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
        fref = FirebaseDatabase.getInstance().getReference("Fauna");
        fref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot d, String s) {
                Log.d("inchild",d.child("uploaderId").getValue().toString());
                Fauna upload = d.getValue(Fauna.class);
                if (uid.equals(d.child("uploaderId").getValue().toString())) {
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_upload) {
            Intent i = new Intent(FinderProfile.this, UploadFaunaDetails.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fauna) {
            Intent i = new Intent(FinderProfile.this, FaunasInNeed.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(FinderProfile.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(FinderProfile.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
            Intent i = new Intent(FinderProfile.this, AllVet.class);
            startActivity(i);
        } else if (id == R.id.nav_myProfile) {
        } else if (id == R.id.nav_tips) {
            Intent i =new Intent(FinderProfile.this,Tips.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
