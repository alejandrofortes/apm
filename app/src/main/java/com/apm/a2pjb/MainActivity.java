package com.apm.a2pjb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apm.a2pjb.model.Teacher;
import com.apm.a2pjb.model.TeacherDB;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1001;

    private Toolbar toolbar;

    private GoogleApiClient apiClient;

    private ProgressDialog progress;
    private TeacherDB teacherDB;
    private boolean teachersLoaded;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            teachersLoaded = true;
            setTeachersLoaded(true);
            progress.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Google API Client
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        teachersLoaded = getTeachersLoaded();
        teacherDB = new TeacherDB(getApplicationContext());
    }

    public void openNavigation(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("teachersLoaded", teachersLoaded);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
        Toast.makeText(this, "Error de conexión!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork.isConnectedOrConnecting() && (activeNetwork.getType() != ConnectivityManager.TYPE_WIFI)){
            menu.findItem(R.id.option_synchronize).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.option_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.sign_out))
                    .setMessage(getString(R.string.ask_sign_out))
                    .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            ActivityCompat.finishAffinity(MainActivity.this);
                                        }
                                    });
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        if (id == R.id.option_synchronize) {
            downloadData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadData(){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://192.168.0.102:8080/api/all");
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        if(urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                            InputStream in = url.openStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            List<Teacher> result = new ArrayList<>();
                            String line;
                            while((line = reader.readLine()) != null) {
                                JSONObject message = new JSONObject(line);
                                JSONArray teachers = (JSONArray) message.get("message");
                                for (int i = 0; i<teachers.length(); i++){
                                    result.add(new Teacher(teachers.getJSONObject(i)));
                                }
                            }
                            in.close();
                            handler.sendEmptyMessage(0);
                            teacherDB.createAll(result);
                        }
                    }catch (IOException | JSONException e){
                        Log.e(e.getMessage(),null);
                    }
                }

            };
            Thread download = new Thread(r);
            progress = new ProgressDialog(this);
            progress.setTitle("Sincronizando");
            progress.setMessage("Espere mientras se sincroniza...");
            progress.setCancelable(false);
            progress.show();
            if (teachersLoaded){
                teachersLoaded = false;
                teacherDB.deleteAll();
            }
            download.start();

    }

    private boolean getTeachersLoaded(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("teachersLoaded", false);
    }

    private void setTeachersLoaded(Boolean teachersLoaded){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("teachersLoaded", teachersLoaded);
        editor.commit();
    }
}
