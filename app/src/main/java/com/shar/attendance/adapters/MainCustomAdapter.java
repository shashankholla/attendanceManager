package com.shar.attendance.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shar.attendance.FirstRunSetup;
import com.shar.attendance.MainActivity;
import com.shar.attendance.R;
import com.shar.attendance.editAlarm;
import com.shar.attendance.editSubject;
import com.shar.attendance.fragments.addSubjectFragment;
import com.shar.attendance.fragments.classDetailsPopup;
import com.shar.attendance.models.OneClass;
import com.shar.attendance.models.SubjectModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainCustomAdapter extends RecyclerView.Adapter<MainCustomAdapter.ViewHolder> {
    private String header;
    private List<SubjectModel> subjects;
    private Context mContext;
    private LocalDateTime date;
    private int attdPerc;
    private FragmentManager fm;

    public MainCustomAdapter(String header, List<SubjectModel> subjects, Context mContext, int attdPerc, FragmentManager fm)
    {
        Log.i("mainCustomAdapter","HELLO");
        this.header = header;
        this.subjects = subjects;
        this.mContext = mContext;
        this.attdPerc = attdPerc;
        this.fm = fm;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        //Header
        public TextView nameHeader;
        public ImageButton editSub;
        public ImageButton editAlarm;




        //This is the Header on the Recycler (viewType = 0)
        public TextView subName;
        public TextView classesConducted;
        public TextView classesAttended;
        public TextView stats;

        public LinearLayout statusColor;
        public Button attdBtn;
        public Button bunkBtn;
        public ImageButton detailedStat;

        //This constructor would switch what to findViewBy according to the type of viewType
        public ViewHolder(View v, int viewType) {
            super(v);
            detailedStat = v.findViewById(R.id.detailedStat);

            if (viewType == 0) {
                nameHeader =  v.findViewById(R.id.mainHeader);
                editSub = v.findViewById(R.id.editSubjects);
                editAlarm = v.findViewById(R.id.editAlarm);
            } else if (viewType == 1) {
                subName = v.findViewById(R.id.subName);
                attdBtn = v.findViewById(R.id.attendedToday);
                bunkBtn = v.findViewById(R.id.bunked);
                classesConducted = v.findViewById(R.id.classesConducted);
                classesAttended = v.findViewById(R.id.classesAttended);
                stats = v.findViewById(R.id.stats);
                statusColor = v.findViewById(R.id.statusColor);
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v;
        ViewHolder vh;
        switch (i) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_header, parent, false);
                vh = new ViewHolder(v,i);
                return  vh;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_subjects, parent, false);
                vh = new ViewHolder(v, i);

                return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if(i == 0) {

            viewHolder.nameHeader.setText(this.header);
            viewHolder.editSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, editSubject.class);
                    i.putExtra("from", "buttonPress");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });

            viewHolder.editAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, editAlarm.class);
                    i.putExtra("from", "buttonPress");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });

        }
        else {


            final SubjectModel subject = this.subjects.get(i-1);
            viewHolder.classesConducted.setText(String.valueOf(subject.countConducted()));
            viewHolder.classesAttended.setText(String.valueOf(subject.countAttended()));
            viewHolder.stats.setText(status(attdPerc, subject.countAttended(), subject.countConducted(), viewHolder.statusColor));


            viewHolder.detailedStat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showInputNameDialog(subject, subjects);
                }
            });

            viewHolder.subName.setText(subject.subjectName);
                    viewHolder.attdBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subject.addClass(new OneClass(new Date(), true));
                            viewHolder.classesConducted.setText(String.valueOf(subject.countConducted()));
                            viewHolder.classesAttended.setText(String.valueOf(subject.countAttended()));
                            viewHolder.stats.setText(status(attdPerc, subject.countAttended(), subject.countConducted(), viewHolder.statusColor));
                            updatePref();
                            notifyDataSetChanged();
                        }
                    });
            viewHolder.bunkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subjects.get(i-1).addClass(new OneClass(new Date(), false));
                    viewHolder.classesConducted.setText(String.valueOf(subject.countConducted()));
                    viewHolder.classesAttended.setText(String.valueOf(subject.countAttended()));
                    viewHolder.stats.setText(status(attdPerc, subject.countAttended(), subject.countConducted(), viewHolder.statusColor));
                    updatePref();
                    notifyDataSetChanged();
                }
            });



        }

    }

    private void showInputNameDialog(SubjectModel sub, List<SubjectModel> subjects) {
        FragmentManager fragmentManager = this.fm;
        classDetailsPopup newSubDialog = new classDetailsPopup();
        Bundle args = new Bundle();
        args.putSerializable("subject", sub);

        newSubDialog.setArguments(args);




        newSubDialog.setCancelable(false);
        newSubDialog.setDialogTitle("Enter Name");
        newSubDialog.show(fragmentManager, "Input Dialog");
    }
    public void updatePref(){

        Gson gson = new Gson();
        String jsonString = gson.toJson(subjects);
        SharedPreferences sp = mContext.getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        sp.edit().putString("SUBJECTS", jsonString).apply();


    }
    public String status(int perc, int attd, int cond, LinearLayout statusColor){

        if(cond == 0)
            return "Classes yet to start?";

        if(perc == 100)
        {
            if(attd != cond)
                return "No chance!";
            else
                return "Have to attend the next class";
        }

        float curPerc = (float) (attd*100)/ cond;


        if(curPerc >= perc)
        {   statusColor.setBackgroundColor(Color.rgb(0, 0xff,0));
            if((float)(attd*100)/(cond + 1) >= perc)
            {   if((float)(attd*100)/(cond + 1) > perc)
            {
                    int x = (int) Math.floor((float)(100*attd - perc*cond) / (perc));
                    if(x != 0) return "Can skip next " + x + " classes";
                    else return "Have to attend the next class";
            }
                return "You are on track";
            }
            else{
                return "Have to attend the next class";
            }
        }else{
            statusColor.setBackgroundColor(Color.rgb(0xff, 0,0));
            int x = (int) Math.ceil((float)(100*attd - perc*cond) / (perc - 100));
            Log.i("..", String.valueOf(curPerc));
            if (x == 1 || x == 0) return "Have to attend the next class to maintain";

            return "Have to attend " + x + " more classes";

        }



    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1
        if (position == 0) viewType = 0; //if zero, it will be a header view
        return viewType;
    }

    @Override
    public int getItemCount() {
        return this.subjects.size() + 1;
    }

    public void updateData(ArrayList<SubjectModel> subjects)
    {
     this.subjects = subjects;
     notifyDataSetChanged();
    }
}
