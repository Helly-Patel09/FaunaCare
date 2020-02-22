package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 13-Apr-18.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AllVetAdapter extends RecyclerView.Adapter<AllVetAdapter.ViewHolder3> {
    private Context context;
    private ArrayList<Vet> arrayList;
    private ArrayList<String> vetKeys;
    private LayoutInflater inflater;
    private ArrayList<Integer> rateList;
    private boolean isListView;
    private int selectedPosition = -1;

    public AllVetAdapter(Context context, ArrayList<Vet> arrayList, ArrayList<String> vetKeys, ArrayList<Integer> rateList ,boolean isListView) {
        Log.d("in","constructor");
        this.context = context;
        this.arrayList = arrayList;
        this.vetKeys = vetKeys;
        this.isListView = isListView;
        this.rateList =rateList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.allvet_layout, parent, false);
        ViewHolder3 viewHolder = new ViewHolder3(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder3 viewHolder, int i) {
        Vet vetobj=arrayList.get(i);
        viewHolder.label.setText(vetobj.getName());
        viewHolder.add.setText(vetobj.getHospitaladd());
        viewHolder.vetdummytxt.setText(vetKeys.get(i));
        viewHolder.ratingBar.setRating(rateList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView label,add;
        private TextView vetdummytxt;
        private RatingBar ratingBar;
        public ViewHolder3(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            add = itemView.findViewById(R.id.labeladd);
            vetdummytxt = itemView.findViewById(R.id.vetdummytxt);
            ratingBar= itemView.findViewById(R.id.ratingBar);
            ratingBar.setClickable(false);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Intent in=new Intent(context,VetInfo.class);
            in.addFlags(in.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("Key",vetdummytxt.getText().toString());
            context.startActivity(in);
        }
    }
}

