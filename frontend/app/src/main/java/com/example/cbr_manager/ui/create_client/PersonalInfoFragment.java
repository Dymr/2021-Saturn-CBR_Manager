package com.example.cbr_manager.ui.create_client;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.client.Client;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;

import static com.example.cbr_manager.ui.create_client.ValidatorHelper.validateStepperTextViewNotNull;

public class PersonalInfoFragment extends Fragment implements Step {
    private static final String[] paths = {"Male", "Female"};

    private Spinner genderSpinner;

    private EditText editTextFirstName, editTextLastName, editTextBirthdate, editTextContactNumber;

    private Client client;

    private TextView errorTextView;

    private static final String[] locationPaths = {"BidiBidi Zone 1", "BidiBidi Zone 2", "BidiBidi Zone 3", "BidiBidi Zone 4", "BidiBidi Zone 5",
            "Palorinya Basecamp", "Palorinya Zone 1", "Palorinya Zone 2", "Palorinya Zone 3"};
    private Spinner locationSpinner;
    private EditText editTextVillageNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_client_personal_info, container, false);

        client = ((CreateClientStepperActivity) getActivity()).formClientObj;

        editTextFirstName = setUpEditView(view, R.id.editTextFirstName);

        editTextLastName = setUpEditView(view, R.id.editTextLastName);

        editTextBirthdate = setUpEditView(view, R.id.editTextBirthdate);

        editTextContactNumber = setUpEditView(view, R.id.editTextContactNumber);

        genderSpinner = setUpSpinner(view, R.id.gender_dropdown, paths);

        setUpLocationSpinner(view);
        setupDatePicker();

        editTextVillageNum = (EditText) view.findViewById(R.id.editTextVillageNum);

        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        try {
            validateStepperTextViewNotNull(editTextVillageNum, "Required");
            validateStepperTextViewNotNull(editTextFirstName, "Required");
            validateStepperTextViewNotNull(editTextLastName, "Required");
            validateStepperTextViewNotNull(editTextBirthdate, "Required");
            validateStepperTextViewNotNull(editTextContactNumber, "Required");
        } catch (InvalidCreateClientFormException e) {
            errorTextView = e.view;
            return new VerificationError(e.getMessage());
        }

        updateClient();

        return null;
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        editTextBirthdate.setText(year + "/" + (month + 1) + "/" + day);
        editTextBirthdate.setFocusable(false);
        editTextBirthdate.setClickable(false);
        editTextBirthdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextBirthdate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
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

    private Spinner setUpSpinner(View view, int spinnerId, String[] options) {
        Spinner spinner = (Spinner) view.findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, options);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    private EditText setUpEditView(View view, int editTextViewId) {
        return (EditText) view.findViewById(editTextViewId);
    }

    public void updateClient() {
        client.setFirstName(editTextFirstName.getText().toString().trim());
        client.setLastName(editTextLastName.getText().toString().trim());
        client.setBirthdate(editTextBirthdate.getText().toString().trim());
        client.setContactClient(editTextContactNumber.getText().toString().trim());

        client.setGender(genderSpinner.getSelectedItem().toString().trim());

        client.setVillageNo(Integer.parseInt(editTextVillageNum.getText().toString()));
        client.setLocation(locationSpinner.getSelectedItem().toString());
    }

    private void setUpLocationSpinner(View view) {
        locationSpinner = (Spinner) view.findViewById(R.id.location_dropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, locationPaths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

}
