package com.shar.attendance.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shar.attendance.R;
import com.shar.attendance.models.OneClass;
import com.shar.attendance.models.SubjectModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class classViewAdapter extends RecyclerView.Adapter<classViewAdapter.ViewHolder> {
    SubjectModel sub;
    Context mContext;


    public classViewAdapter(SubjectModel sub, Context mContext) {
        this.sub = sub;

        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView status;
        TextView date;
        TextView classNo;


        public ViewHolder(View itemView, int viewType) {

            super(itemView);

            status = itemView.findViewById(R.id.status);
            date =  itemView.findViewById(R.id.date);

            classNo = itemView.findViewById(R.id.classNo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        ViewHolder vh;
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.class_view_layout, viewGroup, false);
        vh = new ViewHolder(v,i);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull classViewAdapter.ViewHolder viewHolder, int i) {
        int l = this.sub.conducted.size();
        OneClass thisClass = this.sub.conducted.get(l-i-1);
        viewHolder.classNo.setText(String.valueOf(l-i));

        Log.i("inAdapter",thisClass.toString());
        if(thisClass.attended)
        viewHolder.status.setImageResource(R.drawable.green);
        else viewHolder.status.setImageResource(R.drawable.red);

        Date mDate = thisClass.date;
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");

        String displayDate = mDate.getDate() + "/" +  mDate.getMonth() + "-" + simpleDateformat.format(mDate);
        viewHolder.date.setText(displayDate);

    }

    @Override
    public int getItemCount() {
        return this.sub.conducted.size();
    }


}
