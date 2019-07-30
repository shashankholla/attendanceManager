package com.shar.attendance.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shar.attendance.R;
import com.shar.attendance.models.DataModel;

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;


    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View listItem = layoutInflater.inflate(layoutResourceId, parent, false);
        ImageView imageIcon = listItem.findViewById(R.id.imageViewIcon);
        TextView textView = listItem.findViewById(R.id.textViewName);

        DataModel line = data[position];

        imageIcon.setImageResource(line.icon);
        textView.setText(line.name);

        return listItem;
    }
}
