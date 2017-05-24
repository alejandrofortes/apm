package com.apm.a2pjb.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pablo on 10/4/17.
 */

public class TeacherTask extends AsyncTask<String, Void, String> {

    public static final String URL = "http://ec2-34-253-85-79.eu-west-1.compute.amazonaws.com:8080/api/";
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Context context;

    public TeacherTask(Context mContext, ListView listView) {
        this.context = mContext;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute(){
        adapter = (ArrayAdapter<String>) listView.getAdapter();
    }

    @Override
    protected String doInBackground(String... params) {
        String resource = (params != null && params.length > 0) ? params[0] : null;
        String roomNumber = (params != null && params.length > 1) ? params[1] : null;
        String fullUrl = (resource != null) ? URL+resource+"/": URL;
        fullUrl = (roomNumber != null) ? fullUrl+"/"+roomNumber: fullUrl;

        try{
            if (roomNumber == null){
                return getTeachers(fullUrl);
            }
        } catch (IOException e) {
            Log.d("Error", "error", e);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try{
            if (result == null){
                showErrorDialog();
            }else{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonList = jsonObject.getJSONArray("message");
                for(int i = 0; i< jsonList.length(); i++){
                    adapter.add(jsonList.getString(i));
                }
                adapter.notifyDataSetChanged();
            }
        }catch (JSONException e){
            Log.e("Error", "error", e);
        }
    }

    private String getTeachers(String fullUrl) throws IOException {
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
            in.close();
            return result.toString();
        } else {
            return  null;
        }
    }

    private void showErrorDialog(){
        Toast toast = Toast.makeText(context, "Se ha producido un error conectando con el servidor", Toast.LENGTH_SHORT);
        toast.show();
    }
}