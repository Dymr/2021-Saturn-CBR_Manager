package com.example.cbr_manager.ui.clientdetails;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.goal.Goal;
import com.example.cbr_manager.ui.ClientViewModel;
import com.example.cbr_manager.ui.GoalViewModel;
import com.example.cbr_manager.ui.createreferral.CreateReferralFragment;
import com.example.cbr_manager.ui.createreferral.CreateReferralStepperActivity;
import com.example.cbr_manager.ui.createvisit.CreateVisitStepperActivity;
import com.example.cbr_manager.ui.goalhistory.GoalHistoryFragment;

import com.example.cbr_manager.ui.referral.referral_list.ReferralListFragment;
import com.example.cbr_manager.ui.visits.VisitsFragment;
import com.example.cbr_manager.utils.Helper;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ClientDetailsFragment extends Fragment {

    private APIService apiService = APIService.getInstance();
    private int clientId;
    private View parentLayout;
    private ClientViewModel clientViewModel;
    private GoalViewModel goalViewModel;

    public static String KEY_CLIENT_ID = "KEY_CLIENT_ID";
    public static String HEALTH = "health";
    public static String EDUCATION = "education";
    public static String SOCIAL = "social";

    public ClientDetailsFragment() {
        // Required empty public constructor
    }

    public static ClientDetailsFragment newInstance(int clientId) {
        ClientDetailsFragment fragment = new ClientDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_client_details, container, false);

        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        parentLayout = getActivity().findViewById(android.R.id.content);

        clientId = getArguments().getInt(KEY_CLIENT_ID, -1);
        getClientInfo(clientId);

        setupButtons(root);
        setupVectorImages(root);
        setupBottomNavigationView(root);
        setupCardView(root);
        setupTapTarget(root);

        return root;
    }

    private void setupTapTarget(View root) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!preferences.getBoolean("firstTimeClientDetailsRisk", false)) {
            TapTargetSequence clientDetailsTapSequence = new TapTargetSequence(getActivity()).targets(
                    TapTarget.forView(root.findViewById(R.id.clientDetailsRiskLevelTextView), "Client Risk.", "Prioritize clients by their determined risk level. This is calculated during the risk assessment of client creation.")
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(20)
                            .drawShadow(true)
                            .tintTarget(true)
                            .dimColor(R.color.black),
                    TapTarget.forView(root.findViewById(R.id.createReferralActivityClient), "Changes right at your fingertips.", "Create new visits and referrals here. Additionally, edit client personal info, risk, and goals directly.")
                            .transparentTarget(true)
                            .drawShadow(true)
                            .targetRadius(100)
                            .dimColor(R.color.black),
                    TapTarget.forView(root.findViewById(R.id.visitsFragment), "View previous interactions.", "View details of all previous visits and referrals with the current client here.")
                            .targetRadius(90)
                            .drawShadow(true)
                            .transparentTarget(true)
                            .dimColor(R.color.black)
            );
            clientDetailsTapSequence.start();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTimeClientDetailsRisk", true);
            editor.apply();
        }
    }

    private void setupToolBar() {
        setHasOptionsMenu(true);
    }

    private void setupCardView(View view) {
        CardView healthGoalCardView = view.findViewById(R.id.clientDetailsHealthGoalCardView);
        healthGoalCardView.setVisibility(View.GONE);
        CardView EducationGoalCardView = view.findViewById(R.id.clientDetailsEducationGoalCardView);
        EducationGoalCardView.setVisibility(View.GONE);
        CardView socialGoalCardView = view.findViewById(R.id.clientDetailsSocialGoalCardView);
        socialGoalCardView.setVisibility(View.GONE);
    }

    private void modifyCardView(int cardViewId, boolean hasGoal) {
        if(hasGoal) {
            CardView cardView = (CardView) getView().findViewById(cardViewId);
            cardView.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigationView(View root) {
        BottomNavigationView clientDetailsNavigationView = root.findViewById(R.id.clientDetailsBottomNavigationView);
        clientDetailsNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle arguments = new Bundle();
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.visitsFragment:
                        Bundle args = new Bundle();
                        args.putInt(VisitsFragment.KEY_CLIENT_ID, clientId);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(android.R.id.content, VisitsFragment.class, args)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.newVisitFragment:
                        Intent createVisitIntent = new Intent(getActivity(), CreateVisitStepperActivity.class);
                        createVisitIntent.putExtra("clientId", clientId);
                        startActivity(createVisitIntent);
                        break;
                    case R.id.createReferralActivityClient:
                        Intent referralIntent = new Intent(getContext(), CreateReferralStepperActivity.class);
                        referralIntent.putExtra("CLIENT_ID", clientId);
                        startActivity(referralIntent);
                        break;
                    case R.id.referralsFragment:
                        fragment = ReferralListFragment.newInstance(clientId);
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(android.R.id.content, fragment).addToBackStack(null).commit();
                        break;
                    case R.id.editClient:
                        Bundle bundle = new Bundle();
                        bundle.putInt("clientId", clientId);
                        ClientDetailsEditFragment clientDetailsEditFragment = new ClientDetailsEditFragment();
                        clientDetailsEditFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_client_details, clientDetailsEditFragment, null)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                return false;
            }
        });
    }


    private void setUpTextView(int textViewID, String textValue){
        TextView textView = (TextView)getView().findViewById(textViewID);
        textView.setText(textValue);
    }


    private void setupVectorImages(View root) {
    }

    private void getClientInfo(int clientId) {
        clientViewModel.getClient(clientId).observe(getViewLifecycleOwner(), client -> {
            setupNameTextView(client.getFullName());
            setupImageViews(client.getPhotoURL());

            setupLocationTextView(client.getLocation());
            setupAgeTextView(client.getAge().toString());
            setupGenderTextView(client.getGender());
            setupEducationRiskTextView(client.getEducationRisk().toString());
            setupSocialRiskTextView(client.getSocialRisk().toString());
            setupHealthRiskTextView(client.getHealthRisk().toString());
            setupDisabilityTextView(client.getDisability());
            setupRiskLevelTextView(client.assignRiskLabel().toString());
            setUpTextView(R.id.clientDetailsCBRClientIDTextView, client.getCbrClientId());
            setupGoals();
        });
    }

    private void setupImageViews(String imageURL) {
        ImageView displayPicture = (ImageView)getView().findViewById(R.id.clientDetailsDisplayPictureImageView);
        Helper.setImageViewFromURL(imageURL, displayPicture, R.drawable.client_details_placeholder);
    }

    private void setupNameTextView(String fullName) {
        setUpTextView(R.id.clientDetailsNameTextView, fullName);
    }

    private void setupLocationTextView(String location) {
        setUpTextView(R.id.clientDetailsLocationTextView, location);
    }

    private void setupGenderTextView(String gender) {
        setUpTextView(R.id.clientDetailsGenderTextView, gender);
    }

    private void setupAgeTextView(String age) {
        setUpTextView(R.id.clientDetailsAgeTextView, age);
    }

    private void setupDisabilityTextView(String disability) {
        setUpTextView(R.id.clientDetailsDisabilityTextView, disability);
    }

    private void setupRiskLevelTextView(String riskLevel) {
        setUpTextView(R.id.clientDetailsRiskLevelTextView, riskLevel);
    }

    private void setupHealthRiskTextView(String healthRisk) {
        setUpTextView(R.id.clientDetailsHealthRiskLevelTextView, healthRisk);
    }

    private void setupEducationRiskTextView(String educationRisk) {
        setUpTextView(R.id.clientDetailsEducationRiskLevelTextView, educationRisk);
    }
    private void setupSocialRiskTextView(String socialRisk) {
        setUpTextView(R.id.clientDetailsSocialRiskLevelTextView, socialRisk);
    }

    private void setupButtons(View root) {
        setupEditButton(root);
        setupBackButton(root);
        setupHistoryButton(root,R.id.clientDetailsEducationHistoryTextView,"education_goal", 101);
        setupHistoryButton(root,R.id.clientDetailsHealthHistoryTextView,"health_goal", 100);
        setupHistoryButton(root,R.id.clientDetailsSocialHistoryTextView,"social_goal", 102);
    }

    private void setupEditButton(View root) {

        ImageView editButtonImageView = root.findViewById(R.id.clientDetailsEditImageView);

        editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_client_details, ClientDetailsEditFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setupHistoryButton(View root, int ViewID, String field, int code){
        View someView = root.findViewById(ViewID);
        someView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalHistoryFragment goalHistoryFragment = GoalHistoryFragment.newInstance(code, clientId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_client_details, goalHistoryFragment,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setupBackButton(View root) {
        ImageView backImageView = root.findViewById(R.id.clientDetailsBackImageView);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public int getClientId() {
        return clientId;
    }

    private void setupGoals() {
        TextView healthTitle = getView().findViewById(R.id.clientDetailsHealthGoalTextView);
        TextView healthDescription = getView().findViewById(R.id.clientDetailsHealthDescriptionTextView);
        TextView healthStatus = getView().findViewById(R.id.clientDetailsHealthStatusTextView);
        TextView socialTitle = getView().findViewById(R.id.clientDetailsSocialGoalTextView);
        TextView socialDescription = getView().findViewById(R.id.clientDetailsSocialDescriptionTextView);
        TextView socialStatus = getView().findViewById(R.id.clientDetailsSocialStatusTextView);
        TextView educationTitle = getView().findViewById(R.id.clientDetailsEducationGoalTextView);
        TextView educationDescription = getView().findViewById(R.id.clientDetailsEducationDescriptionTextView);
        TextView educationStatus = getView().findViewById(R.id.clientDetailsEducationStatusTextView);

        goalViewModel.getAllGoals().observe(getViewLifecycleOwner(), goals -> {
            boolean hasHealthGoal = false;
            boolean hasEducationGoal = false;
            boolean hasSocialGoal = false;
            Collections.reverse(goals);
            for (Goal goal : goals) {
                if (goal.getClientId().equals(clientId)) {
                    if (goal.getCategory().toLowerCase().equals(HEALTH) && !hasHealthGoal) {
                        if (!goal.getTitle().isEmpty()) {
                            healthTitle.setText(goal.getTitle());
                        }

                        if (!goal.getDescription().isEmpty()) {
                            healthDescription.setText(goal.getDescription());
                        }
                        healthStatus.setText(goal.getStatus());
                        hasHealthGoal = true;
                    } else if (goal.getCategory().toLowerCase().equals(EDUCATION) && !hasEducationGoal) {
                        if (!goal.getTitle().isEmpty()) {
                            educationTitle.setText(goal.getTitle());
                        }

                        if (!goal.getDescription().isEmpty()) {
                            educationDescription.setText(goal.getDescription());
                        }
                        educationStatus.setText(goal.getStatus());
                        hasEducationGoal = true;
                    } else if (goal.getCategory().toLowerCase().equals(SOCIAL) && !hasSocialGoal) {
                        if (!goal.getTitle().isEmpty()) {
                            socialTitle.setText(goal.getTitle());
                        }

                        if (!goal.getDescription().isEmpty()) {
                            socialDescription.setText(goal.getDescription());
                        }
                        socialStatus.setText(goal.getStatus());
                        hasSocialGoal = true;
                    }
                }
            }
            modifyCardView(R.id.clientDetailsHealthGoalCardView, hasHealthGoal);
            modifyCardView(R.id.clientDetailsEducationGoalCardView, hasEducationGoal);
            modifyCardView(R.id.clientDetailsSocialGoalCardView, hasSocialGoal);
        });
    }

}