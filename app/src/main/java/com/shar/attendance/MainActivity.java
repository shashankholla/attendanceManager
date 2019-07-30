package com.shar.attendance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shar.attendance.adapters.DrawerItemCustomAdapter;
import com.shar.attendance.adapters.MainCustomAdapter;
import com.shar.attendance.models.DataModel;
import com.shar.attendance.models.SubjectModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

interface OnDataUpdateListener {
    void onDataAvailable(ArrayList<String> newDataList);
}

public class MainActivity extends AppCompatActivity implements OnDataUpdateListener{
    public BroadcastReceiver broadCastNewMessage;
    @Override
    protected void onResume(){
        super.onResume();
        if(this.adapter != null) {
            registerReceiver(broadCastNewMessage, new IntentFilter("com.an.sms.example"));
            SharedPreferences sp = getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String subList = gson.toJson(new ArrayList<SubjectModel>());
            ArrayList<SubjectModel> mSelectedList = gson.fromJson(sp.getString("SUBJECTS", subList),
                    new TypeToken<ArrayList<SubjectModel>>() {
                    }.getType());
            this.subjects = mSelectedList;

            this.adapter.updateData((ArrayList<SubjectModel>) this.subjects);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // this.unregisterReceiver(broadCastNewMessage);
    }

    private CharSequence mTitle, mDrawerTile;
    Toolbar toolbar;
    private String[] navTitles;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    public List<SubjectModel> subjects;
    private RecyclerView rV;
    private MainCustomAdapter adapter;
    private int attdPerc;
    private updateUIReceiver myUIReceiver;


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int statusBarHeight = getStatusBarHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
//        broadCastNewMessage = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(context, "Updating UI", Toast.LENGTH_SHORT).show();
//                SharedPreferences sp = context.getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
//
//                Gson gson = new Gson();
//                String subList = gson.toJson(new ArrayList<SubjectModel>());
//
//
//
//                subjects = gson.fromJson(sp.getString("SUBJECTS",subList),
//                        new TypeToken<ArrayList<SubjectModel>>() {
//                        }.getType());
//
//
//                Log.i("LogInMain",String.valueOf(subjects.get(0).countAttended()));
//
//
//
//            }
//        };
//        registerReceiver(broadCastNewMessage, new IntentFilter("com.an.sms.example"));


        myUIReceiver = new updateUIReceiver(this);
        registerReceiver(myUIReceiver, new IntentFilter("com.an.sms.example"));

        Boolean prefs = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if(prefs)
        {

            Intent i = new Intent(MainActivity.this, FirstRunSetup.class);
            i.putExtra("from", "firstLaunch");
            startActivity(i);
            return;
        }

        Bundle b = getIntent().getExtras();
        this.subjects = new ArrayList<>();
        if(b != null) {
          //  this.subjects = (ArrayList) b.getParcelableArrayList("subjects");
           // this.attdPerc = b.getInt("attdPerc");


        }
        SharedPreferences sp = getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String subList = gson.toJson(new ArrayList<SubjectModel>());
        ArrayList<SubjectModel> mSelectedList = gson.fromJson(sp.getString("SUBJECTS", subList),
                new TypeToken<ArrayList<SubjectModel>>() {
                }.getType());
        this.subjects = mSelectedList;

        sp = getSharedPreferences("ALARMPERCDATA", Context.MODE_PRIVATE);

        String alarmpercdataString = gson.toJson(new ArrayList<Integer>());
        ArrayList<Integer> alarmpercdata = gson.fromJson(sp.getString("ALARMPERCDATA", alarmpercdataString),
                new TypeToken<ArrayList<Integer>>() {
                }.getType());
        this.attdPerc = alarmpercdata.get(2);



        setupAlarm(alarmpercdata.get(0),alarmpercdata.get(1));



        setContentView(R.layout.activity_main);

        mTitle = mDrawerTile = getTitle();

        navTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        setupToolbar(statusBarHeight);


        rV = findViewById(R.id.mainRV);
        rV.setHasFixedSize(true);
        rV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Date date = new Date();
        String headerText;
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(h >= 23 || h < 6)
        {
            headerText = "Not sleeping yet?";
        }
        else if(h > 6 && h < 12){
            headerText = "Good Morning!";
        }
        else if(h > 12 && h < 18){
            headerText = "Good Afternoon!";
        }
        else if (h > 18 && h < 20){
            headerText = "Good Evening!";
        }
        else{
            headerText = "Good night!" ;
        }


        adapter = new MainCustomAdapter(headerText, this.subjects, getApplicationContext(), this.attdPerc, getSupportFragmentManager());
        rV.setAdapter(adapter);




    }

    void update(ArrayList<SubjectModel> subjects)
    {
        Log.i("updateMethodInMain", "Hello");
        this.subjects = subjects;
//        adapter = new MainCustomAdapter("HELLO", this.subjects, getApplicationContext(), this.attdPerc);
//        rV.setAdapter(adapter);
        Log.i("updateMethodMain2", String.valueOf(subjects.get(0).countAttended()));
        adapter.updateData(subjects);

    }
    void setupAlarm(int hour, int min){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND,00);

        Intent i = new Intent(getApplicationContext(), Notification_receiver.class);
        List<String> subs = new ArrayList<>();
        List<Integer> attd = new ArrayList<>();
        List<Integer> conducted = new ArrayList<>();
        for(SubjectModel sub : this.subjects){
            subs.add(sub.subjectName);
            attd.add(sub.countConductedToday()[0]);
            conducted.add(sub.countConductedToday()[1]);
        }
        i.putStringArrayListExtra("SUBJECTS", (ArrayList<String>) subs);
        i.putIntegerArrayListExtra("CONDUCTED",(ArrayList<Integer>)conducted);
        i.putIntegerArrayListExtra("ATTENDED",(ArrayList<Integer>)attd);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager =  (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);




    }
    public int getStatusBarHeight() {
        Rect rect = new Rect();
        Window window = this.getWindow();
        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            android.view.View v = window.findViewById(Window.ID_ANDROID_CONTENT);

            android.view.Display display = ((android.view.WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            //return result title bar height
            return display.getHeight() - v.getBottom() + rect.top;
        }
        return 0;
    }

    void setupToolbar(int statusBarHeight) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setPadding(0, 70,0,0);
        final Intent i = new Intent(this, aboutActivity.class);
        ImageButton b = findViewById(R.id.info);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();

                i.putExtra("from", "buttonPress");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        DataModel[] drawerItem = new DataModel[3];

        drawerItem[0] = new DataModel(R.drawable.ic_launcher_background, "Connect");
        drawerItem[1] = new DataModel(R.drawable.ic_launcher_background, "Fixtures");
        drawerItem[2] = new DataModel(R.drawable.ic_launcher_background, "Table");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter drawerAdapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(drawerAdapter );
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    public void onDataAvailable(ArrayList<String> newDataList) {
        adapter.notifyAll();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }
}
