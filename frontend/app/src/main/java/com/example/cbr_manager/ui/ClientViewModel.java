package com.example.cbr_manager.ui;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.cbr_manager.repository.ClientRepository;
import com.example.cbr_manager.service.client.Client;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;

@HiltViewModel
public class ClientViewModel extends ViewModel {
    private final ClientRepository clientRepository;

    private final SavedStateHandle savedStateHandle;

    @Inject
    public ClientViewModel(SavedStateHandle savedStateHandle, ClientRepository clientRepository) {
        this.savedStateHandle = savedStateHandle;
        this.clientRepository = clientRepository;
    }

    public Observable<Client> getAllClients() {
        return this.clientRepository.getAllClient();
    }

    public Single<Client> insert(Client client) {
        return this.clientRepository.insert(client);
    }

    public Single<Client> update(Client client) {
        return this.clientRepository.update(client);
    }

    public Observable<List<Client>> sync() {
        return clientRepository.sync();
    }


}
