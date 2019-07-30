package com.shar.attendance.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shar.attendance.R;
import com.shar.attendance.models.SubjectModel;
import com.shar.attendance.adapters.subjectAddAdapter;

import java.util.ArrayList;
import java.util.List;


public class subjects extends Fragment implements addSubjectFragment.addSubjectFragmentListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView rV;
    private subjectAddAdapter customAdapter;
    private List<SubjectModel> subjects;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn;
    ReturnFromSubjects mCallback;
    //private OnFragmentInteractionListener mListener;

    public subjects() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment subjects.
     */
    // TODO: Rename and change types and number of parameters
    public static subjects newInstance(String param1, String param2) {
        subjects fragment = new subjects();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ReturnFromSubjects{
        public void sendSubjects(List<SubjectModel> subjects);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        subjects = new ArrayList<>();
        SharedPreferences sp = getActivity().getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String subList = gson.toJson(new ArrayList<SubjectModel>());
        this.subjects = gson.fromJson(sp.getString("SUBJECTS", subList),
                new TypeToken<ArrayList<SubjectModel>>() {
                }.getType());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), "Add subjects by pressing the +", Toast.LENGTH_LONG).show();
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);
        rV = v.findViewById(R.id.subjectFragRV);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showInputNameDialog();

            }
        });
        rV.setHasFixedSize(true);
        rV.setLayoutManager(new LinearLayoutManager(getContext()));



        customAdapter = new subjectAddAdapter(subjects, getContext());
        rV.setAdapter(customAdapter);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String jsonString = gson.toJson(subjects);
        SharedPreferences sp = getContext().getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        sp.edit().putString("SUBJECTS", jsonString).commit();
//        mCallback.sendSubjects(subjects);

    }

    private void showInputNameDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        addSubjectFragment newSubDialog = new addSubjectFragment();
        newSubDialog.setTargetFragment(this, 0);

        newSubDialog.setCancelable(false);
        newSubDialog.setDialogTitle("Enter Name");
        newSubDialog.show(fragmentManager, "Input Dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        String message = data.getStringExtra("MESSAGE");
        subjects.add(new SubjectModel(message));
        customAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mCallback = (subjects.ReturnFromSubjects) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement TextClicked");
//        }
//    }

    @Override
    public void onFinishInputDialog(String inputText) {
    }
}
