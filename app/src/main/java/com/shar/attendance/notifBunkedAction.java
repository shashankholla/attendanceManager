package com.shar.attendance;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shar.attendance.models.OneClass;
import com.shar.attendance.models.SubjectModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class notifBunkedAction extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       // android.os.Debug.waitForDebugger();
        //Toast.makeText(context, intent.getStringExtra("SUBJECT_NAME") + ":" + intent.getStringExtra("ACTION"), Toast.LENGTH_SHORT).show();

        String subject = intent.getStringExtra("SUBJECT_NAME");

        List<SubjectModel> subjects = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String subList = gson.toJson(new ArrayList<SubjectModel>());
        subjects = gson.fromJson(sp.getString("SUBJECTS", subList),
                new TypeToken<ArrayList<SubjectModel>>() {
                }.getType());

        SubjectModel mySubject = null;
        int i = 0;
        for(; i < subjects.size() ; i++)
        {
            if (subjects.get(i).subjectName.equals(subject))
            {
                mySubject = subjects.get(i);
                break;
            }
        }



        subjects.get(i).addClass(new OneClass(new Date(), false));




        String jsonString = gson.toJson(subjects);
        sp.edit().putString("SUBJECTS", jsonString).commit();

        Log.i("logInAttended", sp.getString("SUBJECTS",""));


        Intent in = new Intent("com.an.sms.example");
        in.putExtra("jsonString", jsonString);
        context.sendBroadcast(in);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent attendedIntent = new Intent(context, notifAttendedAction.class);
        attendedIntent.putExtra("SUBJECT_NAME", subject);
        attendedIntent.putExtra("ACTION","ATTENDED");


        Intent bunkedIntent = new Intent(context, notifBunkedAction.class);
        bunkedIntent.putExtra("SUBJECT_NAME", subject);
        bunkedIntent.putExtra("ACTION","BUNKED");

        PendingIntent p0 = PendingIntent.getBroadcast(context, i, attendedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent p1 = PendingIntent.getBroadcast(context, i, bunkedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int[] iCount = subjects.get(i).countConductedToday();
        Log.i("notifAttended", String.valueOf(iCount[0]));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(subject)
                .setContentText(String.format(Locale.getDefault(), "Attended %d/%d",iCount[0],iCount[1]))
                .setAutoCancel(true)
               // .setOngoing(true)
                .addAction(R.drawable.ic_launcher_background, "Attended", p0)
                .addAction(R.drawable.ic_launcher_background, "Bunked", p1);
        builder.setVibrate(new long[] { -1 });
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context.getApplicationContext());
        Log.i("notifAttendString",String.format(Locale.getDefault(), "Attended %d/%d",iCount[0],iCount[1]));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(i, builder.build());




    }
}
