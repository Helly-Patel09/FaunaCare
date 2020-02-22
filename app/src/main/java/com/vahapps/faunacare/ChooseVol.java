package com.vahapps.faunacare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;

import java.net.InetAddress;
import java.util.ArrayList;

public class ChooseVol extends AppCompatActivity {
    private ImageView faunaImage, statusIcon, showVolImg,faunaLoc,faunaTreadted;
    private TextView desc, txtlocation, noCommentstxt, txtStatus,txtfinderName,txtvolName,txtDate,txtTime;
    private Button chooseVol;
    private FirebaseDatabase fdb;
    private DatabaseReference myref;
    private StorageReference storageReference;
    private DatabaseReference mDatabase, cDatabase, uDatabase, nDatabase,dDatabase;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "Fauna";
    private ProgressDialog progressDialog;
    private String url,treatedUrl;
    private String loc;
    private ImageButton addTip;
    private EditText txtTip;
    private FirebaseAuth mAuth;
    private String faunaId;
    private ArrayList<String> arrayList, commenterImageList;
    private CommentAdapter adapter;
    private Button BtnReport;
    private Button BtnBlock, BtnUnreport, BtnTreated,BtnVolArrived,BtnTreated2;
    private ScrollView parent, child;
    private LinearLayout tipLL;
    private String privname;
    private LinearLayout choosevollayout;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_vol);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (!CheckInternetClass.isConnected(getApplicationContext())) {
            //EventLogTags.Description.setVisibility(View.INVISIBLE);
            new AlertDialog.Builder(ChooseVol.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("You are not connected to any Network!")
                    .setPositiveButton("OK", null).show();
        } else {
            /*try {
                InetAddress ipAddr = InetAddress.getByName("www.google.com");
                //You can replace it with your name

            } catch (Exception e) {
                new AlertDialog.Builder(ChooseVol.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("You are not connected to Internet!")
                        .setPositiveButton("OK", null).show();
            }*/
            faunaImage = (ImageView) findViewById(R.id.ShowImageView);
            faunaTreadted = (ImageView) findViewById(R.id.ShowImageView2);
            faunaLoc= (ImageView) findViewById(R.id.faunaLocation);
            statusIcon = (ImageView) findViewById(R.id.statusIcon);
            choosevollayout= (LinearLayout) findViewById(R.id.choosevollayout);
            showVolImg = (ImageView) findViewById(R.id.volshowimg);
            desc = (TextView) findViewById(R.id.desc);
            txtlocation = (TextView) findViewById(R.id.location);
            txtvolName = (TextView) findViewById(R.id.volName);
            txtfinderName = (TextView) findViewById(R.id.finderName);
            chooseVol = (Button) findViewById(R.id.confirmVol);
            addTip = (ImageButton) findViewById(R.id.addTip);
            txtTip = (EditText) findViewById(R.id.txtTip);
            txtDate = (TextView) findViewById(R.id.txtdate);
            txtTime = (TextView) findViewById(R.id.txttime);
            //warn = (TextView) findViewById(R.id.warning);
            noCommentstxt = (TextView) findViewById(R.id.noCommentstxt);
            //BtnReport = (Button) findViewById(R.id.btnReport);
            BtnBlock = (Button) findViewById(R.id.btnBlock);
            //BtnUnreport = (Button) findViewById(R.id.btnMark);
            BtnTreated = (Button) findViewById(R.id.btnTreated);
            BtnTreated2 = (Button) findViewById(R.id.btnTreated2);
            BtnVolArrived = (Button) findViewById(R.id.btnVolArrived);
            txtStatus = (TextView) findViewById(R.id.txtStatus);
            parent = (ScrollView) findViewById(R.id.parent);
            tipLL = (LinearLayout) findViewById(R.id.tipLL);
            //child= (ScrollView) findViewById(R.id.child);
            final ListView listView = (ListView) findViewById(R.id.list_view_vol);

            arrayList = new ArrayList<>();
            commenterImageList = new ArrayList<>();
            faunaId = getIntent().getStringExtra("faunaKey");
            //faunaId="-L9UcIEZBswPUuMtiinm";
            Log.wtf("faunakey=====Choosevol===", faunaId);
            final UserType objUser = (UserType) getApplicationContext();
            String userTyp = objUser.getUserType();
            final String userName = objUser.getUserName();
            //String userTyp="finder";
            if (objUser == null)
                userTyp = "finder";//cheat
            Log.d("usertypppppppeeee", userTyp);


        /*parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.v("PARENT", "PARENT TOUCH");
                findViewById(R.id.child).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        child.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.v("child", "child TOUCH");
                view.getParent().requestDisallowInterceptTouchEvent(true);
                //findViewById(R.id.parent).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });*/
            listView.setFriction(ViewConfiguration.getScrollFriction() * 8);
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //Log.v("child", "child TOUCH");
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            if (userTyp.equals("finder")) {

            } else if (userTyp.equals("vet")) {
                /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                BtnTreated.setLayoutParams(param);
                BtnTreated.setVisibility(View.VISIBLE);*/
            } else if (userTyp.equals("admin")) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, 0);
                /*warn.setVisibility(View.INVISIBLE);
                warn.setLayoutParams(param);*/
                tipLL.setLayoutParams(param);
                tipLL.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                BtnBlock.setLayoutParams(param2);
                BtnBlock.setVisibility(View.VISIBLE);
            } else if (userTyp.equals("vol")) {
                /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                chooseVol.setLayoutParams(param);
                chooseVol.setVisibility(View.VISIBLE);*/
            }

        /*Bundle nb=getIntent().getExtras();
        String faunaKey=nb.getString("faunaKey");
        Log.wtf("faunakey=====Choosevol===",faunaKey);*/
        /*fdb = FirebaseDatabase.getInstance();
        myref = fdb.getReference("Fauna");
        myref= (DatabaseReference) myref.limitToLast(1);
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //String myParentNode = dataSnapshot.getKey();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("key=====",child.getKey().toString());
                    *//*if (child.getKey().toString().equals("key")) {
                        String key = myParentNode.toString();
                        //String value = child.getValue().toString();
                        Log.d("main: ", "key: " + key);
                        if (key.equals("Finder")) {
                            Intent intent = new Intent(ChooseVol.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }*//*
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });*/
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            final String userId = user.getUid();
            fdb = FirebaseDatabase.getInstance();
            mDatabase = fdb.getReference("Fauna");
            cDatabase = FirebaseDatabase.getInstance().getReference("Comment");
            final String finalUserTyp = userTyp;
            Log.d("chosse vol","on create");
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getKey().equals(faunaId)) {
                        //Log.d("datasnap keyyyyyyy",dataSnapshot.getKey());
                        url = dataSnapshot.child("url").getValue().toString();
                        if ( dataSnapshot.child("treatedUrl").exists())
                            treatedUrl = dataSnapshot.child("treatedUrl").getValue().toString();
                        //double latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                        //double longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                        //faunaImage = (ImageView) findViewById(R.id.ShowImageView);
                        ViewTreeObserver vto = choosevollayout.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                choosevollayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                width  = choosevollayout.getMeasuredWidth();
                                //height = choosevollayout.getMeasuredHeight();
                                Log.d("width",width+"");
                                int height=width*3/4;
                                Log.d("height",height+"");
                                if (dataSnapshot.child("status").getValue().toString().equals("6") && dataSnapshot.child("treatedUrl").exists()) {
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width/2, height/2);
                                    faunaImage.setLayoutParams(param);
                                    Glide.with(ChooseVol.this)
                                            .load(url)
                                            .placeholder(R.drawable.faunasinneed_load)
                                            .into(faunaImage);
                                    faunaImage.setDrawingCacheEnabled(true);
                                    faunaImage.buildDrawingCache(true);

                                    faunaTreadted.setVisibility(View.VISIBLE);
                                    faunaTreadted.setLayoutParams(param);
                                    Glide.with(ChooseVol.this)
                                            .load(treatedUrl)
                                            .placeholder(R.drawable.faunasinneed_load)
                                            .into(faunaTreadted);
                                    faunaTreadted.setDrawingCacheEnabled(true);
                                    faunaTreadted.buildDrawingCache(true);
                                }else {
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                                    Log.d("frame width",LinearLayout.LayoutParams.MATCH_PARENT+"");
                                    faunaImage.setLayoutParams(param);
                                    Glide.with(ChooseVol.this)
                                            .load(url)
                                            .placeholder(R.drawable.faunasinneed_load)
                                            .into(faunaImage);
                                    faunaImage.setDrawingCacheEnabled(true);
                                    faunaImage.buildDrawingCache(true);
                                }


                            }
                        });
                        //int width = faunaImage.getWidth();
                        //faunaImage.setMinimumHeight(width*3/4);

                        final String uplodrId=dataSnapshot.child("uploaderId").getValue().toString();
                        final String volId=dataSnapshot.child("volId").getValue().toString();
                        dDatabase = fdb.getReference();
                        dDatabase.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                                String key = dataSnapshot2.getKey();
                                for (DataSnapshot child : dataSnapshot2.getChildren()) {
                                    if (child.getKey().equals(uplodrId)) {
                                        //String key = myParentNode;
                                        //String value = child.getValue().toString();
                                        //Log.d("main: ", "key: " + key);
                                        Log.d("finderrrrr", dataSnapshot2.child(uplodrId).child("name").getValue().toString());
                                        txtfinderName.setText(dataSnapshot2.child(uplodrId).child("name").getValue().toString());
                                        //showVolImg.setVisibility(View.VISIBLE);
                                    }
                                    if (child.getKey().equals(volId)) {
                                        //String key = myParentNode;
                                        //String value = child.getValue().toString();
                                        //Log.d("main: ", "key: " + key);
                                        Log.d("volllllname", dataSnapshot2.child(volId).child("name").getValue().toString());
                                        txtvolName.setText(dataSnapshot2.child(volId).child("name").getValue().toString());
                                        txtvolName.setVisibility(View.VISIBLE);
                                        showVolImg.setVisibility(View.VISIBLE);
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
                        String status=dataSnapshot.child("status").getValue().toString();

                        if (finalUserTyp.equals("vol") && status.equals("1")) {
                            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            chooseVol.setLayoutParams(param2);
                            chooseVol.setVisibility(View.VISIBLE);
                            chooseVol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String[] sts = {null};

                                    AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(ChooseVol.this);
                                    aboutDialogBuilder.setTitle("Confirmation").setMessage("You must reach selected Hospital within 1 hour!\nAre you sure?");
                                    aboutDialogBuilder.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {
                                                    final DatabaseReference checkRef= fdb.getReference("Fauna").child(faunaId).child("status");
                                                    checkRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Log.d("status inside", dataSnapshot.getValue().toString());
                                                            sts[0] = dataSnapshot.getValue().toString();
                                                            if (sts[0].equals("1")) {
                                                                checkRef.removeEventListener(this);
                                                                uDatabase = fdb.getReference();
                                                                uDatabase.child("Fauna").child(faunaId).child("status").setValue("2");
                                                                uDatabase.child("Fauna").child(faunaId).child("volId").setValue(mAuth.getCurrentUser().getUid());
                                                                Intent i = new Intent(ChooseVol.this, MapDemo.class);
                                                                i.putExtra("faunaKey", faunaId);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(i);
                                                                finish();
                                                            } else {
                                                                dialog.cancel();
                                                                Toast.makeText(ChooseVol.this, "A Volunteer has already opted!", Toast.LENGTH_LONG).show();
                                                                LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(0, 0);
                                                                chooseVol.setLayoutParams(param4);
                                                                chooseVol.setVisibility(View.INVISIBLE);
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {}
                                                    });
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
                            });
                        }else if (finalUserTyp.equals("vol") && (status.equals("2"))){
                            chooseVol.setText("Select nearest Vet");
                            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            chooseVol.setLayoutParams(param2);
                            chooseVol.setVisibility(View.VISIBLE);
                            chooseVol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String[] sts = {null};

                                    AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(ChooseVol.this);
                                    aboutDialogBuilder.setTitle("Confirmation").setMessage("You must reach selected Hospital within 1 hour!\nAre you sure?");
                                    aboutDialogBuilder.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {

                                                                uDatabase = fdb.getReference();
                                                                uDatabase.child("Fauna").child(faunaId).child("status").setValue("2");
                                                                uDatabase.child("Fauna").child(faunaId).child("volId").setValue(mAuth.getCurrentUser().getUid());
                                                                Intent i = new Intent(ChooseVol.this, MapDemo.class);
                                                                i.putExtra("faunaKey", faunaId);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(i);
                                                                finish();
                                                };
                                            });
                                    aboutDialogBuilder.setNegativeButton("Opt out",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DatabaseReference checkRef= fdb.getReference("Fauna");
                                                    checkRef.child(faunaId).child("status").setValue("1");
                                                    checkRef.child(faunaId).child("volId").setValue("");
                                                    dialog.cancel();
                                                    Intent i=new Intent(ChooseVol.this,ChooseVol.class);
                                                    i.putExtra("faunaKey",faunaId);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                    AlertDialog aboutDialog = aboutDialogBuilder.create();
                                    aboutDialog.show();
                                }
                            });
                        }
                        if (finalUserTyp.equals("vet") && dataSnapshot.child("vetId").getValue().toString().equals(userId)){
                            if (dataSnapshot.child("status").getValue().toString().equals("3")) {
                                if (dataSnapshot.child("vetId").getValue().toString().equals(userId)) {
                                    LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    BtnVolArrived.setLayoutParams(param3);
                                    BtnVolArrived.setVisibility(View.VISIBLE);
                                }
                            }else if (dataSnapshot.child("status").getValue().toString().equals("4")) {
                                if (dataSnapshot.child("vetId").getValue().toString().equals(userId)) {
                                    LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    BtnTreated.setLayoutParams(param3);
                                    BtnTreated.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if (dataSnapshot.child("status").getValue().toString().equals("5")) {
                            if (dataSnapshot.child("volId").getValue().toString().equals(userId)) {
                                LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                BtnTreated2.setLayoutParams(param3);
                                BtnTreated2.setVisibility(View.VISIBLE);
                                ViewTreeObserver vto2 = choosevollayout.getViewTreeObserver();
                                vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        choosevollayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                        width  = choosevollayout.getMeasuredWidth();
                                        //height = choosevollayout.getMeasuredHeight();
                                        Log.d("width",width+"");
                                        int height=width*3/4;
                                        Log.d("height",height+"");
                                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width/2, height/2);
                                        faunaImage.setLayoutParams(param);
                                        Glide.with(ChooseVol.this)
                                                .load(url)
                                                .placeholder(R.drawable.faunasinneed_load)
                                                .into(faunaImage);
                                        faunaImage.setDrawingCacheEnabled(true);
                                        faunaImage.buildDrawingCache(true);

                                        faunaTreadted.setVisibility(View.VISIBLE);
                                        faunaTreadted.setLayoutParams(param);
                                        Glide.with(ChooseVol.this)
                                                .load(treatedUrl)
                                                .placeholder(R.drawable.faunasinneed_load)
                                                .into(faunaTreadted);
                                        faunaTreadted.setDrawingCacheEnabled(true);
                                        faunaTreadted.buildDrawingCache(true);



                                    }
                                });
                            }
                        }
                        //UploadFaunaDetails.LocationAddress locationAddress = new UploadFaunaDetails.LocationAddress();
                        //locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());
                        txtDate.setText(dataSnapshot.child("date").getValue().toString());
                        txtTime.setText(dataSnapshot.child("time").getValue().toString());
                        desc.setText((dataSnapshot.child("type").getValue().toString() + " : " + dataSnapshot.child("description").getValue().toString()).trim());
                        loc = dataSnapshot.child("location").getValue().toString().trim();
                        txtlocation.setText(loc);
                        if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                            statusIcon.setImageResource(R.drawable.required);
                            txtStatus.setText("This Fauna is in need of a Volunteer");
                        } else if (dataSnapshot.child("status").getValue().toString().equals("2")) {
                            statusIcon.setImageResource(R.drawable.got_vol);
                            txtStatus.setText("This Fauna has got Volunteer.");
                        } else if (dataSnapshot.child("status").getValue().toString().equals("3")) {
                            statusIcon.setImageResource(R.drawable.selectedvol2);
                            txtStatus.setText("This fauna is being carried to Hospital");
                        } else if (dataSnapshot.child("status").getValue().toString().equals("4") || dataSnapshot.child("status").getValue().toString().equals("5")) {
                            statusIcon.setImageResource(R.drawable.treating);
                            txtStatus.setText("This fauna is under treatment by Vet");
                        } else if (dataSnapshot.child("status").getValue().toString().equals("6")) {
                            statusIcon.setImageResource(R.drawable.treated);
                            txtStatus.setText("This Fauna has been treated!");
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
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            noCommentstxt.setLayoutParams(param);
            noCommentstxt.setVisibility(View.VISIBLE);
            cDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.child("faunaId").getValue().toString().equals(faunaId)) {
                        final String commenter = dataSnapshot.child("userId").getValue().toString();
                        nDatabase = fdb.getReference();
                        nDatabase.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                                String key = dataSnapshot2.getKey();
                                for (DataSnapshot child : dataSnapshot2.getChildren()) {
                                    if (child.getKey().equals(commenter)) {
                                        //String key = myParentNode;
                                        //String value = child.getValue().toString();
                                        Log.d("main: ", "key: " + key);
                                        Log.d("sjdosdjnameeee", dataSnapshot2.child(commenter).child("name").getValue().toString());

                                        privname = dataSnapshot2.child(commenter).child("name").getValue().toString();
                                        String sourceString = "<b>" + privname + "</b> " + dataSnapshot.child("comment").getValue().toString();
                                        arrayList.add(0, sourceString);
                                        commenterImageList.add(0, key);
                                    }
                                }
                                adapter = new CommentAdapter(getBaseContext(), arrayList, commenterImageList, true);
                                //if (adapter.getCount() > 0) {
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, 0);
                                noCommentstxt.setLayoutParams(param);
                                noCommentstxt.setVisibility(View.INVISIBLE);
                                LinearLayout.LayoutParams param2 = null;
                                if (adapter.getCount() < 3) {
                                    param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, adapter.getCount() * 100);
                                } else {
                                    param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250);
                                }
                                listView.setLayoutParams(param2);
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(adapter);
                                //}
                                 /*else {
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                    noCommentstxt.setLayoutParams(param);
                                    noCommentstxt.setVisibility(View.VISIBLE);
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
                        /*if (adapter==null){
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                            noCommentstxt.setLayoutParams(param);
                            noCommentstxt.setVisibility(View.VISIBLE);
                        }*/
                    }
                    //Log.d("Arraysizeee",""+arrayList.size());
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
        /*mDatabase.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot ds=dataSnapshot.getChildren().iterator().next();
                //Log.d("key======",ds.getKey());
                //faunaId=ds.getKey();
                Log.d("count======", String.valueOf(ds.getChildrenCount()));
                Log.d("url===",ds.child("url").getValue().toString());
                Log.d("url===",ds.child("name").getValue().toString());
                url=ds.child("url").getValue().toString();
                double latitude= Double.parseDouble(ds.child("latitude").getValue().toString());
                double longitude= Double.parseDouble(ds.child("longitude").getValue().toString());
                Glide.with(getBaseContext())
                        .load(url)
                        .into(faunaImage);
                UploadFaunaDetails.LocationAddress locationAddress = new UploadFaunaDetails.LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());
                loc=ds.child("location").getValue().toString();
                desc.setText("Type: " + ds.child("type").getValue().toString() + "\nDecription: " +ds.child("name").getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
       /* url="https://firebasestorage.googleapis.com/v0/b/fauna-care.appspot.com/o/uploads%2F1519149427391.jpeg?alt=media&token=22ad5d84-99d2-4301-9a2b-0e8ab0e3ee7c";
        Log.d("urlnewwwww=======",url);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Glide.with(this)
                .load(url)
                .into(faunaImage);
        progressDialog.dismiss();*/
            /*txtTip.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.d("tipll","???");
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        // release
                        Log.d("tipll","relsed");
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0,35);
                        addTip.setLayoutParams(param2);
                        return false;
                    } else if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // pressed
                        Log.d("tipll","pressed");
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,35);
                        addTip.setLayoutParams(param2);
                        return true;
                    }
                    return false;
                }
            });*/
            /*txtTip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        Log.d("tipll","relsed");
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0,35);
                        addTip.setLayoutParams(param2);
                    }else{
                        Log.d("tipll","pressed");
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,35);
                        addTip.setLayoutParams(param2);
                    }
                }
            });*/
            addTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtTip.getText().toString().trim().length() > 0) {
                        String userId = mAuth.getCurrentUser().getUid();
                        String uploadId = cDatabase.push().getKey();
                        Comment objComment = new Comment(txtTip.getText().toString().trim(), faunaId, userId);
                        txtTip.setText("");
                        cDatabase.child(uploadId).setValue(objComment);
                    }
                }
            });
            faunaLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(ChooseVol.this,FaunaLocation.class);
                    i.putExtra("faunaKey",faunaId);
                    startActivity(i);
                }
            });
        /*BtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uDatabase = fdb.getReference();
                uDatabase.child("Fauna").child(faunaId).child("isReport").setValue("1");
                Toast.makeText(ChooseVol.this, "Reported Picture", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ChooseVol.this, Home.class);
            }
        });*/
            /*BtnUnreport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uDatabase = fdb.getReference();
                    uDatabase.child("Fauna").child(faunaId).child("isReport").setValue("0");
                    Toast.makeText(ChooseVol.this, "Reported Picture", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ChooseVol.this, Admin.class);
                }
            });*/
            BtnBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uDatabase = fdb.getReference();
                    uDatabase.child("Fauna").child(faunaId).removeValue();
                    Toast.makeText(ChooseVol.this, "Blocked Picture", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ChooseVol.this, Admin.class);
                }
            });
            BtnTreated.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(ChooseVol.this);
                    aboutDialogBuilder.setMessage("You need to upload treated Fauna Picture!");
                    aboutDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uDatabase = fdb.getReference();
                                    uDatabase.child("Fauna").child(faunaId).child("status").setValue("5");
                                    //Toast.makeText(ChooseVol.this, "Treated Fauna", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ChooseVol.this, UploadTreatedPic.class);
                                    i.putExtra("faunaKey", faunaId);
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
            });
            BtnTreated2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(ChooseVol.this);
                    aboutDialogBuilder.setMessage("Confirmation");
                    aboutDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uDatabase = fdb.getReference();
                                    uDatabase.child("Fauna").child(faunaId).child("status").setValue("6");
                                    //Toast.makeText(ChooseVol.this, "Treated Fauna", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ChooseVol.this, ChooseVol.class);
                                    i.putExtra("faunaKey", faunaId);
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
            });
            BtnVolArrived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(ChooseVol.this);
                    aboutDialogBuilder.setTitle("Confirmation");
                    aboutDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uDatabase = fdb.getReference();
                                    uDatabase.child("Fauna").child(faunaId).child("status").setValue("4");
                                    Toast.makeText(ChooseVol.this, "Treated Fauna", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ChooseVol.this, ChooseVol.class);
                                    i.putExtra("faunaKey", faunaId);
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
            });
            //storageReference = FirebaseStorage.getInstance().getReference();
            //StorageReference sRef = storageReference.child(url);
        /*Query query=mDatabase.child(DATABASE_PATH_UPLOADS).orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childs:dataSnapshot.getChildren())
                {
                    Log.d("key=============",childs.getKey().toString());
                    //Log.d("value=",childs.child("name").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
        /*try {
            URL ur=new URL(url);
            HttpURLConnection con= (HttpURLConnection) ur.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream in =con.getInputStream();
            Bitmap bmp=BitmapFactory.decodeStream(in);
            faunaImage.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
            //Picasso.with(getApplicationContext()).load(url).into(faunaImage);
       /* try {
            URL ur=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        /*Bitmap bmp= null;
        try {
            bmp = BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {
            //Log.d("errr",e.printStackTrace());
        }
        faunaImage.setImageBitmap(bmp);*/
            //faunaImage.setImageBitmap(decode(getResources(),R.id.ShowImageView,100,100));
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            txtlocation.setText("Location: " + loc);
        }
    }

    /*public static Bitmap decode(Resources res, int resId, int w, int h) {
        final BitmapFactory.Options option =new BitmapFactory.Options();
        option.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,option);
        option.inSampleSize=cal(option,w,h);
        option.inJustDecodeBounds=false;
        return  BitmapFactory.decodeResource(res,resId,option);
    }
    private static int cal(BitmapFactory.Options option, int w, int h) {
        final int ht =option.outHeight;
        final int wt=option.outWidth;
        int ss=1;
        if(ht>h || wt>w)
        {
            final int hfh=ht/2;
            final int hfw=wt/2;
            while((hfh/ss)>=h && (hfw/ss)>=w){
                ss*=2;
            }
        }
        return ss;
    }*/
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("newwwwIntent", "in");
        Bundle nb = intent.getExtras();
        String faunaKey = nb.getString("faunaKey");
        //Log.wtf("faunakey=====Choosevol===", faunaKey);
    }

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
        if (id == R.id.btnReport) {
            uDatabase = fdb.getReference();
            uDatabase.child("Fauna").child(faunaId).child("isReport").setValue("1");
            Toast.makeText(ChooseVol.this, "Reported Picture", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ChooseVol.this, Home.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}