package com.example.cbr_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    public void onClick_AddRecord(View v){
        displayText("Clicked add record!");
    }

    public void onClick_ClearAll(View v){
        displayText("Clicked clear all!");
    }

    public void onClick_DisplayRecords(View v){
        displayText("Clicked display record!");
    }
}