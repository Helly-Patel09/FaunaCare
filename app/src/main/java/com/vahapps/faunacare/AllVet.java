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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AllVet extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<Vet> arrayList;
    private ArrayList<String> vetKeys;
    private ArrayList<Integer> rateList;
    private AllVetAdapter adapter;
    private FirebaseDatabase fdb;
    private DatabaseReference mDatabase;
    private RatingBar ratingBar;
    private RecyclerView listView;
    private String faunaId;
    private SwipeRefreshLayout refresher;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private TextView navmail, navname;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vet);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        listView = (RecyclerView) findViewById(R.id.list_viewvet);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fdb = FirebaseDatabase.getInstance();
        refresher= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();

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

    private void getData() {
        refresher.setRefreshing(true);
        adapter = null;
        //adapter = new AllVetAdapter(getBaseContext(), arrayList, vetKeys, true);
        listView.setAdapter(adapter);
        arrayList = new ArrayList<>();
        vetKeys = new ArrayList<>();
        rateList = new ArrayList<>();
        mDatabase = fdb.getReference("Vet");
        Log.d("vettttttt", "mdatbase initialzed");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("VAH27datasnap key-->", dataSnapshot.getKey());
                String hospitalName = dataSnapshot.child("hospital").getValue().toString().trim();
                String name = dataSnapshot.child("name").getValue().toString();
                String rateString = dataSnapshot.child("serving").getValue().toString();
                Log.d("RateString --> ", rateString);
                Integer r = Integer.parseInt(rateString);
                rateList.add(r);

                Vet vetobj=dataSnapshot.getValue(Vet.class);
                Log.d("VAH27Rate--> ", r.toString());
                arrayList.add(vetobj);
                vetKeys.add(dataSnapshot.getKey());
                adapter = new AllVetAdapter(getBaseContext(), arrayList, vetKeys, rateList, true);
                listView.setAdapter(adapter);
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
            Intent i = new Intent(AllVet.this, UploadFaunaDetails.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fauna) {
            Intent i = new Intent(AllVet.this, FaunasInNeed.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(AllVet.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(AllVet.this, MainActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
        } else if (id == R.id.nav_myProfile) {
            Intent i = new Intent(AllVet.this, VetProfile.class);
            startActivity(i);
        } else if (id == R.id.nav_tips) {
            Intent i = new Intent(AllVet.this, Tips.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {

    }
}
