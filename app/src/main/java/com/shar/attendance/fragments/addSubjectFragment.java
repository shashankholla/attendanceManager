package com.shar.attendance.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shar.attendance.R;

public class addSubjectFragment extends DialogFragment {

    EditText addSub;
    Button btnDone;
    static String DialogboxTitle;

    public interface addSubjectFragmentListener {
        void onFinishInputDialog(String inputText);
    }
    //---empty constructor required
    public addSubjectFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        View view = inflater.inflate(
                R.layout.addsub, container);

        //---get the EditText and Button views
        addSub = (EditText) view.findViewById(R.id.newSub);
        btnDone = (Button) view.findViewById(R.id.btnDone);

        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                Intent intent=new Intent();
                String cap = addSub.getText().toString();
                String myStr = cap.substring(0, 1).toUpperCase() + cap.substring(1);
                intent.putExtra("MESSAGE",myStr);
                //---gets the calling activity
                getTargetFragment().onActivityResult(getTargetRequestCode(),0, intent );
                //---dismiss the alert
                dismiss();
            }
        });

        //---show the keyboard automatically
        addSub.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

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

