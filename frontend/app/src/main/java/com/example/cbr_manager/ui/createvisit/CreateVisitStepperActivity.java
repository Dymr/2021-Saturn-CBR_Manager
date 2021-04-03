package com.example.cbr_manager.ui.createvisit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.ui.ClientViewModel;
import com.example.cbr_manager.ui.VisitViewModel;
import com.example.cbr_manager.ui.stepper.GenericStepperAdapter;
import com.example.cbr_manager.ui.visitdetails.VisitDetailsActivity;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CreateVisitStepperActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    private StepperLayout createVisitStepperLayout;
    public int clientId = -1;
    public int userCreatorId = -1;
    public int visitId;
    public Visit formVisitObj;
    private APIService apiService = APIService.getInstance();
    private Client client;

    private static final String TAG = "CreateVisitStepperActivity";

    private VisitViewModel visitViewModel;
    private ClientViewModel clientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stepper);

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        setTitle("Create Visit");
        Intent intent = getIntent();
        clientId = intent.getIntExtra("clientId", -1);
        formVisitObj = new Visit();
        createVisitStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupStepperAdapterWithFragments();
    }

    private void setupStepperAdapterWithFragments() {
        GenericStepperAdapter createVisitStepperAdapter = new GenericStepperAdapter(getSupportFragmentManager(), this);
        createVisitStepperAdapter.addFragment(new CreateVisitPurposeFragment(), "Purpose");
        createVisitStepperAdapter.addFragment(new CreateVisitLocationFragment(), "Location");
        createVisitStepperAdapter.addFragment(new CreateVisitHealthFragment(), "Health");
        createVisitStepperAdapter.addFragment(new CreateVisitEducationFragment(), "Education");
        createVisitStepperAdapter.addFragment(new CreateVisitSocialFragment(), "Social");

        createVisitStepperLayout.setAdapter(createVisitStepperAdapter);
        createVisitStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {

        clientViewModel.getClient(clientId).subscribe(new DisposableSingleObserver<Client>() {
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Client client) {
                formVisitObj.setClientId(clientId);
                formVisitObj.setClient(client);
                visitViewModel.createVisit(formVisitObj).subscribe(new DisposableSingleObserver<Visit>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Visit visit) {
                        visitId = visit.getId();
                        Log.d(TAG, "Visit creation success: " + visitId);
                        Toast.makeText(CreateVisitStepperActivity.this, "Successfully created visit!", Toast.LENGTH_SHORT).show();
                        onSubmitSuccess();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(CreateVisitStepperActivity.this, "Response error creating visit. " + e.getMessage() , Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Toast.makeText(CreateVisitStepperActivity.this, "Response error finding client.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSubmitSuccess() {
        Intent intent = new Intent(this, VisitDetailsActivity.class);
        intent.putExtra(VisitDetailsActivity.KEY_VISIT_ID, visitId);
        startActivity(intent);
        finish();
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
}