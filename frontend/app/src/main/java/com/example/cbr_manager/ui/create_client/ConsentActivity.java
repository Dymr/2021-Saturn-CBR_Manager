package com.example.cbr_manager.ui.create_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.R;


public class ConsentActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText year, month, day;
    private TextView test;
    private boolean consent;
    private String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client_consent);

        radioGroup = findViewById(R.id.radioGroup);
        Button nextButton = findViewById(R.id.nextButton);

        test = findViewById(R.id.test);
        test.setText(date);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
                checkDate(v);
                nextSurveyPage();
            }
        });
    }

    private void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String c = radioButton.getText().toString();
        if(c.equalsIgnoreCase("Yes")) {
            consent = true;
        } else{
            consent=false;
        }
    }

    private void checkDate(View v) {
        year = findViewById(R.id.editTextYear);
        month = findViewById(R.id.editTextMonth);
        day = findViewById(R.id.editTextDay);
        date = year.getText().toString() + "/" + month.getText().toString() + "/" + day.getText().toString();
        test.setText(date);
    }

    private void nextSurveyPage() {
        Intent intent = new Intent(this, VillageInfoActivity.class);
        startActivity(intent);
    }
}
