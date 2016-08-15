package com.example.jacob.repcapture;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BenchDataEntry extends MainMenuActivity implements SensorEventListener {
    //Creates a new Database
    DBAdapter myDb;
    //Creates final x,y,z values for accelerometer
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    //boolean for start and stop buttons
    boolean start;
    //declares the sensors
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //the integer that stores the amount of reps you do
    int counter = 0 ;
    //Weight that the user enters is stored in this variable
    double Weight_double;
    private final float NOISE = (float) 2.0;
    //nonsense
    int DisplayInt;
    //Filtering constant
    private final float mAlpha = 0.8f;
    //Arrays for storing filtered values
    private float[] mGravity = new float[3];
    private float[] mAccel = new float[3];
    //Values of the High Pass filter
    float highX;
    float highY;
    float highZ;
    //Values of the Low Pass Filter
    float lowX;
    float lowY;
    float lowZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bench_data_entry);
        //opens the database
        openDB();
        //Initializes and declares the sensors, and accelerometer
         mInitialized = false;
         mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
         mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    //When the user clicks the start or stop button
    public void onClick_Start(View v){
        //stores the weight value entered by user
        final EditText sWeight = (EditText) findViewById(R.id.editTextWeight);
        Log.v("EditText", sWeight.getText().toString());
        String w = sWeight.getText().toString();

        Weight_double = Double.parseDouble(w);
        //sets to true because start was clicked
        start = true;
        counter = 0;
        final int x1 = (int)lowX;
        //these are used to check the accuracy of th algorithm
        final int y1 = (int)lowY;
        final int z1 = (int)lowZ;
        //creates a new thread to count reps
        new Thread(new Runnable(){
            public void run() {
                int x2;
                int y2;
                int z2;
                int x3 = 1000000;

               while(start) {
                   x2 = (int)lowX;
                   y2 = (int)lowY;
                   z2 = (int)lowZ;
                   if(x2 != x3) {
                       if(x2 == x1 ) {
                           counter++;
                       }
                   }
                   x3 = x2;}
            }
        }).start();
    }
    
    public void onClick_Stop(View v){
        start = false;
        counter = counter/ 2;
        //writes to the database

        long newId = myDb.insertRow("sdfsdf",Weight_double, counter);
        // Query for the record we just added.
        // Use the ID:
        Cursor cursor = myDb.getRow(newId);
        displayRecordSet(cursor);
    }
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }
    public void onSensorChanged(SensorEvent event) {
        //raw values of x,y,z
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        DisplayInt = (int)mLastX;

        //Apply low pass filter
        mGravity[0]= lowPass(x, mGravity[0]);
        mGravity[1]= lowPass(y, mGravity[1]);
        mGravity[2]= lowPass(z, mGravity[2]);

        lowX = mGravity[0];
        lowY = mGravity[1];
        lowZ = mGravity[2];


        //Apply high pass filter
        mAccel[0] = highPass(x, mGravity[0]);
        mAccel[1] = highPass(y, mGravity[1]);
        mAccel[2] = highPass(z, mGravity[2]);

         highX = mAccel[0];
         highY = mAccel[1];
         highZ = mAccel[2];
    }
    private float highPass(float current, float gravity) {
        return current - gravity;
    }
    private float lowPass(float current, float gravity)
    {
        return gravity * mAlpha + current * (1-mAlpha);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }
    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(message);
    }
    public void onClick_ClearAll(View v) {
       // displayText("Clicked clear all!");
        myDb.open();
        myDb.deleteAll();
        myDb.close();
    }
   public void onClick_ViewAll(View v) {
        displayText("Clicked display record!");

        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }
    // Display an entire recordset to the screen.
   private void displayRecordSet(Cursor cursor) {
        String message = "";
        TextView txtView;
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
               // String date = cursor.getString(DBAdapter.COL_DATE);
                double weight = cursor.getDouble(DBAdapter.COL_WEIGHT);
                int reps = cursor.getInt(DBAdapter.COL_REPS);

                // Append data to the message:
                message += //"id=" + id CHANGE TO "EXERCISE:BENCH"
                       // " Date: " + date
                                " Weight: " + weight
                                + ", Reps: " + counter;
            } while (cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

       // displayText(message);
       setContentView(R.layout.record_display);
       txtView =(TextView)findViewById(R.id.recordView);

       txtView.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bench_data_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}






