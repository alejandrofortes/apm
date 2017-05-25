package com.apm.a2pjb;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv_steps;
    SensorManager sensorManager;

    float x , y , z , actual, step;
    int numSteps=0;
    ArrayList lista = new ArrayList<Float>();

    private SensorManager sm;
    private Sensor sAcc;
    private List<Sensor> sensors;

    private float prevX, prevY, prevZ;
    private float curX, curY, curZ;

    int pasos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
        }

        curX = 0;
        curY = 0;
        curZ = 0;
        prevY = 0;
        numSteps = 0;

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Acciones a realizar si la precisión del sensor cambia
    }

    public void onSensorChanged(SensorEvent event) {

        synchronized (this) {

            long current_time = event.timestamp;

            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            actual =  curX * curX + curY * curY + curZ * curZ;
            lista.add(actual);

            if(lista.size() >50){
                lista.remove(0);

                for(int i=0; i<lista.size();i++){
                    float f = (float)lista.get(i);
                    if (Math.abs(actual - f)>19){
                        step++;
                    }
                }
                if (step>45) {
                    numSteps++;
                    lista = new ArrayList<Float>();
                }
                step = 0;
            }


            ((TextView) findViewById(R.id.tv_steps)).setText("" + numSteps);

        }

    }

    protected void onResume() {
        super.onResume();

        if (sensors.size() > 0) {
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        sm.unregisterListener(this);
    }
    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        super.onStop();
    }
}