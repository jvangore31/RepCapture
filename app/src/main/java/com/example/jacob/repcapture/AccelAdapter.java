package com.example.jacob.repcapture;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Jacob on 8/1/2015.
 */
public class AccelAdapter extends MainMenuActivity implements SensorEventListener{

    private static final int UPDATE_THRESHOLD = 500;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long mLastUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Get reference to SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Get reference to Accelerometer
        if (null == (mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
            finish();
    }
    //Register Listener
    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mLastUpdate = System.currentTimeMillis();
    }
    @Override
    protected void onPause(){
        mSensorManager.unregisterListener(this);
        super.onPause();
    }
    //Process new Reading
    @Override
    public void onSensorChanged(SensorEvent event) {
        long actualTime;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            actualTime = System.currentTimeMillis();
        }
        if((actualTime = (mLastUpdate ))> UPDATE_THRESHOLD) {
            mLastUpdate = actualTime;
            float x = event.values[0], y = event.values[1], z = event.values[2];
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
