package com.shar.attendance;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.shar.attendance.fragments.alarm;
import com.shar.attendance.fragments.subjects;
import com.shar.attendance.models.SubjectModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstRunSetup extends AppCompatActivity implements alarm.ReturnFromAlarm, subjects.ReturnFromSubjects{
    int pos;
    Button next;
    Button prev;
    Fragment subFrag;
    Fragment alarmFrag;
    List<SubjectModel> subjects;
    int hour;
    int min;
    int attdPerc;
    Toolbar toolbar;

    public void changeFragment(View view ){
        Fragment fragment = null;
        if(view == findViewById(R.id.prev)){

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Log.i("firstRun","popping backstack");
                getSupportFragmentManager().popBackStack();
                return;
            }else{
                fragment = subFrag;

            }

        }
        if(view == findViewById(R.id.next)){

            fragment = alarmFrag;

        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.addToBackStack("my_fragment");
        ft.commit();

    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            Log.i("firstRun","else onBackPressed");
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
        setContentView(R.layout.activity_first_run_setup);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        prev.setEnabled(false);
        //Toast.makeText(this, "onCreateFirstRun", Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Setup");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setPadding(0, 70,0,0);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();
        subFrag = new subjects();
        alarmFrag = new alarm();

        ft.replace(R.id.frameLayout, subFrag);
        ft.addToBackStack(null);
        ft.commit();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(next.getText() == "Finish")
                {  getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRun", false).apply();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putParcelableArrayListExtra("subjects", (ArrayList) subjects );
                    i.putExtra("attdPerc", attdPerc);
                    i.putExtra("hour", hour);
                    i.putExtra("min", min);
                    i.putExtra("attdPerc", attdPerc);
                    startActivity(i);
                    return;
                }
                prev.setEnabled(true);
                next.setText("Finish");
                //Toast.makeText(getApplicationContext(), "NEXT", Toast.LENGTH_SHORT);
                changeFragment(v);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setText("Next");
                //Toast.makeText(getApplicationContext(), "PREV", Toast.LENGTH_SHORT);
                changeFragment(v);
            }
        });
    }

    @Override
    public void sendTimeAndPercentage(List<Integer> data) {
        attdPerc = data.get(2);
        min = data.get(1);
        hour = data.get(0);

    }

    @Override
    public void sendSubjects(List<SubjectModel> subjects) {
        this.subjects = subjects;
        for (SubjectModel s:subjects) {
            Log.i(s.subjectName, s.subjectName);

        }
    }
}
