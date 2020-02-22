package com.vahapps.faunacare;


/**
 * Created by Vaibhavi on 08-Mar-18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by admin on 3/7/2018.
 */
public  class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder>{

    private Context context;
    private List<Fauna> uploads;
    private String fkey;
    private Button Block;

    public AdminAdapter(Context context, List<Fauna> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagelayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Fauna upload = uploads.get(position);
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Fauna");
        ///String finalKey;
        fkey=null;
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.child("url").getValue().toString().equals(upload.getUrl())) {

                    fkey = dataSnapshot.getKey();
                    Log.i("Admin_Key-->",fkey);
                    holder.dummyTxt.setText(fkey);
                    // }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("CCcheck report ------> ",dataSnapshot.child("isReport").getValue().toString());
                // if((dataSnapshot.child("isReport").getValue().toString().equals("1")) && (dataSnapshot.child("url").getValue().toString().equals(upload.getUrl()))) {
                Log.i("inCC checkReport-> ",dataSnapshot.child("isReport").getValue().toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {            }

        });
        holder.textViewName.setText(upload.getDescription());
        //holder.dummyTxt.setText("-L6BRyXL9iHbE1OWUOng");
        Glide.with(context).load(upload.getUrl()).into(holder.imageView);

    }




    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public TextView dummyTxt;




        public ViewHolder(View itemView) {
            super(itemView);
            textViewName =  itemView.findViewById(R.id.textViewDesc);
            imageView =  itemView.findViewById(R.id.imageView);
            dummyTxt=itemView.findViewById(R.id.dummytxt);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.e("item title",  dummyTxt.getText().toString());
            Intent i=new Intent(AdminAdapter.this.context,ChooseVol.class);
            i.putExtra("faunaKey",dummyTxt.getText().toString());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AdminAdapter.this.context.startActivity(i);
        }
    }
}
