package com.example.cbr_manager.ui.clientdetails;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.client_history.ClientHistoryFragment;
import com.example.cbr_manager.ui.createreferral.CreateReferralActivity;
import com.example.cbr_manager.ui.createvisit.CreateVisitStepperActivity;
import com.example.cbr_manager.ui.referral.referral_list.ReferralListFragment;
import com.example.cbr_manager.ui.visits.VisitsFragment;
import com.example.cbr_manager.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDetailsFragment extends Fragment {

    private APIService apiService = APIService.getInstance();
    private int clientId;
    private View parentLayout;

    public static String KEY_CLIENT_ID = "KEY_CLIENT_ID";

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

        parentLayout = getActivity().findViewById(android.R.id.content);

        clientId = getArguments().getInt(KEY_CLIENT_ID, -1);
        getClientInfo(clientId);

        setupButtons(root);
        setupVectorImages(root);
        setupBottomNavigationView(root);

        return root;
    }

    private void setupToolBar() {
        setHasOptionsMenu(true);
    }

    private void setupBottomNavigationView(View root) {
        BottomNavigationView clientDetailsNavigationView = root.findViewById(R.id.clientDetailsBottomNavigationView);
        clientDetailsNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                        // TODO: Navigate to create referral fragment instead of activity
                        Intent createReferralIntent = new Intent(getActivity(), CreateReferralActivity.class);
                        createReferralIntent.putExtra("CLIENT_ID", clientId);
                        startActivity(createReferralIntent);
                        break;
                    case R.id.referralsFragment:
                        Bundle arguments = new Bundle();
                        arguments.putInt("CLIENT_ID", clientId);
                        ReferralListFragment fragment = new ReferralListFragment();
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
        apiService.clientService.getClient(clientId).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {

                if (response.isSuccessful()) {
                    Client client = response.body();

                    // Todo: dynamically set the client info here
                    setupNameTextView(client.getFullName());
                    setupImageViews(client.getPhotoURL());

                    setupLocationTextView(client.getLocation());
                    setupAgeTextView(client.getAge().toString());
                    setupGenderTextView(client.getGender());
                    setupHealthTextView(client.getGoalMetHealthProvision());
                    setupSocialTextView(client.getGoalMetSocialProvision());
                    setupEducationTextView(client.getGoalMetEducationProvision());
                    setupEducationRiskTextView(client.getEducationRisk().toString());
                    setupSocialRiskTextView(client.getSocialRisk().toString());
                    setupHealthRiskTextView(client.getHealthRisk().toString());
                    setupDisabilityTextView(client.getDisability());
                    setupRiskLevelTextView(client.getRiskScore().toString());
                    setUpTextView(R.id.clientDetailsCBRClientIDTextView, client.getCbrClientId());
                } else {
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

    private void setupHealthTextView(String health) {
        setUpTextView(R.id.clientDetailsHealthGoalTextView, health);
    }

    private void setupEducationTextView(String education) {
        setUpTextView(R.id.clientDetailsEducationGoalTextView, education);
    }

    private void setupSocialTextView(String social) {
        setUpTextView(R.id.clientDetailsSocialGoalTextView, social);
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
        setupHistoryButton(root,R.id.clientDetailsEducationHistoryTextView,"education_goal");
        setupHistoryButton(root,R.id.clientDetailsHealthHistoryTextView,"health_goal");
        setupHistoryButton(root,R.id.clientDetailsSocialHistoryTextView,"social_goal");
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

    private void setupHistoryButton(View root, int ViewID, String field){
        View someView = root.findViewById(ViewID);
        someView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("clientId", clientId);
                bundle.putString("field",field);
                ClientHistoryFragment clientHistoryFragment = new ClientHistoryFragment();
                clientHistoryFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_client_details, clientHistoryFragment,null)
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

}