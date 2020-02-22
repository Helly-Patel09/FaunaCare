package com.vahapps.faunacare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
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


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseReference mDatabase, currentDb;
    private ProgressDialog progressDialog;
    private List<Fauna> uploads;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "Fauna";
    private FirebaseAuth mAuth;
    private Intent service;
    private NavigationView navigationView;
    private TextView navmail, navname;
    private String userkey, name;
    private SwipeRefreshLayout refresher;
    private FirebaseUser user;
    private Snackbar mSnackbar;
    private RelativeLayout layout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("Hoooooooooooooooooooome", "in home oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            //EventLogTags.Description.setVisibility(View.INVISIBLE);
            new AlertDialog.Builder(Home.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("You are not connected to Internet!")
                    .setPositiveButton("OK", null).show();
        } else {
            // dialog = ProgressDialog.show(WelcomePage.this, "", "Loading...", true,false);
            // new Welcome_Page().execute();
            //navmail= (TextView) findViewById(R.id.textViewhomemail);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            refresher= (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
            layout= (RelativeLayout) findViewById(R.id.activity_show_images);
            refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData();
                }
            });
            final ComponentName onBootReceiver = new ComponentName(getApplication().getPackageName(), MyBroadcastReceiver.class.getName());
            //if(getPackageManager().getComponentEnabledSetting(onBootReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
             //   getPackageManager().setComponentEnabledSetting(onBootReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
            Intent service = new Intent(Home.this, ListenOrder.class);
            startService(service);
            /*recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
                @Override
                public boolean onFling(int velocityX, int velocityY) {
                    if (Math.abs(velocityY) > 2) {
                        velocityY =  (int) (2 *Math.signum((double)velocityY));
                        recyclerView.fling(velocityX, velocityY);
                        return true;
                    }
                    return false;
                }
            });*/
            getData();
            //getData(savedInstanceState);
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Home.this, UploadFaunaDetails.class);
                    startActivity(i);
                }
            });
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            View hView = navigationView.getHeaderView(0);
            navmail = hView.findViewById(R.id.textViewmail);
            navname = hView.findViewById(R.id.textViewname);
            navmail.setText(user.getEmail());
            mSnackbar = Snackbar.make(layout, "Press again to exit", Snackbar.LENGTH_SHORT);
        }
    }

    private void getData() {
        refresher.setRefreshing(true);
        adapter = null;
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        uploads = new ArrayList<>();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference("Fauna");
        currentDb = FirebaseDatabase.getInstance().getReference();
        //service = new Intent(Home.this, ListenOrder.class);
        //startService(service);

       // final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        adapter = new MyAdapter(getApplicationContext(), uploads);
        recyclerView.setAdapter(adapter);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressDialog.dismiss();
                //Log.d("home..ds...",dataSnapshot.getKey());
                final Fauna upload = dataSnapshot.getValue(Fauna.class);
                uploads.add(0,upload);
                adapter.notifyItemInserted(0);
                //recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                //recyclerView.setLayoutManager(mLayoutManager);
                /*ViewTreeObserver vto = recyclerView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int width  = recyclerView.getWidth();
                        //height = choosevollayout.getMeasuredHeight();
                        Log.d("homerecyclerwidth",width+"");
                    }
                });*/
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*Fauna upload = dataSnapshot.getValue(Fauna.class);
                uploads.add(upload);
                *//*for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("home...ds childs",postSnapshot.getKey());
                    Fauna upload = postSnapshot.getValue(Fauna.class);
                    uploads.add(upload);
                }*//*
                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), uploads);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);*/
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        /*mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), uploads);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });*/
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
        //Bundle bundle=getIntent().getExtras();
        //String faunaKey=null;
        //if(bundle!=null)
        //{
        //faunaKey=getIntent().getStringExtra("faunaKey");
        //Log.wtf("key=====Home===",faunaKey);
        //}
       /* Intent service = new Intent(Home.this, ListenOrder.class);
        service.putExtra("faunaKey",faunaKey);
        startService(service);*/
       refresher.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("homestaaaart", "staaaaart");
        /*Intent service = new Intent(Home.this, ListenOrder.class);
        startService(service);*/
        /*String faunaKey=null;
        //if(bundle!=null)
        //{
        faunaKey=getIntent().getStringExtra("faunaKey");
        Log.wtf("key=====Home===",faunaKey);
        //}
        Intent service = new Intent(Home.this, ListenOrder.class);
        service.putExtra("faunaKey",faunaKey);
        startService(service);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("homestooooop", "stoooop");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("restarrrrrt", "restarrrrt");
        /*stopService(service);
        finish();
        startActivity(getIntent());*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (mSnackbar.isShown()) {
                super.onBackPressed();
                //System.exit(0);
            } else {
                mSnackbar.show();
            }
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
            Intent i = new Intent(Home.this, UploadFaunaDetails.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fauna) {
            Intent i = new Intent(Home.this, FaunasInNeed.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(Home.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
            Intent i = new Intent(Home.this, AllVet.class);
            startActivity(i);
        } else if (id == R.id.nav_myProfile) {
            final UserType objUser= (UserType) getApplicationContext();
            String userTyp=objUser.getUserType();
            Log.d("userTyp-->",userTyp);
            if(userTyp.equals("vet")){
                Intent i =new Intent(Home.this,VetProfile.class);
                startActivity(i);
            }
            else if(userTyp.equals("vol")){
                Intent i =new Intent(Home.this,VolProfile.class);
                startActivity(i);
            }else if(userTyp.equals("finder")){
                Intent i =new Intent(Home.this,FinderProfile.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_tips) {
            Intent i =new Intent(Home.this,Tips.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}