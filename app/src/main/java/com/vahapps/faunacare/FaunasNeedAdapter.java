package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 14-Apr-18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FaunasNeedAdapter extends RecyclerView.Adapter<FaunasNeedAdapter.ViewHolder2> {
    private Context context;
    private List<Fauna> uploads;
    private String fkey;

    public FaunasNeedAdapter(Context context, List<Fauna> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fauna_need_layout, parent, false);
        ViewHolder2 viewHolder = new ViewHolder2(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder2 holder, int position) {
        final Fauna upload = uploads.get(position);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Fauna");

        fkey = null;
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("url").getValue().toString().equals(upload.getUrl())) {
                    fkey = dataSnapshot.getKey();
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
        holder.txtfaunaneed.setText(upload.getType() + "in need at " + upload.getLocation().trim());
        holder.txtDate.setText(upload.getDate());
        holder.txtTime.setText(upload.getTime());
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtfaunaneed, dummyTxt,txtDate,txtTime;

        public ViewHolder2(View itemView) {
            super(itemView);
            txtfaunaneed = itemView.findViewById(R.id.txtfaunaneed);
            txtDate = itemView.findViewById(R.id.txtdate);
            txtTime = itemView.findViewById(R.id.txttime);
            dummyTxt = itemView.findViewById(R.id.dummytxtf);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e("item title", dummyTxt.getText().toString());
            Intent i = new Intent(FaunasNeedAdapter.this.context, ChooseVol.class);
            i.putExtra("faunaKey", dummyTxt.getText().toString());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            FaunasNeedAdapter.this.context.startActivity(i);
        }
    }
}
