package com.example.cbr_manager.ui.create_client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.client.Client;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import static com.example.cbr_manager.ui.create_client.ValidatorHelper.validateStepperTextViewNotNull;

public class GoalFragment extends Fragment implements Step {


    /*private EditText editTextEducationGoal, editTextSocialGoal, editTextHealthGoal;

    private Client client;*/

    private TextView errorTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_client_goal, container, false);

        /*client = ((CreateClientStepperActivity) getActivity()).formClientObj;

        editTextEducationGoal = setUpEditView(view, R.id.editTextEducationGoal);

        editTextSocialGoal = setUpEditView(view, R.id.editTextSocialGoal);

        editTextHealthGoal = setUpEditView(view, R.id.editTextHealthGoal);*/

        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        /*
        try {
            validateStepperTextViewNotNull(editTextEducationGoal, "Required");
            validateStepperTextViewNotNull(editTextSocialGoal, "Required");
            validateStepperTextViewNotNull(editTextHealthGoal, "Required");
        } catch (InvalidCreateClientFormException e) {
            errorTextView = e.view;
            return new VerificationError(e.getMessage());
        }

        updateClient();*/

        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        if (errorTextView != null) {
            errorTextView.setError(error.getErrorMessage());
        }
    }

    private EditText setUpEditView(View view, int editTextViewId) {
        return (EditText) view.findViewById(editTextViewId);
    }

    public void updateClient() {
        /*client.setEducationGoal(editTextEducationGoal.getText().toString().trim());
        client.setSocialGoal(editTextSocialGoal.getText().toString().trim());
        client.setHealthGoal(editTextHealthGoal.getText().toString().trim());*/
    }

}
