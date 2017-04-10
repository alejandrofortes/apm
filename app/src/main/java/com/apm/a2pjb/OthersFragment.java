package com.apm.a2pjb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apm.a2pjb.model.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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
        List<String> elements = new ArrayList<>();

        listView = (ListView)getView().findViewById(R.id.othersListView);
        listView.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.custom_textview, elements));
        new RoomDao().execute();

    }

    private class RoomDao extends AsyncTask<String, String, String> {

        private String url = "http://192.168.0.108:8080/api/rooms";
        private ArrayAdapter<String> adapter;

        @Override
        protected void onPreExecute(){
            adapter = (ArrayAdapter<String>) listView.getAdapter();
        }

        @Override
        protected String doInBackground(String... params) {

            String roomNumber = (params != null && params.length > 0) ? params[0] : null;
            String fullUrl = (roomNumber != null) ? url+"/"+roomNumber: url;

            try{
                if (roomNumber == null){
                    return getRooms(fullUrl);
                }
            } catch (IOException e) {
                Log.d("Error", "error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonList = jsonObject.getJSONArray("message");
                for(int i = 0; i< jsonList.length(); i++){
                    String room = jsonList.getString(i);
                    if (room.startsWith("L")) {
                        room = "Laboratorio "+room;
                    }else{
                        room = "Despacho "+room;
                    }
                    adapter.add(room);
                }
                adapter.notifyDataSetChanged();
            }catch (JSONException e){
                Log.e("Error", "error", e);
            }
            Log.d( "Post execute: " , "In the post-execute method" );
            Log.d("Restult", result);
        }

        private String getRooms(String fullUrl) throws IOException {
            URL url = new URL(fullUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if(urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                InputStream in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } else {
                return  null;
            }
        }
    }

}
