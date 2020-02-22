package com.vahapps.faunacare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.DowngradeableSafeParcel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MapDemo extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    SharedPreferences sharedPreferences;
    int locationCount = 0;
    private String faunaId;
    private FirebaseDatabase fdb;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Fauna obj;
    String vet_long,vet_lat;
    HashMap<Double, Vet> nearestVetHashmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_demo);
        faunaId = getIntent().getStringExtra("faunaKey");
        //faunaId="-L6BRyXL9iHbE1OWUOng";
        Log.d("faunakeyintent",faunaId);
        mAuth= FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        //userdb=fdb.getReference(key);

        nearestVetHashmap=new HashMap<>();
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
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
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).setTag("Fauna");

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(obj.getLatitude()),Double.parseDouble(obj.getLongitude())), (float) 11.5));
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
        mDatabase = fdb.getReference("Vet");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vet vetObj=dataSnapshot.getValue(Vet.class);
                Log.d("datasnapvet keyyyyyyy",dataSnapshot.getKey());
                String hospitalName = dataSnapshot.child("hospital").getValue().toString();
                String hospitalAdd = dataSnapshot.child("hospitaladd").getValue().toString();
                vet_long=dataSnapshot.child("gpsLongitude").getValue().toString();
                vet_lat=dataSnapshot.child("gpsLatitude").getValue().toString();
                float[] distanceResults = new float[1];
                //Log.d("vet_lat",vet_lat);
                //Log.d("vet_long",vet_long);
                //Log.d("fname",obj.getDescription());
                //Log.d("f_lat",obj.getLatitude());
                //Log.d("f_long",obj.getLongitude());
                Location.distanceBetween(Double.parseDouble(vet_lat), Double.parseDouble(vet_long), Double.parseDouble(obj.getLatitude()), Double.parseDouble(obj.getLongitude()),distanceResults);
                // String d=Float.toString(result[0]);
                double distanceKm = distanceResults[0]/1000; //* UnitConversions.M_TO_KM;
                Log.d("hospitl",dataSnapshot.child("name").getValue().toString());

                if(distanceKm<20){
                    nearestVetHashmap.put(distanceKm,vetObj);
                }

                System.out.println("Before Sorting:");

                Set set = nearestVetHashmap.entrySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()) {
                    Map.Entry me = (Map.Entry)iterator.next();
                    Log.d(me.getKey() + ": ", "ok");
                }

                Map<Double, Vet> map = new TreeMap<>(nearestVetHashmap);
                System.out.println("After Sorting:");
                int count=0;
                //Set set2 = map.entrySet();
                Iterator<Map.Entry<Double, Vet>> itr = map.entrySet().iterator();
                while(itr.hasNext())
                {
                    Map.Entry<Double, Vet> entry = itr.next();
                    count++;
                    Log.d("count", String.valueOf(count));
                    //if(count>10 && distanceKm>20)
                    //{
                    //  Log.d("Key : "+entry.getKey()," Removed.");
                    //     itr.remove();  // Call Iterator's remove method.
                    //     count--;
                    //}
                    // else {
                    Log.d("Key : "+entry.getKey()," entered");
                    // }
                }
                /*Iterator iterator2 = set2.iterator();
                while(iterator2.hasNext()) {
                    count++;
                    if(count>3)
                    {
                        Log.d("count", String.valueOf(count));
                        Map.Entry me2 = (Map.Entry)iterator2.next();
                        Log.d(me2.getKey() + ": ", "ok");
                        map.remove(me2.getKey());
                    }
                    else {
                        Log.d("count", String.valueOf(count));
                        Map.Entry me2 = (Map.Entry)iterator2.next();
                        Log.d(me2.getKey() + ": ", "ok");
                    }
                    //Map.Entry me2 = (Map.Entry)iterator2.next();

                }*/
                // nearestVetHashmap= (HashMap<Double, Vet>) map;
                if(map.containsKey(distanceKm)){
                    Log.d("distance",""+distanceKm);
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(vet_lat),Double.parseDouble(vet_long)))
                            .title(dataSnapshot.getKey())
                            .snippet(String.valueOf(distanceKm))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(vetObj)
                    ;
                }
                //int count=nearestVetHashmap.size();


                //Log.d("hashmap",entry.getKey() + " : " + entry.getValue());
                /*nearestVet.add(distanceKm);
                Collections.sort(nearestVet);
                if (nearestVet.size()<5){
                    arrayList.add(dataSnapshot.child("name").getValue().toString()+": "+hospitalName+": "+hospitalAdd);
                    vetKeys.add(dataSnapshot.getKey());
                }else if (nearestVet.get(nearestVet.size()-1)>=distanceKm){
                    arrayList.add(dataSnapshot.child("name").getValue().toString()+": "+hospitalName+": "+hospitalAdd);
                    vetKeys.add(dataSnapshot.getKey());
                    //arrayList.add("hello");
                }*/
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
        /*googleMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                locationCount++;
                // Drawing marker on the map
                drawMarker(point);
                *//** Opening the editor object to write data to sharedPreferences *//*
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Storing the latitude for the i-th location
                editor.putString("lat"+ Integer.toString((locationCount-1)), Double.toString(point.latitude));
                // Storing the longitude for the i-th location
                editor.putString("lng"+ Integer.toString((locationCount-1)), Double.toString(point.longitude));
                // Storing the count of locations or marker count
                editor.putInt("locationCount", locationCount);
                *//** Storing the zoom level to the shared preferences *//*
                editor.putString("zoom", Float.toString(googleMap.getCameraPosition().zoom));
                *//** Saving the values stored in the shared preferences *//*
                editor.commit();
                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();
            }
        });*/
        //demoCall();

        /*googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                // Removing the marker and circle from the Google Map
                googleMap.clear();
                // Opening the editor object to delete data from sharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Clearing the editor
                editor.clear();
                // Committing the changes
                editor.commit();
                // Setting locationCount to zero
                locationCount=0;
            }
        });*/
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.w("Click", "test");
                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MapDemo.this);
                googleMap.setInfoWindowAdapter(customInfoWindow);
                marker.showInfoWindow();
                return false;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(final Marker marker) {
                if (!marker.getTag().equals("Fauna")){
                    Toast.makeText(MapDemo.this, "inside Info window clicked", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(MapDemo.this);
                    aboutDialogBuilder.setTitle("Confirmation").setMessage("Select this Vet?");
                    aboutDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String vetKey = marker.getTitle();
                                    DatabaseReference faunaRef=fdb.getReference();
                                    faunaRef.child("Fauna").child(faunaId).child("status").setValue("3");
                                    faunaRef.child("Fauna").child(faunaId).child("vetId").setValue(vetKey);
                                    Intent i=new Intent(MapDemo.this,ChooseVol.class);
                                    i.putExtra("faunaKey",faunaId);
                                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    aboutDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog aboutDialog = aboutDialogBuilder.create();
                    aboutDialog.show();


                }

            }
        });


        // Opening the sharedPreferences object
        /*sharedPreferences = getSharedPreferences("location", 0);
        // Getting number of locations already stored
        locationCount = sharedPreferences.getInt("locationCount", 0);
        // Getting stored zoom level if exists else return 0
        String zoom = sharedPreferences.getString("zoom", "0");
        // If locations are already saved
        if(locationCount!=0){
            String lat = "";
            String lng = "";
            // Iterating through all the locations stored
            for(int i=0;i<locationCount;i++){
                // Getting the latitude of the i-th location
                lat = sharedPreferences.getString("lat"+i,"0");
                // Getting the longitude of the i-th location
                lng = sharedPreferences.getString("lng"+i,"0");
                // Drawing marker on the map
                drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }
            // Moving CameraPosition to last clicked position
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
            // Setting the zoom level in the map on last position  is clicked
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));
        }*/
    }

    private void demoCall() {
        Log.d("afer ref","yes");
        for(HashMap.Entry entry:nearestVetHashmap.entrySet()){
            Vet vetobj= (Vet) entry.getValue();
            String lat=vetobj.getGpsLatitude();
            String lng=vetobj.getGpsLongitude();
            Log.d("marker lat:::::::::::::",lat);
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)))
                    .title(vetobj.getHospital())).setTag(0);

            Log.d("hashmap",entry.getKey() + " : " + entry.getValue());
        }
    }
 /*   private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }*/



    @Override
    public void onMapReady(GoogleMap gm) {

        Log.d("mapready","yes");
        googleMap=gm;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0243979,72.5301738), (float) 10.0));
        //googleMap.setOnInfoWindowClickListener(this);
        setMap();
        //demoCall();
    }
    /*@Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("outside infowindow","clickd");
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("outside marker","clicked");
        Toast.makeText(this, "outside marker clicked",
                Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d("outside getinfowindow","called");
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //return null;
        Log.d("infocontents","called");
        return prepareInfoView(marker);
    }

    private View prepareInfoView(Marker marker) {
        Log.d("prepare","called");
        LinearLayout infoView = new LinearLayout(MapDemo.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(MapDemo.this);
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_dialog_map);
        infoImageView.setImageDrawable(drawable);
        infoView.addView(infoImageView);

        LinearLayout subInfoView = new LinearLayout(MapDemo.this);
        LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subInfoView.setOrientation(LinearLayout.VERTICAL);
        subInfoView.setLayoutParams(subInfoViewParams);

        TextView subInfoLat = new TextView(MapDemo.this);
        subInfoLat.setText("Lat: " + marker.getPosition().latitude);
        TextView subInfoLnt = new TextView(MapDemo.this);
        subInfoLnt.setText("Lnt: " + marker.getPosition().longitude);
        subInfoView.addView(subInfoLat);
        subInfoView.addView(subInfoLnt);
        infoView.addView(subInfoView);

        return infoView;
    }
    GoogleMap.OnInfoWindowClickListener MyOnInfoWindowClickListener
            = new GoogleMap.OnInfoWindowClickListener(){
        @Override
        public void onInfoWindowClick(Marker marker) {
            Toast.makeText(MapDemo.this,"onInfoWindowClick():\n" +
                            marker.getPosition().latitude + "\n" +
                            marker.getPosition().longitude,
                    Toast.LENGTH_LONG).show();
        }
    };*/
}
