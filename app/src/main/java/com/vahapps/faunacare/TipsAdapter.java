package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 13-Apr-18.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class TipsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater inflater;

    public TipsAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() { return arrayList.size();    }

    @Override
    public Object getItem(int position) { return arrayList.get(position);    }

    @Override
    public long getItemId(int position) {        return position;    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        view = inflater.inflate(R.layout.tips_layout, viewGroup, false);
        viewHolder.tips=view.findViewById(R.id.tips);

        viewHolder.tips.setText(arrayList.get(position));

        return view;
    }

    private class ViewHolder {
        private TextView tips;
        private TextView label;


    }
}

