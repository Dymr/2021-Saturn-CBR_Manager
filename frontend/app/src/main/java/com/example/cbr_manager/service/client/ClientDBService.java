package com.example.cbr_manager.service.client;

import android.content.Context;

import com.example.cbr_manager.data.storage.RoomDB;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Deprecated
public class ClientDBService {
    private ClientDao clientDao;
    private static ClientDBService Instance;

    public static ClientDBService getInstance(Context context){
        if(Instance == null){
            Instance = new ClientDBService(context);
        }
        return (Instance);
    }

    private ClientDBService(Context context){
        clientDao = RoomDB.getDatabase(context).clientDao();
    }

    public void insert(Client client){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = () -> {
            clientDao.insert(client);
            return 0;
        };
        Future<Integer> future = executor.submit(callable);

        executor.shutdown();
    }

    public void delete(Client client){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = () -> {
            clientDao.delete(client);
            return 0;
        };
        Future<Integer> future = executor.submit(callable);

        executor.shutdown();
    }

    public void update(Client client){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = () -> {
            clientDao.update(client);
            return 0;
        };
        Future<Integer> future = executor.submit(callable);

        executor.shutdown();
    }

    public List<Client> readAll() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<Client>> callable = () -> {
            List<Client> clients;
//            clients = clientDao.getClients();
//            return clients;
            return null;
        };
        Future<List<Client>> future = executor.submit(callable);

        executor.shutdown();
        return future.get();
    }

    public Client getById(int id) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Client[] client = {new Client()};
        Callable<Client> callable = () ->{
//            client[0] = clientDao.getClient(id);
            return client[0];
        };
        Future<Client> future = executor.submit(callable);

        executor.shutdown();
        return future.get();
    }

    public void clearAll(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = () ->{
            clientDao.clearAll();
            return 0;
        };
        Future<Integer> future = executor.submit(callable);

        executor.shutdown();
    }

}
