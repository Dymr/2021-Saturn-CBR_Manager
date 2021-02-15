package com.example.cbr_manager.ui.visits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitDetailsActivity extends AppCompatActivity {


    private APIService apiService = APIService.getInstance();

    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        parentLayout = findViewById(android.R.id.content);

        Intent intent = getIntent();
        int clientId = intent.getIntExtra("clientId", -1);
        getClientInfo(clientId);

        setupButtons();
        setupTextViews();
        setupImageViews();

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
        ImageView displayPicture = findViewById(R.id.clientDetailsDisplayPictureImageView);
        displayPicture.setImageResource(R.drawable.client_details_placeholder2);
    }

    private void setupTextViews() {
        setupGenderTextView();
        setupLocationTextView();
        setupAgeTextView();
        setupDisabilityTextView();
        setupRiskLevelTextView();
        setupHealthTextView();
        setupEducationTextView();
        setupSocialTextView();
    }

    private void setupNameTextView(String fullName) {
        TextView nameTextView = findViewById(R.id.clientDetailsNameTextView);
        // TODO: Fill this TextView with information from the backend
        nameTextView.setText(fullName);
    }

    private void setupLocationTextView() {
        TextView locationTextView = findViewById(R.id.clientDetailsLocationTextView);
        locationTextView.setText("Location: BidiBidi Zone 1");
    }

    private void setupGenderTextView() {
        TextView genderTextView = findViewById(R.id.clientDetailsGenderTextView);
        genderTextView.setText("Gender: Male");
    }

    private void setupAgeTextView() {
        TextView ageTextView = findViewById(R.id.clientDetailsAgeTextView);
        ageTextView.setText("Age: 50");
    }

    private void setupDisabilityTextView() {
        TextView disabilityTextView = findViewById(R.id.clientDetailsDisabilityTextView);
        disabilityTextView.setText("Disability: Unable to walk");
    }

    private void setupRiskLevelTextView() {
        TextView riskLevelTextView = findViewById(R.id.clientDetailsRiskLevelTextView);
        riskLevelTextView.setText("Risk Level: Critical");
    }

    private void setupHealthTextView() {
        TextView healthTextView = findViewById(R.id.clientDetailsHealthTextView);
        healthTextView.setText("Health: Critical");
    }

    private void setupEducationTextView() {
        TextView educationTextView = findViewById(R.id.clientDetailsEducationTextView);
        educationTextView.setText("Education: Bachelors Degree");
    }

    private void setupSocialTextView() {
        TextView socialTextView = findViewById(R.id.clientDetailsSocialTextView);
        socialTextView.setText("Social: Very Active");
    }

    private void setupButtons() {
        setupBackButton();
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.clientDetailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}