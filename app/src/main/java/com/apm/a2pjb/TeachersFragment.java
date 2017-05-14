package com.apm.a2pjb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apm.a2pjb.model.TeacherTask;
import com.apm.a2pjb.model.TeacherDB;

import java.util.ArrayList;
import java.util.List;


public class TeachersFragment extends Fragment {

    private ListView listView;
    private Boolean teachersLoaded;
    private TeacherDB teacherDB;


    public TeachersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teachersLoaded = getArguments().getBoolean("teachersLoaded", false);
        teacherDB = new TeacherDB(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teachers, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        List<String> elements = new ArrayList<String>();

        listView = (ListView)getView().findViewById(R.id.techersListView);
        listView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.custom_textview, elements));
        if (!teachersLoaded){
            new TeacherTask(getActivity(), listView).execute("teachers");
        }else{
            List<String> teacherList = teacherDB.getTeachers();
            elements.addAll(teacherList);
        }
    }
}


