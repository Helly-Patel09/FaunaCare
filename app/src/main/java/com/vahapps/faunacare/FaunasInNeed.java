package com.vahapps.faunacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FaunasInNeed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Fauna> uploads;
    private SwipeRefreshLayout refresher;
    private FirebaseDatabase fdb;
    private DatabaseReference myref, currentDb;
    private NavigationView navigationView;
    private TextView navmail, navname, noFauna;
    private String userkey, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faunas_in_need);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.faunalistview);
        noFauna = (TextView) findViewById(R.id.txtnoFauna);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresher = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        fdb = FirebaseDatabase.getInstance();
        getData();
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View hView = navigationView.getHeaderView(0);
        navmail = hView.findViewById(R.id.textViewmail);
        navname = hView.findViewById(R.id.textViewname);
        navmail.setText(user.getEmail());
    }

    private void getData() {
        refresher.setRefreshing(true);
        adapter = null;
        recyclerView.setAdapter(adapter);
        noFauna.setVisibility(View.VISIBLE);
        myref = fdb.getReference("Fauna");
        currentDb = FirebaseDatabase.getInstance().getReference();
        uploads = new ArrayList<>();
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ds key-----", dataSnapshot.getKey());
                if (dataSnapshot.child("status").exists()) {
                    Log.d("name:::", dataSnapshot.child("description").getValue().toString());
                    if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                        Fauna upload = dataSnapshot.getValue(Fauna.class);
                        noFauna.setVisibility(View.GONE);
                        uploads.add(upload);
                        adapter = new FaunasNeedAdapter(getApplicationContext(), uploads);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mLayoutManager.setReverseLayout(true);
                        mLayoutManager.setStackFromEnd(true);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(adapter);
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
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        refresher.setRefreshing(false);
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
        //getMenuInflater().inflate(R.menu.home, menu);
        return false;
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
            Intent i = new Intent(FaunasInNeed.this, UploadFaunaDetails.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_fauna) {
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(FaunasInNeed.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(FaunasInNeed.this, MainActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
            Intent i = new Intent(FaunasInNeed.this, AllVet.class);
            startActivity(i);
        } else if (id == R.id.nav_myProfile) {
            Intent i = new Intent(FaunasInNeed.this, VetProfile.class);
            startActivity(i);
        } else if (id == R.id.nav_tips) {
            Intent i = new Intent(FaunasInNeed.this, Tips.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}