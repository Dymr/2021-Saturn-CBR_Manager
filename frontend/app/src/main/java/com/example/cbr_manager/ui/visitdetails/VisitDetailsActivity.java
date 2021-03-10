package com.example.cbr_manager.ui.visitdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitDetailsActivity extends AppCompatActivity {


    private APIService apiService = APIService.getInstance();
    private View parentLayout;
    private String additionalInfo;
    private String location;
    private int villageNum;
    private String formattedDate;
    private int clientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);
        parentLayout = findViewById(android.R.id.content);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String additionalInfo = intent.getStringExtra("additionalInfo");
        int clientId = intent.getIntExtra("clientId", -1);
        String formattedDate = intent.getStringExtra("formattedDate");
        getClientInfo(clientId);
        this.clientId = clientId;
        this.additionalInfo = additionalInfo;
        this.formattedDate = formattedDate;
        this.location = intent.getStringExtra("location");
        setupButtons();
        setupTextViews();
        setupImageViews();
        setupBackImageViewButton();

    }

    private void setupBackImageViewButton() {
        ImageView backButtonImageView = findViewById(R.id.visitDetailsBackImageView);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getClientInfo(int clientId){
        apiService.clientService.getClient(clientId).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {

                if(response.isSuccessful()){
                    Client client = response.body();

                    // Todo: dynamically set the client info here
                    setupNameTextView(client.getFullName());
                } else{
                    Snackbar.make(parentLayout, "Failed to get the client. Please try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Snackbar.make(parentLayout, "Failed to get the client. Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupImageViews() {
        ImageView displayPicture = findViewById(R.id.visitDetailsDisplayPictureImageView);
        displayPicture.setImageResource(R.drawable.client_details_placeholder);
    }

    private void setupTextViews() {
        setupLocationTextView();
        setupAdditionalInfoTextView(additionalInfo);
        setupDateTextView();
        setupLocationTextView();
    }

    private void setupNameTextView(String fullName) {
        TextView nameTextView = findViewById(R.id.visitDetailsNameTextView);
        nameTextView.setText(fullName);
    }

    private void setupLocationTextView() {
        TextView locationTextView = findViewById(R.id.visitDetailsLocationTextView);
        locationTextView.setText("Location: " + this.location);
    }


    private void setupDateTextView() {
        TextView dateTextView = findViewById(R.id.visitDetailsDateTextView);
        dateTextView.setText("Date: " + this.formattedDate);
    }

    private void setupAdditionalInfoTextView(String additionalInfo) {
        TextView additionalInfoTextView = findViewById(R.id.visitDetailsAdditionalInfoTextView);
        additionalInfoTextView.setText(additionalInfo);
    }

    private void setupButtons() {
        setupBackButton();
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.visitDetailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
