package com.example.cbr_manager.ui.clientlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.ClientViewModel;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsActivity;
import com.example.cbr_manager.ui.create_client.CreateClientStepperActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClientListFragment extends Fragment implements ClientListItemAdapter.OnItemClickListener {

    private static final String[] locationPaths = {"Any", "BidiBidi Zone 1", "BidiBidi Zone 2", "BidiBidi Zone 3", "BidiBidi Zone 4", "BidiBidi Zone 5",
            "Palorinya Basecamp", "Palorinya Zone 1", "Palorinya Zone 2", "Palorinya Zone 3"};
    private static final String[] disabilityPaths = {"Any", "Amputee", "Polio", "Spinal Cord Injury", "Cerebral Palsy", "Spina Bifada",
            "Hydrocephalus", "Visual Impairment", "Hearing Impairment", "Don't Know", "Other"};
    private static final String[] genderPaths = {"Any", "Male", "Female"};
    private RecyclerView clientListRecyclerView;
    private ClientListItemAdapter clientListAdapter;
    private RecyclerView.LayoutManager clientListLayoutManager;
    private ClientViewModel clientViewModel;
    private Spinner genderSpinner;
    private Spinner locationSpinner;
    private Spinner disabilitySpinner;
    private String genderTag = "";
    private String locationTag = "";
    private String disabilityTag = "";
    private SearchView clientSearch;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setUpToolBar();

        View root = inflater.inflate(R.layout.fragment_client_list, container, false);

        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        clientListRecyclerView = root.findViewById(R.id.recyclerView);
        clientListRecyclerView.setHasFixedSize(true); // if we know it won't change size.
        clientListLayoutManager = new LinearLayoutManager(getContext());
        clientListAdapter = new ClientListItemAdapter(this);
        clientListRecyclerView.setLayoutManager(clientListLayoutManager);
        clientListRecyclerView.setAdapter(clientListAdapter);

        genderSpinner = setUpSpinner(root, R.id.gender_dropdown, genderPaths);
        locationSpinner = setUpSpinner(root, R.id.location_dropdown, locationPaths);
        disabilitySpinner = setUpSpinner(root, R.id.disability_dropdown, disabilityPaths);

        setUpSpinnerListener(genderSpinner);
        setUpSpinnerListener(locationSpinner);
        setUpSpinnerListener(disabilitySpinner);

        fetchClientsToList();

        clientSearch = root.findViewById(R.id.clientSearchView);
        clientSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                clientListAdapter.getFilterWithTags(genderTag, disabilityTag, locationTag).filter(newText);
                return true;
            }
        });
        return root;
    }

    private Spinner setUpSpinner(View view, int spinnerId, String[] options) {
        Spinner spinner = (Spinner) view.findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, options);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    private void setUpSpinnerListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int pos, long unused) {
                String tag = spinner.getItemAtPosition(pos).toString().toLowerCase().trim();
                if (spinner == genderSpinner) {
                    genderTag = tag;
                } else if (spinner == disabilitySpinner) {
                    disabilityTag = tag;
                } else if (spinner == locationSpinner) {
                    locationTag = tag;
                }
                CharSequence newText = clientSearch.getQuery();
                clientListAdapter.getFilterWithTags(genderTag, disabilityTag, locationTag).filter(newText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> spinner) {
            }
        });
    }

    public void setUpToolBar() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.client_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_client) {
            launchCreateClientActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchCreateClientActivity() {
        Intent createClientIntent = new Intent(getActivity(), CreateClientStepperActivity.class);
        startActivity(createClientIntent);
    }

    public void fetchClientsToList() {
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            clientListAdapter.setClients(clients);
        });
    }

    @Override
    public void onItemClick(int position) {

        Intent clientInfoIntent = new Intent(getContext(), ClientDetailsActivity.class);

        Client client = clientListAdapter.getClient(position);
        clientInfoIntent.putExtra(ClientDetailsActivity.KEY_CLIENT_ID, client.getId());

        startActivity(clientInfoIntent);
    }

}