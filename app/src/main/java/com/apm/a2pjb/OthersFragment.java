package com.apm.a2pjb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apm.a2pjb.dao.TeacherDAO;
import com.apm.a2pjb.model.Teacher;
import com.apm.a2pjb.model.TeacherDB;

import java.util.ArrayList;
import java.util.List;

public class OthersFragment extends Fragment {

    private ListView listView;
    private Boolean teachersLoaded;
    private TeacherDB teacherDB;

    public OthersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teachersLoaded = getArguments().getBoolean("teachersLoaded");
        teacherDB = new TeacherDB(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_others, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        List<String> elements = new ArrayList<>();

        listView = (ListView)getView().findViewById(R.id.othersListView);
        listView.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.custom_textview, elements));
        if (!teachersLoaded){
            new TeacherDAO(getActivity(),listView).execute("rooms");
        }else{
            List<String> teacherList = teacherDB.getRooms();
            elements.addAll(teacherList);
        }
    }

}
