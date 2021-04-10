package com.example.cbr_manager.ui.clientdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.goal.Goal;
import com.example.cbr_manager.ui.ClientViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ClientDetailsEditFragment extends Fragment {

    private APIService apiService = APIService.getInstance();
    private View parentLayout;
    private Spinner genderSpinner;
    String gender="";
    Client localClient;
    private int clientId;
    private static final String[] paths = {"Male", "Female"};
    private ClientViewModel clientViewModel;
    private Goal healthGoal, educationGoal, socialGoal;
    private boolean hasHealthGoal = false, hasEducationGoal = false, hasSocialGoal = false;


    public ClientDetailsEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_client_details_edit, container, false);
        parentLayout = root.findViewById(android.R.id.content);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        Bundle bundle = this.getArguments();
        this.clientId = bundle.getInt("clientId", -1);
        this.localClient = new Client();

        setupGenderSpinner(root);
        setupClientEditTexts(clientId, root);
        setupCardView(root);
        getGoals();
        setupButtons(root);

        return root;
    }

    private void modifyClientInfo(Client client) {

        clientViewModel.modifyClient(client).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Snackbar.make(getView(), "Successfully updated client", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Snackbar.make(getView(), "Failed to update client", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getAndModifyClient(int clientId, View root) {


        EditText editClientName = (EditText) root.findViewById(R.id.clientDetailsEditName);
        EditText editClientAge = (EditText) root.findViewById(R.id.clientDetailsEditAge);
        EditText editClientLocation = (EditText) root.findViewById(R.id.clientDetailsEditLocation);
        EditText editClientDisability = (EditText) root.findViewById(R.id.clientDetailsEditDisability);

        EditText editClientEducationRisk = (EditText) root.findViewById(R.id.clientDetailsEditEducationRiskLevel);
        EditText editClientSocialRisk = (EditText) root.findViewById(R.id.clientDetailsEditSocialRiskLevel);
        EditText editClientHealthRisk = (EditText) root.findViewById(R.id.clientDetailsEditHealthRiskLevel);

        localClient.setGender(gender);
        String [] clientName = editClientName.getText().toString().split(" ");
        localClient.setFirstName(clientName[0]);
        localClient.setLastName(clientName[1]);
        localClient.setAge(Integer.parseInt(editClientAge.getText().toString()));
        localClient.setLocation(editClientLocation.getText().toString());
        localClient.setDisability(editClientDisability.getText().toString());
        localClient.setEducationRisk(Integer.parseInt((editClientEducationRisk.getText().toString())));
        localClient.setSocialRisk(Integer.parseInt(editClientSocialRisk.getText().toString()));
        localClient.setHealthRisk(Integer.parseInt(editClientHealthRisk.getText().toString()));
        modifyClientInfo(localClient);
    }

    private void setupGenderSpinner(View root) {
        String[] paths = {"Male", "Female"};
        Spinner spinner = (Spinner) root.findViewById(R.id.clientDetailsEditGenderSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = paths[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = paths[0];
            }
        });
    }

    private void setupClientEditTexts(int clientId, View root) {
        EditText editClientName = (EditText) root.findViewById(R.id.clientDetailsEditName);
        EditText editClientAge = (EditText) root.findViewById(R.id.clientDetailsEditAge);
        EditText editClientLocation = (EditText) root.findViewById(R.id.clientDetailsEditLocation);
        EditText editClientDisability = (EditText) root.findViewById(R.id.clientDetailsEditDisability);

        EditText editClientEducationRisk = (EditText) root.findViewById(R.id.clientDetailsEditEducationRiskLevel);
        EditText editClientSocialRisk = (EditText) root.findViewById(R.id.clientDetailsEditSocialRiskLevel);
        EditText editClientHealthRisk = (EditText) root.findViewById(R.id.clientDetailsEditHealthRiskLevel);

        clientViewModel.getClient(clientId).observe(getViewLifecycleOwner(), observeClient -> {
            this.localClient = observeClient;
            String clientFirstName = observeClient.getFirstName();
            String clientLastName = observeClient.getLastName();
            editClientName.setText(clientFirstName + " " + clientLastName);
            editClientAge.setText(observeClient.getAge().toString());
            editClientLocation.setText(observeClient.getLocation());
            editClientDisability.setText(observeClient.getDisability());
            editClientEducationRisk.setText(observeClient.getEducationRisk().toString());
            editClientSocialRisk.setText(observeClient.getSocialRisk().toString());
            editClientHealthRisk.setText(observeClient.getHealthRisk().toString());
        });
    }

    private void setupCardView(View view) {
        CardView healthGoalCardView = view.findViewById(R.id.clientDetailsEditHealthCardView);
        healthGoalCardView.setVisibility(View.GONE);
        CardView EducationGoalCardView = view.findViewById(R.id.clientDetailsEditEducationCardView);
        EducationGoalCardView.setVisibility(View.GONE);
        CardView socialGoalCardView = view.findViewById(R.id.clientDetailsEditSocialCardView);
        socialGoalCardView.setVisibility(View.GONE);
    }

    private void modifyCardView(int cardViewId, boolean hasGoal) {
        if(hasGoal) {
            CardView cardView = (CardView) getView().findViewById(cardViewId);
            cardView.setVisibility(View.VISIBLE);
        }
    }


    private void setupButtons(View root) {
        setupBackButton(root);
        setupSubmitButton(root);
    }

    private void setupSubmitButton(View root) {
        Intent intent = getActivity().getIntent();
        int clientId = this.clientId;

        Button submitButton = root.findViewById(R.id.clientDetailsEditSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGoalsInfo();
                if(hasHealthGoal) {
                    updateGoal(healthGoal);
                }
                if(hasEducationGoal) {
                    updateGoal(educationGoal);
                }
                if(hasSocialGoal) {
                    updateGoal(socialGoal);
                }
                getAndModifyClient(clientId, root);
            }
        });
    }

    private void setupBackButton(View root) {
        ImageView backButtonImageView = root.findViewById(R.id.clientDetailsEditBackImageView);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getGoals() {
        apiService.goalService.getGoals().enqueue(new Callback<List<Goal>>() {
            @Override
            public void onResponse(Call<List<Goal>> call, Response<List<Goal>> response) {
                List<Goal> goals = new ArrayList<>();
                goals = response.body();
                Collections.reverse(goals);
                for (Goal goal : goals) {
                    if (goal.getClientId().equals(clientId)) {
                        if (goal.getCategory().toLowerCase().equals("health") && !hasHealthGoal) {
                            healthGoal = goal;
                            setupGoalEditTexts(R.id.clientDetailsEditHealthTitleEditText, healthGoal.getTitle());
                            setupGoalEditTexts(R.id.clientDetailsEditHealthDescriptionEditText, healthGoal.getDescription());
                            setupGoalEditTexts(R.id.clientDetailsEditHealthStatusEditText, healthGoal.getStatus());
                            hasHealthGoal = true;
                        } else if (goal.getCategory().toLowerCase().equals("education") && !hasEducationGoal) {
                            educationGoal = goal;
                            setupGoalEditTexts(R.id.clientDetailsEditEducationTitleEditText, educationGoal.getTitle());
                            setupGoalEditTexts(R.id.clientDetailsEditEducationDescriptionEditText, educationGoal.getDescription());
                            setupGoalEditTexts(R.id.clientDetailsEditEducationStatusEditText, educationGoal.getStatus());
                            hasEducationGoal = true;
                        } else if (goal.getCategory().toLowerCase().equals("social") && !hasSocialGoal) {
                            socialGoal = goal;
                            setupGoalEditTexts(R.id.clientDetailsEditSocialTitleEditText, socialGoal.getTitle());
                            setupGoalEditTexts(R.id.clientDetailsEditSocialDescriptionEditText, socialGoal.getDescription());
                            setupGoalEditTexts(R.id.clientDetailsEditSocialStatusEditText, socialGoal.getStatus());
                            hasSocialGoal = true;
                        }
                    }
                    if(hasHealthGoal && hasEducationGoal && hasSocialGoal) {
                        break;
                    }
                }
                modifyCardView(R.id.clientDetailsEditHealthCardView, hasHealthGoal);
                modifyCardView(R.id.clientDetailsEditEducationCardView, hasEducationGoal);
                modifyCardView(R.id.clientDetailsEditSocialCardView, hasSocialGoal);
            }

            @Override
            public void onFailure(Call<List<Goal>> call, Throwable t) {

            }
        });
    }
    private void setupGoalEditTexts(int editTextId, String content) {
        EditText editText = (EditText) getView().findViewById(editTextId);
        editText.setText(content);
    }

    private void updateGoalsInfo() {
        if(hasHealthGoal) {
            healthGoal.setTitle(getStringDataFromEditText(R.id.clientDetailsEditHealthTitleEditText));
            healthGoal.setDescription(getStringDataFromEditText(R.id.clientDetailsEditHealthDescriptionEditText));
            healthGoal.setStatus(getStringDataFromEditText(R.id.clientDetailsEditHealthStatusEditText));
        }
        if(hasEducationGoal) {
            educationGoal.setTitle(getStringDataFromEditText(R.id.clientDetailsEditEducationTitleEditText));
            educationGoal.setDescription(getStringDataFromEditText(R.id.clientDetailsEditEducationDescriptionEditText));
            educationGoal.setStatus(getStringDataFromEditText(R.id.clientDetailsEditEducationStatusEditText));
        }
        if(hasSocialGoal) {
            socialGoal.setTitle(getStringDataFromEditText(R.id.clientDetailsEditSocialTitleEditText));
            socialGoal.setDescription(getStringDataFromEditText(R.id.clientDetailsEditSocialDescriptionEditText));
            socialGoal.setStatus(getStringDataFromEditText(R.id.clientDetailsEditSocialStatusEditText));
        }
    }

    private String getStringDataFromEditText(int editTextId) {
        EditText editText = (EditText) getView().findViewById(editTextId);
        return editText.getText().toString().trim();
    }

    private void updateGoal(Goal goal) {
        apiService.goalService.modifyGoal(goal).enqueue(new Callback<Goal>() {
            @Override
            public void onResponse(Call<Goal> call, Response<Goal> response) {
                if(response.isSuccessful()) {
                    Goal goal = response.body();
                }
                else {
                    Snackbar.make(getView(), "Failed to update goal", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Goal> call, Throwable t) {
                Snackbar.make(getView(), "Failed to update goal", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}