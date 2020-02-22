package com.vahapps.faunacare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static int timeout1 = 1000;
    private static int timeout2 = 000;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //addAutoStartup();
        //if (user == null) {
        if (ContextCompat.checkSelfPermission(SplashScreen.this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SplashScreen.this, "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SplashScreen.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{"android.permission.CAMERA"
                            , "android.permission.ACCESS_FINE_LOCATION"
                            , "android.permission.WRITE_EXTERNAL_STORAGE"}, 44);
        }else {
            final String count = getIntent().getStringExtra("count");
            final UserType objUser = (UserType) getApplicationContext();
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                // Permission has already been granted
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, timeout1);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        //NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                        //if (conMgr.getActiveNetworkInfo() == null ) {
                        //EventLogTags.Description.setVisibility(View.INVISIBLE);
                        if (!CheckInternetClass.isConnected(getApplicationContext())) {
                            new AlertDialog.Builder(SplashScreen.this)
                                    .setTitle(getResources().getString(R.string.app_name))
                                    .setMessage("You are not connected to Internet!")
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(SplashScreen.this, SplashScreen.class);
                                            i.putExtra("count", "1");
                                            startActivity(i);
                                            finish();
                                        }
                                    }).show();
                        } else {

                            Log.d("yayyyy", "internet");
                            if (count != null)
                                Snackbar.make(getWindow().getDecorView().getRootView(), "You are now online...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            //if (user.isEmailVerified())
                            if (true) {
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
                                                    intent = new Intent(SplashScreen.this, Admin.class);
                                                    objUser.setUserType("admin");
                                                    //Log.d("username::",dataSnapshot.child(userId).child("name").getValue().toString());
                                                    //objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                    //intent.putExtra("userType","admin");
                                                } else if (key.equals("Finder")) {
                                                    intent = new Intent(SplashScreen.this, Home.class);
                                                    objUser.setUserType("finder");
                                                    Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                    objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                    //intent.putExtra("userType","finder");
                                                } else if (key.equals("Volunteer")) {
                                                    intent = new Intent(SplashScreen.this, Home.class);
                                                    objUser.setUserType("vol");
                                                    Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                    objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                    //intent.putExtra("userType","vol");
                                                } else if (key.equals("Vet")) {
                                                    intent = new Intent(SplashScreen.this, Home.class);
                                                    objUser.setUserType("vet");
                                                    Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                    objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                    //intent.putExtra("userType","vet");
                                                }
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
                            } else {
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                    /*if(user.getEmail().equals("admin@gmail.com"))
                    {
                        Intent intent = new Intent(SplashScreen.this, Admin.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(SplashScreen.this, Home.class);
                        startActivity(intent);
                        finish();
                    }*/
                        }
                    }
                }, timeout2);
            }
        }
    }

    /*public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            try {
                Log.d("activenet","not null");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                InetAddress ipAddr = InetAddress.getByName("www.javatpoint.com");
                return !ipAddr.equals("");
                *//*URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }*//*
            } catch (Exception e) {
                Log.i("warning", "Error checking internet connection", e);
                return false;
            }
        }
        Log.d("activenet","null");
        return false;
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("req", "permmm");
        switch (requestCode) {
            case 44: {
                Log.d("req", "44");
                /*int index = 0;
                Map<String, Integer> PermissionsMap = new HashMap<>();
                for (String permission : permissions){
                    PermissionsMap.put(permission, grantResults[index]);
                    index++;
                }
                if((PermissionsMap.get(ACCESS_FINE_LOCATION) != 0)
                        && PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != 0
                        && PermissionsMap.get(CAMERA) != 0){
                    Toast.makeText(this, "Location and SMS permissions are a must", Toast.LENGTH_SHORT).show();
                    finish();
                }*/
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 3
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    final String count = getIntent().getStringExtra("count");
                    final UserType objUser = (UserType) getApplicationContext();
                    mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        // Permission has already been granted
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, timeout1);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                //NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                                //if (conMgr.getActiveNetworkInfo() == null ) {
                                //EventLogTags.Description.setVisibility(View.INVISIBLE);
                                if (!CheckInternetClass.isConnected(getApplicationContext())) {
                                    new AlertDialog.Builder(SplashScreen.this)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("You are not connected to Internet!")
                                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent i = new Intent(SplashScreen.this, SplashScreen.class);
                                                    i.putExtra("count", "1");
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }).show();
                                } else {

                                    Log.d("yayyyy", "internet");
                                    if (count != null)
                                        Snackbar.make(getWindow().getDecorView().getRootView(), "You are now online...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                                    //if (user.isEmailVerified())
                                    if (true) {
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
                                                            intent = new Intent(SplashScreen.this, Admin.class);
                                                            objUser.setUserType("admin");
                                                            //Log.d("username::",dataSnapshot.child(userId).child("name").getValue().toString());
                                                            //objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            //intent.putExtra("userType","admin");
                                                        } else if (key.equals("Finder")) {
                                                            intent = new Intent(SplashScreen.this, Home.class);
                                                            objUser.setUserType("finder");
                                                            Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            //intent.putExtra("userType","finder");
                                                        } else if (key.equals("Volunteer")) {
                                                            intent = new Intent(SplashScreen.this, Home.class);
                                                            objUser.setUserType("vol");
                                                            Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            //intent.putExtra("userType","vol");
                                                        } else if (key.equals("Vet")) {
                                                            intent = new Intent(SplashScreen.this, Home.class);
                                                            objUser.setUserType("vet");
                                                            Log.d("username::", dataSnapshot.child(userId).child("name").getValue().toString());
                                                            objUser.setUserName(dataSnapshot.child(userId).child("name").getValue().toString());
                                                            //intent.putExtra("userType","vet");
                                                        }
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
                                    } else {
                                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }

                    /*if(user.getEmail().equals("admin@gmail.com"))
                    {
                        Intent intent = new Intent(SplashScreen.this, Admin.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(SplashScreen.this, Home.class);
                        startActivity(intent);
                        finish();
                    }*/
                                }
                            }
                        }, timeout2);
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "All Permissions necessary!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            // Log.d()
            if (list.size() > 0) {
                Log.d("instartup", "yes");
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }
}