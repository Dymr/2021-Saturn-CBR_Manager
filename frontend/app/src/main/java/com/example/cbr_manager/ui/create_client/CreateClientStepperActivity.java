package com.example.cbr_manager.ui.create_client;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.goal.Goal;
import com.example.cbr_manager.ui.ClientViewModel;
import com.example.cbr_manager.ui.GoalViewModel;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsActivity;
import com.example.cbr_manager.ui.stepper.GenericStepperAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;


@AndroidEntryPoint
public class CreateClientStepperActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    public Client formClientObj;
    public File photoFile;
    public Goal healthGoal, educationGoal, socialGoal;
    private StepperLayout CreateClientStepperLayout;
    private APIService apiService = APIService.getInstance();
    private static final String TAG = "CreateClientActivity";
    private View view;

    private ClientViewModel clientViewModel;
    private GoalViewModel goalViewModel;

    // Provides the entry point to the Fused Location Provider API
    private FusedLocationProviderClient fusedLocationClient;

    protected Location lastLocation;
    private int LOCATION_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stepper);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        formClientObj = new Client();
        healthGoal = new Goal();
        educationGoal = new Goal();
        socialGoal = new Goal();
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);


        setTitle("Create a Client");

        requestLocationPermissions();

        CreateClientStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        CreateClientStepperLayout.setAdapter(setUpStepperAdapterWithFragments());
        CreateClientStepperLayout.setListener(this);
    }

    public GenericStepperAdapter setUpStepperAdapterWithFragments() {
        // order of these fragments added matters
        GenericStepperAdapter createClientStepperAdapter = new GenericStepperAdapter(getSupportFragmentManager(), this);

        createClientStepperAdapter.addFragment(new ConsentFragment(), "Consent");
        createClientStepperAdapter.addFragment(new PersonalInfoFragment(), "Personal Info");
        createClientStepperAdapter.addFragment(new DisabilityFragment(), "Disability Info");
        createClientStepperAdapter.addFragment(new CaregiverInfoFragment(), "Caregiver Info");
        createClientStepperAdapter.addFragment(new HealthRiskFragment(), "Health Risk");
        createClientStepperAdapter.addFragment(new EducationRiskFragment(), "Education Risk");
        createClientStepperAdapter.addFragment(new SocialRiskFragment(), "Social Risk");
        createClientStepperAdapter.addFragment(new GoalFragment(), "Goals");
        return createClientStepperAdapter;
    }

    private void onSubmitSuccess(Client client) {

        Intent intent = new Intent(this, ClientDetailsActivity.class);
        intent.putExtra(ClientDetailsActivity.KEY_CLIENT_ID, client.getId());
        startActivity(intent);
        this.finish();
    }

    private void submitGoalSurvey(Goal goal) {
        goalViewModel.createGoal(goal).subscribe(new DisposableSingleObserver<Goal>() {
        
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Goal goal) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Log.d(TAG, "onError: ");
            }
        });
    }

    private void submitSurvey() {

        clientViewModel.createClient(formClientObj).subscribe(new DisposableSingleObserver<Client>() {
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Client client) {
                Log.d(TAG, "onSuccess Client created: " + client.getId());
                Log.d("Testing", client.getBirthdate());
                if(photoFile.exists()) {
                    clientViewModel.uploadphoto(photoFile, client).subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onSuccess Client photo uploaded: " + client.getId());
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                        }
                    });
                }
                if(healthGoal.getStatus().equalsIgnoreCase("Ongoing")) {
                    healthGoal.setClientId(client.getId());
                    submitGoalSurvey(healthGoal);
                }
                if(educationGoal.getStatus().equalsIgnoreCase("Ongoing")) {
                    educationGoal.setClientId(client.getId());
                    submitGoalSurvey(educationGoal);
                }
                if(socialGoal.getStatus().equalsIgnoreCase("Ongoing")) {
                    socialGoal.setClientId(client.getId());
                    submitGoalSurvey(socialGoal);
                }

                onSubmitSuccess(client);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Snackbar.make( CreateClientStepperLayout, "Failed to create the client.", Snackbar.LENGTH_LONG)
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



    private void requestLocationPermissions() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLastLocation();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location
                            if ( location != null ) {
                                lastLocation = location;
                                formClientObj.setLatitude(lastLocation.getLatitude());
                                formClientObj.setLongitude(lastLocation.getLongitude());
                            }
                        }
                    });

        }
        else {
            Toast.makeText(CreateClientStepperActivity.this, "You have to allow location permissions in order to associate a location with this client!", Toast.LENGTH_LONG).show();
        }
    }

}