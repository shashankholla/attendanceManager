package com.shar.attendance.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shar.attendance.R;
import com.shar.attendance.models.SubjectModel;

import java.util.List;

public class subjectAddAdapter extends RecyclerView.Adapter<subjectAddAdapter.ViewHolder> {
    private List<SubjectModel> subjects;
    private Context mContext;


    public subjectAddAdapter(List<SubjectModel> subjects, Context mContext) {
        this.subjects = subjects;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.subjectitem,viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        SubjectModel sM = subjects.get(i);
        viewHolder.subjectName.setText(sM.subjectName);
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjects.remove(i);
                notifyItemRemoved(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView subjectName;
        public ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            deleteBtn = itemView.findViewById(R.id.subDelete);
        }
    }
}
