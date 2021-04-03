package com.example.cbr_manager.ui.clientselector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cbr_manager.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClientSelectorActivity extends AppCompatActivity {

    int code = -1;
    boolean isNewVisit = false;
    boolean isNewReferral = false;
    private final int NEW_VISIT_CODE = 100;
    private final int NEW_REFERRAL_CODE = 101;
    private final int NEW_BASELINE_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_selector);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Select a Client");

        Intent intent = getIntent();
        code = intent.getIntExtra("CODE", -1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public int getCode() {
        return code;
    }
}