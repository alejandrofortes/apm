package com.apm.a2pjb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OthersFragment extends Fragment {

    private ListView listView;

    public OthersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        List<String> elements = new ArrayList<String>();
        //TODO Obtain elements from some source

        listView = (ListView)getView().findViewById(R.id.othersListView);
        listView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.custom_textview, elements));
    }

}
