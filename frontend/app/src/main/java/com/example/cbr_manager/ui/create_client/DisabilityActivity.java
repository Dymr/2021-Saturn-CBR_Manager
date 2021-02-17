    package com.example.cbr_manager.ui.create_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.R;

public class DisabilityActivity extends AppCompatActivity {

    CheckBox[] checkBoxes = new CheckBox[10];
    boolean[] disabilities_check = new boolean[10];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client_disability);
        String txt;

        for(int i=0 ; i<10 ; i++) {
            txt = "checkBox" + i;
            int resourceId = this.getResources().getIdentifier(txt, "id", this.getPackageName());
            checkBoxes[i] = (CheckBox) findViewById(resourceId);
        }

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(v);
                nextSurveyPage();
            }
        });
        Button prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSurveyPage();
            }
        });
    }

    public void updateInfo(View v) {
        for(int i=0 ; i<10 ; i++) {
            if(checkBoxes[i].isChecked()) {
                disabilities_check[i] = true;
            } else {
                disabilities_check[i] = false;
            }
        }
    }
    private void nextSurveyPage() {
        Intent intent = new Intent(this, CaregiverInfoActivity.class);
        startActivity(intent);
    }

    private void prevSurveyPage() {
        Intent intent = new Intent(this, PersonalInfoActivity.class);
        startActivity(intent);
    }
}
