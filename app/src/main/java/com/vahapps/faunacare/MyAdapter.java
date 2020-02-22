package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 07-Feb-18.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<Fauna> uploads;
    private String fkey;
    private int parameter_width;

    public MyAdapter(Context context, List<Fauna> uploads) {
        this.uploads = uploads;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagelayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Fauna upload = uploads.get(position);
        //int width1=holder.ll.getWidth();
        //holder.ll.setMinimumHeight(width1*3/4);
        //FrameLayout.LayoutParams paramL = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width1*3/4);
        //holder.ll.setLayoutParams(paramL);
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Fauna");

        ///String finalKey;
        fkey=null;
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.d("datakeyyyyyy", dataSnapshot.getKey());
                if(dataSnapshot.child("url").getValue().toString().equals(upload.getUrl()))
                {
                    //Log.d("childddssurlll",dataSnapshot.child("url").getValue().toString());
                    //String k =dataSnapshot.getKey();
                    //Log.d("key00000000000001111",fkey);
                    final String uplodrId=dataSnapshot.child("uploaderId").getValue().toString();
                    final String volId=dataSnapshot.child("volId").getValue().toString();
                    holder.dDatabase = FirebaseDatabase.getInstance().getReference();
                    holder.dDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                            String key = dataSnapshot2.getKey();
                            for (DataSnapshot child : dataSnapshot2.getChildren()) {
                                if (child.getKey().equals(uplodrId)) {
                                    //String key = myParentNode;
                                    //String value = child.getValue().toString();
                                    //Log.d("main: ", "key: " + key);
                                    Log.d("finderrrrr", dataSnapshot2.child(uplodrId).child("name").getValue().toString());
                                    holder.txtfinderName.setText(dataSnapshot2.child(uplodrId).child("name").getValue().toString());
                                    //showVolImg.setVisibility(View.VISIBLE);
                                }
                                if (child.getKey().equals(volId)) {
                                    //String key = myParentNode;
                                    //String value = child.getValue().toString();
                                    //Log.d("main: ", "key: " + key);
                                    Log.d("volllllname", dataSnapshot2.child(volId).child("name").getValue().toString());
                                    holder.txtvolName.setText(dataSnapshot2.child(volId).child("name").getValue().toString());
                                    holder.txtvolName.setVisibility(View.VISIBLE);
                                    holder.showVolImg.setVisibility(View.VISIBLE);
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
                    fkey=dataSnapshot.getKey();
                    holder.dummyTxt.setText(fkey);
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
        /*holder.txtDesc.setText(upload.getDescription());
        holder.txtType.setText(upload.getType());
        holder.txtLoc.setText(upload.getLocation());*/
        //holder.txtDetails.setText((upload.getType()+" found at "+upload.getLocation()).trim());
        ViewTreeObserver vto = holder.ll.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  =holder.ll.getMeasuredWidth();
                //height = choosevollayout.getMeasuredHeight();
                Log.d("adapterwidth",width+"");
                int height=width*3/4;
                Log.d("adapterheight",height+"");
                if (upload.getStatus().equals("6")) {
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width/2, height/2);
                    holder.imageView.setLayoutParams(param);
                    Glide.with(context)
                            .load(upload.getUrl())
                            .placeholder(R.drawable.faunasinneed_load)
                            .into(holder.imageView);
                    //faunaImage.setDrawingCacheEnabled(true);
                    //faunaImage.buildDrawingCache(true);

                    holder.faunaTreadted.setVisibility(View.VISIBLE);
                    holder.faunaTreadted.setLayoutParams(param);
                    Glide.with(context)
                            .load(upload.getTreatedUrl())
                            .placeholder(R.drawable.faunasinneed_load)
                            .into(holder.faunaTreadted);
                    //faunaTreadted.setDrawingCacheEnabled(true);
                    //faunaTreadted.buildDrawingCache(true);
                }else {
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                    holder.imageView.setLayoutParams(param);
                    holder.imageView.setImageResource(R.drawable.faunasinneed_load);
                    if(upload.getUrl().contains("http")){
                        Glide.with(context).load(upload.getUrl())
                                .placeholder(R.drawable.faunasinneed_load)
                                .into(holder.imageView);
                    }else {
                        byte[] decodedByteArray = android.util.Base64.decode(upload.getUrl(), Base64.DEFAULT);
                        Bitmap imageBitmap= BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                        holder.imageView.setImageBitmap(imageBitmap);
                    }
                }


            }
        });
        /*Log.d("adapterwidth",parameter_width+"");
        int height=parameter_width*3/4;
        Log.d("adapterheight",height+"");
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        Log.d("frame width",FrameLayout.LayoutParams.MATCH_PARENT+"");
        holder.imageView.setImageResource(R.drawable.faunasinneed_load);
        if(upload.getUrl().contains("http")){
            Glide.with(context).load(upload.getUrl())
                    .placeholder(R.drawable.faunasinneed_load)
                    .into(holder.imageView);
        }else {
            byte[] decodedByteArray = android.util.Base64.decode(upload.getUrl(), Base64.DEFAULT);
            Bitmap imageBitmap= BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            holder.imageView.setImageBitmap(imageBitmap);
        }*/
        holder.txtDetails.setText(upload.getLocation().trim());
        if (upload.getStatus().equals("1")){
            holder.statusIcon.setImageResource(R.drawable.required);
            holder.txtStatus.setText("This Fauna is in need of a Volunteer");
        }else if (upload.getStatus().equals("2")){
            holder.statusIcon.setImageResource(R.drawable.got_vol);
            holder.txtStatus.setText("This Fauna has got Volunteer.");
        }else if (upload.getStatus().equals("3")){
            holder.statusIcon.setImageResource(R.drawable.selectedvol2);
            holder.txtStatus.setText("This fauna is being carried to Hospital");
        }else if (upload.getStatus().equals("4") || upload.getStatus().equals("5")){
            holder.statusIcon.setImageResource(R.drawable.treating);
            holder.txtStatus.setText("This fauna is under treatment by Vet");
        }else if (upload.getStatus().equals("6")){
            holder.statusIcon.setImageResource(R.drawable.treated);
            holder.txtStatus.setText("This Fauna has been treated!");
        }
        //holder.dummyTxt.setText("-L6BRyXL9iHbE1OWUOng");
        //Glide.with(context).load(upload.getUrl()).into(holder.imageView);



        //int width=holder.imageView.getWidth();
        //holder.imageView.setMinimumHeight(width*3/4);

        //holder.imageView.setImageResource(R.drawable.faunasinneed_load);
        //holder.imageView.setImageDrawable(R.drawable.faunasinneed);
        /*if(upload.getUrl().contains("http")){
            Glide.with(context).load(upload.getUrl())
                    .placeholder(R.drawable.faunasinneed_load)
                    .into(holder.imageView);
        }else {
            byte[] decodedByteArray = android.util.Base64.decode(upload.getUrl(), Base64.DEFAULT);
            Bitmap imageBitmap= BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            holder.imageView.setImageBitmap(imageBitmap);
        }*/
        //FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width*3/4);
        //holder.imageView.setLayoutParams(param);
        holder.imageView.setDrawingCacheEnabled(true);
        holder.imageView.buildDrawingCache(true);
        holder.txtDate.setText(upload.getDate());
        holder.txtTime.setText(upload.getTime());
       // holder.imageView.setBackgroundResource(R.drawable.border_image);

    }
    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDesc,txtType,txtLoc,txtStatus,txtDetails,txtfinderName,txtvolName;
        private ImageView imageView,statusIcon,showVolImg,faunaTreadted;
        private TextView dummyTxt,txtDate,txtTime;
        private DatabaseReference dDatabase;
        private LinearLayout ll;
        //private LinearLayout ll;
        public ViewHolder(View itemView) {
            super(itemView);
            /*txtDesc =  itemView.findViewById(R.id.textViewDesc);
            txtType=itemView.findViewById(R.id.textViewType);
            txtLoc=itemView.findViewById(R.id.textViewLocation);*/
            txtDetails=itemView.findViewById(R.id.textViewDetails);
            txtStatus=itemView.findViewById(R.id.textViewStatus);
            txtDate = itemView.findViewById(R.id.txtdate);
            txtTime = itemView.findViewById(R.id.txttime);
            showVolImg = itemView.findViewById(R.id.homevolshowimg);
            txtvolName = itemView.findViewById(R.id.homevolName);
            txtfinderName = itemView.findViewById(R.id.homefinderName);
            imageView =  itemView.findViewById(R.id.imageView);
            faunaTreadted = itemView.findViewById(R.id.ShowImageView2);
            statusIcon = itemView.findViewById(R.id.statusIcon2);
            dummyTxt=itemView.findViewById(R.id.dummytxt);
            //ll= itemView.findViewById(R.id.liniearlayouthome);
            ll=itemView.findViewById(R.id.myadapterll);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Log.e("item title",  dummyTxt.getText().toString());
            Intent i=new Intent(MyAdapter.this.context,ChooseVol.class);
            i.putExtra("faunaKey",dummyTxt.getText().toString());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyAdapter.this.context.startActivity(i);
        }
    }
}