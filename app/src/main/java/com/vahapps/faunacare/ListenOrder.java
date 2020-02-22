package com.vahapps.faunacare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.vahapps.faunacare.ChooseVol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class ListenOrder extends Service {
    DatabaseReference orders,faunaVetRef;
    Query messages;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myref,userTyperef;
    private int flag=0,flagstart=0;
    private static int startcount=0;
    private String faunaId;
    private String CountFile;
    private File path;
    private int countfileflag=0;
    private String userId;
    private int incrementingCount;

    //String faunaKey;
    //int flag=0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Listenooooooooorder","in listenorder oncreate");
        //countfileflag=0;
        /*path = new File(Environment.getExternalStorageDirectory() +"/Android/data/com.vahapps.faunacare");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!path.exists()) {
                    //countfileflag=1;
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() +"/Android/data/com.vahapps.faunacare/");
                    wallpaperDirectory.mkdirs();
                    Log.d("no file to write","yes");
                    writeToFile(0);
                }
            }
        }, 0);

        //flagstart=0;
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        fdb = FirebaseDatabase.getInstance();
        incrementingCount=0;
        orders=db.getReference("Fauna");
        //messages = orders.orderByKey().limitToLast(1);
        flag=0;
        orders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                incrementingCount++;
                if (incrementingCount>readFromFile()){
                    Log.d("incrementinCount",incrementingCount+"");
                    writeToFile(incrementingCount);
                    myref = fdb.getReference();
                    flag=0;
                    myref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                            Log.d("child added------",dataSnapshot2.getKey());
                            String key = dataSnapshot2.getKey();
                            for (DataSnapshot child : dataSnapshot2.getChildren()) {
                                flag=0;
                                if (child.getKey().equals(userId)) {
                                    if (key.equals("Volunteer")){
                                        Log.d("Volcheck","key=vol");
                                        if(!child.getKey().equals(dataSnapshot.child("uploaderId").getValue().toString()) && dataSnapshot.child("status").getValue().toString().equals("1")){
                                            Log.d("flaaag","setting flag=1");
                                            flag=1;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(flag==1) {
                                showNotification(dataSnapshot.child("type").getValue().toString()+" in need at "+dataSnapshot.child("location").getValue().toString());
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
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("status").getValue().toString().equals("3"))
                {
                    if(dataSnapshot.child("vetId").getValue().toString().equals(userId)){
                        faunaId=dataSnapshot.getKey();
                        Log.d("listen fauna key::::",faunaId);
                        *//*faunaVetRef=db.getReference();
                        faunaVetRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.getKey().equals(dataSnapshot.child("volId").getValue().toString())) {
                                        volname = child.child("name").getValue().toString();
                                        volkey = dataSnapshot.getKey();
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
                        });*//*
                        showNotification("A volunteer is coming to your hospital...");
                    }
                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
        /*messages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(startcount!=0){
                    Log.i("Messages========", dataSnapshot.toString());
                    final DataSnapshot ds=dataSnapshot.getChildren().iterator().next();
                    faunaId=ds.getKey();
                    Log.d("FaunaId-----",faunaId);
                    //faunaKey=dataSnapshot.getKey();
                    *//*myref = fdb.getReference();
                    flag=0;
                    myref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("child added------",dataSnapshot.getKey());
                            String key = dataSnapshot.getKey();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                flag=0;
                                if (child.getKey().equals(userId)) {
                                    if (key.equals("Volunteer")){
                                        Log.d("Volcheck","key=vol");
                                        if(!child.getKey().equals(ds.child("uploaderId").getValue().toString()) && ds.child("status").getValue().toString().equals("1")){
                                            Log.d("flaaag","setting flag=1");
                                            flag=1;
                                            break;
                                        }
                                    }
                                        *//**//*else {
                                    Log.d("Volcheck","key=other");
                                    flag=0;
                                    break;
                                }*//**//*
                                    }
                                    *//**//*if (key.equals("Finder")) {
                                        abc=child.child("email").getValue().toString();
                                        flag=1;
                                        break;
                                    } else if (key.equals("Volunteer")) {
                                        abc=child.child("email").getValue().toString();
                                        if(child.getKey().equals(ds.child("uploaderId").getValue().toString()) || child.getKey().equals(userId)){
                                            flag=1;
                                            break;
                                        }
                                    } else if (key.equals("Vet")) {
                                        abc=child.child("email").getValue().toString();
                                        flag=1;
                                        break;
                                    } else if (key.equals("Fauna")){
                                        flag=1;
                                        break;
                                    } else if (key.equals("uploads")) {
                                        flag = 1;
                                        break;
                                    } else {
                                        abc="extra: "+child.child("email").getValue().toString();
                                    }*//**//*
                            }
                            if(flag==1)
                            {
                                showNotification(ds.child("type").getValue().toString()+" in need at "+ds.child("location").getValue().toString());
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
                    });*//*
                    *//*myref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Log.d("VolKey::::",child.getKey());
                                if((userId.equals(child.getKey()) && child.getKey().equals(ds.child("uploaderId").getValue().toString())) || (userId.equals("vet") || userId.equals("fin"))){
                                    Log.d("match key:::","yesss");
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==0)
                            {showNotification("Fauna in need... Please help!");}
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });*//*
                }
                startcount++;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
        //flagstart=0;
    }

    private int readFromFile() {

        CountFile = userId+".txt";
        File file = new File(path, CountFile);
        if (!file.exists()) {
            writeToFile(0);
        }
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String contents = new String(bytes);
        Log.d("readed data",contents+".");
        return Integer.parseInt(contents);
    }

    private void writeToFile(int countinfile) {
        CountFile = userId+".txt";
        File file = new File(path, CountFile);
        ////file write
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(String.valueOf(countinfile).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Log.d("Listenooooooooorder","in listenorder onstartcommand");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            CountFile = userId+".txt";
            File file = new File(path, CountFile);
            path = new File(Environment.getExternalStorageDirectory() +"/faunacare");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!path.exists()) {
                        //countfileflag=1;
                        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() +"/faunacare/");
                        wallpaperDirectory.mkdirs();
                        Log.d("no file to write","yes");
                        writeToFile(0);
                    }
                }
            }, 0);

            //flagstart=0;
            fdb = FirebaseDatabase.getInstance();
            userId = user.getUid();
            final UserType objUser = (UserType) getApplicationContext();
            userTyperef = fdb.getReference();
            userTyperef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key = dataSnapshot.getKey();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(userId)) {
                            Log.d("listenmain: ", "key: " + key);
                            if (key.equals("Admin")) {
                                objUser.setUserType("admin");
                            } else if (key.equals("Finder")) {
                                objUser.setUserType("finder");
                            } else if (key.equals("Volunteer")) {
                                objUser.setUserType("vol");
                            } else if (key.equals("Vet")) {
                                objUser.setUserType("vet");
                            }
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
            incrementingCount=0;
            orders=fdb.getReference("Fauna");
            //messages = orders.orderByKey().limitToLast(1);
            flag=0;
            orders.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    incrementingCount++;
                    if (incrementingCount>readFromFile()){
                        Log.d("incrementinCount",incrementingCount+"");
                        writeToFile(incrementingCount);
                        myref = fdb.getReference();
                        flag=0;
                        faunaId=dataSnapshot.getKey();
                        myref.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                                Log.d("child added------",dataSnapshot2.getKey());
                                String key = dataSnapshot2.getKey();
                                for (DataSnapshot child : dataSnapshot2.getChildren()) {
                                    flag=0;
                                    if (child.getKey().equals(userId)) {
                                        if (key.equals("Volunteer")){
                                            Log.d("Volcheck","key=vol");
                                            if(!child.getKey().equals(dataSnapshot.child("uploaderId").getValue().toString()) && dataSnapshot.child("status").getValue().toString().equals("1")){
                                                Log.d("flaaag","setting flag=1");
                                                flag=1;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(flag==1) {
                                    showNotification(dataSnapshot.child("type").getValue().toString()+" in need at "+dataSnapshot.child("location").getValue().toString());
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
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.child("status").getValue().toString().equals("3"))
                    {
                        if(dataSnapshot.child("vetId").getValue().toString().equals(userId)){
                            faunaId=dataSnapshot.getKey();
                            Log.d("listen fauna key::::",faunaId);
                        /*faunaVetRef=db.getReference();
                        faunaVetRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.getKey().equals(dataSnapshot.child("volId").getValue().toString())) {
                                        volname = child.child("name").getValue().toString();
                                        volkey = dataSnapshot.getKey();
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
                        });*/
                            showNotification("A volunteer is coming to your hospital...");
                        }
                    } else if(dataSnapshot.child("status").getValue().toString().equals("5")){
                        if(dataSnapshot.child("volId").getValue().toString().equals(userId)){
                            faunaId=dataSnapshot.getKey();
                            //Log.d("listen fauna key::::",faunaId);
                            showNotification("Your Approval Pending...");
                        }
                    } else if(dataSnapshot.child("status").getValue().toString().equals("6")){
                        if(dataSnapshot.child("uploaderId").getValue().toString().equals(userId)){
                            faunaId=dataSnapshot.getKey();
                            //Log.d("listen fauna key::::",faunaId);
                            showNotification("Your Fauna has been treated!");
                        }
                    }
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        /*countfileflag=0;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();
        db = FirebaseDatabase.getInstance();
        faunaVetRef=db.getReference("Fauna");
        faunaVetRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("status").getValue().toString().equals("3"))
                {
                    if(dataSnapshot.child("vetId").getValue().toString().equals(userId)){
                        faunaId=dataSnapshot.getKey();
                        Log.d("listen fauna key::::",faunaId);
                        showNotification("A vol is coming");
                    }
                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
        //if(intent.getExtras()!=null)
        //{
        //faunaKey=intent.getStringExtra("faunaKey");
        //Log.wtf("key=====ListenOrder===",faunaKey);
        //}
       /* if(flag==0){
            flag=1;
        }*/
        //orders.addChildEventListener(this);
        //if(flagstart==0) {
        /*if(startcount==0) {
            Log.d("startcoooount","sc=0");
            //Log.d("flagstart","fs=0");
            //messages.removeEventListener(this);
            //flagstart=1;
            startcount++;
        }*/
        //else{
        //Log.d("flagstart","fs=1");
        //Log.d("startcoooount", String.valueOf(startcount));
        /*flag=0;
        messages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Messages========", dataSnapshot.toString());
                final DataSnapshot ds=dataSnapshot.getChildren().iterator().next();
                //showNotification(dataSnapshot.getKey());
                //faunaKey=dataSnapshot.getKey();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userId = user.getUploaderId();
                Log.d("userid::::::",userId);
                fdb = FirebaseDatabase.getInstance();

                myref = fdb.getReference();
                flag=0;
                myref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("child added------",dataSnapshot.getKey());
                        String abc=null;
                        String key = dataSnapshot.getKey();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            if (child.getKey().equals(userId)) {
                                //String key = myParentNode;
                                //String value = child.getValue().toString();
                                Log.d("main: ", "key: ++++++" + key);
                                if (key.equals("Volunteer")){
                                    Log.d("Volcheck","key=vol");
                                    if(!child.getKey().equals(ds.child("uploaderId").getValue().toString())){
                                        abc=child.child("email").getValue().toString();
                                        flag=1;
                                    }
                                } else {
                                    Log.d("Volcheck","key=other");
                                    flag=0;
                                }
                                    *//*if (key.equals("Finder")) {
                                        abc=child.child("email").getValue().toString();
                                        flag=1;
                                        break;
                                    } else if (key.equals("Volunteer")) {
                                        abc=child.child("email").getValue().toString();
                                        if(child.getKey().equals(ds.child("uploaderId").getValue().toString()) || child.getKey().equals(userId)){
                                            flag=1;
                                            break;
                                        }
                                    } else if (key.equals("Vet")) {
                                        abc=child.child("email").getValue().toString();
                                        flag=1;
                                        break;
                                    } else if (key.equals("Fauna")){
                                        flag=1;
                                        break;
                                    } else if (key.equals("uploads")) {
                                        flag = 1;
                                        break;
                                    } else {
                                        abc="extra: "+child.child("email").getValue().toString();
                                    }*//*
                            }
                        }
                        if(flag==1)
                        {showNotification(abc + " :Fauna in need");}
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
                    *//*myref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Log.d("VolKey::::",child.getKey());
                                if((userId.equals(child.getKey()) && child.getKey().equals(ds.child("uploaderId").getValue().toString())) || (userId.equals("vet") || userId.equals("fin"))){
                                    Log.d("match key:::","yesss");
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==0)
                            {showNotification("Fauna in need... Please help!");}
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });*//*
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
        //}
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    //public ListenOrder() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Listen Ooooooooooder","destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showNotification(String msg) {
        Intent intent=new Intent(getBaseContext(), ChooseVol.class);
        intent.putExtra("faunaKey",faunaId);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent i=PendingIntent.getActivity(getBaseContext(),uniqueInt,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Fauna Care")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setContentTitle("Fauna Care")
                .setContentText(msg)
                .setContentIntent(i)
                .setAutoCancel(true)
                .build();
        int random=new Random().nextInt(9999-1)+1;
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(random, notification);
        /*NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());

        //Bundle nb=new Bundle();
        //nb.putString("faunaKey",faunaId);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Fauna Care")
                .setContentTitle("Fauna Care")
                .setContentInfo("new")
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(i);*/
        //NotificationManager manager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //int random=new Random().nextInt(9999-1)+1;
        //manager.notify(random,builder.build());
    }
}