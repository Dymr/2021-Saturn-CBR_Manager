package com.example.cbr_manager.ui.clientselector;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.ClientViewModel;
import com.example.cbr_manager.ui.baselinesurvey.BaselineSurveyStepperActivity;
import com.example.cbr_manager.ui.clientlist.ClientListRecyclerItemAdapter;
import com.example.cbr_manager.ui.createreferral.CreateReferralStepperActivity;
import com.example.cbr_manager.ui.createvisit.CreateVisitStepperActivity;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

@AndroidEntryPoint
public class ClientSelectorFragment extends Fragment implements ClientListRecyclerItemAdapter.OnItemListener {

    private static final String TAG = ClientSelectorFragment.class.getName();
    private final int NEW_VISIT_CODE = 100;
    private final int NEW_REFERRAL_CODE = 101;
    private final int NEW_BASELINE_CODE = 102;
    List<Client> clientList = new ArrayList<>();
    private RecyclerView clientListRecyclerView;
    private ClientListRecyclerItemAdapter clientListAdapter;
    private RecyclerView.LayoutManager clientSelectorLayoutManager;
    private ClientViewModel clientViewModel;

    public ClientSelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_client_selector, container, false);

        clientListRecyclerView = root.findViewById(R.id.clientSelectorRecyclerView);
        clientSelectorLayoutManager = new LinearLayoutManager(getContext());
        clientListAdapter = new ClientListRecyclerItemAdapter(clientList, this);
        clientListRecyclerView.setLayoutManager(clientSelectorLayoutManager);
        clientListRecyclerView.setAdapter(clientListAdapter);

        fetchClientsToList(clientList);

        SearchView search = root.findViewById(R.id.clientSelectorSearchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                clientListAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return root;
    }

    private void fetchClientsToList(List<Client> clientList) {
        clientViewModel.getAllClients().subscribe(new DisposableObserver<Client>() {
            @Override
            public void onNext(@NonNull Client client) {
                clientList.add(client);
                Log.d(TAG, "onNext: ");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
                clientListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        int code = ((ClientSelectorActivity) getActivity()).getCode();
        Client client = clientListAdapter.getClient(position);
        int clientId = client.getId();

        if (code == NEW_REFERRAL_CODE) {
            Intent referralIntent = new Intent(getContext(), CreateReferralStepperActivity.class);
            referralIntent.putExtra("CLIENT_ID", clientId);
            startActivity(referralIntent);
            getActivity().finish();
        } else if (code == NEW_VISIT_CODE) {
            Intent visitsIntent = new Intent(getContext(), CreateVisitStepperActivity.class);
            visitsIntent.putExtra("clientId", clientId);
            startActivity(visitsIntent);
            getActivity().finish();
        } else if (code == NEW_BASELINE_CODE) {
            Intent baselineIntent = new Intent(getContext(), BaselineSurveyStepperActivity.class);
            baselineIntent.putExtra("CLIENT_ID", clientId);
            startActivity(baselineIntent);
            getActivity().finish();
        }
    }
}