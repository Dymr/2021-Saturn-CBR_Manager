package com.example.cbr_manager.ui.clientlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientListFragment extends Fragment implements ClientListRecyclerItemAdapter.OnItemListener{

    private ClientListViewModel clientListViewModel;
    private RecyclerView mRecyclerView;
    private ClientListRecyclerItemAdapter adapter; // TODO
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;
    ArrayList<ClientListRecyclerItem> clientRecyclerItems = new ArrayList<>();

    private APIService apiService = APIService.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clientListViewModel =
                new ViewModelProvider(this).get(ClientListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_client_list, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true); // if we know it won't change size.
        mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new ClientListRecyclerItemAdapter(clientRecyclerItems, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        fetchClientsToList(clientRecyclerItems);

        searchView = root.findViewById(R.id.clientSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return root;
    }

    public void fetchClientsToList(List<ClientListRecyclerItem> clientUIList) {
        if (apiService.isAuthenticated()) {
            apiService.clientService.getClients().enqueue(new Callback<List<Client>>() {
                @Override
                public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                    if (response.isSuccessful()) {
                        List<Client> clientList = response.body();
                        for (Client client : clientList) {
                            Integer riskScore = client.getRiskScore();
                            String stringRiskScore = riskScore.toString();
                            clientUIList.add(new ClientListRecyclerItem(R.drawable.client_details_placeholder, client.getFullName(), client.getLocation(), client, stringRiskScore));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Client>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onItemClick(int position) {

        Intent clientInfoIntent = new Intent(getContext(), ClientDetailsActivity.class);

        ClientListRecyclerItem clientListRecyclerItem = clientRecyclerItems.get(position);
        clientInfoIntent.putExtra("clientId", clientListRecyclerItem.getClient().getId());

        startActivity(clientInfoIntent);
    }
}