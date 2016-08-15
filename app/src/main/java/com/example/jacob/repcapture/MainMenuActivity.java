package com.example.jacob.repcapture;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    public void onClick_Bench(View v) {
        //Intent to create a new activity
        Intent intent = new Intent(MainMenuActivity.this, BenchDataEntry.class);
        //Goes to the Data Entry Screen
        startActivity(intent);
    }
    public void onClick_Squat(View v) {
        //Intent to create a new activity
        Intent intent = new Intent(MainMenuActivity.this, BenchDataEntry.class);
        //Goes to the Data Entry Screen
        startActivity(intent);
    }
    public void onClick_Deadlift(View v) {
        //Intent to create a new activity
        Intent intent = new Intent(MainMenuActivity.this, BenchDataEntry.class);
        //Goes to the Data Entry Screen
        startActivity(intent);
    }
    public void onClick_Overhead(View v) {
        //Intent to create a new activity
        Intent intent = new Intent(MainMenuActivity.this, BenchDataEntry.class);
        //Goes to the Data Entry Screen
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
