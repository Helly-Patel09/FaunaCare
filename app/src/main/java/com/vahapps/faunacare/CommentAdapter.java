package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 28-Feb-18.
 */

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList, commenterImageList;
    private LayoutInflater inflater;
    private boolean isListView;
    private int selectedPosition = -1;

    public CommentAdapter(Context context, ArrayList<String> arrayList, ArrayList<String> commenterImageList, boolean isListView) {
        this.context = context;
        this.arrayList = arrayList;
        this.isListView = isListView;
        this.commenterImageList = commenterImageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //ViewHolder2 viewHolder;
        TextView label;
        ImageView commenterImage;
        //if (view == null) {
        //viewHolder = new ViewHolder2();
        //if (isListView)
        view = inflater.inflate(R.layout.comment_row, viewGroup, false);
        label = view.findViewById(R.id.label_vol);
        commenterImage = view.findViewById(R.id.commenterImage);
        //view.setTag(viewHolder);
        // } else {
        //viewHolder = (ViewHolder2) view.getTag();
        label.setText(Html.fromHtml(arrayList.get(i)));
        label.setTag(i);

        String key = commenterImageList.get(i);
        Log.d("commenter key", key);
        Log.d("comment:",arrayList.get(i));
        if (key.equals("Vet")) {
            //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(20, 20);
            //viewHolder.commenterImage.setLayoutParams(param);
            //commenterImage.setVisibility(View.VISIBLE);
            commenterImage.setImageResource(R.drawable.vet);

        } else if (key.equals("Volunteer")) {
            //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(20, 20);
            //viewHolder.commenterImage.setLayoutParams(param);
            //commenterImage.setVisibility(View.VISIBLE);
            commenterImage.setImageResource(R.drawable.volunteer);
        } else if(key.equals("Finder")){
            //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, 0);
            //viewHolder.commenterImage.setLayoutParams(param);
            //commenterImage.setVisibility(View.INVISIBLE);
            commenterImage.setImageResource(R.drawable.user);
        }
        commenterImage.setTag(i);
            /*viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });*/
        //}
        return view;
    }

    /*private class ViewHolder2 {
        private TextView label;
        private ImageView commenterImage;
    }*/

    //Return the selectedPosition item
   /* public String getSelectedItem() {
        if (selectedPosition != -1) {
            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return arrayList.get(selectedPosition);
        }
        return "";
    }*/
    //Delete the selected position from the arrayList
    /*public void deleteSelectedPosition() {
        if (selectedPosition != -1) {
            arrayList.remove(selectedPosition);
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
        }
    }*/
}
