package com.example.cbr_manager.ui.create_client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsActivity;
import com.example.cbr_manager.ui.stepper.GenericStepperAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClientStepperActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    public Client formClientObj;
    public File photoFile;
    private StepperLayout CreateClientStepperLayout;
    private APIService apiService = APIService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stepper);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        formClientObj = new Client();

        setTitle("Create a Client");

        CreateClientStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        CreateClientStepperLayout.setAdapter(setUpStepperAdapterWithFragments());
        CreateClientStepperLayout.setListener(this);
    }

    public GenericStepperAdapter setUpStepperAdapterWithFragments() {
        // order of these fragments added matters
        GenericStepperAdapter createClientStepperAdapter = new GenericStepperAdapter(getSupportFragmentManager(), this);

        createClientStepperAdapter.addFragment(new ConsentFragment(), "Consent");
        createClientStepperAdapter.addFragment(new VillageInfoFragment(), "Village Info");
        createClientStepperAdapter.addFragment(new PersonalInfoFragment(), "Personal Info");
        createClientStepperAdapter.addFragment(new DisabilityFragment(), "Disability Info");
        createClientStepperAdapter.addFragment(new GoalFragment(), "Goal Info");
        createClientStepperAdapter.addFragment(new CaregiverInfoFragment(), "Caregiver Info");
        createClientStepperAdapter.addFragment(new PhotoFragment(), "Photo");

        return createClientStepperAdapter;
    }

    private void onSubmitSuccess(Client client) {
        Intent intent = new Intent(this, ClientDetailsActivity.class);
        intent.putExtra(ClientDetailsActivity.KEY_CLIENT_ID, client.getId());
        startActivity(intent);
        this.finish();
    }

    private void submitSurvey() {
        Call<Client> call = apiService.clientService.createClientManual(formClientObj);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {

                    Client client = response.body();

                    if (photoFile.exists()) {
                        Call<ResponseBody> photoCall = apiService.clientService.uploadClientPhoto(photoFile, client.getId());
                        photoCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                    onSubmitSuccess(client);
                } else {
                    Snackbar.make( CreateClientStepperLayout, "Failed to create the client.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Snackbar.make( CreateClientStepperLayout, "Failed to create the client. Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Leave page");
        alertDialogBuilder.setMessage("Are you sure you want to leave? Changes will not be saved.");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onCompleted(View completeButton) {
        submitSurvey();
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    public void setPhotoFile(File file) {
        photoFile = file;
    }
}