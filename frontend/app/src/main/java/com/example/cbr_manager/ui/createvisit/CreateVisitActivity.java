package com.example.cbr_manager.ui.createvisit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.cbr_manager.R;

public class CreateVisitActivity extends AppCompatActivity {

    int clientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_visit);

        Intent intent = getIntent();
        clientId = intent.getIntExtra("clientId", -1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}