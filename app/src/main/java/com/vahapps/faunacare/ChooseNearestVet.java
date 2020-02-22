package com.vahapps.faunacare;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ChooseNearestVet extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ArrayList<String> vetKeys;
    private GridListAdapter adapter;
    private FirebaseDatabase fdb;
    private DatabaseReference mDatabase,userdb;
    private Button btnChooseVet;
    private String faunaId;
    private FirebaseAuth mAuth;
    private Fauna obj;
    private ArrayList<Double> nearestVet;
    String fauna_long,fauna_lat,vet_long,vet_lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_nearest_vet);

        faunaId = getIntent().getStringExtra("faunaKey");
        btnChooseVet= (Button) findViewById(R.id.btnChooseVet);
        //View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.choose_nearest_vet,null);
        ListView listView = (ListView) findViewById(R.id.list_view);
        arrayList = new ArrayList<>();
        nearestVet=new ArrayList<>();
        vetKeys = new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        String key=mAuth.getCurrentUser().getUid();
        fdb = FirebaseDatabase.getInstance();
        //userdb=fdb.getReference(key);
        mDatabase = fdb.getReference("Vet");

        /*userdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });*/

        FirebaseDatabase.getInstance().getReference("Fauna")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //Log.d("inn added","yes");
                        if (dataSnapshot.getKey().equals(faunaId))
                        {
                            Log.d("datasnapkey",dataSnapshot.getKey());
                            obj=dataSnapshot.getValue(Fauna.class);
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

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vet vetObj=dataSnapshot.getValue(Vet.class);
                //Log.d("datasnap keyyyyyyy",dataSnapshot.getKey());
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
                Log.d("distance",""+distanceKm);
                nearestVet.add(distanceKm);
                Collections.sort(nearestVet);
                if (nearestVet.size()<5){
                    arrayList.add(dataSnapshot.child("name").getValue().toString()+": "+hospitalName+": "+hospitalAdd);
                    vetKeys.add(dataSnapshot.getKey());
                }else if (nearestVet.get(nearestVet.size()-1)>=distanceKm){
                    arrayList.add(dataSnapshot.child("name").getValue().toString()+": "+hospitalName+": "+hospitalAdd);
                    vetKeys.add(dataSnapshot.getKey());
                    //arrayList.add("hello");
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
        });/*
        for (int i = 1; i <= 50; i++)
            arrayList.add("ListView Items " + i);*/
        //ArrayList<Double> d= (ArrayList<Double>) nearestVet.subList(0,5);
        adapter = new GridListAdapter(getBaseContext(), arrayList,vetKeys, true);
        listView.setAdapter(adapter);
        findViewById(R.id.btnChooseVet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected position
                String vetKey = adapter.getSelectedItem();
                DatabaseReference faunaRef=fdb.getReference();
                faunaRef.child("Fauna").child(faunaId).child("status").setValue("3");
                faunaRef.child("Fauna").child(faunaId).child("vetId").setValue(vetKey);
                Intent i=new Intent(ChooseNearestVet.this,ChooseVol.class);
                i.putExtra("faunaKey",faunaId);
                startActivity(i);
            }
        });
    }
}
