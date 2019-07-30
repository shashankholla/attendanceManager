package com.shar.attendance.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.shar.attendance.R;
import com.shar.attendance.adapters.MainCustomAdapter;
import com.shar.attendance.adapters.classViewAdapter;
import com.shar.attendance.models.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public class classDetailsPopup extends DialogFragment {

   SubjectModel sub;
   classViewAdapter adapter;
   //MainCustomAdapter adapter;
    static String DialogboxTitle;

    public interface addSubjectFragmentListener {
        void onFinishInputDialog(String inputText);
    }
    //---empty constructor required
    public classDetailsPopup() {


    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        //getArguments().getSerializable("subject");



    }

    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        View view = inflater.inflate(
                R.layout.fragment_class_details_popup, container);
        Bundle arguments = getArguments();
        sub = (SubjectModel) arguments.get("subject");
        RecyclerView rv = view.findViewById(R.id.classRv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new classViewAdapter(sub, getContext());
        //List<SubjectModel> subjects = new ArrayList<>();
        //subjects.add(sub);
       // adapter = new MainCustomAdapter("OK", subjects, getContext(), 1, getFragmentManager());
        rv.setAdapter(adapter);

        return view;


    }

    @Override
    public void onResume()
    {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode,android.view.KeyEvent event)
            {
                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                }
                // Otherwise, do nothing else
                else return false;
            }
        });
    }
}

