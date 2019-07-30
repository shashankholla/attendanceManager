package com.shar.attendance;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> s = intent.getExtras().getStringArrayList("SUBJECTS");
        List<Integer> c = intent.getExtras().getIntegerArrayList("CONDUCTED");
        List<Integer> a = intent.getExtras().getIntegerArrayList("ATTENDED");

        //Toast.makeText(context, "ALARM", Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int i = 0;



        for(String sub: s){
            Intent attendedIntent = new Intent(context, notifAttendedAction.class);
            attendedIntent.putExtra("SUBJECT_NAME", sub);
            attendedIntent.putExtra("ACTION","ATTENDED");


            Intent bunkedIntent = new Intent(context, notifBunkedAction.class);
            bunkedIntent.putExtra("SUBJECT_NAME", sub);
            bunkedIntent.putExtra("ACTION","BUNKED");

            PendingIntent p0 = PendingIntent.getBroadcast(context, i, attendedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent p1 = PendingIntent.getBroadcast(context, i, bunkedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.arrow_up_float)
                    .setContentTitle(sub)
                    .setContentText(String.format(Locale.getDefault(), "Attended %d/%d today",a.get(i),c.get(i)))
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_launcher_background, "Attended", p0)
                    .addAction(R.drawable.ic_launcher_background, "Bunked", p1);


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
            i = i + 1;

        }


    }
}
