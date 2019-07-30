package com.shar.attendance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shar.attendance.fragments.subjects;
import com.shar.attendance.models.SubjectModel;

import java.util.ArrayList;

public class updateUIReceiver extends BroadcastReceiver {
    public MainActivity mainActivity;
    public updateUIReceiver(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Updating UI", Toast.LENGTH_SHORT).show();
        SharedPreferences sp = context.getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String subList = gson.toJson(new ArrayList<SubjectModel>());



        ArrayList<SubjectModel> mySubjects = gson.fromJson(intent.getStringExtra("jsonString"),
                new TypeToken<ArrayList<SubjectModel>>() {
                }.getType());
        Log.i("updateReceiver",intent.getStringExtra("jsonString"));
        mainActivity.update(mySubjects);

     //   Log.i("LogInMain",String.valueOf(subjects.get(0).countAttended()));
    }
}
