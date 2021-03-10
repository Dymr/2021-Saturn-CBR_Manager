package com.example.cbr_manager.ui.clientdetails;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.createreferral.CreateReferralActivity;
import com.example.cbr_manager.ui.createvisit.CreateVisitActivity;

import com.example.cbr_manager.ui.referral.referral_list.ReferralListFragment;
import com.example.cbr_manager.ui.visits.VisitsFragment;
import com.example.cbr_manager.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private APIService apiService = APIService.getInstance();
    private int clientId = -1;
    private View parentLayout;

    public ClientDetailsFragment() {
        // Required empty public constructor
    }

    public static ClientDetailsFragment newInstance(String param1, String param2) {
        ClientDetailsFragment fragment = new ClientDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_client_details, container, false);

        parentLayout = root.findViewById(android.R.id.content);

        Intent intent = getActivity().getIntent();
        int clientId = intent.getIntExtra("clientId", -1);
        getClientInfo(clientId);

        this.clientId = clientId;

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

                switch(item.getItemId()) {
                    case R.id.visitsFragment:
                        
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(android.R.id.content, new VisitsFragment())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.newVisitFragment:
                        Intent createVisitIntent = new Intent(getActivity(), CreateVisitActivity.class);
                        createVisitIntent.putExtra("clientId", clientId);
                        startActivity(createVisitIntent);
                        break;
                    case R.id.createReferralActivityClient:
                        // TODO: Navigate to create referral fragment instead of activity
                        Intent createReferralIntent = new Intent(getActivity(), CreateReferralActivity.class);
                        createReferralIntent.putExtra("CLIENT_ID", clientId);
                        startActivity(createReferralIntent);
                    case R.id.referralsFragment:
                        Bundle arguments = new Bundle();
                        arguments.putInt("CLIENT_ID", clientId);
                        ReferralListFragment fragment = new ReferralListFragment();
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(android.R.id.content, fragment).addToBackStack(null).commit();
                        break;
                    case R.id.editClient:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_client_details, ClientDetailsEditFragment.class, null)
                                .addToBackStack(null)
                                .commit();
                }
                return false;
            }
        });
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
                    setupHealthTextView(client.getHealthGoal());
                    setupSocialTextView(client.getSocialGoal());
                    setupEducationTextView(client.getEducationGoal());
                    setupEducationRiskTextView(client.getEducationRisk().toString());
                    setupSocialRiskTextView(client.getSocialRisk().toString());
                    setupHealthRiskTextView(client.getHealthRisk().toString());
                    setupDisabilityTextView(client.getDisability());
                    setupRiskLevelTextView(client.getRiskScore().toString());
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

    private void setUpTextView(int textViewId, String text) {
        TextView textView = getView().findViewById(textViewId);
        textView.setText(text);
    }

    private void setupButtons(View root) {
        setupEditButton(root);
        setupBackButton(root);
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