package com.shar.attendance.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shar.attendance.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class alarm extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TimePicker timepicker;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private JSONObject data;
    private SeekBar attdPerSB;
    //private OnFragmentInteractionListener mListener;

    public alarm() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (ReturnFromAlarm) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement TextClicked");
//        }
//    }

//    ReturnFromAlarm mCallback;
    @Override
    public void onDetach() {
       // mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

    public interface ReturnFromAlarm {
        public void sendTimeAndPercentage(List<Integer> data);
    }



    // TODO: Rename and change types and number of parameters
    public static alarm newInstance(String param1, String param2) {
        alarm fragment = new alarm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v =  inflater.inflate(R.layout.fragment_alarm, container, false);
        timepicker=(TimePicker)v.findViewById(R.id.timePicker);
        final TextView t = v.findViewById(R.id.getAttdPer);
        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                updateData();
            }
        });

        attdPerSB = v.findViewById(R.id.attendancePercentage);

        SharedPreferences sp = getContext().getSharedPreferences("ALARMPERCDATA", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String alarmpercdataString = gson.toJson(new ArrayList<Integer>());
        ArrayList<Integer> alarmpercdata = gson.fromJson(sp.getString("ALARMPERCDATA", alarmpercdataString),
                new TypeToken<ArrayList<Integer>>() {
                }.getType());

        if(alarmpercdata != null && alarmpercdata.size() == 3)
        {


            timepicker.setHour(alarmpercdata.get(0));
            timepicker.setMinute(alarmpercdata.get(1));
            attdPerSB.setProgress(alarmpercdata.get(2));

        }
        else{
            attdPerSB.setProgress(85);
        }

        updateData();
        t.setText("Minimum Attendence: " + attdPerSB.getProgress() + "%");


        attdPerSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean minPerNotif = true;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                t.setText("Minimum Attendence: " + progress + "%");
                if(minPerNotif && progress < 10)
                {
                    Toast.makeText(getContext(), "Min % should be greater than 10%", Toast.LENGTH_SHORT).show();
                    minPerNotif = false;
                }
                if(progress < 10){
                    attdPerSB.setProgress(10);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                minPerNotif = true;
                updateData();
            }
        });

        return v;

    }


    public void updateData() {


        //Toast.makeText(getContext(), "update Data", Toast.LENGTH_SHORT).show();
        List<Integer> data = new ArrayList<>();
        data.add(timepicker.getHour());
        data.add(timepicker.getMinute());
        data.add(attdPerSB.getProgress());

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        SharedPreferences sp = getContext().getSharedPreferences("ALARMPERCDATA", Context.MODE_PRIVATE);
        sp.edit().putString("ALARMPERCDATA", jsonString).commit();

      //  mCallback.sendTimeAndPercentage(data);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      
    }
}
