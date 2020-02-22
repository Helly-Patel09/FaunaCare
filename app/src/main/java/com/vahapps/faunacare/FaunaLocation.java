package com.vahapps.faunacare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FaunaLocation extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    private String faunaId;
    private Fauna obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fauna_location);
        faunaId = getIntent().getStringExtra("faunaKey");
        //faunaId="-L6BRyXL9iHbE1OWUOng";
        Log.d("faunakeyintent",faunaId);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available
            // Getting reference to the SupportMapFragment of activity_main.xml
            if (googleMap == null) {
                Log.d("map","null");
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                fm.getMapAsync(this);
            }
            // Getting GoogleMap object from the fragment
            // googleMap = fm.getMapAsync();
            // Enabling MyLocation Layer of Google Map
        }
    }

    @Override
    public void onMapReady(GoogleMap gm) {
        googleMap=gm;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0243979,72.5301738), (float) 10.0));
        //googleMap.setOnInfoWindowClickListener(this);
        setMap();
    }
    private void setMap(){
        Log.d("setmap","in");
        FirebaseDatabase.getInstance().getReference("Fauna")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //Log.d("inn added","yes");
                        if (dataSnapshot.getKey().equals(faunaId))
                        {
                            Log.d("datasnapkey",dataSnapshot.getKey());
                            obj=dataSnapshot.getValue(Fauna.class);
                            Log.d("fauna name",obj.getDescription());
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(obj.getLatitude()),Double.parseDouble(obj.getLongitude())))
                                    .title(obj.getDescription())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).setTag("FaunaLoc");

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(obj.getLatitude()),Double.parseDouble(obj.getLongitude())), (float) 13.0));
                            //obj.setLatitude(dataSnapshot.child("latitude").getValue().toString());
                            //fauna_lat=dataSnapshot.child("latitude").getValue().toString();
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {                    }
                });


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.w("Click", "test");
                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(FaunaLocation.this);
                googleMap.setInfoWindowAdapter(customInfoWindow);
                marker.showInfoWindow();
                return false;
            }
        });
    }
}
