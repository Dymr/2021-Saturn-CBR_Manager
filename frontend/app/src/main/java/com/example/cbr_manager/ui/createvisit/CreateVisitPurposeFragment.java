package com.example.cbr_manager.ui.createvisit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.user.User;
import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.ui.AuthViewModel;
import com.example.cbr_manager.ui.ClientViewModel;
import com.google.android.material.chip.Chip;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.observers.DisposableSingleObserver;

@AndroidEntryPoint
public class CreateVisitPurposeFragment extends Fragment implements Step {

    EditText clientNameEditText;
    EditText cbrWorkerName;
    EditText dateEditText;
    Chip cbrChip;
    Chip referralChip;
    Chip followUpChip;
    Chip healthChip;
    Chip educationChip;
    Chip socialChip;
    private View view;
    private AuthViewModel authViewModel;
    private ClientViewModel clientViewModel;
    private int clientId = -1;
    private int userId = -1;
    private Visit visit;

    public CreateVisitPurposeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_visit_purpose, container, false);
        visit = ((CreateVisitStepperActivity) getActivity()).formVisitObj;
        clientId = ((CreateVisitStepperActivity) getActivity()).clientId;
        initializeChips(view);
        setupAutoFilledTextViews(view);
        return view;
    }

    private void initializeChips(View view) {
        cbrChip = view.findViewById(R.id.cbrChip);
        referralChip = view.findViewById(R.id.purposeReferralChip);
        followUpChip = view.findViewById(R.id.purposeFollowUpChip);
        healthChip = view.findViewById(R.id.healthProvisionChip);
        educationChip = view.findViewById(R.id.educationProvisionChip);
        socialChip = view.findViewById(R.id.socialProvisionChip);
    }

    private void setupAutoFilledTextViews(View view) {
        clientNameEditText = view.findViewById(R.id.fragmentPreambleClientEditText);
        cbrWorkerName = view.findViewById(R.id.fragmentPreambleCBRNameEditText);
        dateEditText = view.findViewById(R.id.fragmentPreambleEditTextDate);

        clientViewModel.getClient(clientId).subscribe(new DisposableSingleObserver<Client>() {
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Client client) {
                clientNameEditText.setText(client.getFullName());
                clientNameEditText.setEnabled(false);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });

        authViewModel.getUser().subscribe(new DisposableSingleObserver<User>() {
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull User user) {
                userId = user.getId();
                visit.setUserId(userId);
                visit.setCbrWorkerName(user.getUsername());
                cbrWorkerName.setText(user.getUsername());
                cbrWorkerName.setEnabled(false);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Toast.makeText(getContext(), "User response error. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = format.format(today);
        dateEditText.setText(todayString);
        dateEditText.setEnabled(false);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        updateCreateVisit();
        return null;
    }

    private void updateCreateVisit() {
        visit.setCBRPurpose(cbrChip.isChecked());
        visit.setDisabilityReferralPurpose(referralChip.isChecked());
        visit.setDisabilityFollowUpPurpose(followUpChip.isChecked());
        visit.setHealthProvision(healthChip.isChecked());
        visit.setEducationProvision(educationChip.isChecked());
        visit.setSocialProvision(socialChip.isChecked());
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}